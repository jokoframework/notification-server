/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.config;

import java.util.Timer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import py.com.sodep.notificationserver.timers.AndroidNotificationTimer;
import py.com.sodep.notificationserver.timers.IosNotificationTimer;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.Parametro;

@WebListener
@ApplicationScoped
public class InitApplicationListener implements ServletContextListener {

    @Inject
    Logger log;

    @Inject
    AndroidNotificationTimer androidTask;

    @Inject
    IosNotificationTimer iosTask;

    @Inject
    Timer androidTimer;

    @Inject
    Timer iosTimer;

    @Inject
    ParametroDao pdao;

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //SessionFactory sessionFactory = (SessionFactory) servletContextEvent.getServletContext().getAttribute("SessionFactory");
        log.info("Cancelando tareas pendientes del timer android");
        androidTimer.cancel();
        log.info("Cancelando tareas pendientes del timer ios");
        iosTimer.cancel();
        SessionFactory sessionFactory = HibernateSessionLocal.sessionFactory;
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            log.info("Closing sessionFactory");
            sessionFactory.close();
        }
        
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        log.info("Hibernate Configuration created successfully");
        SessionFactory sessionFactory = configuration
                .buildSessionFactory();
        log.info("SessionFactory created successfully");
        HibernateSessionLocal.sessionFactory = sessionFactory;
        log.info("Hibernate SessionFactory Configured successfully");
        log.info("Released Hibernate sessionFactory resource");

        try {
            log.info("Creando Parametro: PATH_CERTIFICADOS");
            pdao.save(new Parametro("PATH_CERTIFICADOS", "C:\\Users\\Vanessa\\Documents\\work", "String"));

            log.info("Creando Parametro: URL_GCM");
            pdao.save(new Parametro("URL_GCM", "https://android.googleapis.com/gcm/send", "String"));

            log.info("Creando Parametro: IOS_THREADS");
            pdao.save(new Parametro("IOS_THREADS", "3", "Integer"));

            log.info("Creando Parametro: IOS_TIMER");
            pdao.save(new Parametro("IOS_TIMER", "60", "Integer"));

            log.info("Creando Parametro: ANDROID_TIMER");
            pdao.save(new Parametro("ANDROID_TIMER", "50", "Integer"));

        } catch (Exception ex) {
            log.error("Error al crear los parámetros: " + ex.getMessage());
        }
        log.info("Inicializando timer");
        initializeTimer(20);

    }

    public void initializeTimer(int seconds) {
        log.info("Iniciando Timer Android");
        androidTimer.schedule(androidTask, 1000, Integer.valueOf(pdao.getByName("ANDROID_TIMER").getValor()) * 1000);
        log.info("Iniciando Timer Ios");
        iosTimer.schedule(iosTask, 1000, Integer.valueOf(pdao.getByName("IOS_TIMER").getValor()) * 1000);
    }

}
