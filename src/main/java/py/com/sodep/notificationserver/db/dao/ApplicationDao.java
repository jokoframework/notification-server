/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.db.entities.Application;

/**
 *
 * @author Vanessa
 */
public class ApplicationDao extends BaseDAO<Application, Long> {

    public Application getByName(String name) {
        Application a = (Application) getSession().createCriteria(Application.class)
                .add(Restrictions.like("name", name))
                .uniqueResult();
        return a;
    }
}
