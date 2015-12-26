package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "ios_reg_id")
public class IosRegistrationId {

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

    public IosRegistrationId() {
    }

    public IosRegistrationId(String registrationId, Evento e) {
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

}
