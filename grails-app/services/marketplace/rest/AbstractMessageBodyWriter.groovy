package marketplace.rest

import java.io.Writer

import java.lang.reflect.Type
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter
import java.lang.annotation.Annotation

import grails.converters.JSON
import grails.core.GrailsApplication
import org.grails.plugins.jaxrs.provider.ConverterUtils


/**
 * A superclass to hold common logic from different custom MessageBodyWriters that we
 * have, other than those that handle domain objects
 */
abstract class AbstractMessageBodyWriter<T> implements MessageBodyWriter<T> {
    GrailsApplication grailsApplication

    protected Class<T> clazz

    protected AbstractMessageBodyWriter(Class<T> clazz) {
        this.clazz = clazz
    }

    @Override
    long getSize(T t, Class type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        //TODO make this return an accurate number
        return -1;
    }

    @Override
    boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        clazz.isAssignableFrom(type)
    }

    @Override
    void writeTo(T t, Class type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
            OutputStream entityStream) {
        Writer writer = new OutputStreamWriter(entityStream,
            ConverterUtils.getDefaultJSONEncoding(grailsApplication))
        (new JSON(toBodyMap(t))).render(writer)
    }

    /**
     * @return a Map representing the JSON structure of the
     * response body
     */
    protected abstract Map toBodyMap(T t)
}
