/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest;

import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import py.com.sodep.notificationserver.db.entities.*;
import py.com.sodep.notificationserver.business.AplicacionBusiness;

/**
 *
 * @author Vanessa
 */
@Path("/aplicacion")
@RequestScoped
public class AplicacionService {

    @Inject
    Logger logger;

    @Inject
    AplicacionBusiness appBussines;

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newApplication(Aplicacion a) throws SQLException, Exception {
        logger.info("Application/id " + a);
        appBussines.createAplicacionJson(a, null);
        return Response.ok(a).build();

    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@MultipartForm AplicacionFile form) throws SQLException, Exception {
        logger.info("Recibido: " + form);
        Aplicacion a = appBussines.newAplicacionFileUpload(form, null);
        return Response.ok(a).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateApplication(Aplicacion b, @PathParam(value = "id") String id) throws SQLException, Exception {
        logger.info("Application/id " + b);
        Aplicacion a = appBussines.createAplicacionJson(b, Long.valueOf(id));
        return Response.ok(a).build();

    }

    @PUT
    @Path("/upload/{id}")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFileUpdate(@MultipartForm AplicacionFile form, @PathParam(value = "id") String id) throws SQLException, Exception {
        Aplicacion a = appBussines.newAplicacionFileUpload(form, Long.valueOf(id));
        return Response.ok(a).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAplicacion(@PathParam(value="id") Long id) throws SQLException, Exception {
        Aplicacion a = appBussines.eliminarAplicacion(id);
        return Response.ok(a).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationById(@PathParam("id") Long id) throws SQLException, Exception {
        logger.info("Application/id " + id);
        Aplicacion a = appBussines.getApplication(id);
        return Response.ok(a).build();

    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAplicacion(@QueryParam(value = "nombre") String nombre) throws SQLException, Exception {
        logger.info("Application/nombre " + nombre);
        Aplicacion a = appBussines.findAplicacion(nombre);
        return Response.ok(a).build();

    }
    
    @POST
    @Path("/{id}/habilitar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response habilitarAplicacion(@PathParam(value = "id") Long id) throws SQLException, Exception {
        Aplicacion a = appBussines.habilitarAplicacion(id);
        return Response.ok(a).build();
    }
}
