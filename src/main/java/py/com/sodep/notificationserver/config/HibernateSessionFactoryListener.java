/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.config;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@WebListener
public class HibernateSessionFactoryListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //SessionFactory sessionFactory = (SessionFactory) servletContextEvent.getServletContext().getAttribute("SessionFactory");
        SessionFactory sessionFactory = HibernateSessionLocal.sessionFactory;
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            System.out.println("Closing sessionFactory");
            sessionFactory.close();
        }
        System.out.println("Released Hibernate sessionFactory resource");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        System.out.println("Hibernate Configuration created successfully");

//        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//        System.out.println("ServiceRegistry created successfully");
        SessionFactory sessionFactory = configuration
                .buildSessionFactory();
        System.out.println("SessionFactory created successfully");

        //servletContextEvent.getServletContext().setAttribute("SessionFactory", sessionFactory);
        HibernateSessionLocal.sessionFactory = sessionFactory;
        System.out.println("Hibernate SessionFactory Configured successfully");
    }

}
