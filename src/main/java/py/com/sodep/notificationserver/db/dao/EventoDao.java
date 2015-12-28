/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.config.InitApplicationListener;
import py.com.sodep.notificationserver.db.entities.Evento;

/**
 *
 * @author Vanessa
 */
public class EventoDao extends BaseDAO<Evento> {
    private static final Logger LOGGER = Logger.getLogger(EventoDao.class);

    public List<Evento> getPendientesAndroid() {
        getSession().beginTransaction();
        List<Evento> eventos = getSession()
                .createCriteria(Evento.class)
                .add(Restrictions.like("estadoAndroid", "PENDIENTE")).list();
        LOGGER.info("[EventoDao] Eventos ANDROID: " + eventos.size());
        getSession().getTransaction().commit();
        return eventos;
    }
    public List<Evento> getPendientesIos() {
        getSession().beginTransaction();
        List<Evento> eventos = getSession()
                .createCriteria(Evento.class)
                .add(Restrictions.like("estadoIos", "PENDIENTE")).list();
        LOGGER.info("[EventoDao] Eventos IOS: " + eventos.size());
        getSession().getTransaction().commit();
        return eventos;
    }
}
