package marketplace.rest

import org.grails.web.converters.exceptions.ConverterException
import spock.lang.Specification

//@TestMixin(GrailsUnitTestMixin)
class ConverterExceptionMapperSpec extends Specification {
    ConverterExceptionMapper mapper

    void setup() {
        mapper = new ConverterExceptionMapper()
    }

    void testStatusCode() {
        expect:
        mapper.toResponse(new ConverterException()).status == 400
    }
}

