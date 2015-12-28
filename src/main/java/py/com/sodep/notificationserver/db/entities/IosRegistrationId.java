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

/**
 *
 * @author canetev
 */
@Entity
@Table(name = "ios_reg_id")
public class IosRegistrationId implements Serializable {
    @Id
    @GeneratedValue
    Long id;

    @JsonProperty(value = "token")
    @Column(name = "token")
    String token;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    @JsonIgnore
    private final Evento evento;
    
    public IosRegistrationId(String token, Evento e) {
        this.token = token;
        this.evento = e;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "IosRegistrationId{" + "id=" + id + ", token=" + token + ", evento=" + evento.getId() + '}';
    }
    
}
