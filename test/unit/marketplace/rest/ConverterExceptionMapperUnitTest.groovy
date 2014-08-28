package marketplace.rest

import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException

import grails.test.mixin.support.GrailsUnitTestMixin
import grails.test.mixin.TestMixin

@TestMixin(GrailsUnitTestMixin)
class ConverterExceptionMapperUnitTest {
    ConverterExceptionMapper mapper

    void setUp() {
        mapper = new ConverterExceptionMapper()
    }

    void testStatusCode() {
        assert mapper.toResponse(new ConverterException()).status == 400
    }
}

