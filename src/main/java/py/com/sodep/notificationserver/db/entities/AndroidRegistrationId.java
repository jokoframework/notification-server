/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "android_reg_id")
public class AndroidRegistrationId implements Serializable{

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

}
