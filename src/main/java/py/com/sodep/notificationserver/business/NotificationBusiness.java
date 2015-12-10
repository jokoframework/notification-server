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
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.db.entities.notification.AndroidNotification;
import py.com.sodep.notificationserver.db.entities.notification.AndroidResponse;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.exceptions.handlers.ExceptionMapperHelper;
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
    EventoDao eventoDao;
    @Inject
    Logger logger;

    public Evento crearEvento(Evento e) throws BusinessException {
        Aplicacion a = appDao.getByName(e.getApplicationName());
        if (a != null) {
            e.setApplication(a);
            e.setEstado("PENDIENTE");
            eventoDao.create(e);
            return e;
        } else {
            throw new BusinessException(ExceptionMapperHelper.appError.APLICACION_NOT_FOUND.ordinal(), "La aplicacion " + e.getApplicationName() + " no existe.");
        }
    }

    public Evento actualizarEvento(Evento e) {
        return eventoDao.create(e);
    }

    public boolean notificar(Evento e) throws BusinessException {
        Aplicacion app = appDao.getByName(e.getApplicationName());
        if (app != null) {
            try {
                if (e.isProductionMode()) {
                    notificarAndroid(app.getApiKeyProd(), e);
                    //notificarIos(app.getCertificadoProd(), app.getKeyFileProd(), e, true);
                } else {
                    notificarAndroid(app.getApiKeyDev(), e);
                    //notificarIos(app.getCertificadoDev(), app.getKeyFileProd(), e, false);
                }
            } catch (Exception ex) {
                logger.error("Error al enviar notificaciones: ", ex);
            }
        } else {
            throw new BusinessException(ExceptionMapperHelper.appError.APLICACION_NOT_FOUND.ordinal(), "La aplicacion " + e.getApplicationName() + " no existe.");
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

    private Evento notificarAndroid(String apiKey, Evento evento) {

        logger.info("[Evento: " + evento.getId() + "]: notificación android");
        if (evento.getAndroidDevicesList().size() == 1) {
            notification.setTo(evento.getAndroidDevicesList().get(0));
        } else {
            notification.setRegistration_ids(evento.getAndroidDevicesList());
        }
        notification.setData(evento.getPayload());
        AndroidResponse ar = service.send(apiKey, notification);
        ar.setEvento(evento);
        evento.setAndroidResponse(ar);
        eventoDao.create(evento);
        return evento;
    }
    
}
