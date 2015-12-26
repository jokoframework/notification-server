/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.rest.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import py.com.sodep.notificationserver.db.entities.Aplicacion;

/**
 *
 * @author Vanessa
 */
@JsonAutoDetect
public class EventoRequest implements Serializable{

    private Long id;

    private Aplicacion application;
    private boolean sendToSync;
    private boolean productionMode;
    private String alert;
    private String prioridad;

    private HashMap<String, String> payload;
    
    private List<String> androidDevicesList;

    private List<String> iosDevicesList;

    private String applicationName;

    public void setPayload(Object payload) {
        this.payload = (HashMap) payload;
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

    public boolean isSendToSync() {
        return sendToSync;
    }

    public void setSendToSync(boolean sendToSync) {
        this.sendToSync = sendToSync;
    }

    public boolean isProductionMode() {
        return productionMode;
    }

    public void setProductionMode(boolean productionMode) {
        this.productionMode = productionMode;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public HashMap<String, String> getPayload() {
        return payload;
    }

    public void setPayload(HashMap<String, String> payload) {
        this.payload = payload;
    }

    public List<String> getAndroidDevicesList() {
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
