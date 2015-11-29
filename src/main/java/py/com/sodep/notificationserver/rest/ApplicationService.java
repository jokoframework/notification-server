/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import py.com.sodep.notificationserver.bussines.ApplicationBussines;

/**
 *
 * @author Vanessa
 */
@Path("/application")
@RequestScoped
public class ApplicationService {

    ApplicationBussines appBussines = new ApplicationBussines();

    @GET
    @Path("/new/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newApplication(@PathParam("id") String id) {
        System.out.println("Application/id " + id);
        return appBussines.getApplication(id);

    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationById(@PathParam("id") String id) {
        System.out.println("Application/id " + id);
        return appBussines.getApplication(id);

    }
}
