package py.com.sodep.notificationserver.db.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table
public class Evento {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "application_id")
	private Application application;
	
	private List<String> androidDevices;
	
	private List<String> iosDevices;
	
	private boolean sendToSync;
	
	private String estado;
	
	private boolean productionMode;
	
	private String payload;
	
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public boolean isProductionMode() {
		return productionMode;
	}
	public void setProductionMode(boolean productionMode) {
		this.productionMode = productionMode;
	}
	@Transient
	private String applicationName;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	
	public boolean isSendToSync() {
		return sendToSync;
	}
	public void setSendToSync(boolean sendToSync) {
		this.sendToSync = sendToSync;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<String> getAndroidDevices() {
		return androidDevices;
	}
	public void setAndroidDevices(List<String> androidDevices) {
		this.androidDevices = androidDevices;
	}
	public List<String> getIosDevices() {
		return iosDevices;
	}
	public void setIosDevices(List<String> iosDevices) {
		this.iosDevices = iosDevices;
	}
	
}
