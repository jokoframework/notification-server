/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.timers;

import java.util.ArrayList;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import py.com.sodep.notificationserver.business.NotificationBusiness;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;

/**
 *
 * @author Vanessa
 */
@Singleton
public class IosNotificationTimer extends TimerTask {

    final static Logger log = Logger.getLogger(IosNotificationTimer.class);

    @Inject
    NotificationBusiness business;
    @Inject
    EventoDao dao;
    @Inject
    AplicacionDao appDao;

    @Override

    public void run() {
        ArrayList<Evento> eventos = (ArrayList) dao.getPendientesIos();
        log.info("[IOS]: se encontraron " + eventos.size() + " eventos.");
        for (Evento e : eventos) {
            log.info("[IOS]: Notificando evento: " + e);
            if (aplicacionHabilitada(e.getAplicacion())) {
                try {
                    notificar(e);
                } catch (RuntimeException ex) {
                    log.error("[IOS][Evento: " + e.getId() + "]Error al notificar: ", ex);
                } catch (BusinessException ex) {
                    log.error("[ANDROID][Evento: " + e.getId() + "]Error al notificar: ", ex);
                    if (isErrorIosApp(ex.getError().getCodigo())) {
                        bloquearAplicacion(e.getAplicacion(), ex.getError());
                    }
                }
            } else {
                suspenderNotificaciones(e);
            }
        }
    }

    public void bloquearAplicacion(Aplicacion a, py.com.sodep.notificationserver.exceptions.handlers.Error e) {
        a.setError(e.getCodigo() + ": " + e.getMensaje());
        a.setEstadoIos(GlobalCodes.BLOQUEADA);
        try {
            appDao.create(a);
        } catch (HibernateException ex) {
            log.error("[ANDROID]Error al bloquear aplicacion: ", ex);
        }
    }

    public boolean isErrorIosApp(String codigo) {
        return codigo.equals(String.valueOf(GlobalCodes.errors.IOS_KEY_STORE.ordinal()));
    }

    public void notificar(Evento e) throws BusinessException {
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
            e.setEstadoIos(GlobalCodes.ERROR);
        }

        dao.create(e);
    }

    public static boolean aplicacionHabilitada(Aplicacion a) {
        return (a.getEstadoIos() != null && !a.getEstadoIos().equals(GlobalCodes.BLOQUEADA)) || a.getEstadoIos() == null;
    }

    public void suspenderNotificaciones(Evento e) {
        try {
            log.info("La aplicacion " + e.getAplicacion().getNombre() + " se encuentra BLOQUEADA, se suspenden las notificaciones.");
            e.setEstadoIos(GlobalCodes.SUSPENDIDO);
            dao.create(e);
        } catch (HibernateException ex) {
            log.error("[ANDROID][Evento: " + e.getId() + "]Error al suspender notificación: ", ex);
        }
    }
}
