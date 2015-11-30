/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import javax.enterprise.context.ApplicationScoped;
import org.hibernate.SessionFactory;

/**
 *
 * @author Vanessa
 */
@ApplicationScoped
public class HibernateSessionLocal {
    public static SessionFactory sessionFactory;
    
    
}
