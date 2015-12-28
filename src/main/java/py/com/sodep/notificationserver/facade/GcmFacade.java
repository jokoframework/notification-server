package py.com.sodep.notificationserver.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import javax.ws.rs.core.Response.Status;
import py.com.sodep.notificationserver.db.dao.DeviceRegistrationDao;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.AndroidNotification;
import py.com.sodep.notificationserver.db.entities.AndroidResponse;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.exceptions.handlers.GlobalCodes;
import py.com.sodep.notificationserver.exceptions.handlers.SQLExceptionHandler;

/**
 * Se comunica con el servidor GCM para enviar la notificación correspondiente
 *
 * @author Gabriela Gaona <gabriela.gaona@konecta.com.py>
 *
 */
/**
 * Se comunica con el servidor GCM para enviar la notificación correspondiente
 *
 * @author Gabriela Gaona <gabriela.gaona@konecta.com.py>
 *
 */
public class GcmFacade {

    private static final Logger log = Logger.getLogger(GcmFacade.class);

    @Inject
    ObjectMapper map;

    @Inject
    EventoDao eventoDao;

    @Inject
    DeviceRegistrationDao deviceDao;

    @Inject
    ParametroDao parametroDao;

    public AndroidResponse send(String apiKey, AndroidNotification notification) throws BusinessException {
        try {
            log.info("API KEY: " + apiKey);
            Client client = ClientBuilder.newBuilder().build();
            WebTarget target = client.target(parametroDao.getByName("URL_GCM").getValor());
            Invocation.Builder builder = target.request().accept(MediaType.APPLICATION_JSON);
            builder.header(HttpHeaders.AUTHORIZATION, "key=" + apiKey);
            AndroidResponse r;
            String jsonInString = map.writeValueAsString(notification);
            log.info("Json a enviar: " + jsonInString);
            Response response = builder.post(Entity.entity(jsonInString, MediaType.APPLICATION_JSON));
            if (response.getStatus() != 200) {
                log.info("Error en la respuesta : HTTP error code :" + response.getStatus());
                log.info(response.getStringHeaders().toString());
                log.info(response.toString());
                throw new BusinessException((Status) response.getStatusInfo(), "Error en la respuesta : HTTP error code : "
                        + response.getStatus());
            }
            r = response.readEntity(AndroidResponse.class);
            log.info("[Android/Response]: " + r);
            return r;
        } catch (JsonProcessingException ex) {
            throw new BusinessException(GlobalCodes.errors.NOTIFICATION_ERROR, ex);
        }
    }
}
