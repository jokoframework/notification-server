/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest;

import java.sql.SQLException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.business.AplicacionBusiness;

/**
 *
 * @author Vanessa
 */
@Path("/aplicacion")
public class RegIdService {
@Inject
    Logger logger;

    @Inject
    AplicacionBusiness appBussines;

    @GET
    @Path("/{appName}/android")
    @Produces(MediaType.APPLICATION_JSON)
    public Response newDevice(@PathParam(value = "appName") String appName) throws SQLException, Exception {
        logger.info("REGISTRACION EN " + appName);
        
        return Response.ok().build();

    }
    
}
