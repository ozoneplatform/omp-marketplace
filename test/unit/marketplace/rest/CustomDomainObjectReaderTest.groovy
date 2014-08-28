
package marketplace.rest;

import org.junit.Before
import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.codehaus.groovy.grails.web.json.JSONObject

class CustomDomainObjectReaderTest {

    CustomDomainObjectReader customDomainObjectReader


    @Before
    public void setUp(){
        this.customDomainObjectReader = new CustomDomainObjectReader()
    }

    @Test
    public void testPreprocessTrimProperties() {
        def mock = new JSONObject(
            name : "   name ",
            title: "title"
        )

        def trimmed = customDomainObjectReader.preprocess(mock)
        assertThat(trimmed.name, is(equalTo("name")))
        assertThat(trimmed.title, is(equalTo("title")))
    }

    @Test
    public void testPreprocessNonStringsAreNotModified() {
        Date date = new Date()
        def mock = new JSONObject(date : date)
        def trimmed = customDomainObjectReader.preprocess(mock)
        assertThat(trimmed.date, is(equalTo(date)))
    }


    @Test
    public void testPreprocessWithEmptyStrings() {
        def mock = new JSONObject(name : "    ")
        def trimmed = customDomainObjectReader.preprocess(mock)
        assertThat(trimmed.name, is(''))
    }

    @Test
    public void testPreprocessIsNullSafe() {
        def mock = new JSONObject()
        def trimmed = customDomainObjectReader.preprocess(mock)
        assertThat(trimmed.name, is(nullValue()))
    }

    @Test
    public void testPreprocessConvertsJsonNullToJavaNull() {
        def mock = new JSONObject(name: JSONObject.NULL)
        def processed = customDomainObjectReader.preprocess(mock)
        assertThat(processed.name, is(nullValue()))
    }

    @Test
    public void testIdFromMapWhenIdIsString(){
        assertThat(customDomainObjectReader.idFromMap(["id": "1000"] as Map), is(equalTo(Long.valueOf(1000))))
    }

    @Test
    public void testIdFromMapWhenIdIsNull(){
        assertThat(customDomainObjectReader.idFromMap(["id": null] as Map), is(nullValue()))
    }

    @Test
    public void testIdFromMapWhenIdIsNumber(){
        assertThat(customDomainObjectReader.idFromMap(["id": 1000 as Number] as Map), is(equalTo(Long.valueOf(1000))))
    }
}
