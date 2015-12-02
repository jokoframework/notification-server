package py.com.sodep.notificationserver.db.entities.notification;


import java.util.ArrayList;
import java.util.List;

/**
 * Utilizado para generar JSon de notificaciones tipo Payload
 * que contienen información a mostrar en los dispositivos
 * Created by gaby.lorely on 19/04/2015.
 */
public class AndroidNotification {

    private String collapse_key = "payload";
  
    private List<String> registration_ids;
    
    private Object data;

   
    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public String getCollapse_key() {
        return collapse_key;
    }

    public void setCollapse_key(String collapse_key) {
        this.collapse_key = collapse_key;
    }


    public void addRegistrationId(String registrationId) {
        if(this.getRegistration_ids() == null){
            this.registration_ids = new ArrayList<String>();
        }
        this.getRegistration_ids().add(registrationId);
    }
}
