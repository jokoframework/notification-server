package py.com.sodep.notificationserver.db.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table
public class Aplicacion implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6163667597446187934L;

    @Id
    @GeneratedValue
    @Column(name = "application_id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;
    @Column(name = "api_key_dev")
    private String apiKeyDev;
    @Column(name = "api_key_prod")
    private String apiKeyProd;
    @Column(name = "certificado_dev")
    private String certificadoDev;
    @Column(name = "certificado_prod")
    private String certificadoProd;

    public Aplicacion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String name) {
        this.nombre = name;
    }

    public String getApiKeyDev() {
        return apiKeyDev;
    }

    public void setApiKeyDev(String apiKeyDev) {
        this.apiKeyDev = apiKeyDev;
    }

    public String getApiKeyProd() {
        return apiKeyProd;
    }

    public void setApiKeyProd(String apiKeyProd) {
        this.apiKeyProd = apiKeyProd;
    }

    public String getCertificadoDev() {
        return certificadoDev;
    }

    public void setCertificadoDev(String certificadoDev) {
        this.certificadoDev = certificadoDev;
    }

    public String getCertificadoProd() {
        return certificadoProd;
    }

    public void setCertificadoProd(String certificadoProd) {
        this.certificadoProd = certificadoProd;
    }

    @Override
    public String toString() {
        return "Application{" + "id=" + id + ", name=" + nombre + ", apiKeyDev=" + apiKeyDev + ", apiKeyProd=" + apiKeyProd + ", certificadoDev=" + certificadoDev + ", certificadoProd=" + certificadoProd + '}';
    }

}