package py.com.sodep.notificationserver.interceptors;

import org.jboss.resteasy.annotations.interception.DecoderPrecedence;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.*;
import org.jboss.resteasy.util.InputStreamToByteArray;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.jboss.resteasy.core.ResourceMethodInvoker;

@Provider
@ServerInterceptor
@DecoderPrecedence
public class LoggingInterceptor implements MessageBodyReaderInterceptor, MessageBodyWriterInterceptor, PreProcessInterceptor {

    private static final Logger LOGGER = Logger.getLogger(LoggingInterceptor.class);

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public Object read(MessageBodyReaderContext context) throws IOException, WebApplicationException {
        InputStream old = context.getInputStream();

        try {
            InputStreamToByteArray stream = new InputStreamToByteArray(old);
            context.setInputStream(stream);
            Object proceed = context.proceed();
            byte[] body = stream.toByteArray();
            LOGGER.info("Contenido de entrada: " + new String(body));

            return proceed;
        } finally {
            context.setInputStream(old);
        }
    }

    @Override
    public void write(MessageBodyWriterContext context) throws IOException, WebApplicationException {
        OutputStream old = context.getOutputStream();

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.writeTo(old);
            context.setOutputStream(stream);
            context.proceed();
            byte[] body = stream.toByteArray();
            LOGGER.info("Body request de salida: " + new String(body));
            stream.close();
        } finally {
            context.setOutputStream(old);
            context.proceed();
        }
    }

    @Override
    public ServerResponse preProcess(HttpRequest hr, ResourceMethodInvoker rmi) throws Failure, WebApplicationException {
        String methodName = rmi.getMethod().getName();
        String remoteAddress = servletRequest.getRemoteAddr();
        String requestUri = servletRequest.getRequestURI();
        String queryString = servletRequest.getQueryString();
        String httpMethod = servletRequest.getMethod();

        String requestData = "Remote Adress: " + remoteAddress + "\n\t\t\t"
                + "Method: " + methodName + "\n\t\t\t"
                + "Request URI: " + requestUri + "\n\t\t\t"
                + "Query String: " + queryString + "\n\t\t\t"
                + "HTTP Method: " + httpMethod;

        LOGGER.info(requestData);

        Enumeration<String> headerNames = servletRequest.getHeaderNames();
        servletRequest.getHeader("");

        if (LOGGER.isTraceEnabled()) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();

                Enumeration<String> headers = servletRequest.getHeaders(headerName);
                while (headers.hasMoreElements()) {
                    String headerValue = headers.nextElement();
                    LOGGER.trace(headerName + ": " + headerValue);
                }
            }
        } else {
            String contentType = servletRequest.getHeader("content-type");
            LOGGER.info("content-type: " + contentType);

            String acceptEnconding = servletRequest.getHeader("accept-encoding");
            LOGGER.info("accept-encoding: " + acceptEnconding);
        }

        return null;
    }
}
