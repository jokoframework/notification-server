package py.com.sodep.notificationserver.config;

/**
 *
 * @author Vanessa
 */

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestConfig extends Application {

	/**
	 *
	 * @return
	 */
	/*@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<>(Arrays.asList(UploadFileService.class,
				AplicacionService.class, NotificationService.class));
	}*/
}