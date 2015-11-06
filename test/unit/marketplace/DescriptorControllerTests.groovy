package marketplace

import grails.test.mixin.TestFor
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.web.ControllerUnitTestMixin

import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.decorator.JSONDecoratorService

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(DescriptorController)
@TestMixin([ControllerUnitTestMixin, DomainClassUnitTestMixin])
class DescriptorControllerTests {

    final static IN_PROGRESS = Constants.APPROVAL_STATUSES["IN_PROGRESS"]
    final static APPROVED = Constants.APPROVAL_STATUSES["APPROVED"]
    final static MY_UUID = "a517513c-62da-4a4b-afac-19f2cb8848ac"

    def notOwfType = new Types(title: "t", ozoneAware: false)
    def owfType = new Types(title: "t", ozoneAware: true)
    def notOwfState = new State(title: "t", isPublished: false)
    def owfState = new State(title: "t", isPublished: true)
    def category = new Category(title: "c")
    def profile = new Profile(username: "u", createdDate: new Date())
    def intentAction = new IntentAction(title: 'someActionName')
    def intentDataType = new IntentDataType(title: 'application/json.test')
    def intent1 = new Intent(send: false, receive: true, action: intentAction, dataType: intentDataType)
    def intent2 = new Intent(send: true, receive: true, action: intentAction, dataType: intentDataType)
    def owfProperties = new OwfProperties(intents: [intent1, intent2])

    def serviceItem = new ServiceItem(title: "t",
                                      description: "d",
                                      uuid: MY_UUID,
                                      versionName: "v",
                                      releaseDate: new Date(),
                                      approvalStatus: APPROVED,
                                      categories: [category],
                                      owfProperties: owfProperties,
                                      types: owfType,
                                      owners: [profile],
                                      techPocs: ["t"],
                                      itemComments: [] as Set,
                                      rejectionListings: [] as SortedSet,
                                      serviceItemActivities: [],
                                      customFields: [],
                                      state: owfState,
                                      launchUrl: "https://",
                                      imageLargeUrl: "http://www.ozone.unit.testing.com",
                                      imageSmallUrl: "http://123foo.bar")

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(TextCustomField)
        mockForConstraintsTests(TextAreaCustomField)
        mockDomain(IntentAction, [intentAction])
        mockDomain(IntentDataType, [intentDataType])
        mockDomain(Intent, [intent1, intent2])
        mockDomain(ServiceItem, [serviceItem])

        def JSONDecoratorService = mockFor(JSONDecoratorService)
        controller.JSONDecoratorService = JSONDecoratorService.createMock()
        JSONDecoratorService.demand.postProcessJSON(9999) { json -> return }

        def relationshipService = mockFor(RelationshipService)
        controller.relationshipService = relationshipService.createMock()
        relationshipService.demand.getRequiresItems(9999) { a, b, c -> [] }

        controller.params.id = MY_UUID
    }

    void testAnOwfCompatibleListingReturnsADescriptor() {
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        assertEquals json.total, 1

        def descriptorMap = json.data[0]
        assertEquals descriptorMap.widgetUuid, serviceItem.uuid
        assertEquals descriptorMap.widgetGuid, serviceItem.uuid
        assertEquals descriptorMap.displayName, serviceItem.title
        assertEquals descriptorMap.description, serviceItem.description
        assertEquals descriptorMap.widgetVersion, serviceItem.versionName
        assertEquals descriptorMap.visible, serviceItem.owfProperties.visibleInLaunch
        assertEquals descriptorMap.singleton, serviceItem.owfProperties.singleton
        assertEquals descriptorMap.background, serviceItem.owfProperties.background
        assertEquals descriptorMap.mobileReady, serviceItem.owfProperties.mobileReady
        assertEquals descriptorMap.height, serviceItem.owfProperties.height
        assertEquals descriptorMap.width, serviceItem.owfProperties.width
        assertEquals descriptorMap.widgetUrl, serviceItem.launchUrl
        assertEquals descriptorMap.imageUrlLarge, serviceItem.imageLargeUrl
        assertEquals descriptorMap.imageUrlSmall, serviceItem.imageSmallUrl
        assertEquals descriptorMap.intents.receive.size(), 2
        assertEquals descriptorMap.intents.send.size(), 1
        assertEquals descriptorMap.intents.send[0].action, intent1.action.title
        assertEquals descriptorMap.intents.send[0].dataTypes[0], intent1.dataType.title
    }

    void testNoDescriptorForNonApprovedListing() {
        serviceItem.approvalStatus = IN_PROGRESS
        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        assertEquals json.total, 0
    }

    void testNoDescriptorForNonOwfType() {
        serviceItem.types = notOwfType
        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        assertEquals json.total, 0
    }

    void testNoDescriptorForNonOwfState() {
        serviceItem.state = notOwfState
        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        assertEquals json.total, 0
    }

    void testNoDescriptorForHiddenListing() {
        serviceItem.isHidden = 1
        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        assertEquals json.total, 0
    }

    void testOwfWidgetTypeIsReproduced() {
        serviceItem.owfProperties.owfWidgetType = "standard"
        serviceItem.save()
        controller.getItemAsJSON()

        def json = new JSONObject(controller.response.contentAsString)
        assertEquals json.total, 1

        def descriptorMap = json.data[0]
        assert descriptorMap?.widgetTypes[0] == serviceItem.owfProperties.owfWidgetType
    }

    void testStackUsesDescriptorField() {
        def stackDescriptor = new JSONObject([stackContext: "foo", name: "Foo"])
        serviceItem.owfProperties.stackDescriptor = stackDescriptor.toString()
        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        assertEquals 1, json.total

        def descriptorMap = json.data[0]
        assertEquals stackDescriptor.stackContext, descriptorMap.stackContext
        assertEquals stackDescriptor.name, descriptorMap.name
    }
}
