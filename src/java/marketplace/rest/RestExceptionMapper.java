package marketplace.rest;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import grails.util.Environment;

/**
 * Base class of REST API exception mappers that handles common
 * exception formatting logic
 */
public class RestExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {
    private static final Logger log = Logger.getLogger(RestExceptionMapper.class);


    private final Response.Status responseCode;

    //whether or not to log the exception
    private final boolean logException;

    protected RestExceptionMapper(Response.Status responseCode) {
        this.responseCode = responseCode;

        //log exceptions that go back to the client as 5xx responses.
        //In dev mode log all exceptions
        this.logException =  isInternalError(responseCode.getStatusCode()) ||
            Environment.getCurrent() == Environment.DEVELOPMENT;
    }

    //NOTE: For some strange reason, every time I try to extend this method in a subclass
    //and use super.toResponse, it causes a stack overflow
    public Response toResponse(E exception) {
        doLog(exception);
        return Response.status(responseCode).entity(exception).build();
    }

    /**
     * Log the exception if necessary
     */
    protected void doLog(E exception) {
        if (this.logException) {
            log.error("Exception during REST call", exception);
        }
    }

    protected boolean isInternalError(int responseCode) {
        return (responseCode / 100) == 5;
    }
}
