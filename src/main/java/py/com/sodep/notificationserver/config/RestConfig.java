package py.com.sodep.notificationserver.config;

/**
 *
 * @author Vanessa
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import py.com.sodep.notificationserver.rest.AplicacionService;
import py.com.sodep.notificationserver.rest.NotificationService;
import py.com.sodep.notificationserver.rest.UploadFileService;

@ApplicationPath("/api")
public class RestConfig extends Application {

	/**
	 *
	 * @return
	 */
	@Override
	public Set<Class<?>> getClasses() {
		return new HashSet<>(Arrays.asList(UploadFileService.class,
				AplicacionService.class, NotificationService.class));
	}
}