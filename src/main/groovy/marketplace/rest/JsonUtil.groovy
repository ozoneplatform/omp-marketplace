package marketplace.rest

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject


class JsonUtil {

    static JSONObject toJSON(Map value) {
        JSONObject object = new JSONObject()
        ((Map) value).forEach { String key, val -> object.put(key, toJSON(val)) }
        object
    }

    static JSONArray toJSON(Collection value) {
        JSONArray array = new JSONArray()
        ((Collection) value).each { array.put(toJSON(it)) }
        array
    }

    static Object toJSON(Object value) {
        switch (value) {
            case Map:
                return toJSON((Map) value)
            case Collection:
                return toJSON((Collection) value)
            case Date:
                return formatIsoDate((Date) value)
            default:
                return value
        }
    }

    static String formatIsoDate(Date date) {
        DateTimeFormatter.ISO_INSTANT.format(date.toInstant().truncatedTo(ChronoUnit.SECONDS))
    }

}
