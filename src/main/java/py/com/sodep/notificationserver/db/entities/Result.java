package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    @JsonProperty(value = "message_id")
    @Column(name = "message_id")
    String messageId;
    
    
    String error;
    
    @Column(name = "original_registration_id")
    String originalRegistrationId;
    
    @JsonProperty(value = "registration_id")
    @Column(name = "registration_id")
    String registrationId;
    
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

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
        return "Result{" + "error=" + error + ", message_id=" + messageId + ", registration_id=" + registrationId + '}';
    }

}
