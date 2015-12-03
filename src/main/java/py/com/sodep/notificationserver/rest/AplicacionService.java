/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import py.com.sodep.notificationserver.db.entities.*;
import py.com.sodep.notificationserver.business.AplicacionBusiness;

/**
 *
 * @author Vanessa
 */
@Path("/aplicacion")
@RequestScoped
public class AplicacionService {

    AplicacionBusiness appBussines = new AplicacionBusiness();

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newApplication(Aplicacion a) throws Exception {
        System.out.println("Application/id " + a);
        appBussines.newApplication(a);
        return Response.ok().build();

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationById(@PathParam("id") String id) throws Exception {
        System.out.println("Application/id " + id);
        Aplicacion a = appBussines.getApplication(id);
        return Response.ok(a).build();

    }
}
