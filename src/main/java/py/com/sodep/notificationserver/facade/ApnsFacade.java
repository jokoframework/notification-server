package py.com.sodep.notificationserver.facade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.notification.Payload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.IosResponse;
import py.com.sodep.notificationserver.db.entities.Result;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;
import py.com.sodep.notificationserver.exceptions.handlers.GlobalCodes;

public class ApnsFacade {

    private static final Logger LOGGER = Logger.getLogger(ApnsFacade.class);

    @Inject
    ParametroDao parametroDao;

    public IosResponse send(Payload payload, File certificado, String keyFile,
            Boolean productionMode, List<String> devices) throws BusinessException {
        try {
            String threads = parametroDao.getByName("IOS_THREADS").getValor();
            PushedNotifications result;
            if (threads != null) {
                result = Push.payload(payload, certificado,
                        keyFile, productionMode, Integer.parseInt(threads), devices);
            } else {
                result = Push.payload(payload, certificado,
                        keyFile, productionMode, devices);
            }
            return procesarResponse(result);
        } catch (CommunicationException e) {
            throw new BusinessException(GlobalCodes.errors.IOS_COMM, e);
        } catch (KeystoreException e1) {
            throw new BusinessException(GlobalCodes.errors.IOS_KEY_STORE, e1);
        } catch (Exception ex) {
            throw new BusinessException(GlobalCodes.errors.IOS_PUSH_PAYLOAD, ex);
        }
    }

    private static IosResponse procesarResponse(PushedNotifications result) {
        IosResponse ios = new IosResponse();
        ios.setFailure(result.getFailedNotifications().size());
        ios.setSuccess(result.getSuccessfulNotifications().size());
        ios.setResults(new ArrayList<Result>());
        for (PushedNotification notification : result) {
            LOGGER.info("[iOs] Divice: " + notification.getDevice().getToken());
            Result r = procesarPushed(notification);
            r.setIosResponse(ios);
            ios.getResults().add(r);
        }
        return ios;
    }

    public static Result procesarPushed(PushedNotification notification) {
        Result r = new Result();
        String diviceToken = notification.getDevice().getToken();
        if (notification.isSuccessful()) {
            r.setMessageId("OK");
        } else {
            Exception theProblem = notification.getException();
            if (theProblem != null) {
                r.setError(theProblem.getMessage());
                LOGGER.error("[iOS] Excepcion retornada al notificar "
                        + diviceToken + " " + theProblem.getMessage());
            }
            /*
             * If the problem was an error-response packet returned by
             * Apple, get it
             */
            ResponsePacket theErrorResponse = notification.getResponse();
            if (theErrorResponse != null) {
                if (GlobalCodes.iosTokenError.contains(String.valueOf(theErrorResponse.getStatus()))) {
                    r.setOriginalRegistrationId(diviceToken);
                }
                r.setMessageId(String.valueOf(theErrorResponse.getIdentifier()));
                r.setStatus(theErrorResponse.getStatus());
                r.setError(theErrorResponse.getMessage());
            }
        }
        return r;
    }
    /**
     * 
     * @param certificado
     * @param keyFile
     * @param productionMode
     * @return
     * @throws BusinessException 
     */
    public List<Device> getInactiveDevices(File certificado, String keyFile,
            Boolean productionMode) throws BusinessException {
        try {
            Vector<Device> lista;
            lista = (Vector) Push.feedback(certificado, keyFile, productionMode);
            return lista;
        } catch (CommunicationException e) {
            throw new BusinessException(GlobalCodes.errors.IOS_COMM, e);
        } catch (KeystoreException e1) {
            throw new BusinessException(GlobalCodes.errors.IOS_KEY_STORE, e1);
        }
    }
}
