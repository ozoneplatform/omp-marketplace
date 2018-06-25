package marketplace.dataexchange

import grails.testing.gorm.DataTest
import marketplace.Types
import org.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.TypeImporter
import spock.lang.Specification

import java.text.SimpleDateFormat

class TypeImporterTest extends Specification implements DataTest{
    TypeImporter typeImporter
    def dateFormat
    final String futureDate =  "2051-10-21T04:29:00Z" // Back to the future!!
    final String oldDate =  "1970-01-01T06:00:00Z"

    void setup() {
        mockDomain(Types)
        typeImporter = new TypeImporter()
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }


    void testUpdateFromNewerType() {
        when:
        JSONObject typeJSON = new JSONObject([
                editedDate: futureDate
        ])
        def mockType = [
                editedDate: dateFormat.parse(oldDate)
        ]

        then:
        typeImporter.shouldUpdate(typeJSON, mockType)
    }

    void testDoNotUpdatePermanentType() {
        when:
        JSONObject typeJSON = new JSONObject([
                editedDate: futureDate
        ])
        def mockType = [
                editedDate: dateFormat.parse(oldDate),
                isPermanent: true
        ]

        then:
        !typeImporter.shouldUpdate(typeJSON, mockType)
    }
}
