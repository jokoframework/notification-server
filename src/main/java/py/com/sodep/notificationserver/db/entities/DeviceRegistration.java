package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Vanessa
 */
@Entity
@Table(name = "device_registration")
@JsonAutoDetect
public class DeviceRegistration implements Serializable {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "registration_id")
    String registrationId;

    @Column(name = "cannonical_id")
    String cannonicalId;

    String estado;
    String error;
    String accion;
    String tipo;

    @ManyToOne
    @JoinColumn(name = "aplicacion_id")
    @JsonIgnore
    private Aplicacion aplicacion;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    public DeviceRegistration() {
        //Default constructor
    }

    public DeviceRegistration(String registrationId, String cannonicalId,
            String estado, String error, Aplicacion aplicacion, String accion, String tipo) {
        this.registrationId = registrationId;
        this.cannonicalId = cannonicalId;
        this.error = error;
        this.aplicacion = aplicacion;
        this.estado = estado;
        this.accion = accion;
        this.tipo = tipo;
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

    public String getCannonicalId() {
        return cannonicalId;
    }

    public void setCannonicalId(String cannonicalId) {
        this.cannonicalId = cannonicalId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
