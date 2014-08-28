package marketplace.rest

import grails.converters.JSON

import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.UriInfo
import javax.ws.rs.ext.Provider
import java.lang.annotation.Annotation
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import org.grails.jaxrs.support.DomainObjectWriterSupport
import static org.grails.jaxrs.support.ConverterUtils.getDefaultEncoding

@Provider
@Produces(['text/javascript', 'application/javascript'])
class JSONPDomainObjectWriter extends DomainObjectWriterSupport {

    public static final String[] JSONP_MEDIA_TYPES = ['text/javascript', 'application/javascript']
    public static final String JSONP_CALLBACK_PARAM = 'callback'

    @Context
    UriInfo uriInfo

    /**
     * Puts the P in JSONP - otherwise it's very similar to the method it overrides.
     *
     */
    @Override
    void writeTo(Object t, Class type, Type genericType,
                 Annotation[] annotations, MediaType mediaType,
                 MultivaluedMap httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        def callback = uriInfo.queryParameters?.getFirst(JSONP_CALLBACK_PARAM)

        //TODO: This exception ends up with the GrailsExceptionResolver and not the RESTException Mapper
        if(!callback)
            throw new IllegalArgumentException('JSONP requests require a callback parameter')

        def writer = new OutputStreamWriter(entityStream, getDefaultEncoding(grailsApplication))
        def converter = new JSON(t)
        writer.write("$callback($converter)" as String)
        writer.close()
    }

    /**
     * Mostly the same as the method it overrides, but returns true for valid JSONP media types
     *
     */
    @Override
    boolean isWriteable(Class type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        def subType = mediaType.getType() + "/" + mediaType.getSubtype();

        boolean compatibleMediaType = (subType in JSONP_MEDIA_TYPES)

        if (Collection.class.isAssignableFrom(type) && !isRequireGenericCollection()) {
            return compatibleMediaType
        } else if (genericType instanceof ParameterizedType) {
            return isDomainObjectCollectionType(genericType)
        } else if (grailsApplication.isDomainClass(type)) {
            return compatibleMediaType
        }
        return false;
    }
}
