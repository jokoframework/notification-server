package py.com.sodep.notificationserver.business;

import javax.enterprise.context.RequestScoped;

import py.com.sodep.notificationserver.db.dao.ApplicationDao;
import py.com.sodep.notificationserver.db.entities.Application;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.db.entities.notification.AndroidNotification;
import py.com.sodep.notificationserver.facade.GcmFacade;

@RequestScoped
public class NotificationBusiness {

	public boolean notificar(Evento evento){
		
		ApplicationDao appDao = new ApplicationDao();
		Application app = appDao.getByName(evento.getApplicationName());
		if(app != null){
			if(evento.isProductionMode()){
				notificarAndroid(app.getApiKeyProd(), evento);
			}else{
				notificarAndroid(app.getApiKeyDev(), evento);
			}
		}else{
			//Aplicación no encontrada
			return false; 
		}
		return true;
		
	}

	private void notificarAndroid(String apiKey, Evento evento) {
		
		GcmFacade service = new GcmFacade();
		
		AndroidNotification notification = new AndroidNotification();
		notification.setRegistration_ids(evento.getAndroidDevices());
		notification.setData(evento.getPayload());
		
		service.send(apiKey,notification);
	}
}
