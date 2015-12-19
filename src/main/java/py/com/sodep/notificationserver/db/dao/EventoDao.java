/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.db.entities.Evento;

/**
 *
 * @author Vanessa
 */
public class EventoDao extends BaseDAO<Evento, Long> {

    public List<Evento> getPendientesAndroid() {
        getSession().beginTransaction();
        List<Evento> eventos = getSession()
                .createCriteria(Evento.class)
                .add(Restrictions.like("estadoAndroid", "PENDIENTE")).list();
        System.out.println("[EventoDao] Eventos ANDROID: " + eventos.size());
        getSession().getTransaction().commit();
        return eventos;
    }
    public List<Evento> getPendientesIos() {
        getSession().beginTransaction();
        List<Evento> eventos = getSession()
                .createCriteria(Evento.class)
                .add(Restrictions.like("estadoIos", "PENDIENTE")).list();
        System.out.println("[EventoDao] Eventos IOS: " + eventos.size());
        getSession().getTransaction().commit();
        return eventos;
    }
}
