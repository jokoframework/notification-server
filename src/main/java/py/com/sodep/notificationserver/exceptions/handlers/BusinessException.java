package py.com.sodep.notificationserver.exceptions.handlers;

import py.com.sodep.notificationserver.config.GlobalCodes;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanessa
 */
public class BusinessException extends Exception {

    private final GlobalCodes.errors error;

    public BusinessException(GlobalCodes.errors error, String mensaje) {
        super(mensaje);
        this.error = error;
    }

    public BusinessException(GlobalCodes.errors error, Throwable cause) {
        super(cause);
        this.error = error;
    }

    public BusinessException(Response.Status status, Throwable cause) {
        super(cause);
        this.error = GlobalCodes.getStatusErr(status);
    }

    public BusinessException(Response.Status status, String mensaje) {
        super(mensaje);
        this.error = GlobalCodes.getStatusErr(status);
    }

    public Error getError() {
        return new Error(this.error.ordinal(), this.getMessage());
    }
}
