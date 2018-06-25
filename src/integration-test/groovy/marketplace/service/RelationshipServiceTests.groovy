package marketplace.service

import grails.gorm.transactions.Rollback
import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import grails.testing.mixin.integration.Integration
import marketplace.AccessControlException
import marketplace.PermissionException
import marketplace.Profile
import marketplace.Relationship
import marketplace.ServiceItem
import marketplace.Types
import marketplace.controller.MarketplaceIntegrationTestCase
import ozone.marketplace.domain.ValidationException
import ozone.marketplace.enums.RelationshipType;
import grails.test.*
import marketplace.Constants

@Integration
@Rollback
class RelationshipServiceTests extends MarketplaceIntegrationTestCase {

	def relationshipService
	def serviceItemService
	def serviceItemActivityService

    void setup() {
//        super.setUp()
		relationshipService.serviceItemService = serviceItemService
    }

	void testSaveRelationship(){
		setupTestUser()
		when:
		ServiceItem parent = new ServiceItem(
            title:"Parent",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		relationshipService.saveRelationship(new Relationship(relationshipType: RelationshipType.REQUIRE, owningEntity: parent))
		then:
		notThrown(grails.validation.ValidationException)
	}

	void testSaveRelationshipThrowsException(){
		setupTestUser()
		when:
		ServiceItem parent = new ServiceItem(
            title:"Parent",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		relationshipService.saveRelationship(new Relationship(
                relationshipType: null,
                owningEntity: parent)
            )
		then:
		thrown(grails.validation.ValidationException)
	}

    void testAddRequirestoEmptyRelationship() {
		setupTestUser()
		when:
		ServiceItem parent = new ServiceItem(
            title:"Parent",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		ServiceItem requires1 = new ServiceItem(
            title:"Requires 1",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		ServiceItem requires2 = new ServiceItem(
            title:"Requires 2",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		ServiceItem requires3 = new ServiceItem(
            title:"Requires 3",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])
		def rel  = Relationship.findByOwningEntity(parent)
		then:
		3 == rel.relatedItems.size()
		"Requires 1" == rel.relatedItems[0].title
		"Requires 2" == rel.relatedItems[1].title
		"Requires 3" == rel.relatedItems[2].title
		RelationshipType.REQUIRE == rel.relationshipType
    }

	def getActivity = { si,action ->
		si.serviceItemActivities.findAll { it.action == action}.max(){ it.activityTimestamp }
	}

	void testAddRequiresToExistingRelationship() {
		setupTestUser()
		when:
		ServiceItem parent = buildServiceItem("Parent", [testUser1])
		parent.save(failOnError: true, flush: true)
		ServiceItem requiresA = buildServiceItem("Requires A", [testUser1])
		requiresA.save(failOnError: true, flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requiresA.id])

		def rel  = Relationship.findByOwningEntity(parent)
		def parentActivity = getActivity(parent,Constants.Action.ADDRELATEDITEMS)
		then:
		RelationshipType.REQUIRE == rel.relationshipType
		"Requires A" == rel.relatedItems[0].title
		null != parentActivity
		1 == parentActivity.items.size()
		"Requires A" == parentActivity.items[0].title

		when:
		ServiceItem requires1 = buildServiceItem("Requires 1", [testUser1])
		ServiceItem requires2 = buildServiceItem("Requires 2", [testUser1])
		relationshipService.addOrRemoveRequires(parent.id,[requiresA.id, requires1.id,requires2.id])

		rel  = Relationship.findByOwningEntity(parent)
		parentActivity = getActivity(parent,Constants.Action.ADDRELATEDITEMS)
		def requires1Activity = getActivity(requires1,Constants.Action.ADDRELATEDTOITEM)
		def requires2Activity = getActivity(requires2,Constants.Action.ADDRELATEDTOITEM)

		then:
		3 == rel.relatedItems.size()
		RelationshipType.REQUIRE == rel.relationshipType
		"Requires A" == rel.relatedItems[0].title
		"Requires 1" == rel.relatedItems[1].title
		"Requires 2" == rel.relatedItems[2].title

		null != parentActivity
		2 == parentActivity.items.size()
		"Requires 1" == parentActivity.items[0].title
		"Requires 2" == parentActivity.items[1].title
		Constants.Action.ADDRELATEDITEMS == parentActivity.action

		null != requires1Activity
		"Parent" == requires1Activity.items[0].title

		null != requires2Activity
		"Parent" == requires2Activity.items[0].title
		Constants.Action.ADDRELATEDTOITEM == requires2Activity.action

		when:
		ServiceItem requires3 = buildServiceItem("Requires 3", [testUser1])
		relationshipService.addOrRemoveRequires(parent.id,[requires3.id,requires2.id,requires1.id])

		rel  = Relationship.findByOwningEntity(parent)
		def parentAddActivity = getActivity(parent,Constants.Action.ADDRELATEDITEMS)
		def parentRemoveActivity = getActivity(parent,Constants.Action.REMOVERELATEDITEMS)
		def requiresAActivity = getActivity(requiresA,Constants.Action.REMOVERELATEDTOITEM)

		then:
		3 == rel.relatedItems.size()
		"Requires 3" == rel.relatedItems[0].title
		"Requires 2" == rel.relatedItems[1].title
		"Requires 1" == rel.relatedItems[2].title
		RelationshipType.REQUIRE == rel.relationshipType

		null != parentAddActivity
		1 == parentAddActivity.items.size()
		"Requires 3" == parentAddActivity.items[0].title

		null != parentRemoveActivity
		1 == parentRemoveActivity.items.size()
		"Requires A" == parentRemoveActivity.items[0].title

		null != requiresAActivity
		"Parent" == requiresAActivity.items[0].title

		when:
		relationshipService.addOrRemoveRequires(parent.id,[requires2.id])
		parentActivity = getActivity(parent,Constants.Action.REMOVERELATEDITEMS)

		then:
		1 == rel.relatedItems.size()
		"Requires 2" == rel.relatedItems[0].title
		RelationshipType.REQUIRE == rel.relationshipType

		null != parentActivity
		2 == parentActivity.items.size()
		"Requires 3" == parentActivity.items[0].title
		"Requires 1" == parentActivity.items[1].title
	}

	void testAddNonApprovedRequiresThatIDoNotOwn() {
		setupTestUser()
		when:
		ServiceItem parent = buildServiceItem("Parent", [testUser1])

		def userLewis = new Profile(username: 'rLewis', displayName: 'Ray Lewis')
        userLewis.save(failOnError: true, flush: true)
		ServiceItem requires1 = buildServiceItem("Requires 1", [userLewis])
        requires1.approvalStatus = Constants.APPROVAL_STATUSES["REJECTED"]
        requires1.save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id])

		then:
		thrown(AccessControlException)
	}

	void testAddRequirestoAListingIDoNotOwn() {
		setupTestUser()
		when:
		def userLewis = new Profile(username: 'rLewis', displayName: 'Ray Lewis')
		userLewis.save(flush:true, failOnError: true)
		ServiceItem parent = buildServiceItem("Parent", [userLewis])
		parent.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]
		parent.save(flush:true, failOnError:true)

		ServiceItem requires1 = buildServiceItem("Requires 1", [testUser1])
		ServiceItem requires2 = buildServiceItem("Requires 2", [testUser1])
		ServiceItem requires3 = buildServiceItem("Requires 3", [testUser1])
		relationshipService.addOrRemoveRequires(parent.id, [requires1.id, requires2.id, requires3.id])

		then:
		thrown(PermissionException)
	}

