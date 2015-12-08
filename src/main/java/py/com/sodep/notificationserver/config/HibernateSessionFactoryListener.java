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
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Parametro;

@WebListener
@ApplicationScoped
public class HibernateSessionFactoryListener implements ServletContextListener {
    final static Logger logger = Logger.getLogger(HibernateSessionFactoryListener.class);
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //SessionFactory sessionFactory = (SessionFactory) servletContextEvent.getServletContext().getAttribute("SessionFactory");
        SessionFactory sessionFactory = HibernateSessionLocal.sessionFactory;
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            System.out.println("Closing sessionFactory");
            sessionFactory.close();
        }
    }
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        System.out.println("Hibernate Configuration created successfully");
        SessionFactory sessionFactory = configuration
                .buildSessionFactory();
        System.out.println("SessionFactory created successfully");
        HibernateSessionLocal.sessionFactory = sessionFactory;
        System.out.println("Hibernate SessionFactory Configured successfully");
        System.out.println("Released Hibernate sessionFactory resource");
        ParametroDao pdao = new ParametroDao();
        
        try {
            System.out.println("Creando Parametro: PATH_CERTIFICADOS");
            pdao.save(new Parametro("PATH_CERTIFICADOS", "C:\\Users\\Vanessa\\Documents\\work", "String"));
            System.out.println("Creando Parametro: URL_GCM");
            pdao.save(new Parametro("URL_GCM", "https://android.googleapis.com/gcm/send", "String"));
        } catch (Exception ex) {
            logger.error("Error al crear los parámetros: " + ex.getMessage());
        }
        
    }
    
}
