package py.com.sodep.notificationserver.exceptions.handlers;

import py.com.sodep.notificationserver.config.GlobalCodes;
import com.google.common.base.Throwables;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import javax.ws.rs.core.Response.Status;
import org.apache.log4j.Logger;

@RequestScoped
public class ExceptionMapperHelper {

    private static final Logger LOGGER = Logger.getLogger(ExceptionMapperHelper.class);

    private static final int httpErrorCodes[] = {400, 401, 402, 403, 404, 405,
        406, 407, 408, 409, 410, 411, 412, 413,
        414, 415, 416, 417, 422, 423, 424, 500,
        501, 502, 503, 504, 505, 506, 507, 510};

    protected static final int DEFAULT_RESPONSE_CODE = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

    public Response toResponse(Object entity, int statusCode) {
        Response.ResponseBuilder response = Response.status(statusCode);
        response.type(MediaType.APPLICATION_JSON);
        response.entity(entity);
        return finishResponse(response, statusCode).build();
    }

    public Response toResponse(Object entity, Response.Status status) {
        return toResponse(entity, status.getStatusCode());
    }

    public Response.ResponseBuilder finishResponse(Response.ResponseBuilder response, int statusCode) {
        LOGGER.info("StatusCode: " + statusCode);
        LOGGER.info("Body request de salida: " + response.build().getEntity());

        return response;
    }

    public Response.ResponseBuilder finishResponse(Response response) {
        LOGGER.info("response: " + response);
        Response.ResponseBuilder builder = Response.fromResponse(response);
        return finishResponse(builder, response.getStatus());
    }

    public Response.ResponseBuilder finishResponse(Response.ResponseBuilder response, Response.Status status) {
        LOGGER.info("status.getStatusCode(): " + status.getStatusCode());
        return finishResponse(response, status.getStatusCode());
    }

    public String getMessage(Exception e) {
        String message = Throwables.getRootCause(e).getMessage();

        if (message == null) {
            message = e.getMessage();
        }

        return message;
    }

    public static boolean isInErrorCodes(int errorCode) {
        return Arrays.binarySearch(httpErrorCodes, errorCode) >= 0;
    }

    //TODO: Implementar función de mapeo
    public static int mapErrorCode(int code) {
        if (code == GlobalCodes.errors.APLICACION_NOT_FOUND.ordinal()) {
            return Status.BAD_REQUEST.getStatusCode();
        }
        return Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
