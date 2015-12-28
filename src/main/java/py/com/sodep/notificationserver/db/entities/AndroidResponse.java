package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Json de respuesta de GCM Created by gaby.lorely on 17/05/2015.
 */
@Entity
@Table(name = "android_response")
@JsonAutoDetect
public class AndroidResponse implements Serializable {
    @Id
    @GeneratedValue   
    Long id;
    
    @JsonProperty("multicast_id")
    @Column(name = "multicast_id")
    Long multicastId;
    @JsonProperty("success")
    int success;
    @JsonProperty("failure")
    int failure;
    @JsonProperty("canonical_ids")
    Integer canonicalIds;
    
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
        for (Result r: results){
            r.setAndroidResponse(this);
        }
        this.results = results;
    }

    public Long getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(Long multicastId) {
        this.multicastId = multicastId;
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

    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }  

    @Override
    public String toString() {
        return "AndroidResponse{" + "multicast_id=" + multicastId 
                + ", success=" + success 
                + ", failure=" + failure + ", canonical_ids=" + canonicalIds 
                + ", results=" + results + '}';
    }

}
