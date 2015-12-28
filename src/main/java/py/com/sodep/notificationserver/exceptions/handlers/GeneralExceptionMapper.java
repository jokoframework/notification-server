package py.com.sodep.notificationserver.exceptions.handlers;

import com.google.common.base.Throwables;
import javax.inject.Inject;
import org.jboss.logging.Logger;
import org.jboss.resteasy.spi.Failure;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
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
    private static final Logger LOGGER = Logger.getLogger(GeneralExceptionMapper.class);

    @Inject
    private ExceptionMapperHelper helper;

    @Override
    public Response toResponse(Exception exception) {
        Throwable cause = Throwables.getRootCause(exception);
        if (exception instanceof BusinessException){
            System.out.println("Es un bussibesException");
            Error error = ((BusinessException)exception).getError();
            return helper.toResponse(error, mapErrorCode(Integer.valueOf(error.getCodigo())));
        }
        //Errores de Resteasy
        if (cause instanceof Failure) {
            Failure failure = (Failure) cause;
            // Si ya se construyó una respuesta para la excepción,
            // usarla solo en caso que el objeto a retornar sea un Error
            if (failure.getResponse() != null) {
                Response response = failure.getResponse();

                if (response.getEntity() instanceof Error) {
                    LOGGER.trace("Usando respuesta obtenida de la excepción");
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
                LOGGER.trace("El código " + entityErrorCode + " no pertenece a los códigos de error estándares, la respuesta se retorna con errorCode " + mapErrorCode(DEFAULT_RESPONSE_CODE));
                responseErrorCode = DEFAULT_RESPONSE_CODE;
            }

            String message = cause.getMessage();

            if (message == null) {
                message = "Error interno de Resteasy";
            }

            return helper.toResponse(new Error(mapErrorCode(entityErrorCode), message), responseErrorCode);
        }

        LOGGER.info("No se puede encontrar un mapper para la excepción, se maneja como una excepción general", exception);

        // Cualquier otro tipo de excepcion.
        Error error = new Error(mapErrorCode(DEFAULT_RESPONSE_CODE), exception.getMessage());
        return helper.toResponse(error, Response.Status.INTERNAL_SERVER_ERROR);
    }
}
