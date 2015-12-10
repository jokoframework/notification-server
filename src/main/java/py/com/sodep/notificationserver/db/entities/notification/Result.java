package py.com.sodep.notificationserver.db.entities.notification;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import py.com.sodep.notificationserver.db.entities.Evento;

/**
 * Resultado de enviar una notificacion por cada dispositivo Created by
 * gaby.lorely on 17/05/2015.
 */
@Table(name = "android_result")
@Entity
public class Result implements Serializable {

    @Id
    String message_id;

    String error;
    String registration_id;

    @ManyToOne
    @JoinColumn(name = "android_response_id")
    private AndroidResponse androidResponse;

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

    @Override
    public String toString() {
        return "Result{" + "error=" + error + ", message_id=" + message_id + ", registration_id=" + registration_id + '}';
    }

}
