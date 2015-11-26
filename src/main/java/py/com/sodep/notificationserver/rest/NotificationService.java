/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanessa
 */
@Path("/message")
public class NotificationService {

    @GET
    @Path("/{param}")
    public Response printMessage(@PathParam("param") String msg) {

        String result = "Restful example : " + msg;

        return Response.status(200).entity(result).build();

    }
}
