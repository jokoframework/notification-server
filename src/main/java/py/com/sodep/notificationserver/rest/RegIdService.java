/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest;

import java.sql.SQLException;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.business.AplicacionBusiness;
import py.com.sodep.notificationserver.db.entities.DeviceRegistration;

/**
 *
 * @author Vanessa
 */
@Path("/aplicacion")
public class RegIdService {

    final static Logger log = Logger.getLogger(RegIdService.class);

    @Inject
    AplicacionBusiness appBussines;

    @GET
    @Path("/{id}/android")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAndroidInactiveDev(@PathParam(value = "id") Long id) throws SQLException, Exception {
        List<DeviceRegistration> devices = appBussines.getListaRegIdInvalido(id);
        return Response.ok(devices).build();
    }

    @GET
    @Path("/{id}/ios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIosInactiveDev(@PathParam(value = "id") Long id) throws SQLException, Exception {
        List<DeviceRegistration> devices = appBussines.getListaIosRegIdInvalido(id);
        return Response.ok(devices).build();

    }

}
