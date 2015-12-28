package py.com.sodep.notificationserver.exceptions.handlers;

import org.jboss.resteasy.spi.ReaderException;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

@Provider
public class ReaderExceptionHandler implements ExceptionMapper<ReaderException> {

    private static final Logger LOGGER = Logger.getLogger(ReaderExceptionHandler.class);

    @Inject
    private ExceptionMapperHelper helper;

    protected Error getEntity(ReaderException e) {
        String message = helper.getMessage(e);

        LOGGER.error("Error en la lectura: " + e.getMessage());

        Error error = new Error(GlobalCodes.errors.READER_ERROR.ordinal(), message);

        return error;
    }

    protected Response.Status getStatus() {
        return Response.Status.BAD_REQUEST;
    }

    @Override
    public Response toResponse(ReaderException e) {
        return helper.toResponse(getEntity(e), getStatus());
    }
}
