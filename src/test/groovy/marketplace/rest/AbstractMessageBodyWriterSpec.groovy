package marketplace.rest

import grails.core.DefaultGrailsApplication
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AbstractMessageBodyWriterSpec extends Specification implements ControllerUnitTest<AbstractMessageBodyWriter>{
    AbstractMessageBodyWriter writer

    Map bodyMap = [
        a: 1,
        b: 'two',
        c: [[
            asdf: 5
        ], [:]]
    ]

    String expectedJSON = '{"a":1,"b":"two","c":[{"asdf":5},{}]}'

    //a class for the writer to write
    static class TestClass {}

    void setup() {
        writer = new AbstractMessageBodyWriter<TestClass>(TestClass.class) {
            Map toBodyMap(TestClass t) { bodyMap }
        }

        writer.grailsApplication = new DefaultGrailsApplication()
    }

    void testGetSize() {
        //For now getSize returns -1 to signal that it doesn't know
        expect:
        writer.getSize(new TestClass(), TestClass.class, TestClass.class, null, null) == -1
    }

    void testIsWriteable() {
        when:
        def isWriteable = { obj ->
            writer.isWriteable(obj.class, obj.class, null, null)
        }

        then:
        !isWriteable(this)
        isWriteable(new TestClass())

        //test subclass
        isWriteable(new TestClass() {})
    }

    void testWriteTo() {
        when:
        ByteArrayOutputStream stream = new ByteArrayOutputStream()

        writer.writeTo(new TestClass(), null, null, null, null, null, stream)

        then:
        stream.toString('UTF-8') == expectedJSON
    }
}
