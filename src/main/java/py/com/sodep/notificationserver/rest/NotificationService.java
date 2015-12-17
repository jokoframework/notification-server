package py.com.sodep.notificationserver.rest;

import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import py.com.sodep.notificationserver.business.NotificationBusiness;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

@Path("/evento")
@RequestScoped
public class NotificationService {

    @Inject
    NotificationBusiness business;

    final static Logger logger = Logger.getLogger(NotificationService.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newNotification(Evento evento) throws BusinessException, HibernateException, SQLException {
        logger.info("Evento " + evento.getApplicationName());
        evento = business.crearEvento(evento);
        //business.notificar(evento);
        return Response.ok().entity(evento).build();

    }

    @GET
    public Response newApplication() {

        return Response.ok().build();

    }

}
