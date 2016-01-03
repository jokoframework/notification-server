package py.com.sodep.notificationserver.business;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.util.Iterator;
import javapns.json.JSONException;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import py.com.sodep.notificationserver.db.dao.AplicacionDao;
import py.com.sodep.notificationserver.db.dao.DeviceRegistrationDao;
import py.com.sodep.notificationserver.db.dao.EventoDao;
import py.com.sodep.notificationserver.db.entities.Aplicacion;
import py.com.sodep.notificationserver.db.entities.Evento;
import py.com.sodep.notificationserver.db.entities.AndroidNotification;
import py.com.sodep.notificationserver.db.entities.AndroidResponse;
import py.com.sodep.notificationserver.db.entities.DeviceRegistration;
import py.com.sodep.notificationserver.db.entities.IosResponse;
import py.com.sodep.notificationserver.db.entities.Result;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.config.GlobalCodes;
import py.com.sodep.notificationserver.facade.ApnsFacade;
import py.com.sodep.notificationserver.facade.GcmFacade;
import py.com.sodep.notificationserver.rest.entities.EventoResponse;

@ApplicationScoped
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
    DeviceRegistrationDao deviceDao;

    private static final Logger LOGGER = Logger.getLogger(NotificationBusiness.class);

    public EventoResponse crearEvento(Evento e, String appName) throws BusinessException {
        Aplicacion a = appDao.getByName(appName);
        if (a != null) {
            e.setAplicacion(a);
            validate(e);
            eventoDao.create(e);
            EventoResponse er = new EventoResponse(e);
            verificarNotificacionBloqueada(e);
            return er;
        } else {
            throw new BusinessException(GlobalCodes.errors.APLICACION_NOT_FOUND, "La aplicacion " + appName + " no existe.");
        }
    }

    public Evento actualizarEvento(Evento e) {
        eventoDao.update(e);
        return e;
    }

    public Evento notificar(Evento e) throws BusinessException {
        Aplicacion app = appDao.getByName(e.getAplicacion().getNombre());
        if (app != null) {
            if (e.isProductionMode()) {
                if (app.getApiKeyProd() != null) {
                    e.setAndroidResponse(notificarAndroid(app.getApiKeyProd(), e));
                }
                if (app.getCertificadoProd() != null && app.getKeyFileProd() != null) {
                    e.setIosResponse(notificarIos(app.getCertificadoProd(), app.getKeyFileProd(), e, true));
                }
            } else {
                if (app.getApiKeyDev() != null) {
                    e.setAndroidResponse(notificarAndroid(app.getApiKeyDev(), e));
                }
                if (app.getCertificadoDev() != null && app.getKeyFileDev() != null) {
                    e.setIosResponse(notificarIos(app.getCertificadoDev(), app.getKeyFileDev(), e, false));
                }
            }
        } else {
            throw new BusinessException(GlobalCodes.errors.APLICACION_NOT_FOUND, "La aplicacion " + e.getAplicacion().getNombre() + " no existe.");
        }
        e.setEstadoAndroid(GlobalCodes.ENVIADO);
        e.setEstadoIos(GlobalCodes.ENVIADO);
        return e;
    }

    @SuppressWarnings("rawtypes")
    public IosResponse notificarIos(String certifadoPath, String keyFile,
            Evento evento, Boolean productionMode) throws BusinessException {
        LOGGER.info("[Evento: " + evento.getId() + "]: Notificando iOs");
        File certificado = new File(certifadoPath);
        Payload payload = PushNotificationPayload.complex();
        ObjectNode pay = evento.getObjectNodePayLoad();
        try {
            if (evento.isSendToSync()) {
                ((PushNotificationPayload) payload).addCustomDictionary("content-available", "1");
            } else {
                ((PushNotificationPayload) payload).addAlert(evento
                        .getAlert());

                ((PushNotificationPayload) payload).addSound("default");
                if (evento.isSendToSync()) {
                    ((PushNotificationPayload) payload).addSound("default");
                }
                Iterator it = pay.fieldNames();
                while (it.hasNext()) {
                    String pair = (String) it.next();
                    LOGGER.info(pair + " = " + pay.get(pair));
                    payload.addCustomDictionary((String) pair,
                            pay.get(pair).asText());
                }
            }
        } catch (JSONException e) {
            LOGGER.error(e);
            throw new BusinessException(GlobalCodes.errors.BAD_REQUEST, "Error al parsear payload en notificacion iOs.");
        }
        return facade.send(payload, certificado, keyFile, productionMode, evento.getIosDevicesList());

    }

    public AndroidResponse notificarAndroid(String apiKey, Evento evento) throws BusinessException {
        LOGGER.info("[Evento: " + evento.getId() + "]: notificando android");
        LOGGER.info("[Evento: " + evento.getId() + "]: Lista. Notificando android");
        notification.setRegistrationIds(evento.getAndroidDevicesList());
        notification.setData(evento.getObjectNodePayLoad().put("alert", evento.getAlert()));

        AndroidResponse ar;
        ar = service.send(apiKey, notification);

        procesarErrores(evento, ar);
        return ar;
    }

    public void procesarErrores(Evento evento, AndroidResponse ar) {
        if (ar.getFailure() > 0) {
            for (int i = 0; i < ar.getResults().size(); i++) {
                Result r = ar.getResults().get(i);
                LOGGER.info("Analizando resultado: " + r);
                if (r.getError() != null && (r.getError().equals(GlobalCodes.NotRegistered)
                        || r.getError().equals(GlobalCodes.InvalidRegistration)
                        || r.getError().equals(GlobalCodes.MissingRegistration))) {
                    DeviceRegistration d = new DeviceRegistration(
                            evento.getAndroidDevicesList().get(i),
                            r.getRegistrationId(),
                            GlobalCodes.NUEVO,
                            r.getError(),
                            evento.getAplicacion(),
                            GlobalCodes.getAccion(r.getError()),
                            GlobalCodes.ANDROID);
                    deviceDao.create(d);
                }
                if (r.getError() != null && (r.getError().equals(GlobalCodes.InvalidPackageName)
                        || r.getError().equals(GlobalCodes.MismatchSenderId))) {
                    LOGGER.info("Se bloquea la aplicación: " + r.getError());
                    Aplicacion a = evento.getAplicacion();
                    a.setError(r.getError());
                    a.setEstadoAndroid(GlobalCodes.BLOQUEADA);
                    appDao.create(a);
                }
            }
        }
    }

    public void validate(Evento e) throws BusinessException {
        String s = e.getAlert() + e.getPayload().asText();
        if (s.getBytes().length > e.getAplicacion().getPayloadSize()) {
            throw new BusinessException(GlobalCodes.errors.PAYLOAD_SIZE, "El tamaño del payload supera el "
                    + "configurado para la aplicación: "
                    + e.getAplicacion().getPayloadSize());
        }
        if (e.getAndroidDevicesList() != null && e.getAndroidDevicesList().size() > 1000) {
            throw new BusinessException(GlobalCodes.errors.DEVICES_SIZE, "No se pueden enviar notificaciones a mas de 1000 dispositivos.");
        }
        if (e.getIosDevicesList() != null && e.getIosDevicesList().size() > 1000) {
            throw new BusinessException(GlobalCodes.errors.DEVICES_SIZE, "No se pueden enviar notificaciones a mas de 1000 dispositivos.");
        }
    }

    public void verificarNotificacionBloqueada(Evento e) throws BusinessException {
        if (e.getAplicacion().getEstadoAndroid() != null
                && e.getAplicacion().getEstadoAndroid().equals(GlobalCodes.BLOQUEADA)
                && e.isAndroidEvent()) {
            throw new BusinessException(
                    GlobalCodes.errors.APLICACION_BLOCKED,
                    "La aplicacion " + e.getAplicacion().getNombre()
                    + " esta bloqueada para notificaciones Android. Error: " + e.getAplicacion().getError());
        }
        if (e.getAplicacion().getEstadoIos() != null
                && e.getAplicacion().getEstadoIos().equals(GlobalCodes.BLOQUEADA)
                && e.isIosEvent()) {
            throw new BusinessException(
                    GlobalCodes.errors.APLICACION_BLOCKED,
                    "La aplicacion " + e.getAplicacion().getNombre()
                    + " esta bloqueada para notificaciones iOs. Error: " + e.getAplicacion().getError());
        }

    }
}
