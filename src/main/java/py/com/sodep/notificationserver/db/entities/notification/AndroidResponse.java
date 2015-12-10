package py.com.sodep.notificationserver.db.entities.notification;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import py.com.sodep.notificationserver.db.entities.Evento;

/**
 * Json de respuesta de GCM Created by gaby.lorely on 17/05/2015.
 */
@Entity
@Table(name = "android_response")
@JsonAutoDetect
public class AndroidResponse implements Serializable {

    @JsonProperty("multicast_id")
    @Id
    @Column(name = "id")
    Long multicast_id;
    @JsonProperty("success")
    int success;
    @JsonProperty("failure")
    int failure;
    @JsonProperty("canonical_ids")
    Integer canonical_ids;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "evento_id")
    @JsonIgnore
    private Evento evento;
    
    @OneToMany(targetEntity = Result.class, fetch = FetchType.EAGER,
            mappedBy = "androidResponse", cascade = CascadeType.ALL)
    List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(Long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public Integer getCanonical_ids() {
        return canonical_ids;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setCanonical_ids(Integer canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    @Override
    public String toString() {
        return "AndroidResponse{" + "multicast_id=" + multicast_id + ", success=" + success + ", failure=" + failure + ", canonical_ids=" + canonical_ids + ", evento=" + evento + ", results=" + results + '}';
    }

}
