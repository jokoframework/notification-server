package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
import py.com.sodep.notificationserver.exceptions.handlers.GlobalCodes;
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

    @OneToMany(targetEntity = Payload.class,
            mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Payload> payloads;

    @OneToMany(targetEntity = AndroidRegistrationId.class,
            mappedBy = "evento", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AndroidRegistrationId> androidDevices;

    @OneToMany(targetEntity = IosRegistrationId.class,
            mappedBy = "evento", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<IosRegistrationId> iosDevices;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "android_response_id")
    private AndroidResponse androidResponse;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ios_response_id")
    private IosResponse iosResponse;

    public Evento(EventoRequest e) {

        if (e.getAndroidDevicesList() != null) {
            this.androidDevices = new ArrayList<>();
            for (String s : e.getAndroidDevicesList()) {
                this.androidDevices.add(new AndroidRegistrationId(s, this));
            }
        }

        if (e.getIosDevicesList() != null) {
            this.iosDevices = new ArrayList<>();
            for (String s : e.getIosDevicesList()) {
                this.iosDevices.add(new IosRegistrationId(s, this));
            }
        }

        this.sendToSync = e.isSendToSync();
        this.productionMode = e.isProductionMode();
        this.alert = e.getAlert();
        this.prioridad = e.getPrioridad();
        this.setPayload(e.getPayload());
        if (e.getAndroidDevicesList() != null && !e.getAndroidDevicesList().isEmpty()) {
            this.estadoAndroid = GlobalCodes.PENDIENTE;
        } else {
            this.estadoAndroid = GlobalCodes.NO_APLICA;
        }
        if (e.getIosDevicesList() != null && !e.getIosDevicesList().isEmpty()) {
            this.estadoIos = GlobalCodes.PENDIENTE;
        } else {
            this.estadoIos = GlobalCodes.NO_APLICA;
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
    }

    public ObjectNode getPayload() {
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode jn = new ObjectNode(factory);
        for (Payload s : this.payloads) {
            jn.put(s.getClave(), s.getValor());
        }

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
        return jn;
    }

    public List<Payload> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<Payload> payloads) {
        this.payloads = payloads;
    }

    public List<AndroidRegistrationId> getAndroidDevices() {
        return androidDevices;
    }

    public void setAndroidDevices(List<AndroidRegistrationId> androidDevices) {
        this.androidDevices = androidDevices;
    }

    public List<IosRegistrationId> getIosDevices() {
        return iosDevices;
    }

    public void setIosDevices(List<IosRegistrationId> iosDevices) {
        this.iosDevices = iosDevices;
    }

    @JsonIgnore
    public List<String> getIosDevicesList() {
        List<String> devices = new ArrayList<>();
        if (this.getIosDevices() != null) {
            for (IosRegistrationId ri : this.getIosDevices()) {
                devices.add(ri.getToken());
            }
        }
        return devices;
    }

    @JsonIgnore
    public List<String> getAndroidDevicesList() {
        List<String> devices = new ArrayList<>();
        if (this.getAndroidDevices() != null) {
            for (AndroidRegistrationId ri : this.getAndroidDevices()) {
                devices.add(ri.getRegistrationId());
            }
        }
        return devices;
    }

    public boolean isIosEvent() {
        return this.getIosDevicesList() != null
                && !this.getIosDevicesList().isEmpty();
    }

    public boolean isAndroidEvent() {
        return this.getAndroidDevicesList() != null
                && !this.getAndroidDevicesList().isEmpty();
    }

    @Override
    public String toString() {
        return "Evento{" + "id=" + id + ", aplicacion=" + aplicacion + ", sendToSync=" + sendToSync + ", productionMode=" + productionMode + ", alert=" + alert + ", prioridad=" + prioridad + ", estadoAndroid=" + estadoAndroid + ", estadoIos=" + estadoIos + ", payloads=" + payloads + ", androidDevices=" + androidDevices + ", iosDevices=" + iosDevices + ", androidResponse=" + androidResponse + ", iosResponse=" + iosResponse + '}';
    }

}
