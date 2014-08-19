package marketplace

import grails.test.mixin.TestFor

import ozone.marketplace.enums.RelationshipType

import ozone.utils.TestUtil

@TestFor(Relationship)
class RelationshipTests {

	def relationship

    void setUp() {
		mockForConstraintsTests(Relationship)
		relationship = new Relationship()
    }

    void testNullConstraints(){
        TestUtil.assertPropertyRequired('owningEntity',relationship)
        assert relationship.relationshipType == RelationshipType.REQUIRE
    }
}
