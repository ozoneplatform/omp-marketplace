package ozone.marketplace.dataexchange

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import marketplace.Types
import org.codehaus.groovy.grails.web.json.JSONObject

import java.text.SimpleDateFormat

@TestMixin(DomainClassUnitTestMixin)
class TypeImporterTest {
    TypeImporter typeImporter
    def dateFormat
    final String futureDate =  "2051-10-21T04:29:00Z" // Back to the future!!
    final String oldDate =  "1970-01-01T06:00:00Z"

    void setUp() {
        mockDomain(Types)
        typeImporter = new TypeImporter()
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }


    void testUpdateFromNewerType() {
        JSONObject typeJSON = new JSONObject([
                editedDate: futureDate
        ])
        def mockType = [
                editedDate: dateFormat.parse(oldDate)
        ]

        assertTrue typeImporter.shouldUpdate(typeJSON, mockType)
    }

    void testDoNotUpdatePermanentType() {
        JSONObject typeJSON = new JSONObject([
                editedDate: futureDate
        ])
        def mockType = [
                editedDate: dateFormat.parse(oldDate),
                isPermanent: true
        ]

        assertFalse typeImporter.shouldUpdate(typeJSON, mockType)
    }
}
