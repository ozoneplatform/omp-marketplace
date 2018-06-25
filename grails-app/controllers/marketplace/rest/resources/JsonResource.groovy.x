package marketplace.rest

import javax.ws.rs.Consumes
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
abstract class JsonResource {

    protected parseParams(Map params) {
        Map returnParams = [:]

        params.each { String key, val ->
            // handle jquery style list values
            boolean isList = key.endsWith('[]')
            String formattedKey = isList ? (key.substring(0, key.length() - 2)) : key

            if(val instanceof List && val.size() == 1) {
                returnParams[formattedKey] = (val as List).get(0)
            }
            else {
                returnParams[formattedKey] = val
            }
        }
        returnParams
    }

}
