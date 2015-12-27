/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.sodep.notificationserver.exceptions.handlers;

import java.sql.SQLException;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;

/**
 *
 * @author Vanessa
 */
@Provider
public class SQLExceptionHandler implements ExceptionMapper<SQLException> {

    private static final Logger log = Logger.getLogger(SQLExceptionHandler.class);

    @Inject
    private ExceptionMapperHelper helper;

    @Override
    public Response toResponse(SQLException exception) {
        log.error("SQL Error Code: " + exception.getErrorCode());
        log.error("SQL Error Message: " + exception.getMessage());
        log.error("SQL State: " + exception.getSQLState());
        Error error = new Error(exception.getErrorCode(), exception.getLocalizedMessage());
        return helper.toResponse(error, 500);
    }

}
