package py.com.sodep.notificationserver.facade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.Payload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import py.com.sodep.notificationserver.db.dao.ParametroDao;
import py.com.sodep.notificationserver.db.entities.notification.IosResponse;
import py.com.sodep.notificationserver.db.entities.notification.Result;
import py.com.sodep.notificationserver.exceptions.handlers.BusinessException;

public class ApnsFacade {

    @Inject
    Logger logger;
    @Inject
    ParametroDao parametroDao;

    public IosResponse send(Payload payload, File certificado, String keyFile,
            Boolean productionMode, List<String> devices) throws BusinessException {
        try {
            String threads = parametroDao.getByName("IOS_THREADS").getValor();
            PushedNotifications result = null;
            if (threads != null) {
                result = Push.payload(payload, certificado,
                        keyFile, productionMode, Integer.parseInt(threads), devices);
            } else {
                result = Push.payload(payload, certificado,
                        keyFile, productionMode, devices);
            }
            return procesarResponse(result, devices);
        } catch (CommunicationException | KeystoreException e) {
            throw new BusinessException(500, e);
        } catch (Exception ex) {
            throw new BusinessException(500, ex);
        }

    }

    private IosResponse procesarResponse(PushedNotifications result, List<String> devices) {
        IosResponse ios = new IosResponse();
        ios.setFailure(result.getFailedNotifications().size());
        ios.setSuccess(result.getSuccessfulNotifications().size());
        ios.setResults(new ArrayList<Result>());
        int canonicalIds = 0;
        for (int i = 0; i < result.size(); i++) {
            PushedNotification notification = result.get(i);
            logger.info("[iOS] Divice: " + notification.getDevice().getToken());
            Result r = new Result();

            String diviceToken = notification.getDevice().getToken();

            if (notification.isSuccessful()) {
                r.setMessage_id("OK");
            } else {
                Exception theProblem = notification.getException();
                if (theProblem != null) {
                    r.setError(theProblem.getMessage());
                    logger.error("[iOS] Excepcion retornada al notificar "
                            + diviceToken + " " + theProblem.getMessage());
                }
                /*
                 * If the problem was an error-response packet returned by
                 * Apple, get it
                 */
                ResponsePacket theErrorResponse = notification.getResponse();
                if (theErrorResponse != null) {
                    if (theErrorResponse.getStatus() == 2 || theErrorResponse.getStatus() == 5) {
                        r.setRegistration_id(diviceToken);
                        r.setOriginalRegistrationId(devices.get(i));
                        r.setStatus(theErrorResponse.getStatus());
                        canonicalIds++;
                    }
                    r.setError(theErrorResponse.getMessage());
                    logger.error("[iOS]  ResponsePacket Error: "
                            + theErrorResponse.getMessage());

                }
            }
            ios.getResults().add(r);
        }
        ios.setCanonical_ids(canonicalIds);
        return ios;
    }

}
