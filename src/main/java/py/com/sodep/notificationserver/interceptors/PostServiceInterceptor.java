package py.com.sodep.notificationserver.interceptors;

import org.jboss.resteasy.annotations.interception.EncoderPrecedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.interception.MessageBodyWriterContext;
import org.jboss.resteasy.spi.interception.MessageBodyWriterInterceptor;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import org.apache.log4j.Logger;

@Provider
@ServerInterceptor
@EncoderPrecedence
public class PostServiceInterceptor implements PostProcessInterceptor, MessageBodyWriterInterceptor {
    @Inject
    private Logger log;
    @Inject
    private RequestParams requestParams;

    @Context
    private HttpServletResponse httpResponse;


    @Override
    public void postProcess(ServerResponse response)
            throws Failure, WebApplicationException {

        if (response.getEntity() == null) {
            log.info("Entity de la respuesta es null");
        }
    }

    @Override
    public void write(MessageBodyWriterContext context) throws IOException, WebApplicationException {
        context.proceed();
    }
}
