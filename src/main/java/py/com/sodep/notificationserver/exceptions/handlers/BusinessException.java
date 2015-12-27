/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.exceptions.handlers;

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
        this.error = GlobalCodes.statusError.get(status);
    }

    public BusinessException(Response.Status status, String mensaje) {
        super(mensaje);
        this.error = GlobalCodes.statusError.get(status);
    }

    public Error getError() {
        return new Error(this.error.ordinal(), this.getMessage());
    }
}
