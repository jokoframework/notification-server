package py.com.sodep.notificationserver.rest;

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
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

/**
 *
 * @author Vanessa
 */
@Path("/aplicacion")
@RequestScoped
public class AplicacionService {

    private static final Logger LOGGER = Logger.getLogger(AplicacionService.class);

    @Inject
    AplicacionBusiness appBussines;

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newApplication(Aplicacion a) throws BusinessException{
        LOGGER.info("Application/id " + a);
        appBussines.createAplicacionJson(a, null);
        return Response.ok(a).build();

    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@MultipartForm AplicacionFile form) throws BusinessException {
        LOGGER.info("Recibido: " + form);
        Aplicacion a = appBussines.newAplicacionFileUpload(form, null);
        return Response.ok(a).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateApplication(Aplicacion b, @PathParam(value = "id") String id) throws BusinessException {
        LOGGER.info("Application/id " + b);
        Aplicacion a = appBussines.createAplicacionJson(b, Long.valueOf(id));
        return Response.ok(a).build();

    }

    @PUT
    @Path("/upload/{id}")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFileUpdate(@MultipartForm AplicacionFile form, @PathParam(value = "id") String id)
            throws BusinessException {
        Aplicacion a = appBussines.newAplicacionFileUpload(form, Long.valueOf(id));
        return Response.ok(a).build();
    }

    @DELETE
    @Path("/{id}")
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAplicacion(@PathParam(value = "id") Long id) throws BusinessException {
        Aplicacion a = appBussines.eliminarAplicacion(id);
        return Response.ok(a).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApplicationById(@PathParam("id") Long id) throws BusinessException {
        LOGGER.info("Application/id " + id);
        Aplicacion a = appBussines.getApplication(id);
        return Response.ok(a).build();

    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAplicacion(@QueryParam(value = "nombre") String nombre) throws BusinessException {
        LOGGER.info("Application/nombre " + nombre);
        Aplicacion a = appBussines.findAplicacion(nombre);
        return Response.ok(a).build();

    }

    @POST
    @Path("/{id}/android/habilitar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response habilitarAplicacionAndroid(@PathParam(value = "id") Long id) throws BusinessException {
        Aplicacion a = appBussines.habilitarAplicacionAndroid(id);
        return Response.ok(a).build();
    }

    @POST
    @Path("/{id}/ios/habilitar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response habilitarAplicacionIos(@PathParam(value = "id") Long id) throws BusinessException {
        Aplicacion a = appBussines.habilitarAplicacionIos(id);
        return Response.ok(a).build();
    }
}
