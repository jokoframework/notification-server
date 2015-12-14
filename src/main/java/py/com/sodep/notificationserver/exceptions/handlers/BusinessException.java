/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.exceptions.handlers;

/**
 *
 * @author Vanessa
 */
public class BusinessException extends Exception {

    private final int errorCode;

    public BusinessException(int errorCode, String mensaje) {
        super(mensaje);
        this.errorCode = errorCode;
    }

    public BusinessException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public Error getError() {
        return new Error(errorCode, this.getMessage());
    }
}
