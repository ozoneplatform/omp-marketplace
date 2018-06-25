package marketplace

import grails.gorm.validation.ConstrainedProperty
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

//@TestFor(IntentDirection)
class IntentDirectionSpec extends Specification implements DomainConstraintsUnitTest<IntentDirection> {
    List<Class> getDomainClasses() {[IntentDirection]}
    //Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    @Autowired
    FakeAuditTrailHelper auditTrailHelper

    def mock
    void setup() {
        mock = Mock(IntentDirection)
//        FakeAuditTrailHelper.install()
//
//        mockForConstraintsTests(IntentDirection)
    }

    void testBlankTitleIsInvalid() {
        expect:
        propertyValueIsInvalid('title', "", ConstrainedProperty.BLANK_CONSTRAINT)
//        def blankDirection = new IntentDirection()
//        assertFalse blankDirection.validate()
    }

    void testInvalidTitleIsRejected() {
        when:
        def invalidDirectionTitle = new IntentDirection(title: "Wrong")
        then:
        !invalidDirectionTitle.validate()
    }

    void testValidTitleIsAccepted() {
        when:
        def validDirectionTitle = new IntentDirection(title: "Send")
        then:
        validDirectionTitle.validate()
    }
}
