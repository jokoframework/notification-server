package py.com.sodep.notificationserver.exceptions.handlers;

import com.google.common.base.Throwables;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.Failure;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.hibernate.ObjectNotFoundException;
import static py.com.sodep.notificationserver.exceptions.handlers.ExceptionMapperHelper.DEFAULT_RESPONSE_CODE;
import static py.com.sodep.notificationserver.exceptions.handlers.ExceptionMapperHelper.isInErrorCodes;
import static py.com.sodep.notificationserver.exceptions.handlers.ExceptionMapperHelper.mapErrorCode;


/**
 * Maneja las excepciones que no tengan un ExceptionHandler
 *
 * @author duartm
 * @version 1.0 03/06/2014
 */

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger log = Logger.getLogger(GeneralExceptionMapper.class);

    //@Inject
    private final ExceptionMapperHelper helper = new ExceptionMapperHelper();

    @Override
    public Response toResponse(Exception exception) {
        System.out.println("EN EL MAPPER" + exception.getMessage());
        Throwable cause = Throwables.getRootCause(exception);

        //Errores de Resteasy
        if (cause instanceof Failure) {
            Failure failure = (Failure) cause;

            // Si ya se construyó una respuesta para la excepción,
            // usarla solo en caso que el objeto a retornar sea un Error
            if (failure.getResponse() != null) {
                Response response = failure.getResponse();

                if (response.getEntity() instanceof Error) {
                    log.trace("Usando respuesta obtenida de la excepción");
                    return helper.toResponse(response.getEntity(), response.getStatus());
                } else {
                    // Tratar de reproducir el error usando Error
                    int codigo = failure.getErrorCode();
                    String mensaje = failure.getMessage();

                    Error error = new Error(mapErrorCode(codigo), mensaje);

                    return helper.toResponse(error, response.getStatus());
                }
            }

            // Construir una respuesta propia
            int entityErrorCode = failure.getErrorCode();
            int responseErrorCode = failure.getErrorCode();

            if (!isInErrorCodes(responseErrorCode)) {
                log.trace("El código " + entityErrorCode + " no pertenece a los códigos de error estándares, la respuesta se retorna con errorCode " + mapErrorCode(DEFAULT_RESPONSE_CODE));
                responseErrorCode = DEFAULT_RESPONSE_CODE;
            }

            String message = cause.getMessage();

            if (message == null) {
                message = "Error interno de Resteasy";
            }

            return helper.toResponse(new Error(mapErrorCode(entityErrorCode), message), responseErrorCode);
        }

        log.info("No se puede encontrar un mapper para la excepción, se maneja como una excepción general", exception);

        // Cualquier otro tipo de excepcion.
        Error error = new Error(mapErrorCode(DEFAULT_RESPONSE_CODE), "Error en el servidor");
        return helper.toResponse(error, Response.Status.INTERNAL_SERVER_ERROR);
    }
}
