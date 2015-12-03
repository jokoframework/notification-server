package models.notificaciones;

/**
 * Resultado de enviar una notificacion por cada dispositivo
 * Created by gaby.lorely on 17/05/2015.
 */
public class Result {

    String error;
    String message_id;
    String registration_id;


    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
