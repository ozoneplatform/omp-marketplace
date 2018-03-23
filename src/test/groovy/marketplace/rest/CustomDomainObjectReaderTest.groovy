
package marketplace.rest

import marketplace.testutil.NullObject
import spock.lang.Specification
import org.grails.web.json.JSONObject

class CustomDomainObjectReaderTest extends Specification {

    CustomDomainObjectReader customDomainObjectReader

    void setup(){
        this.customDomainObjectReader = new CustomDomainObjectReader()
    }

    void testPreprocessTrimProperties() {
        when:
        def mock = new JSONObject(
            name : "   name ",
            title: "title"
        )

        def trimmed = customDomainObjectReader.preprocess(mock)
        then:
        trimmed.name == "name"
        trimmed.title == "title"
//        assertThat(trimmed.title, is(equalTo("title")))
    }

    void testPreprocessNonStringsAreNotModified() {
        when:
        Date date = new Date()
        def mock = new JSONObject(date : date)
        def trimmed = customDomainObjectReader.preprocess(mock)

        then:
        trimmed.date == date
    }


    void testPreprocessWithEmptyStrings() {
        when:
        def mock = new JSONObject(name: "    ")
        def trimmed = customDomainObjectReader.preprocess(mock)
        then:
        trimmed.name == ''
    }
    void testPreprocessIsNullSafe() {
        when:
        def mock = new JSONObject()
        def trimmed = customDomainObjectReader.preprocess(mock)
        then:
        trimmed.name == null
    }

    void testPreprocessConvertsJsonNullToJavaNull() {
        when:
        def mock = new JSONObject(name: NullObject.NULL)
        def processed = customDomainObjectReader.preprocess(mock)
        then:
        processed.name == null
    }

    void testIdFromMapWhenIdIsString(){
        expect:
        customDomainObjectReader.idFromMap(["id": "1000"] as Map) == Long.valueOf(1000)
    }

    void testIdFromMapWhenIdIsNull(){
        expect:
        customDomainObjectReader.idFromMap(["id": null] as Map) == null
    }

    void testIdFromMapWhenIdIsNumber(){
        expect:
        customDomainObjectReader.idFromMap(["id": 1000 as Number] as Map) == Long.valueOf(1000)
    }
}
