/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Vanessa
 */
public class GenericDao<T> {

    private static final String PERSISTENCE_UNIT_NAME = "notification-server";
    private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    private Class<T> type;

    public GenericDao(Class<T> type) {
        this.type = type;
    }

    public void create(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void update(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(o);
            em.getTransaction().commit();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void delete(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(o);
            em.getTransaction().commit();

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public T findById(Object primaryKey) {
        EntityManager em = getEntityManager();
        try {
            return em.find(type, primaryKey);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
