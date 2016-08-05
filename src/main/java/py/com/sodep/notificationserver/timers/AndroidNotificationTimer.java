/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.timers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import py.com.sodep.notificationserver.business.NotificationBusiness;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.exceptions.handlers.Error;

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
            if (aplicacionHabilitada(e.getAplicacion())) {
                try {
                    notificar(e);
                } catch (BusinessException ex) {
                    log.error("[ANDROID][Evento: " + e.getId() + "]Error al notificar: ", ex);
                    if (ex.getError().getCodigo().equals(String.valueOf(Response.Status.UNAUTHORIZED.getStatusCode()))) {
                        bloquearAplicacion(e.getAplicacion(), ex.getError());
                    }
                } catch (RuntimeException ex) {
                    log.error("[ANDROID][Evento: " + e.getId() + "]Error al notificar: ", ex);
                }
            } else {
                suspenderNotificaciones(e);
            }
            log.info("Siguiente evento...");
        }
    }

    public void notificar(Evento e) throws BusinessException {
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
            e.setEstadoAndroid(GlobalCodes.ENVIADO);
        } else {
            e.setEstadoAndroid(GlobalCodes.ERROR);
        }
        try {
            dao.create(e);
        } catch (HibernateException | SQLException ex) {
            throw new BusinessException(GlobalCodes.errors.DB_ERROR, ex);
        }
    }

    public void suspenderNotificaciones(Evento e) {
        try {
            log.info("La aplicacion " + e.getAplicacion().getNombre() + " se encuentra BLOQUEADA, se suspenden las notificaciones.");
            e.setEstadoAndroid(GlobalCodes.SUSPENDIDO);
            dao.create(e);
        } catch (HibernateException | SQLException ex) {
            log.error("[ANDROID][Evento: " + e.getId() + "]Error al suspender notificación: ", ex);
        }
    }

    public void bloquearAplicacion(Aplicacion a, Error e) {
        a.setError(e.getCodigo() + ": " + e.getMensaje());
        a.setEstadoAndroid(GlobalCodes.BLOQUEADA);
        try {
            appDao.create(a);
        } catch (HibernateException | SQLException ex) {
            log.error("[ANDROID]Error al bloquear aplicacion: ", ex);
        }
    }

    public static boolean aplicacionHabilitada(Aplicacion a) {
        return (a.getEstadoAndroid() != null && !a.getEstadoAndroid().equals(GlobalCodes.BLOQUEADA)) || a.getEstadoAndroid() == null;
    }
}
