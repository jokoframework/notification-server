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
        int canonicalIds = 0;
        for (PushedNotification notification : result) {
            LOGGER.info("[iOS] Divice: " + notification.getDevice().getToken());
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
                    if (theErrorResponse.getStatus() == 2 || theErrorResponse.getStatus() == 5 || theErrorResponse.getStatus() == 8) {
                        r.setOriginalRegistrationId(diviceToken);
                        canonicalIds++;
                    }
                    r.setMessageId(String.valueOf(theErrorResponse.getIdentifier()));
                    r.setStatus(theErrorResponse.getStatus());
                    r.setError(theErrorResponse.getMessage());
                }
            }
            r.setIosResponse(ios);
            ios.getResults().add(r);
        }
        ios.setCanonical_ids(canonicalIds);
        return ios;
    }
    /*
     if (status == 0) return prefix + "No errors encountered";
     if (status == 1) return prefix + "Processing error";
     if (status == 2) return prefix + "Missing device token";
     if (status == 3) return prefix + "Missing topic";
     if (status == 4) return prefix + "Missing payload";
     if (status == 5) return prefix + "Invalid token size";
     if (status == 6) return prefix + "Invalid topic size";
     if (status == 7) return prefix + "Invalid payload size";
     if (status == 8) return prefix + "Invalid token";
     if (status == 255) return prefix + "None (unknown)";
     return prefix + "Undocumented status code: " + status;
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
