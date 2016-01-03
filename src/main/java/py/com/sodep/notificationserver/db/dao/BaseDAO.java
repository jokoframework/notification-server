/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import py.com.sodep.notificationserver.config.HibernateSessionLocal;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Vanessa
 * @param <T>
 */
public class BaseDAO<T> {

    private static final Logger LOGGER = Logger.getLogger(BaseDAO.class);

    public Session getSession() {
        return HibernateSessionLocal.getSessionFactory().getCurrentSession();
    }

    public void save(T entity) {
        try {
            getSession().beginTransaction();
            getSession().persist(entity);
            getSession().getTransaction().commit();
        } catch (HibernateException e) {
            if (getSession().getTransaction() != null) {
                LOGGER.error("ROLLBACK: " + entity);
                getSession().getTransaction().rollback();
            }
            throw e;
        }
    }

    public T findById(long id, Class<T> objectClass) {
        LOGGER.info("Buscando: " + objectClass);
        getSession().beginTransaction();
        T result = (T) getSession().get(objectClass, id);
        if (result != null) {
            Hibernate.initialize(result);
            getSession().getTransaction().commit();
            return result;
        } else {
            throw new ObjectNotFoundException(id, objectClass.getName());
        }
    }

    public T create(T entity) throws HibernateException {
        try {
            getSession().beginTransaction();
            getSession().saveOrUpdate(entity);
            getSession().getTransaction().commit();
        } catch (HibernateException e) {
            if (getSession().getTransaction() != null) {
                LOGGER.error("ROLLBACK: " + entity);
                getSession().getTransaction().rollback();
            }
            throw e;
        }
        return entity;
    }

    public boolean update(T entity) {
        Transaction tx = getSession().beginTransaction();
        try {
            if (entity == null) {
                return false;
            }
            getSession().evict(entity);
            getSession().update(entity);
            tx.commit();
            return true;
        } catch (Exception e) {
            LOGGER.error("ROLLBACK: " + entity);
            tx.rollback();
            throw e;
        }
    }

    public boolean delete(T entity) {
        try {
            if (entity == null) {
                return false;
            }
            getSession().delete(entity);
            getSession().getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.error("ROLLBACK: " + entity);
            getSession().getTransaction().rollback();
            throw e;
        }
    }
}
