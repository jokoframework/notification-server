package py.com.sodep.notificationserver.business;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javapns.json.JSONException;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;

import javax.enterprise.context.RequestScoped;

import org.apache.log4j.Logger;

import py.com.sodep.notificationserver.db.dao.ApplicationDao;
import py.com.sodep.notificationserver.db.entities.Application;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.db.entities.notification.AndroidNotification;
import py.com.sodep.notificationserver.facade.ApnsFacade;
import py.com.sodep.notificationserver.facade.GcmFacade;

@RequestScoped
public class NotificationBusiness {

	final static Logger logger = Logger.getLogger(NotificationBusiness.class);

	public boolean notificar(Evento evento) {

		logger.info("----NOTIFICAR A DISPOSITIVOS-----");
		ApplicationDao appDao = new ApplicationDao();
		Application app = appDao.getByName(evento.getApplicationName());
		if (app != null) {
			if (evento.isProductionMode()) {
				notificarAndroid(app.getApiKeyProd(), evento);
				notificarIos(app.getCertificadoProd(), app.getKeyFileProd(),
						evento, true);
			} else {
				notificarAndroid(app.getApiKeyDev(), evento);
				notificarIos(app.getCertificadoDev(), app.getKeyFileProd(),
						evento, false);
			}
		} else {
			// Aplicación no encontrada
			return false;
		}
		return true;

	}

	@SuppressWarnings("rawtypes")
	private void notificarIos(String certifadoPath, String keyFile,
			Evento evento, Boolean productionMode) {
		logger.info("----NOTIFICAR IOS-----");
		ApnsFacade facade = new ApnsFacade();
		File certificado = new File(certifadoPath);
		Payload payload = PushNotificationPayload.complex();
		HashMap<String, String> pay = evento.getPayload();

		try {
			((PushNotificationPayload) payload).addAlert(evento
					.getDescripcion());

			((PushNotificationPayload) payload).addSound("default");

			Iterator it = pay.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				logger.info(pair.getKey() + " = " + pair.getValue());
				payload.addCustomDictionary((String) pair.getKey(),
						(String) pair.getValue());
			}

			facade.send(payload, certificado, keyFile, productionMode,
					evento.getIosDevicesList());

		} catch (JSONException e) {
			logger.error("[iOS] Error al parsear payload: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void notificarAndroid(String apiKey, Evento evento) {

		logger.info("notificando a Android");
		GcmFacade service = new GcmFacade();

		AndroidNotification notification = new AndroidNotification();
		notification.setRegistration_ids(evento.getAndroidDevicesList());
		notification.setData(evento.getPayload());

		service.send(apiKey, notification);
	}
}
