package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import py.com.sodep.notificationserver.db.entities.notification.AndroidResponse;

@Entity
@Table
public class Evento implements Serializable {

    private static final long serialVersionUID = 6055960223584022815L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aplicacion_id")
    @JsonIgnore
    private Aplicacion application;

    @Column(name = "android_devices", length = 10240)
    @JsonIgnore
    private String androidDevices;

    @Column(name = "ios_devices", length = 10240)
    @JsonIgnore
    private String iosDevices;

    private boolean sendToSync;
    private String estado;
    private boolean productionMode;
    private String descripcion;

    @OneToMany(targetEntity = Payload.class, fetch = FetchType.EAGER,
            mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Payload> payloads;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "android_response_id")
    private AndroidResponse androidResponse;

    @Transient
    private HashMap<String, String> payload;

    @Transient
    @Column(length = 1)
    private List<String> androidDevicesList;

    @Transient
    @Column(length = 1)
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

    public Aplicacion getApplication() {
        return application;
    }

    public void setApplication(Aplicacion application) {
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

    public void setPayload(Object payload) {
        this.payload = (HashMap) payload;
        System.out.println("PAYLOAD STRING: " + this.payload.toString());
    }

    public ObjectNode getPayload() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode jn = new ObjectNode(factory);
        HashMap<String, String> map = (HashMap<String, String>) this.payload;
        for (String s : map.keySet()) {
            jn.put(s, String.valueOf(map.get(s)));
        }
        System.out.println("ANDROID PAYLOAD: " + jn.toString());
        return jn;
    }

    public List<String> getAndroidDevicesList() {
        //to-do: parsear lista de registration ids en el string de androidDevices
        if (androidDevicesList == null || androidDevicesList.size() == 0) {
            ObjectMapper ob = new ObjectMapper();
            try {
                androidDevicesList
                        = ob.readValue(androidDevices, ob.getTypeFactory().constructCollectionType(List.class, String.class));
            } catch (IOException ex) {
                Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return androidDevicesList;
    }

    public void setAndroidDevicesList(List<String> androidDevicesList) {
        this.androidDevicesList = androidDevicesList;
        try {
            this.androidDevices = new ObjectMapper().writeValueAsString(this.androidDevicesList);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> getIosDevicesList() {
        if (iosDevicesList == null || iosDevicesList.size() == 0) {
            ObjectMapper ob = new ObjectMapper();
            try {
                iosDevicesList
                        = ob.readValue(iosDevices, ob.getTypeFactory().constructCollectionType(List.class, String.class));
            } catch (IOException ex) {
                Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return iosDevicesList;
    }

    public void setIosDevicesList(List<String> iosDevicesList) {
        this.iosDevicesList = iosDevicesList;
        try {
            this.iosDevices = new ObjectMapper().writeValueAsString(this.iosDevicesList);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public AndroidResponse getAndroidResponse() {
        return androidResponse;
    }

    public void setAndroidResponse(AndroidResponse androidResponse) {
        this.androidResponse = androidResponse;
    }

}
