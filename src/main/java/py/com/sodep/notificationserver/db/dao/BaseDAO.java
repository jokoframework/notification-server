/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import py.com.sodep.notificationserver.config.HibernateSessionLocal;
import java.io.Serializable;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Vanessa
 * @param <T>
 * @param <PK>
 */
public class BaseDAO<T, PK extends Serializable> {

    public BaseDAO() {
    }

    public Session getSession() {
        return HibernateSessionLocal.sessionFactory.getCurrentSession();
    }

    public void save(T entity) throws Exception {
        try {
            getSession().beginTransaction();
            getSession().persist(entity);
            getSession().getTransaction().commit();
        } catch (Exception e) {
            if (getSession().getTransaction() != null) {
                getSession().getTransaction().rollback();
            }
            throw new Exception("Error al crear registro: " + e.getMessage());
        }
    }

    public T findById(long id, Class<T> objectClass) throws Exception {
        try {
            getSession().beginTransaction();
            T result = (T) getSession().get(objectClass, id);
            if (result != null) {
                Hibernate.initialize(result);
                return result;
            } else {
                throw new ObjectNotFoundException(id, objectClass.getName());
            }
        } finally {
            getSession().getTransaction().commit();
        }
    }

    public T create(T newInstance) {
        try {
            getSession().beginTransaction();
            getSession().saveOrUpdate(newInstance);
            getSession().getTransaction().commit();
        } catch (Exception e) {
            if (getSession().getTransaction() != null) {
                getSession().getTransaction().rollback();
            }
            throw e;
        }
        return newInstance;
    }

    public boolean updpate(T updateInstance) {
        try {
            if (updateInstance == null) {
                return false;
            }
            getSession().update(updateInstance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            getSession().getTransaction().commit();
        }
    }

    public boolean delete(T entity) {
        try {
            if (entity == null) {
                return false;
            }
            getSession().delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            getSession().getTransaction().commit();
        }
    }

    /*private Class<T> type;

     @SuppressWarnings("unchecked")
     public PK create(T object) {
     return (PK) getSession().save(object);
     }

     @SuppressWarnings("unchecked")
     public T read(PK id) {
     return (T) getSession().get(type, id);
     }

     public List<T> readAll() {
     return readByCriteria();
     }

     @SuppressWarnings("unchecked")
     public List<T> readByCriteria(Criterion... criterion) {
     Criteria crit = getSession().createCriteria(type);
     for (Criterion c : criterion) {
     crit.add(c);
     }
     return crit.list();
     }

     @Transactional
     public void update(T object) {
     getSession().update(object);
     }

     @Transactional
     public void delete(T object) {
     getSession().delete(object);
     }*/
}
