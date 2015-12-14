/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import py.com.sodep.notificationserver.db.entities.Evento;

/**
 *
 * @author Vanessa
 */
@Entity
@Table(name = "ios_response")
@JsonAutoDetect
public class IosResponse implements Serializable {

    @JsonProperty("id")
    @Id
    @GeneratedValue
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
            mappedBy = "iosResponse", cascade = CascadeType.ALL)
    List<Result> results;

    String error;

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

    public void setCanonical_ids(Integer canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
