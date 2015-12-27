/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.config;

import javax.enterprise.context.ApplicationScoped;
import org.hibernate.SessionFactory;

/**
 *
 * @author Vanessa
 */
@ApplicationScoped
public class HibernateSessionLocal {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateSessionLocal.sessionFactory = sessionFactory;
    }

}
