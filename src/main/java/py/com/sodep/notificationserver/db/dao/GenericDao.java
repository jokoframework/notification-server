/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import java.io.Serializable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Vanessa
 * @param <T>
 */
public class GenericDao<T, PK extends Serializable> extends GenericDAOImpl {

    /*private final Class<T> type;
     private static final String PERSISTENCE_UNIT_NAME = "notification-server";
     private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
     */
    private SessionFactory sf;
    private Session session;

    public void init() {
        sf = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        this.setSessionFactory(sf);
        this.setSession(sf.getCurrentSession());
    }

    @Override
    protected Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        setSessionFactory(session.getSessionFactory());
    }
}
