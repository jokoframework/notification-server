package py.com.sodep.notificationserver.business;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.util.Iterator;
import javapns.json.JSONException;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.db.entities.notification.AndroidNotification;
import py.com.sodep.notificationserver.facade.ApnsFacade;
import py.com.sodep.notificationserver.facade.GcmFacade;

@RequestScoped
public class NotificationBusiness {

    @Inject
    AplicacionDao appDao;
    @Inject
    ApnsFacade facade;
    @Inject
    GcmFacade service;
    @Inject
    AndroidNotification notification;
    @Inject
    Logger logger;

    public boolean notificar(Evento evento) {

        //AplicacionDao appDao = new AplicacionDao();
        Aplicacion app = appDao.getByName(evento.getApplicationName());
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
        //ApnsFacade facade = new ApnsFacade();
        File certificado = new File(certifadoPath);
        Payload payload = PushNotificationPayload.complex();
        ObjectNode pay = evento.getPayload();

        try {
            ((PushNotificationPayload) payload).addAlert(evento
                    .getDescripcion());

            ((PushNotificationPayload) payload).addSound("default");

            Iterator it = pay.fieldNames();
            while (it.hasNext()) {
                String pair = (String) it.next();
                logger.info(pair + " = " + pay.get(pair));
                payload.addCustomDictionary((String) pair,
                        pay.get(pair).asText());
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
        //GcmFacade service = new GcmFacade();
        //AndroidNotification notification = new AndroidNotification();
        notification.setRegistration_ids(evento.getAndroidDevicesList());
        notification.setData(evento.getPayload());

        service.send(apiKey, notification);
    }
}
