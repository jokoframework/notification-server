package py.com.sodep.notificationserver.db.entities.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "message_result")
@Entity
public class Result implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    Long id;

    String message_id;
    String error;
    String originalRegistrationId;
    String registration_id;
    Integer status;

    @ManyToOne
    @JoinColumn(name = "android_response_id")
    @JsonIgnore
    private AndroidResponse androidResponse;

    @ManyToOne
    @JoinColumn(name = "ios_response_id")
    @JsonIgnore
    private IosResponse iosResponse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOriginalRegistrationId() {
        return originalRegistrationId;
    }

    public void setOriginalRegistrationId(String originalRegistrationId) {
        this.originalRegistrationId = originalRegistrationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AndroidResponse getAndroidResponse() {
        return androidResponse;
    }

    public void setAndroidResponse(AndroidResponse androidResponse) {
        this.androidResponse = androidResponse;
    }

    public IosResponse getIosResponse() {
        return iosResponse;
    }

    public void setIosResponse(IosResponse iosResponse) {
        this.iosResponse = iosResponse;
    }

    @Override
    public String toString() {
        return "Result{" + "error=" + error + ", message_id=" + message_id + ", registration_id=" + registration_id + '}';
    }

}
