/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import java.sql.SQLException;
import py.com.sodep.notificationserver.config.HibernateSessionLocal;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

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
        Transaction t = getSession().getTransaction();
        try {
            t.begin();
            getSession().persist(entity);
            t.commit();
        } catch (HibernateException e) {
            if (t != null) {
                LOGGER.error("ROLLBACK: " + entity);
                t.rollback();
            }
            throw e;
        }
    }

    public T findById(long id, Class<T> objectClass) throws BusinessException {
        T result = null;
        Transaction t = getSession().getTransaction();
        try {
            LOGGER.info("Buscando: " + objectClass);
            t.begin();
            LOGGER.info("Transaccion iniciada!");
            result = (T) getSession().get(objectClass, id);
            if (result != null) {
                Hibernate.initialize(result);
            } else {
                throw new ObjectNotFoundException(id, objectClass.getName());
            }
            t.commit();
        } catch (Exception h) {
            LOGGER.error("Error al buscar por id: ", h);
            t.rollback();            
            throw new BusinessException(GlobalCodes.errors.APLICACION_NOT_FOUND, h.getLocalizedMessage());
        } finally {
            if (!t.wasCommitted() && !t.wasRolledBack() ){
                t.commit();
            }
        }
        return result;
    }

    public T create(T entity) throws HibernateException, SQLException {
        Transaction t = getSession().getTransaction();
        try {
            t.begin();
            getSession().saveOrUpdate(entity);
            t.commit();
        } catch (HibernateException e) {
            if (t != null) {
                LOGGER.error("ROLLBACK: " + entity);
                t.rollback();
            }
            if (e instanceof ConstraintViolationException){
                throw ((ConstraintViolationException)e).getSQLException();
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
        Transaction tx = getSession().beginTransaction();
        try {
            if (entity == null) {
                return false;
            }
            getSession().delete(entity);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.error("ROLLBACK: " + entity);
            tx.rollback();
            throw e;
        }
    }
}
