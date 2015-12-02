package py.com.sodep.notificationserver.facade;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import javax.inject.Inject;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import py.com.sodep.notificationserver.db.entities.notification.AndroidNotification;
import py.com.sodep.notificationserver.util.Parametro;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Se comunica con el servidor GCM para enviar la notificación correspondiente
 * 
 * @author Gabriela Gaona <gabriela.gaona@konecta.com.py>
 *
 */
/**
 * Se comunica con el servidor GCM para enviar la notificación correspondiente
 * 
 * @author Gabriela Gaona <gabriela.gaona@konecta.com.py>
 *
 */
public class GcmFacade {

	@Inject
	ObjectMapper map;

	public void send(String apiKey, AndroidNotification notification) {
		System.out.println("API KEY: " + apiKey);
		ClientRequest request = new ClientRequest(Parametro.URL_GCM);
		request.accept("application/json");
		request.header("Authorization", "key=" + apiKey);

		try {
			map = new ObjectMapper();
			String jsonInString = map.writeValueAsString(notification);
			System.out.println("Json a enviar: " + jsonInString);
			request.body("application/json", jsonInString);

			ClientResponse<String> response = request.post(String.class);

			if (response.getStatus() != 200) {
				System.out.println("Error en la respuesta : HTTP error code :" + response.getStatus());
				throw new RuntimeException("Error en la respuesta : HTTP error code : "
						+ response.getStatus());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(response.getEntity().getBytes())));

			String output;
			System.out.println("Salida del servidor .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
