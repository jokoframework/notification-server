package py.com.sodep.notificationserver.db.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToOne;

/**
 * Utilizado para generar JSon de notificaciones tipo Payload que contienen
 * información a mostrar en los dispositivos Created by gaby.lorely on
 * 19/04/2015.
 */
public class AndroidNotification {

    @JsonProperty(value = "collapse_key")
    private String collapseKey = "payload";

    private String to;
    @JsonProperty(value = "registration_ids")
    private List<String> registrationIds;

    private Object data;

    @OneToOne(mappedBy = "androidResponse")
    private Evento evento;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<String> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(List<String> registrationIds) {
        this.registrationIds = registrationIds;
    }

    public String getCollapseKey() {
        return collapseKey;
    }

    public void setCollapseKey(String collapseKey) {
        this.collapseKey = collapseKey;
    }

    public void addRegistrationId(String registrationId) {
        if (this.getRegistrationIds() == null) {
            this.registrationIds = new ArrayList<String>();
        }
        this.getRegistrationIds().add(registrationId);
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return "AndroidNotification{" + "collapse_key=" + collapseKey + ", to=" + to + ", registration_ids=" + registrationIds + ", data=" + data + ", evento=" + evento + '}';
    }

}
