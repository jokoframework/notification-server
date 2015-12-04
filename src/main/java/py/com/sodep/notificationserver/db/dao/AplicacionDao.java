/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import org.hibernate.criterion.Restrictions;
import py.com.sodep.notificationserver.db.entities.Aplicacion;

/**
 *
 * @author Vanessa
 */
public class AplicacionDao extends BaseDAO<Aplicacion, Long> {

	public Aplicacion getByName(String name) {
		getSession().beginTransaction();
		Aplicacion a = (Aplicacion) getSession()
				.createCriteria(Aplicacion.class)
				.add(Restrictions.like("name", name)).uniqueResult();
		getSession().getTransaction().commit();
		return a;
	}
}
