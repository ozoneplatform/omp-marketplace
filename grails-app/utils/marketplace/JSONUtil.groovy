package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.utils.Utils

import java.text.SimpleDateFormat
import java.text.ParseException

/**
 * JSON utilities to assist in binding from JSON to domain objects
 * @author bhcohen
 *
 */
class JSONUtil {

    /*
  * Only attempt to perform assignment if given property exists in JSON.
  */

    static String getStr(def json, def prop) {
        String returnValue = null

        if (json?.has(prop)) {
            returnValue = (json?.isNull(prop)) ? null : json?.getString(prop)?.trim()
        }

        return returnValue
    }

    /*
     * Only attempt to perform assignment if given property exists in JSON.
     */
    static Closure optStr = { json, obj, prop ->
        if (json?.has(prop)) {
            String propVal = (json?.isNull(prop)) ? null : json?.getString(prop)?.trim()
            Utils.setDomainClassFieldByReflection(obj, prop, propVal)
        }
    }

    static Closure optDate = { json, obj, prop ->
        if (json?.has(prop)) {
            if (json?.isNull(prop)) {
                obj[prop] = null
            } else {

                def val = json?.get(prop)
                def sdf = new SimpleDateFormat(Constants.OPT_DATE_FORMAT, Locale.US)
                sdf.setCalendar Calendar.getInstance(new SimpleTimeZone(0, "GMT"))
                obj[prop] = sdf.parse(val)
            }
        }
    }

    static Closure optInt = { json, obj, prop ->
        if (json?.has(prop))
            obj[prop] = (json?.isNull(prop)) ? null : json?.getInt(prop)
    }

    static Closure optBoolean = { json, obj, prop ->
        if (json?.has(prop))
            obj[prop] = (json?.isNull(prop)) ? null : json?.getBoolean(prop)
    }

    static Closure reencodeDate = { parseFmt, json, obj, prop ->
        if (json?.has(prop)) {
            if (json?.isNull(prop)) {
                obj[prop] = null
            } else {
                def val = json?.get(prop)
                if (val.size() <= 0) {
                    obj[prop] = ""
                } else {
                    def parseSdf = new SimpleDateFormat(parseFmt, Locale.US)
                    def targetSdf = new SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT,
                        Locale.US)

                    if (val[-1..-1] == 'Z') {
                        //Java 6 doesn't support 'Z'
                        val = "${val[0..-2]}UTC"
                    }

                    //TODO Ensure that using system-local timezone is correct
                    //targetSdf.setCalendar Calendar.getInstance(new SimpleTimeZone(0, "GMT"))
                    obj[prop] = targetSdf.format(parseSdf.parse(val))
                }
            }
        }
    }

    //Takes a object that has a title (like Type, Category, etc) and assembles a map with  appropriate key/values, then returns the list as json
	static def getListFromDomainObject(aggregation){
        def result = []
        aggregation.each {
            def map = [:]
            def item = it.key
            map["id"] = item?.id
            map["title"] = item?.title
            map['description'] = item?.description
            map["count"] = it.value
            result << map
        }
		result
    }

    //Takes a string (like the aggregation.term) and assembles a map with  appropriate key/values, then returns the list as json
	static def getListFromStringTerm(aggregation){
        def result = []
        aggregation.each {
            if (it.term && it.count) {
                def map = [:]
                map["id"] = it.term
                map["title"] = it.term
                map["count"] = it.count
                result << map
            }
        }
		result
    }

    static def addCreatedAndEditedInfo(JSONObject jsonObject, def domainObj) {
        jsonObject.put("createdDate", domainObj.createdDate ?: new Date())
        if (domainObj.createdBy) jsonObject.put("createdBy", new JSONObject(id: domainObj.createdBy?.id, name: domainObj.createdBy?.display(), username: domainObj.createdBy?.username))
        jsonObject.put("editedDate", domainObj.editedDate ?: new Date())
        if (domainObj.editedBy) jsonObject.put("editedBy", new JSONObject(id: domainObj.editedBy?.id, name: domainObj.editedBy?.display(), username: domainObj.editedBy?.username))
    }

}
