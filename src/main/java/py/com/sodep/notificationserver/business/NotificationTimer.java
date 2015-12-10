/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.business;

import java.util.ArrayList;
import java.util.TimerTask;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Evento;

/**
 *
 * @author Vanessa
 */
public class NotificationTimer extends TimerTask {

    @Inject
    NotificationBusiness business;
    @Inject
    EventoDao dao;
    @Inject
    Logger log;

    @Override
    public void run() {
        ArrayList<Evento> eventos = (ArrayList) dao.getPendientes();
        for (Evento e : eventos) {
            log.info("Notificando evento: " + e);
            try {
                business.notificar(e);
            } catch (Exception ex) {
                log.error("[Evento: " + e.getId() + "]Error al notificar: ", ex);
            }
        }
    }
}
