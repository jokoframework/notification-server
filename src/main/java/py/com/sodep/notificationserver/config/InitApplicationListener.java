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

    private static final Logger LOGGER = Logger.getLogger(InitApplicationListener.class);

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
        LOGGER.info("Cancelando tareas pendientes del timer android");
        androidTimer.cancel();
        LOGGER.info("Cancelando tareas pendientes del timer ios");
        iosTimer.cancel();
        SessionFactory sessionFactory = HibernateSessionLocal.getSessionFactory();
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            LOGGER.info("Closing sessionFactory");
            sessionFactory.close();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        LOGGER.info("Hibernate Configuration created successfully");
        SessionFactory sessionFactory = configuration
                .buildSessionFactory();
        LOGGER.info("SessionFactory created successfully");
        HibernateSessionLocal.setSessionFactory(sessionFactory);
        LOGGER.info("Hibernate SessionFactory Configured successfully");
        LOGGER.info("Released Hibernate sessionFactory resource");

        try {
            createParam("PATH_CERTIFICADOS", "/srv/notification-server/certs", "String");
            createParam("URL_GCM", "https://android.googleapis.com/gcm/send", "String");
            createParam("IOS_THREADS", "3", "Integer");
            createParam("IOS_TIMER", "10", "Integer");
            createParam("ANDROID_TIMER", "10", "Integer");
        } catch (Exception ex) {
            LOGGER.error("Error al crear los parámetros: ", ex);
        }
        LOGGER.info("Inicializando timer");
        initializeTimer();

    }

    public void createParam(String key, String value, String datatype) {
        if (pdao.getByName(key) == null) {
            LOGGER.info("Creando Parametro: " + key + " - " + value + " - " + datatype);
            pdao.save(new Parametro(key, value, datatype));
        }
    }

    public void initializeTimer() {
        LOGGER.info("Iniciando Timer Android");
        androidTimer.schedule(androidTask, 1000, Long.valueOf(pdao.getByName("ANDROID_TIMER").getValor()) * 1000);
        LOGGER.info("Iniciando Timer Ios");
        iosTimer.schedule(iosTask, 1000, Long.valueOf(pdao.getByName("IOS_TIMER").getValor()) * 1000);
    }

}
