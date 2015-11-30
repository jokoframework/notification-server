package py.com.sodep.notificationserver.db.entities;

import java.io.Serializable;
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
public class Evento implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;
    private String androidDevices;
    private String iosDevices;
    private boolean sendToSync;
    private String estado;
    private boolean productionMode;
    private String payload;
    @Transient
    private List<String> androidDevicesList;
    @Transient
    private List<String> iosDevicesList;
    @Transient
    private String applicationName;

    public Evento() {
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

    public String getAndroidDevices() {
        return androidDevices;
    }

    public void setAndroidDevices(String androidDevices) {
        this.androidDevices = androidDevices;
    }

    public String getIosDevices() {
        return iosDevices;
    }

    public void setIosDevices(String iosDevices) {
        this.iosDevices = iosDevices;
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

    public boolean isProductionMode() {
        return productionMode;
    }

    public void setProductionMode(boolean productionMode) {
        this.productionMode = productionMode;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public List<String> getAndroidDevicesList() {
        //to-do: parsear lista de registration ids en el string de androidDevices
        return androidDevicesList;
    }

    public void setAndroidDevicesList(List<String> androidDevicesList) {
        this.androidDevicesList = androidDevicesList;
    }

    public List<String> getIosDevicesList() {
        return iosDevicesList;
    }

    public void setIosDevicesList(List<String> iosDevicesList) {
        this.iosDevicesList = iosDevicesList;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

}
