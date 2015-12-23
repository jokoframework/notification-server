package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import py.com.sodep.notificationserver.rest.entities.EventoRequest;

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
    private Aplicacion aplicacion;

    @Column(name = "android_devices")
    @JsonIgnore
    private String androidDevices;

    @Column(name = "ios_devices")
    @JsonIgnore
    private String iosDevices;

    @Column(name = "send_to_sync")
    private boolean sendToSync;
    
    @Column(name = "production_mode")
    private boolean productionMode;
    private String alert;
    private String prioridad;
    @Column(name = "estado_android")
    private String estadoAndroid;
    @Column(name = "estado_ios")
    private String estadoIos;

    @OneToMany(targetEntity = Payload.class, //fetch = FetchType.EAGER,
            mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Payload> payloads;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "android_response_id")
    private AndroidResponse androidResponse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ios_response_id")
    private IosResponse iosResponse;
    

    public Evento() {
    }

    public Evento(EventoRequest e) {
        try {
            this.androidDevices = new ObjectMapper().writeValueAsString(e.getAndroidDevicesList());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.iosDevices = new ObjectMapper().writeValueAsString(e.getIosDevicesList());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Evento.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.sendToSync = e.isSendToSync();
        this.productionMode = e.isProductionMode();
        this.alert = e.getAlert();
        this.prioridad = e.getPrioridad();
        this.setPayload(e.getPayload());
        if (e.getAndroidDevicesList() != null && e.getAndroidDevicesList().size() > 0) {
            this.estadoAndroid = "PENDIENTE";
        } else {
            this.estadoAndroid = "NO APLICA";
        }
        if (e.getIosDevicesList() != null && e.getIosDevicesList().size() > 0) {
            this.estadoIos = "PENDIENTE";
        } else {
            this.estadoIos = "NO APLICA";
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
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

    public String getEstadoAndroid() {
        return estadoAndroid;
    }

    public void setEstadoAndroid(String estadoAndroid) {
        this.estadoAndroid = estadoAndroid;
    }

    public String getEstadoIos() {
        return estadoIos;
    }

    public void setEstadoIos(String estadoIos) {
        this.estadoIos = estadoIos;
    }

    public boolean isProductionMode() {
        return productionMode;
    }

    public void setProductionMode(boolean productionMode) {
        this.productionMode = productionMode;
    }

    public void setPayload(Object payload) {
        HashMap<String, String> pl = (HashMap<String, String>) payload;
        this.payloads = new ArrayList();
        if (pl != null) {
            for (Entry<String, String> e : pl.entrySet()) {
                Payload p = new Payload(e.getKey(), e.getValue());
                p.setEvento(this);
                payloads.add(p);
            }
        }
        System.out.println("PAYLOAD STRING: " + this.payloads.toString());
    }

    public ObjectNode getPayload() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode jn = new ObjectNode(factory);
        for (Payload s : this.payloads) {
            jn.put(s.getClave(), s.getValor());
        }
        System.out.println("ANDROID PAYLOAD: " + jn.toString());
        return jn;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public AndroidResponse getAndroidResponse() {
        return androidResponse;
    }

    public void setAndroidResponse(AndroidResponse androidResponse) {
        androidResponse.setEvento(this);
        this.androidResponse = androidResponse;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public IosResponse getIosResponse() {
        return iosResponse;
    }

    public void setIosResponse(IosResponse iosResponse) {
        iosResponse.setEvento(this);
        this.iosResponse = iosResponse;
    }

    @JsonIgnore
    public ObjectNode getObjectNodePayLoad() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode jn = new ObjectNode(factory);
        for (Payload s : this.payloads) {
            jn.put(s.getClave(), String.valueOf(s.getValor()));
        }
        System.out.println("ANDROID PAYLOAD: " + jn.toString());
        return jn;
    }

    @JsonIgnore
    public List<String> getIosDevicesList() {
        return (new Gson()).fromJson(iosDevices, List.class);
    }

    @JsonIgnore
    public List<String> getAndroidDevicesList() {
        return (new Gson()).fromJson(androidDevices, List.class);
    }
}
