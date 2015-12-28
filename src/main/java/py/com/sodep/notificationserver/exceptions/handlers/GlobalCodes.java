/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.exceptions.handlers;

import java.util.EnumMap;
import java.util.HashMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vanessa
 */
public class GlobalCodes {
    private GlobalCodes(){
        
    }
    protected static final EnumMap<Response.Status, GlobalCodes.errors> statusError
            = new EnumMap<>(Response.Status.class);

    static {
        statusError.put(Response.Status.BAD_REQUEST, errors.BAD_REQUEST);
        statusError.put(Response.Status.UNAUTHORIZED, errors.UNAUTHORIZED);
        statusError.put(Response.Status.INTERNAL_SERVER_ERROR, errors.UNKNOWN_ERROR);
    }

    public enum errors {

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
    //ESTADOS DE APLICACION
    public static final String HABILITADA = "HABILITADA";
    public static final String BLOQUEADA = "BLOQUEADA";
    //ACCIONES DE REGISTRATION ID
    public static final String ELIMINAR = "ELIMINAR";
    public static final String CAMBIAR = "CAMBIAR";
    //ESTADOS DE REGISTRATION ID
    public static final String NUEVO = "NUEVO";
    public static final String CONSULTADO = "CONSULTADO";
    //ESTADO DE NOTIFICACIONES
    public static final String NO_APLICA = "N/A";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String ENVIADO = "ENVIADO";
    public static final String SUSPENDIDO = "SUSPENDIDO";
    public static final String ERROR = "ERROR";
    

    public static final String NotRegistered = "NotRegistered";
    public static final String InvalidRegistration = "InvalidRegistration";
    public static final String MissingRegistration = "MissingRegistration";
    public static final String InvalidPackageName = "InvalidPackageName";
    public static final String MismatchSenderId = "MismatchSenderId";

    protected static final HashMap<String, String> accion
            = new HashMap<>();

    static {
        accion.put(NotRegistered, ELIMINAR);
        accion.put(InvalidRegistration, CAMBIAR);
        accion.put(MissingRegistration, ELIMINAR);
    }

    public static String getAccion(String error) {
        return accion.get(error);
    }
}
