/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.db.entities.Parametro;

/**
 *
 * @author Vanessa
 */
public class ParametroDao extends BaseDAO<Parametro> {

    public Parametro getByName(String name) {
        getSession().beginTransaction();
        Parametro a = (Parametro) getSession().createCriteria(Parametro.class)
                .add(Restrictions.like("nombre", name))
                .uniqueResult();
        getSession().getTransaction().commit();
        return a;
    }
}
