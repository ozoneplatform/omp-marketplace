package marketplace.rest

import java.lang.reflect.Type
import javax.ws.rs.core.MediaType
import java.lang.annotation.Annotation

import grails.converters.JSON

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider
import org.grails.jaxrs.support.DomainObjectWriterSupport
import static org.grails.jaxrs.support.ConverterUtils.idFromMap

@Provider
@Produces(['text/x-json', 'application/json'])
class CustomDomainObjectWriter extends DomainObjectWriterSupport {
    /**
     * Custom reader that does not require the json to specify the class name.  This
     * is very similar to the method it overrides, but the first line is different
     */
    @Override
    protected Object writeToJson(Object t, OutputStream entityStream, String charset) {
        def writer = new OutputStreamWriter(entityStream, charset)
        def converter = new JSON(t)
        converter.render(writer)
    }

    /**
     * In addition to domain object, also serialize Maps
     */
    boolean isWriteable(Class type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {

        super.isWriteable(type, genericType, annotations, mediaType) ||
            Map.class.isAssignableFrom(type)
    }
}
