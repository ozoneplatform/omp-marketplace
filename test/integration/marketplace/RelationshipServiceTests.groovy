package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import ozone.marketplace.enums.RelationshipType;
import grails.test.*
import marketplace.Constants


@TestMixin(IntegrationTestMixin)
class RelationshipServiceTests extends MarketplaceIntegrationTestCase {

	def relationshipService
	def serviceItemService
	def serviceItemActivityService

    void setUp() {
        super.setUp()
		relationshipService.serviceItemService = serviceItemService
    }

	void testSaveRelationship(){
		ServiceItem parent = new ServiceItem(
            title:"Parent",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		relationshipService.saveRelationship(new Relationship(relationshipType: RelationshipType.REQUIRE, owningEntity: parent))
	}

	void testSaveRelationshipThrowsException(){
		ServiceItem parent = new ServiceItem(
            title:"Parent",
            launchUrl: 'https:///',
            types: new Types(title: 'test type').save(failOnError: true),
            owners: [testUser1]
        ).save(flush:true, failOnError:true)
		shouldFail(grails.validation.ValidationException){
			relationshipService.saveRelationship(new Relationship(
                relationshipType: null,
                owningEntity: parent)
            )
		}
	}

    void testAddRequirestoEmptyRelationship() {
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
		assert 3 == rel.relatedItems.size()
		assert "Requires 1" == rel.relatedItems[0].title
		assert "Requires 2" == rel.relatedItems[1].title
		assert "Requires 3" == rel.relatedItems[2].title
		assert RelationshipType.REQUIRE == rel.relationshipType
    }

	def getActivity = { si,action ->
		si.serviceItemActivities.findAll { it.action == action}.max(){ it.activityDate }
	}

	void testAddRequiresToExistingRelationship() {
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem requiresA = ServiceItem.build(title:"Requires A",owners: [testUser1]).save()
		relationshipService.addOrRemoveRequires(parent.id,[requiresA.id])

		def rel  = Relationship.findByOwningEntity(parent)
		def parentActivity = getActivity(parent,Constants.Action.ADDRELATEDITEMS)
		assert RelationshipType.REQUIRE == rel.relationshipType
		assert "Requires A" == rel.relatedItems[0].title
		assert null != parentActivity
		assert 1 == parentActivity.items.size()
		assert "Requires A" == parentActivity.items[0].title

		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requiresA.id, requires1.id,requires2.id])

		rel  = Relationship.findByOwningEntity(parent)
		parentActivity = getActivity(parent,Constants.Action.ADDRELATEDITEMS)
		def requires1Activity = getActivity(requires1,Constants.Action.ADDRELATEDTOITEM)
		def requires2Activity = getActivity(requires2,Constants.Action.ADDRELATEDTOITEM)

		assert 3 == rel.relatedItems.size()
		assert RelationshipType.REQUIRE == rel.relationshipType
		assert "Requires A" == rel.relatedItems[0].title
		assert "Requires 1" == rel.relatedItems[1].title
		assert "Requires 2" == rel.relatedItems[2].title

		assert null != parentActivity
		assert 2 == parentActivity.items.size()
		assert "Requires 1" == parentActivity.items[0].title
		assert "Requires 2" == parentActivity.items[1].title
		assert Constants.Action.ADDRELATEDITEMS == parentActivity.action

		assert null != requires1Activity
		assert "Parent" == requires1Activity.items[0].title

		assert null != requires2Activity
		assert "Parent" == requires2Activity.items[0].title
		assert Constants.Action.ADDRELATEDTOITEM == requires2Activity.action


		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires3.id,requires2.id,requires1.id])

		rel  = Relationship.findByOwningEntity(parent)
		def parentAddActivity = getActivity(parent,Constants.Action.ADDRELATEDITEMS)
		def parentRemoveActivity = getActivity(parent,Constants.Action.REMOVERELATEDITEMS)
		def requiresAActivity = getActivity(requiresA,Constants.Action.REMOVERELATEDTOITEM)

		assert 3 == rel.relatedItems.size()
		assert "Requires 3" == rel.relatedItems[0].title
		assert "Requires 2" == rel.relatedItems[1].title
		assert "Requires 1" == rel.relatedItems[2].title
		assert RelationshipType.REQUIRE == rel.relationshipType

		assert null != parentAddActivity
		assert 1 == parentAddActivity.items.size()
		assert "Requires 3" == parentAddActivity.items[0].title

		assert null != parentRemoveActivity
		assert 1 == parentRemoveActivity.items.size()
		assert "Requires A" == parentRemoveActivity.items[0].title

		assert null != requiresAActivity
		assert "Parent" == requiresAActivity.items[0].title

		relationshipService.addOrRemoveRequires(parent.id,[requires2.id])
		parentActivity = getActivity(parent,Constants.Action.REMOVERELATEDITEMS)

		assert 1 == rel.relatedItems.size()
		assert "Requires 2" == rel.relatedItems[0].title
		assert RelationshipType.REQUIRE == rel.relationshipType

		assert null != parentActivity
		assert 2 == parentActivity.items.size()
		assert "Requires 3" == parentActivity.items[0].title
		assert "Requires 1" == parentActivity.items[1].title
	}

	void testAddNonApprovedRequiresThatIDoNotOwn() {
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()

		def userLewis = Profile.build(username: 'rLewis', displayName: 'Ray Lewis').save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1", owners: [userLewis], approvalStatus:Constants.APPROVAL_STATUSES["REJECTED"]).save()
		shouldFail(AccessControlException){
			relationshipService.addOrRemoveRequires(parent.id,[requires1.id])
		}
	}

	void testAddRequirestoAListingIDoNotOwn() {
		def userLewis = Profile.build(username: 'rLewis', displayName: 'Ray Lewis').save()
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [userLewis],approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]).save()

		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)

		shouldFail(PermissionException){
			relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])
		}
	}

	void testRemoveAllRequires() {
		ServiceItem parent = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent.id,[requires1.id,requires2.id,requires3.id])

		relationshipService.addOrRemoveRequires(parent.id,[null])

		def rel  = Relationship.findByOwningEntity(parent)
		assert 0 == rel.relatedItems.size()
	}

	void testGetAllRequiredBy() {
		ServiceItem parent1 = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem requires1 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires2 = ServiceItem.build(title:"Requires 2",owners: [testUser1]).save()
		ServiceItem requires3 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
        relationshipService.addOrRemoveRequires(parent1.id,[requires1.id,requires2.id, requires3.id])
        ServiceItem parent2 = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
		ServiceItem requires4 = ServiceItem.build(title:"Requires 1",owners: [testUser1]).save()
		ServiceItem requires5 = ServiceItem.build(title:"Requires 3",owners: [testUser1]).save(flush:true)
		relationshipService.addOrRemoveRequires(parent2.id,[requires4.id,requires5.id])

		def required = relationshipService.getAllRequiredBy([parent1.id, parent2.id]);

		assert 7 == required.size()

	}

    void testGetAllRequiredChildren() {
        ServiceItem parent1 = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
        ServiceItem parent2 = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
        ServiceItem parent3 = ServiceItem.build(title:"Parent",owners: [testUser1]).save()
        relationshipService.addOrRemoveRequires(parent1.id,[parent2.id])
        relationshipService.addOrRemoveRequires(parent2.id,[parent3.id])

        def required = relationshipService.getAllRequiredChildren([parent1.id])
        assert 3 == required.size()
    }
}
