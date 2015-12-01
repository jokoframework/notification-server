package py.com.sodep.notificationserver.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.com.sodep.notificationserver.business.NotificationBusiness;
import py.com.sodep.notificationserver.db.entities.Evento;

@Path("/evento")
@RequestScoped
public class NotificationService {

	@Inject
	NotificationBusiness business;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newNotification(Evento evento) {
		System.out.println("Evento " + evento.getApplicationName()
				+ " Payload:" + evento.getPayload());
		System.out.println("business: " + business);
		business = new NotificationBusiness();
		business.notificar(evento);
		return Response.ok().build();

	}

	@GET
	public Response newApplication() {

		return Response.ok().build();

	}

}
