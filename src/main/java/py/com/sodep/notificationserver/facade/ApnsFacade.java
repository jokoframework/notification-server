package py.com.sodep.notificationserver.facade;

import java.io.File;
import java.util.List;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.Payload;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import javapns.notification.ResponsePacket;

import org.apache.log4j.Logger;

public class ApnsFacade {

	final static Logger logger = Logger.getLogger(ApnsFacade.class);

	public void send(Payload payload, File certificado, String keyFile,
			Boolean productionMode, List<String> devices) {

		try {
			PushedNotifications result = Push.payload(payload, certificado,
					keyFile, productionMode, devices);
			procesarResponse(result);
		} catch (CommunicationException | KeystoreException e) {
			logger.error("[iOS] Error al enviar notificacion: "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	private void procesarResponse(PushedNotifications result) {
		for (int i = 0; i < result.size(); i++) {
			logger.info("[iOS] Divice: " + result.get(i).getDevice().getToken());

			PushedNotification notification = result.get(i);
			String diviceToken = notification.getDevice().getToken();
			if (notification.isSuccessful()) {
				/* Apple accepted the notification and should deliver it */
				logger.info("[iOS] Push notification sent successfully to: "
						+ diviceToken);

			} else {
				String invalidToken = diviceToken;

				/* Find out more about what the problem was */
				Exception theProblem = notification.getException();
				if (theProblem != null) {
					logger.error("[iOS] Excepcion retornada al notificar "
							+ invalidToken + " " + theProblem.getMessage());
				}

				/*
				 * If the problem was an error-response packet returned by
				 * Apple, get it
				 */
				ResponsePacket theErrorResponse = notification.getResponse();
				if (theErrorResponse != null) {
					logger.error("[iOS]  ResponsePacket Error: "
							+ theErrorResponse.getMessage());
				}
			}
		}

	}

}
