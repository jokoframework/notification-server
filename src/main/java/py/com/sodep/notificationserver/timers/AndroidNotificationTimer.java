/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.timers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.business.NotificationBusiness;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.exceptions.handlers.ExceptionMapperHelper;

/**
 *
 * @author Vanessa
 */
@Singleton
public class AndroidNotificationTimer extends TimerTask {

    @Inject
    NotificationBusiness business;
    @Inject
    EventoDao dao;
    @Inject
    Logger log;

    @Override
    public void run() {
        ArrayList<Evento> eventos = (ArrayList) dao.getPendientesAndroid();
        log.info("[ANDROID]: se encontraron " + eventos.size() + " eventos.");
        for (Evento e : eventos) {
            log.info("[ANDROID]: Notificando evento: " + e);
            try {
                if (e.getAplicacion() != null) {
                    if (e.isProductionMode()) {
                        if (e.getAplicacion().getApiKeyProd() != null) {
                            e.setAndroidResponse(business.notificarAndroid(e.getAplicacion().getApiKeyProd(), e));
                        }
                    } else {
                        if (e.getAplicacion().getApiKeyDev() != null) {
                            e.setAndroidResponse(business.notificarAndroid(e.getAplicacion().getApiKeyDev(), e));
                        }
                    }
                } else {
                    throw new BusinessException(ExceptionMapperHelper.appError.APLICACION_NOT_FOUND.ordinal(), "La aplicacion " + e.getAplicacion().getNombre() + " no existe.");
                }
                if (e.getAndroidResponse().getSuccess() > 0) {
                    e.setEstadoAndroid("ENVIADO");
                } else {
                    e.setEstadoAndroid("ERROR");
                }
                dao.create(e);
            } catch (RuntimeException | BusinessException | SQLException ex) {
                log.error("[ANDROID][Evento: " + e.getId() + "]Error al notificar: ", ex);
            }
        }
    }
}
