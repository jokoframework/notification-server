package py.com.sodep.notificationserver.db.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.ws.rs.FormParam;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

@Entity
@Table(name = "aplicacion")
public class Aplicacion implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6163667597446187934L;

    @Id
    @GeneratedValue
    @FormParam("id")
    @PartType("application/text")
    @Column(name = "application_id")
    private Long id;

    @Column(name = "nombre", unique = true)
    @FormParam("nombre")
    @PartType("application/text")
    private String nombre;

    @Column(name = "api_key_dev")
    @FormParam("apiKeyDev")
    @PartType("application/text")
    private String apiKeyDev;

    @Column(name = "api_key_prod")
    @FormParam("apiKeyProd")
    @PartType("application/text")
    private String apiKeyProd;

    @Column(name = "certificado_dev")
    @FormParam("certificadoDev")
    @PartType("application/text")
    private String certificadoDev;

    @Column(name = "certificado_prod")
    @FormParam("certificadoProd")
    @PartType("application/text")
    private String certificadoProd;

    @Column(name = "key_file_prod")
    @FormParam("keyFileProd")
    @PartType("application/text")
    private String keyFileProd;

    @Column(name = "key_file_dev")
    @FormParam("keyFileDev")
    @PartType("application/text")
    private String keyFileDev;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getKeyFileProd() {
        return keyFileProd;
    }

    public void setKeyFileProd(String keyFileProd) {
        this.keyFileProd = keyFileProd;
    }

    public String getKeyFileDev() {
        return keyFileDev;
    }

    public void setKeyFileDev(String keyFileDev) {
        this.keyFileDev = keyFileDev;
    }

    @Override
    public String toString() {
        return "Aplicacion{" + "id=" + id + ", nombre=" + nombre + ", apiKeyDev=" + apiKeyDev + ", apiKeyProd=" + apiKeyProd + ", certificadoDev=" + certificadoDev + ", certificadoProd=" + certificadoProd + ", keyFileProd=" + keyFileProd + ", keyFileDev=" + keyFileDev + '}';
    }

}
