package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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

/**
 *
 * @author canetev
 */
@Entity
@Table(name = "android_reg_id")
@JsonAutoDetect
public class AndroidRegistrationId implements Serializable {
    @Id
    @GeneratedValue
    Long id;

    @JsonProperty(value = "registration_id")
    @Column(name = "registration_id")
    String registrationId;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    @JsonIgnore
    private Evento evento;

    public AndroidRegistrationId() {
        //Default Constructor
    }

    public AndroidRegistrationId(String registrationId, Evento e) {
        this.registrationId = registrationId;
        this.evento = e;
    }

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

    public Evento getEvento() {
        return evento;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" + "id=" + id + ", registrationId=" + registrationId + '}';
    }
}
