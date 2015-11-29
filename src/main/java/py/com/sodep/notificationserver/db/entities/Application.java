
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
    private Long id;

    private String name;
    private String apiKeyDev;
    private String apiKeyProd;
    private String certificadoDev;
    private String certificadoProd;
    
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
