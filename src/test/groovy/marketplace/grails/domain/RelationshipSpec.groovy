package marketplace

import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import ozone.marketplace.enums.RelationshipType

import ozone.utils.TestUtil
import spock.lang.Specification

//@TestFor(Relationship)
class RelationshipSpec extends Specification implements DomainConstraintsUnitTest<Relationship> {

    List<Class> getDomainClasses() {[Relationship]}

    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}

	def relationship

    void setup() {
//		mockForConstraintsTests(Relationship)
		relationship = new Relationship()
    }

    void testNullConstraints(){
        expect:
        propertyIsRequired('owningEntity')
//        TestUtil.assertPropertyRequired('owningEntity',relationship)
        assert relationship.relationshipType == RelationshipType.REQUIRE
    }
}
