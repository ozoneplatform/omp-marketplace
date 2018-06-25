package marketplace.controller

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.Category
import marketplace.Constants
import marketplace.CustomFieldDefinition
import marketplace.DescriptorController
import marketplace.Intent
import marketplace.IntentAction
import marketplace.IntentDataType
import marketplace.OwfProperties
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.State
import marketplace.Types

import org.grails.web.json.JSONObject

import spock.lang.Specification

@Integration
@Rollback
class DescriptorControllerSpec extends Specification implements ControllerTestMixin<DescriptorController> {
//    List<Class> getDomainClasses() {[ItemComment]}

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
    def custField = new CustomFieldDefinition()
    //techPocs: ["t"],
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
                                      itemComments: [] as Set,
                                      rejectionListings: [] as SortedSet,
                                      serviceItemActivities: [],
                                      customFields: [],
                                      state: owfState,
                                      launchUrl: "https://",
                                      imageLargeUrl: "http://www.ozone.unit.testing.com",
                                      imageSmallUrl: "http://123foo.bar")

    void setup() {
        controller.params.id = MY_UUID
    }

    //TODO BVEST revisit
//    void testAnOwfCompatibleListingReturnsADescriptor() {
//        when:
//
//        controller.getItemAsJSON()
//        def json = new JSONObject(controller.response.contentAsString)
//
//        then:
//        json.total == 1
//
//        when:
//        def descriptorMap = json.data[0]
//
//        then:
//        assert descriptorMap.widgetUuid == serviceItem.uuid
//        assert descriptorMap.widgetGuid == serviceItem.uuid
//        assert descriptorMap.displayName == serviceItem.title
//        assert descriptorMap.description == serviceItem.description
//        assert descriptorMap.widgetVersion == serviceItem.versionName
//        assert descriptorMap.visible == serviceItem.owfProperties.visibleInLaunch
//        assert descriptorMap.singleton == serviceItem.owfProperties.singleton
//        assert descriptorMap.background == serviceItem.owfProperties.background
//        assert descriptorMap.mobileReady == serviceItem.owfProperties.mobileReady
//        assert descriptorMap.height == serviceItem.owfProperties.height
//        assert descriptorMap.width == serviceItem.owfProperties.width
//        assert descriptorMap.widgetUrl == serviceItem.launchUrl
//        assert descriptorMap.imageUrlLarge == serviceItem.imageLargeUrl
//        assert descriptorMap.imageUrlSmall == serviceItem.imageSmallUrl
//        assert descriptorMap.intents.receive.size() == 2
//        assert descriptorMap.intents.send.size() == 1
//        assert descriptorMap.intents.send[0].action == intent1.action.title
//        assert descriptorMap.intents.send[0].dataTypes[0] == intent1.dataType.title
//    }

    void testNoDescriptorForNonApprovedListing() {
        when:

        serviceItem.approvalStatus = IN_PROGRESS
        //serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)

        then:
        assert json.total == 0
    }

    void testNoDescriptorForNonOwfType() {
       when:
       serviceItem.types = notOwfType
       // serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)

       then:
       assert json.total == 0
    }

    void testNoDescriptorForNonOwfState() {
        when:
        serviceItem.state = notOwfState
//        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        then:
        assert json.total == 0
    }

    void testNoDescriptorForHiddenListing() {
        when:
        serviceItem.isHidden = 1
//        serviceItem.save()
        controller.getItemAsJSON()
        def json = new JSONObject(controller.response.contentAsString)
        then:
        assert json.total == 0
    }
//TODO BVEST Revisit after domain fix for ServiceItem
//    void testOwfWidgetTypeIsReproduced() {
//        when:
//        serviceItem.owfProperties.owfWidgetType = "standard"
//        serviceItem.save()
//        controller.getItemAsJSON()
//println(controller.response.contentAsString)
//        def json = new JSONObject(controller.response.contentAsString)
//        then:
//        assert json.total == 1
//
//        when:
//        def descriptorMap = json.data[0]
//
//        then:
//        assert descriptorMap?.widgetTypes[0] == serviceItem.owfProperties.owfWidgetType
//    }
//
//    void testStackUsesDescriptorField() {
//        def stackDescriptor = new JSONObject([stackContext: "foo", name: "Foo"])
//        serviceItem.owfProperties.stackDescriptor = stackDescriptor.toString()
//        serviceItem.save()
//        controller.getItemAsJSON()
//        def json = new JSONObject(controller.response.contentAsString)
//        assertEquals 1, json.total
//
//        def descriptorMap = json.data[0]
//        assertEquals stackDescriptor.stackContext, descriptorMap.stackContext
//        assertEquals stackDescriptor.name, descriptorMap.name
//    }
}
