package py.com.sodep.notificationserver.interceptors;

import org.jboss.resteasy.annotations.interception.SecurityPrecedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;
import org.jboss.resteasy.core.ResourceMethodInvoker;

/**
 * @author duartm
 * @version 1.0 25/03/14
 */
@Provider
@ServerInterceptor
@SecurityPrecedence
public class SecurityInterceptor implements PreProcessInterceptor {

    private static final Logger LOGGER = Logger.getLogger(SecurityInterceptor.class);

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private RequestParams requestParams;

    @Override
    public ServerResponse preProcess(HttpRequest hr, ResourceMethodInvoker rmi) throws Failure, WebApplicationException {
        
        return null;
    }
}
