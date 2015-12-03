package py.com.sodep.notificationserver.db.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table
public class Application implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6163667597446187934L;

    @Id
    @GeneratedValue
    @Column(name = "application_id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "api_key_dev")
    private String apiKeyDev;
    @Column(name = "api_key_prod")
    private String apiKeyProd;
    @Column(name = "certificado_dev")
    private String certificadoDev;
    @Column(name = "certificado_prod")
    private String certificadoProd;
    
    @Column(name = "key_file_prod")
    private String keyFileProd;
    
    @Column(name = "key_file_dev")
    private String keyFileDev;

    

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

	public Application() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Application{" + "id=" + id + ", name=" + name + ", apiKeyDev=" + apiKeyDev + ", apiKeyProd=" + apiKeyProd + ", certificadoDev=" + certificadoDev + ", certificadoProd=" + certificadoProd + '}';
    }

}
