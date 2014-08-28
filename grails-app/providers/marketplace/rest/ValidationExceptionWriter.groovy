package marketplace.rest

import java.lang.reflect.Type
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter
import java.lang.annotation.Annotation

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider

import grails.validation.ValidationException

@Provider
@Produces(['text/x-json', 'application/json'])
class ValidationExceptionWriter extends ThrowableWriterSupport<ValidationException> {
    boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        ValidationException.class.isAssignableFrom(type)
    }

    @Override
    protected Map toBodyMap(ValidationException exception) {
        super.toBodyMap(exception) + [
            //Just get the first part of the error string
            message: exception.message.split('\\n')[0],
            resolvedMessges: getResolvedMessages(exception.errors)
        ]
    }

    //This will resolve the messages based on local and pass them back
    def getResolvedMessages(def errors){
        List errMessages = []
        errors.getAllErrors().each {
            def fieldErrorCode =  it.objectName + "." + it.field + "." + it.code
                errMessages << grailsApplication.getMainContext().getMessage(fieldErrorCode,
                    it.arguments, it.defaultMessage, Locale.getDefault())
        }
        errMessages
    }
}