	void testRemoveAllRequires() {
		setupTestUser()
		when:
		ServiceItem parent = buildServiceItem("Parent", [testUser1])
		ServiceItem requires1 = buildServiceItem("Requires 1", [testUser1])
		ServiceItem requires2 = buildServiceItem("Requires 2", [testUser1])
		ServiceItem requires3 = buildServiceItem("Requires 3", [testUser1])
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])

		relationshipService.addOrRemoveRequires(parent.id,[null])

		def rel  = Relationship.findByOwningEntity(parent)
		then:
		0 == rel.relatedItems.size()
	}

	void testGetAllRequiredBy() {
		setupTestUser()
		when:
		ServiceItem parent1 = buildServiceItem("Parent", [testUser1])
		ServiceItem requires1 = buildServiceItem("Requires 1", [testUser1])
		ServiceItem requires2 = buildServiceItem("Requires 2", [testUser1])
		ServiceItem requires3 = buildServiceItem("Requires 3", [testUser1])
        relationshipService.addOrRemoveRequires(parent1.id,[requires1.id,requires2.id, requires3.id])
        ServiceItem parent2 = buildServiceItem("Parent", [testUser1])
		ServiceItem requires4 = buildServiceItem("Requires 1", [testUser1])
		ServiceItem requires5 = buildServiceItem("Requires 3", [testUser1])
		relationshipService.addOrRemoveRequires(parent2.id,[requires4.id,requires5.id])

		def required = relationshipService.getAllRequiredBy([parent1.id, parent2.id]);

		then:
		7 == required.size()

	}

    void testGetAllRequiredChildren() {
		setupTestUser()
		when:
        ServiceItem parent1 = buildServiceItem("Parent", [testUser1])
        ServiceItem parent2 = buildServiceItem("Parent", [testUser1])
        ServiceItem parent3 = buildServiceItem("Parent", [testUser1])
        relationshipService.addOrRemoveRequires(parent1.id,[parent2.id])
        relationshipService.addOrRemoveRequires(parent2.id,[parent3.id])

        def required = relationshipService.getAllRequiredChildren([parent1.id])

		then:
		3 == required.size()
    }

    private ServiceItem buildServiceItem(title, owner) {
        def item = new ServiceItem(title: title, owner: owner, launchUrl: 'https:///', types: new Types(title: 'test type').save(failOnError: true))
        item.save()
        return item
    }
}
