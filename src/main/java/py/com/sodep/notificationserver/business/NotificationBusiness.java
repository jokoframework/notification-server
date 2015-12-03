package py.com.sodep.notificationserver.business;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.json.JSONException;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

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
				notificarIos(app.getCertificadoProd(),app.getKeyFileProd(),evento,true);
			}else{
				notificarAndroid(app.getApiKeyDev(), evento);
				notificarIos(app.getCertificadoDev(),app.getApiKeyProd(),evento,false);
			}
		}else{
			//Aplicación no encontrada
			return false; 
		}
		return true;
		
	}

	private void notificarIos(String certifadoPath, String keyFile,
			Evento evento, Boolean productionMode) {
		File certificado = new File(certifadoPath);
		Payload payload = PushNotificationPayload.complex();
		HashMap<String, String> pay = evento.getPayload();
		try {
			((PushNotificationPayload)payload).addAlert(evento.getDescripcion());
			 ((PushNotificationPayload)payload).addSound("default");
			 
			 
			 Iterator it = pay.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println(pair.getKey() + " = " + pair.getValue());
			        payload.addCustomDictionary((String)pair.getKey(), (String)pair.getValue());
			    }
			    
			    PushedNotifications result = Push.payload(payload, certificado, keyFile, productionMode, evento.getId());
		} catch (JSONException | CommunicationException | KeystoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		
	}

	private void notificarAndroid(String apiKey, Evento evento) {
		
		GcmFacade service = new GcmFacade();
		
		AndroidNotification notification = new AndroidNotification();
		notification.setRegistration_ids(evento.getAndroidDevicesList());
		notification.setData(evento.getPayload());
		
		service.send(apiKey,notification);
	}
}
