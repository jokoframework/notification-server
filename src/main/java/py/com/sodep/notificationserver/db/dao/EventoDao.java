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

    public List<Evento> getPendientes() {
        getSession().beginTransaction();
        List<Evento> eventos = getSession()
                .createCriteria(Evento.class)
                .add(Restrictions.like("estado", "PENDIENTE")).list();
        getSession().getTransaction().commit();
        return eventos;
    }

}
