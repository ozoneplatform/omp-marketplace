package marketplace.rest

import spock.lang.Specification

import grails.converters.JSON
import org.grails.datastore.gorm.GormEntity
import org.grails.web.json.JSONElement
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse

import marketplace.ToJSON

import static marketplace.rest.JsonUtil.toJSON


abstract class RestSpec extends Specification implements SecurityTrait {

    boolean showResponses = false

    String baseUrl
    RestBuilder rest = new RestBuilder()

    def setup() {
        baseUrl = "http://localhost:${serverPort}"
        rest = new RestBuilder()
    }

    static void marshaller(Class<? extends ToJSON> clazz) {
        JSON.registerObjectMarshaller(clazz, { ToJSON it -> it.asJSON() })
    }

    static <T extends GormEntity<?>> T save(T entity) {
        (T) entity.save(failOnError: true)
    }

    static <T extends GormEntity<?>> List<T> save(List<T> entities) {
        entities.collect { save it }
    }


    RestResponse get(String url) {
        RestResponse response = rest.get(resolveUrl(url)) {
            accept('application/json')
        }

        if (showResponses) showResponse(response)

        response
    }

    /** Send a POST request. */
    RestResponse post(String url, Object body) {
        JSONElement jsonBody = convertToJson(body)

        RestResponse response = rest.post(resolveUrl(url)) {
            accept('application/json')
            json(jsonBody.toString())
        }

        if (showResponses) showResponse(response)

        response
    }

    /** Send a PUT request. */
    RestResponse put(String url, Object body) {
        JSONElement jsonBody = convertToJson(body)

        RestResponse response = rest.put(resolveUrl(url)) {
            accept('application/json')
            json(jsonBody.toString())
        }

        if (showResponses) showResponse(response)

        response
    }

    /** Send a PUT request. Converts the Map body to JSON. */
    RestResponse put(String url, Map body) {
        put(url, (JSONElement) toJSON(body))
    }

    /** Send a DELETE request. */
    RestResponse delete(String url) {
        RestResponse response = rest.delete(resolveUrl(url)) {
            accept('application/json')
        }

        if (showResponses) showResponse(response)

        response
    }

    private static JSONElement convertToJson(Object obj) {
        switch (obj) {
            case null:
                throw new IllegalArgumentException("unable to convert null value")
            case JSONElement:
                return (JSONElement) obj
            case Map:
                return toJSON((Map) obj)
            case List:
                return toJSON((List) obj)
            default:
                throw new IllegalArgumentException("unable to convert type '${obj.class.canonicalName}'")
        }
    }

    private static void showResponse(RestResponse response) {
        println "Response JSON: ${response.json}"
        println "Response Text: ${response.text}"
    }

    private resolveUrl(String url) {
        "$baseUrl" + (url.startsWith("/") ? url : "/$url")
    }

}
