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
import org.hibernate.HibernateException;
import py.com.sodep.notificationserver.business.NotificationBusiness;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.rest.RegIdService;

/**
 *
 * @author Vanessa
 */
@Singleton
public class AndroidNotificationTimer extends TimerTask {

    final static Logger log = Logger.getLogger(AndroidNotificationTimer.class);

    @Inject
    NotificationBusiness business;
    @Inject
    EventoDao dao;
    @Inject
    AplicacionDao appDao;

    @Override
    public void run() {
        ArrayList<Evento> eventos = (ArrayList) dao.getPendientesAndroid();
        log.info("[ANDROID]: se encontraron " + eventos.size() + " eventos.");
        for (Evento e : eventos) {
            log.info("[ANDROID]: Notificando evento: " + e);
            if ((e.getAplicacion().getEstadoAndroid() != null && !e.getAplicacion().getEstadoAndroid().equals("BLOQUEADA"))
                    || e.getAplicacion().getEstadoAndroid() == null) {
                try {
                    if (e.isProductionMode()) {
                        if (e.getAplicacion().getApiKeyProd() != null) {
                            e.setAndroidResponse(business.notificarAndroid(e.getAplicacion().getApiKeyProd(), e));
                        }
                    } else {
                        if (e.getAplicacion().getApiKeyDev() != null) {
                            e.setAndroidResponse(business.notificarAndroid(e.getAplicacion().getApiKeyDev(), e));
                        }
                    }

                    if (e.getAndroidResponse().getSuccess() > 0) {
                        e.setEstadoAndroid("ENVIADO");
                    } else {
                        e.setEstadoAndroid("ERROR");

                    }
                    dao.create(e);
                } catch (BusinessException ex) {
                    log.error("[ANDROID][Evento: " + e.getId() + "]Error al notificar: ", ex);
                    if (ex.getError().getCodigo().equals("401")) {
                        Aplicacion a = e.getAplicacion();
                        a.setError(ex.getError().getCodigo());
                        a.setEstadoAndroid("BLOQUEADA");
                        try {
                            appDao.create(a);
                        } catch (HibernateException ex1) {
                            log.error("[ANDROID][Evento: " + e.getId() + "]Error al bloquear aplicacion: ", ex1);
                        }
                    }
                } catch (RuntimeException ex) {
                    log.error("[ANDROID][Evento: " + e.getId() + "]Error al notificar: ", ex);
                }
            } else {
                try {
                    log.info("La aplicacion " + e.getAplicacion().getNombre() + " se encuentra BLOQUEADA, se suspenden las notificaciones.");
                    e.setEstadoAndroid("SUSPENDIDO");
                    dao.create(e);
                } catch (HibernateException ex) {
                    log.error("[ANDROID][Evento: " + e.getId() + "]Error al suspender notificación: ", ex);
                }
            }
            log.info("Siguiente evento...");
        }
    }
}
