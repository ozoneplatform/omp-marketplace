package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

//@TestFor(Intent)
class IntentSpec extends Specification implements DomainConstraintsUnitTest<Intent> {
    List<Class> getDomainClasses() {[Agency]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    void setup() {
//        FakeAuditTrailHelper.install()

//        mockForConstraintsTests(Intent)
    }

    void testBlankIsInvalid() {
        expect:
        propertyValueIsInvalid('action', null, ConstrainedProperty.NULLABLE_CONSTRAINT)
        propertyValueIsInvalid('dataType', null, ConstrainedProperty.NULLABLE_CONSTRAINT)

    }

    void testValidIsAccepted() {
        when:
        def dataType = new IntentDataType(title: "unique title")
        def action = new IntentAction(title: "unique action")
        def validIntent = new Intent(dataType: dataType, action: action, send: true, receive: false)
        
        then:
        validIntent.validate()
    }
}
