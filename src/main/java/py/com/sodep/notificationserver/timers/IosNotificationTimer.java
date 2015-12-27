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
import py.com.sodep.notificationserver.exceptions.handlers.GlobalCodes;

/**
 *
 * @author Vanessa
 */
@Singleton
public class IosNotificationTimer extends TimerTask {

    @Inject
    NotificationBusiness business;
    @Inject
    EventoDao dao;
    @Inject
    Logger log;

    @Override
    public void run() {
        ArrayList<Evento> eventos = (ArrayList) dao.getPendientesIos();
        log.info("[IOS]: se encontraron " + eventos.size() + " eventos.");
        for (Evento e : eventos) {
            log.info("[IOS]: Notificando evento: " + e);
            try {
                if (e.getAplicacion() == null) {
                    throw new BusinessException(GlobalCodes.errors.APLICACION_NOT_FOUND, "La aplicacion " + e.getAplicacion().getNombre() + " no existe.");
                }
                if (e.isProductionMode()) {
                    if (e.getAplicacion().getCertificadoProd() != null && e.getAplicacion().getKeyFileProd() != null) {
                        e.setIosResponse(business.notificarIos(e.getAplicacion().getCertificadoProd(), e.getAplicacion().getKeyFileProd(), e, true));
                    }
                } else {
                    if (e.getAplicacion().getCertificadoDev() != null && e.getAplicacion().getKeyFileDev() != null) {
                        e.setIosResponse(business.notificarIos(e.getAplicacion().getCertificadoDev(), e.getAplicacion().getKeyFileDev(), e, false));
                    }
                }

                if (e.getIosResponse() != null && e.getIosResponse().getError() == null) {
                    e.setEstadoIos("ENVIADO");
                } else {
                    e.setEstadoIos("ERROR");
                }

                dao.create(e);
            } catch (RuntimeException | BusinessException | SQLException ex) {
                log.error("[IOS][Evento: " + e.getId() + "]Error al notificar: ", ex);
            }
        }
    }
}
