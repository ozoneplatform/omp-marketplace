package marketplace.rest

import java.text.ParseException

import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

@Provider
class ParseExceptionMapper extends RestExceptionMapper<ParseException> {
    ParseExceptionMapper() {
        super(Response.Status.BAD_REQUEST)
    }
}
