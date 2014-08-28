package marketplace.rest

import grails.test.mixin.web.ControllerUnitTestMixin
import grails.test.mixin.TestMixin

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

//ControllerUnitTestMixin needed for JSON support
@TestMixin(ControllerUnitTestMixin)
class AbstractMessageBodyWriterUnitTest {
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

    void setUp() {
        writer = new AbstractMessageBodyWriter<TestClass>(TestClass.class) {
            Map toBodyMap(TestClass t) { bodyMap }
        }

        writer.grailsApplication = new DefaultGrailsApplication()
    }

    void testGetSize() {
        //For now getSize returns -1 to signal that it doesn't know
        assert writer.getSize(new TestClass(), TestClass.class, TestClass.class, null, null) == -1
    }

    void testIsWriteable() {
        def isWriteable = { obj ->
            writer.isWriteable(obj.class, obj.class, null, null)
        }

        assert !isWriteable(this)
        assert isWriteable(new TestClass())

        //test subclass
        assert isWriteable(new TestClass() {})
    }

    void testWriteTo() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream()

        writer.writeTo(new TestClass(), null, null, null, null, null, stream)

        assert stream.toString('UTF-8') == expectedJSON
    }
}
