package py.com.sodep.notificationserver.business;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import javapns.json.JSONException;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.hibernate.HibernateException;
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
import py.com.sodep.notificationserver.exceptions.handlers.ExceptionMapperHelper;
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
    @Inject
    Logger logger;

    public EventoResponse crearEvento(Evento e, String appName) throws BusinessException, HibernateException, SQLException {
        Aplicacion a = appDao.getByName(appName);
        if (a != null) {
            e.setAplicacion(a);
            validate(e);
            eventoDao.create(e);
            EventoResponse er = new EventoResponse(e);
            verificarNotificacionBloqueada(e);
            return er;
        } else {
            throw new BusinessException(ExceptionMapperHelper.appError.APLICACION_NOT_FOUND.ordinal(), "La aplicacion " + appName + " no existe.");
        }
    }

    public Evento actualizarEvento(Evento e) throws HibernateException, SQLException {
        eventoDao.update(e);
        return e;
    }

    public Evento notificar(Evento e) throws BusinessException, HibernateException, SQLException, Exception {
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
            throw new BusinessException(ExceptionMapperHelper.appError.APLICACION_NOT_FOUND.ordinal(), "La aplicacion " + e.getAplicacion().getNombre() + " no existe.");
        }
        e.setEstadoAndroid("ENVIADO");
        e.setEstadoIos("ENVIADO");
        return e;
    }

    @SuppressWarnings("rawtypes")
    public IosResponse notificarIos(String certifadoPath, String keyFile,
            Evento evento, Boolean productionMode) throws BusinessException, HibernateException, SQLException {
        logger.info("[Evento: " + evento.getId() + "]: Notificando iOs");
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
                    logger.info(pair + " = " + pay.get(pair));
                    payload.addCustomDictionary((String) pair,
                            pay.get(pair).asText());
                }
            }
        } catch (JSONException e) {
            throw new BusinessException(ExceptionMapperHelper.appError.BAD_REQUEST.ordinal(), "Error al parsear payload en notificacion iOs.");
        }
        return facade.send(payload, certificado, keyFile, productionMode, evento.getIosDevicesList());

    }

    public AndroidResponse notificarAndroid(String apiKey, Evento evento) throws BusinessException, HibernateException, SQLException, Exception {

        logger.info("[Evento: " + evento.getId() + "]: notificando android");
        if (evento.getAndroidDevicesList().size() == 1) {
            logger.info("[Evento: " + evento.getId() + "]: Un solo device. Notificando android");
            notification.setTo(evento.getAndroidDevicesList().get(0));
        } else {
            logger.info("[Evento: " + evento.getId() + "]: Lista. Notificando android");
            notification.setRegistration_ids(evento.getAndroidDevicesList());
        }

        notification.setData(evento.getObjectNodePayLoad().put("alert", evento.getAlert()));

        AndroidResponse ar = service.send(apiKey, notification);

        if (ar.getFailure() > 0) {
            for (int i = 0; i < ar.getResults().size(); i++) {
                Result r = ar.getResults().get(i);
                logger.info("Analizando resultado: " + r);
                if (r.getError() != null && (r.getError().equals("NotRegistered")
                        || r.getError().equals("DeviceMessageRate")
                        || r.getError().equals("InvalidRegistration")
                        || r.getError().equals("MissingRegistration"))) {
                    DeviceRegistration d = new DeviceRegistration(
                            evento.getAndroidDevicesList().get(i), r.getRegistration_id(),
                            "NUEVO", r.getError(), evento.getAplicacion());
                    deviceDao.create(d);
                }
                if (r.getError() != null && (r.getError().equals("InvalidPackageName")
                        || r.getError().equals("MismatchSenderId"))) {
                    logger.info("Se bloquea la aplicación: " + r.getError());
                    Aplicacion a = evento.getAplicacion();
                    a.setError(r.getError());
                    a.setEstadoAndroid("BLOQUEADA");
                    appDao.create(a);
                }
            }
        }
        return ar;
    }

    public void validate(Evento e) throws BusinessException {
        String s = e.getAlert() + e.getPayload().asText();
        if (s.getBytes().length > e.getAplicacion().getPayloadSize()) {
            throw new BusinessException(500, "El tamaño del payload supera el "
                    + "configurado para la aplicación: "
                    + e.getAplicacion().getPayloadSize());
        }
    }

    public void verificarNotificacionBloqueada(Evento e) throws BusinessException {
        if (e.getAplicacion().getEstadoAndroid() != null
                && e.getAplicacion().getEstadoAndroid().equals("BLOQUEADA")
                && (e.getAndroidDevicesList() != null
                && e.getAndroidDevicesList().size() > 0)) {
            throw new BusinessException(
                    ExceptionMapperHelper.appError.APLICACION_BLOCKED.ordinal(),
                    "La aplicacion " + e.getAplicacion().getNombre()
                    + " esta bloqueada para notificaciones Android. Error: " + e.getAplicacion().getError());
        }
        if (e.getAplicacion().getEstadoIos() != null
                && e.getAplicacion().getEstadoIos().equals("BLOQUEADA")
                && (e.getIosDevicesList() != null
                && e.getIosDevicesList().size() > 0)) {
            throw new BusinessException(
                    ExceptionMapperHelper.appError.APLICACION_BLOCKED.ordinal(),
                    "La aplicacion " + e.getAplicacion().getNombre()
                    + " esta bloqueada para notificaciones iOs. Error: " + e.getAplicacion().getError());
        }

    }
}
