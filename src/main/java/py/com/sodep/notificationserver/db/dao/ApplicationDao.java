/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.db.dao;

import py.com.sodep.notificationserver.db.entities.Application;

/**
 *
 * @author Vanessa
 */
public class ApplicationDao extends GenericDao<Application>{

    public ApplicationDao() {
        super(Application.class);
    }
    
}
