/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.business;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import py.com.sodep.notificationserver.db.dao.ApplicationDao;
import py.com.sodep.notificationserver.config.HibernateSessionLocal;
import py.com.sodep.notificationserver.db.entities.Application;

/**
 *
 * @author Vanessa
 */
@RequestScoped
public class ApplicationBusiness {

    

    public Response newApplication(String id) {
        System.out.println("Recibido " + id);
        ApplicationDao applicationDao = new ApplicationDao();
        Application a = new Application();
        System.out.println(HibernateSessionLocal.sessionFactory.getCurrentSession().isDirty());
        a.setName("HOLA");
        applicationDao.save(a);
        return Response.ok().build();
    }
    
    public Response getApplication(String id) {
        System.out.println("Recibido " + id);
        ApplicationDao applicationDao = new ApplicationDao();
        Object a = applicationDao.find(Long.valueOf(id));
        System.out.println("Application encontrado:" + a);
        return Response.ok(a).build();
    }
}