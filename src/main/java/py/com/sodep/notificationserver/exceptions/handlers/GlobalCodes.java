/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.exceptions.handlers;

import java.util.HashMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanessa
 */
public class GlobalCodes {

    protected static final HashMap<Response.Status, GlobalCodes.errors> statusError
            = new HashMap<>();

    static {
        statusError.put(Response.Status.BAD_REQUEST, errors.BAD_REQUEST);
        statusError.put(Response.Status.UNAUTHORIZED, errors.UNAUTHORIZED);
        statusError.put(Response.Status.INTERNAL_SERVER_ERROR, errors.UNKNOWN_ERROR);
    }

    public static enum errors {

        APLICACION_NOT_FOUND,
        APP_PATH_NOT_FOUND,
        APP_PATH_ERROR,
        APP_EMPTY_FILE,
        APLICACION_BLOCKED,
        BAD_REQUEST,
        NOTIFICATION_ERROR,
        PAYLOAD_SIZE,
        DEVICES_SIZE,
        IOS_COMM,
        IOS_KEY_STORE,
        IOS_PUSH_PAYLOAD,
        READER_ERROR,
        DB_ERROR,
        UNKNOWN_ERROR,
        UNAUTHORIZED
    }
    public static final String HABILITADA = "HABILITADA";
    public static final String BLOQUEADA = "BLOQUEADA";

}
