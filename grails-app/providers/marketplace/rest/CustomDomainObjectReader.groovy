package marketplace.rest

import java.text.DateFormat
import java.text.SimpleDateFormat

import grails.converters.JSON
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.codehaus.groovy.grails.exceptions.InvalidPropertyException
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONElement
import org.codehaus.groovy.grails.web.json.JSONException
import org.codehaus.groovy.grails.web.json.JSONObject
import org.grails.jaxrs.support.DomainObjectReaderSupport

import javax.ws.rs.Consumes
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.Provider
import java.lang.annotation.Annotation
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

import static org.grails.jaxrs.support.ConverterUtils.getDefaultJSONEncoding
import static org.grails.jaxrs.support.ConverterUtils.getDefaultXMLEncoding
import static org.grails.jaxrs.support.ProviderUtils.isJsonType
import static org.grails.jaxrs.support.ProviderUtils.isXmlType
import static ozone.utils.Utils.collectEntries

@Provider
@Consumes(['text/x-json', 'application/json'])
class CustomDomainObjectReader extends DomainObjectReaderSupport {


    @Override
    boolean isReadable(Class type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return isEnabled() && grailsApplication.isDomainClass(type.getName() == List.getName() ? genericType.actualTypeArguments.getAt(0) : type) && (isXmlType(mediaType) || isJsonType(mediaType))
    }

    /**
     * Passes correct type to readFromXml or readFromJson if a List is passed.
     */
    @Override
    Object readFrom(Class type, Type genericType,
                    Annotation[] annotations, MediaType mediaType,
                    MultivaluedMap httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {

        if(type.getName() == List.getName())
            type = (genericType as ParameterizedType).actualTypeArguments.getAt(0)

        if (isXmlType(mediaType)) {
            return readFromXml(type, entityStream, getDefaultXMLEncoding(grailsApplication))
        } else { // JSON
            return readFromJson(type, entityStream, getDefaultJSONEncoding(grailsApplication))
        }

    }

    /**
     * Custom reader that does not require the json to specify the class name.
     */
    @Override
    protected Object readFromJson(Class type, InputStream entityStream, String charset) {
        def map
        try {
            map = JSON.parse(entityStream, charset)

            if(map instanceof JSONObject) {
                return instantiate(type, preprocess(map))
            }
            else if(map instanceof JSONArray) {
                List list = (map as JSONArray).toList()
                return list.collect { it ->
                    instantiate(type, preprocess(it))
                }
            }
        }
        catch (JSONException e) {
            throw new IllegalArgumentException(e)
        }
    }

    static Long idFromMap(Map map) {
        def id = map.id
        if (id == null || id == '') {
            return null
        }
        if(id instanceof Number){
            return id.toLong()
        }
        return Long.parseLong(id as String)
    }

    /**
     * Create a domain object from a Map. The domain object will be of
     * the specified class unless the map has a 'class' property.  In that case,
     * the class specified by that property will be created, if it is a subclass
     * of 'type'. Otherwise, an exception is thrown.
     *
     * @param type The type to be instantiated. The returned object may be
     * a subclass of this type
     * @param map The properties to use to instantiate the object
     * @throws IllegalArugmentException if the map specifies a class property
     * that is not a subclass of type
     */
    private <T> T instantiate(Class<T> type, Map map) {
        if (map == null) {
            null
        }
        else {
            //if the JSON map specifies a specific class, try to use that class.
            if (map.get('class')) {
                Class specifiedClass =
                    Class.forName(map.get('class'), true, type.classLoader)

                if (type.isAssignableFrom(specifiedClass)) {
                    type = specifiedClass
                }
                else {
                    throw new IllegalArgumentException("Invalid class specified. " +
                        "$specifiedClass cannot be cast to $type")
                }
            }

            Map instantiatedMap = instantiateSubObjects(type, map)

            def retval = type.metaClass.invokeConstructor()

            //map constructor doesn't work well with overloaded setters, so use the setters
            //explicitly
            instantiatedMap.each { k, v ->
                String method = "set${k.capitalize()}"
                if (retval.respondsTo(method, v.getClass())) {
                    retval."$method"(v)
                }
                else {
                    log.debug "Discarding unusable property in JSON - $k: $v"
                }
            }

            // Workaround for http://jira.codehaus.org/browse/GRAILS-1984
            if (!retval.id) {
                retval.id = this.idFromMap(map)
            }

            retval
        }
    }

    /**
     * Given a map and a domain class type, transform any immediate subobjects or collections
     * of subobjects to the appropriate classes for the corresponding properties of the type
     */
    private Map instantiateSubObjects(Class<?> type, Map map) {

        //return either the value passed in or a domain object, depending on
        //whether the corresponding property is a domain class
        def domainOrPrimitive = { valueType, value ->

            //instantiate the subobject if it is a domain object
            //and the value is a map.  If its a domain object
            //but the value is not a map, assume that the
            //parent object has a setter that can handle the value
            //present, and just return that value
            if (grailsApplication.isDomainClass(valueType) &&
                    value instanceof Map) {
                instantiate(valueType, value)
            }
            //instantiate enums
            else if (Enum.isAssignableFrom(valueType) && value instanceof String) {
                valueType.valueOf(value)
            }
            else {
                value
            }
        }

        GrailsDomainClass grailsClass = grailsApplication.getDomainClass(type.name)

        collectEntries(map) { key, value ->
            if (key == 'createdDate' || key == editedDate) {
                DateFormat auditDateFormat =
                    new SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT)
                value = auditDateFormat.parse(value)
            }

            if (key != 'class') {
                try {
                    //the given property of the parent domain class
                    GrailsDomainClassProperty property = grailsClass.getPropertyByName(key)

                    //the instantiated domain object or list of domain objects, or primitive or
                    //list of primitives
                    def retval
                    if (property) {
                        if (value instanceof Collection) {
                            retval = value.collect(
                                domainOrPrimitive.curry(property.referencedPropertyType))

                            if (Set.isAssignableFrom(property.type)) {
                                retval = retval as Set
                            }
                        }
                        else {
                            retval = domainOrPrimitive(property.type, value)
                        }
                    }
                    else {
                        retval = value
                    }


                    [key, retval]
                }
                catch (InvalidPropertyException e) {
                    return null
                }
            }
            else {
                null
            }
        }
    }

    /**
     * Recursively navigate the json, performing a variety of preprocessing
     * functions on the items encountered
     *
     * Current preprocessing functions:
     *  Convert JSON null into java null
     *  trim leading and trailing whitespace from strings
     *
     * @param map a map to preprocess.  This map is not modified
     * @return A map just like the input map, but with proprocessing steps performed
     */
    protected static preprocess(json) {
        //trim whitespace from strings
        def handleString = { it.trim() }

        def handlePrimitive = {
            it instanceof String ? handleString(it) :
            it == JSONObject.NULL ? null :
            it
        }

        //a function that either recurses back into preprocess or returns a primitive
        def recurseOrPrimitive = {
            it instanceof JSONElement ? preprocess(it) : handlePrimitive(it)
        }


        //preprocess an json object
        def handleObject = {
            collectEntries(it) { key, value ->
                [key, recurseOrPrimitive(value)]
            }
        }

        //preprocess a json list
        def handleList = {
            it.collect { recurseOrPrimitive(it) }
        }

        if (json instanceof List) handleList(json)
        else if (json instanceof Map) handleObject(json)
        else throw new IllegalArgumentException("Invalid type passed into preprocess: ${json}")
    }
}
