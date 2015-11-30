/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import py.com.sodep.notificationserver.config.HibernateSessionLocal;
import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

/**
 *
 * @author Vanessa
 * @param <T>
 * @param <PK>
 */
public class BaseDAO<T, PK extends Serializable> extends GenericDAOImpl<T, PK> {

    /*public SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
     public Session session = sessionFactory.getCurrentSession();
     public Transaction tx = session.beginTransaction();*/
    public Transaction tx;

    public BaseDAO() {
        super();
        System.out.println("CREANDO SESSION Y ESO");
        super.setSessionFactory(HibernateSessionLocal.sessionFactory);
        tx = HibernateSessionLocal.sessionFactory.getCurrentSession().beginTransaction();
    }

    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);

    }

    @Override
    public Session getSession() {
        return HibernateSessionLocal.sessionFactory.getCurrentSession();
    }

    @Override
    public boolean save(T entity) {
        boolean a = super.save(entity);
        getSession().getTransaction().commit();
        return a;
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
