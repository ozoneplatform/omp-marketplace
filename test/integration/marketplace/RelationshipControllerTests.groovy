package marketplace;

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import static org.junit.Assert.*
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

@TestMixin(IntegrationTestMixin)
class RelationshipControllerTests extends MarketplaceIntegrationTestCase {
	def relationshipService
	def serviceItemService
	def genericQueryService

	def parent
	def requires1, requires2, requires3
    def sub4, sub5, sub6, sub7
    def complexRelationships

    def testType1

	void setUp() {
		super.setUp()
        testType1 = new Types(title:"Orange Marmalade", ozoneAware:  true).save(failOnError:true)
		controller = new RelationshipController()
		controller.relationshipService = relationshipService
		controller.serviceItemService = serviceItemService
		controller.genericQueryService = genericQueryService
	}

    def logIt(def strIn)
    {
        relationshipService.logIt(strIn)
    }

	private void buildRelationship(){
        def state = new State(title: 'Relationship Test State', isPublished: true).save()
		parent = new ServiceItem(
            title:"Parent",
            types: testType1,
            launchUrl: 'https:///',
            owners: [testUser1],
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            imageSmallUrl: "http://www.image.com",
            state: state
        ).save(failOnError:true)
		requires1 = new ServiceItem(
            title:"Requires 1",
            launchUrl: 'https:///',
            types: testType1,
            owners: [testUser1],
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            state: state
        ).save(failOnError:true)
		requires2 = new ServiceItem(
            title:"Requires 2",
            owners: [testUser1],
            launchUrl: 'https:///',
            types: testType1,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            imageSmallUrl: "http://www.image.com",
            state: state
        ).save(failOnError:true)
		requires3 = new ServiceItem(
            title:"Requires 3",
            owners: [testUser1],
            launchUrl: 'https:///',
            types: testType1,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            state: state
        ).save(failOnError:true, flush:true)
	}

    /*
     * Constructs a tree of requires relationships (with circular references) as follows:
     parent
        requires1
            sub4
            sub5
                requires1
        requires2
            parent
        requires3
            sub5
                requires1
            sub6
            sub7
    */
	private void buildAndSaveMoreRelationships(){
        buildAndSaveRelationship()
		sub4 = new ServiceItem(
            title:"sub4",
            owners: [testUser1],
            launchUrl: 'https:///',
            types: testType1,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
		sub5 = new ServiceItem(
            title:"sub5",
            owners: [testUser1],
            launchUrl: 'https:///',
            types: testType1,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            imageSmallUrl: "http://www.image.com"
        ).save(failOnError:true)
		sub6 = new ServiceItem(
            title:"sub6",
            owners: [testUser1],
            launchUrl: 'https:///',
            types: testType1,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true, flush:true)
		sub7 = new ServiceItem(
            title:"sub7",
            owners: [testUser1],
            launchUrl: 'https:///',
            types: testType1,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true, flush:true)

        saveRelationship(requires1.id, [sub4.id, sub5.id])
        saveRelationship(requires2.id, [parent.id])
        saveRelationship(requires3.id, [sub5.id, sub6.id, sub7.id])
        saveRelationship(sub5.id, [requires1.id])

        complexRelationships = [
            [parent: parent.id, requires: [[id: requires1.id, title: requires1.title, uuid: requires1.uuid],
                                           [id: requires2.id, title: requires2.title, uuid: requires2.uuid],
                                           [id: requires3.id, title: requires3.title, uuid: requires3.uuid]
                                       ]],
            [parent: requires1.id, requires: [[id: sub4.id, title: sub4.title, uuid: sub4.uuid],
                                              [id: sub5.id, title: sub5.title, uuid: sub5.uuid]
                                       ]],
            [parent: requires2.id, requires: [[id: parent.id, title: parent.title, uuid: parent.uuid]
                                       ]],
            [parent: requires3.id, requires: [[id: sub5.id, title: sub5.title, uuid: sub5.uuid],
                                              [id: sub6.id, title: sub6.title, uuid: sub6.uuid],
                                              [id: sub7.id, title: sub7.title, uuid: sub7.uuid]
                                       ]],
            [parent: sub4.id, requires: [
                                       ]],
            [parent: sub5.id, requires: [[id: requires1.id, title: requires1.title, uuid: requires1.uuid]
                                       ]],
            [parent: sub6.id, requires: [
                                       ]],
            [parent: sub7.id, requires: [
                                       ]]
        ]
	}

    private saveRelationship(def parentId, def requires){
		relationshipService.addOrRemoveRequires(parentId.toLong(), requires)
		resetController()
    }

	private void buildAndSaveRelationship(){
		buildRelationship()
        saveRelationship(parent.id, [requires1.id,requires2.id,requires3.id])
	}

	void resetController() {
		controller.response.committed = false
		controller.response.reset()
	}

	void testGetRelatedItems() {
        logIt "testGetRelatedItems:"
		buildAndSaveRelationship()
		controller.params.id = parent.id
		controller.getRelatedItems()

		def controllerResponse = JSON.parse(controller.response.contentAsString)
		assert true ==  controllerResponse.success
		assert 3 == controllerResponse.totalCount
		assert "Requires 1" == controllerResponse.data[0].title
		assert "Requires 2" == controllerResponse.data[1].title
		assert "Requires 3" == controllerResponse.data[2].title
		assert JSONObject.NULL == controllerResponse.data[2].versionName
	}

	void testGetRelatedItemsErrorCase() {
        logIt "running testGetRelatedItemsErrorCase:"
		buildAndSaveRelationship()
		controller.params.id = '98989898989898989889898'
		controller.getRelatedItems()

		def controllerResponse = JSON.parse(controller.response.contentAsString)
		assert false == controllerResponse.success
	}

	void testGetOWFRequiredItems() {
        logIt "running testGetOWFRequiredItems:"
		buildAndSaveRelationship()
		controller.params.id = parent.id
		controller.getOWFRequiredItems()

        println controller.response.contentAsString

		def controllerResponse = JSON.parse(controller.response.contentAsString)
		assert 4 == controllerResponse.total
        assert "Parent" == controllerResponse.data[0].title
        assert 3 == controllerResponse.data[0].requires.size()
        def expectedIds = [requires1.id, requires2.id, requires3.id] as Set
        assert expectedIds == controllerResponse.data[0].requires.collect {it.id as Long } as Set
        logIt "${controllerResponse.data[0].requires}"

		assert "Requires 1" == controllerResponse.data[1].title
		assert "Requires 2" == controllerResponse.data[2].title
	}

    def findResult(def data, def id){
        def idAsInteger = id as Integer
        return data.find{it.id == id}
    }

    void compareRequiresItem(def expectedItem, def resultsItem){
        logIt "compareRequiresItem: ${expectedItem.title} ${resultsItem.title} "
        assert expectedItem.id == resultsItem.id
        assert expectedItem.title == resultsItem.title
        assert expectedItem.uuid == resultsItem.uuid
    }

    void compareToResults(def expectedItem, def resultsItem){
        logIt "compareToResults: ${expectedItem.parent} ${resultsItem.id}"
        assert expectedItem.parent == resultsItem.id
        assert expectedItem.requires.size() == resultsItem.requires.size()
        expectedItem.requires.each{
            compareRequiresItem(it, resultsItem.requires.find{it2 -> it2.id == it.id})
        }
    }
}
