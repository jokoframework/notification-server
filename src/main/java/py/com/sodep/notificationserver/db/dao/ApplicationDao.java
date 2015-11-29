/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import py.com.sodep.notificationserver.db.entities.Application;

/**
 *
 * @author Vanessa
 */
public class ApplicationDao extends GenericDao<Application> {

	public ApplicationDao() {
		super(Application.class);
	}

	public Application getByName(String applicationName) {
		EntityManager em = getEntityManager();
		try {

			Query q = em.createQuery(
					"SELECT c FROM Application c WHERE c.nombre = :nombre",
					Application.class);
			return (Application) q.setParameter("nombre", applicationName)
					.getSingleResult();

		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
