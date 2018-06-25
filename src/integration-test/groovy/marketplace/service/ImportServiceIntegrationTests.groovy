package marketplace.service

import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import marketplace.ImportService
import marketplace.ImportTaskService
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.State
import marketplace.Types
import marketplace.controller.MarketplaceIntegrationTestCase

import ozone.marketplace.enums.DefinedDefaultTypes
import ozone.utils.Utils


@Integration
@Rollback
class ImportServiceIntegrationTests extends MarketplaceIntegrationTestCase {

    ImportService importService
    ImportTaskService importTaskService

    int nextId = 1
    final String futureDate = "2051-10-21T04:29:00Z" // Back to the future!!
    final String oldDate = "1970-01-01T06:00:00Z"

    void setup() {
        new Profile(username: "testAdmin2").save()
        switchAdmin("testAdmin2")
    }

    void testImportSimpleListing() {
        when:
        String username = "theuser"
        String typeName = "A Type"
        String stateName = "A State"
        String listingTitle = "The title"

        JSONObject json = generateEmptyImportStructure()
        JSONObject profile = generateProfile(username)
        json.profiles.add(profile)
        JSONObject type = generateType(typeName)
        json.types.add(type)
        JSONObject state = generateState(stateName)
        json.states.add(state)
        json.serviceItems.add(generateSimpleServiceItem(listingTitle, profile, type, state))
        json.options.put("importAllProfiles", true)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        then:
        ServiceItem importedItem = ServiceItem.findByUuid(json.serviceItems[0].uuid)

        importedItem != null
        importedItem.title == listingTitle
        importedItem.state.title == stateName
        importedItem.types.title == typeName
        importedItem.owners.size() == 1
        importedItem.owners[0].username == username
    }

    void testUpdateSimpleListing() {
        given:
        def serviceItem = new ServiceItem([title    : "Service Item 1",
                                           types    : findWebAppType(),
                                           owners   : [Profile.findByUsername("testAdmin2")],
                                           state    : State.findByTitle("Active"),
                                           launchUrl: "https:///"])
                .save(failOnError: true)

        def serviceItemId = serviceItem.id

        when:
        JSONObject listingJson = serviceItem.asJSON()
        String listingTitle = "Updated title"
        listingJson.title = listingTitle
        listingJson.lastActivity = new JSONObject([activityTimestamp: futureDate])
        JSONObject json = generateEmptyImportStructure()
        json.serviceItems.add(listingJson)
        json.profiles.add(serviceItem.owners[0].asJSON())
        json.types.add(serviceItem.types.asJSON())
        json.types[0].needsResolving = false
        json.types[0].editedDate = oldDate
        json.states.add(serviceItem.state.asJSON())
        json.states[0].needsResolving = false
        json.states[0].editedDate = oldDate
        json.options.put("importAllProfiles", true)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        then:
        // Make sure that the existing item was updated by fetching it using the old database id.
        ServiceItem updatedItem = ServiceItem.get(serviceItemId)

        updatedItem != null
        updatedItem.title == listingTitle
    }

    void testMapType() {
        when:
        String username = "theuser"
        String typeName = "A Type"
        String stateName = "A State"
        String listingTitle = "The title"

        JSONObject json = generateEmptyImportStructure()
        JSONObject profile = generateProfile(username)
        json.profiles.add(profile)
        JSONObject type = generateType(typeName)
        def mappedTypeId = findWebAppType().id
        type.mapsTo = mappedTypeId
        json.types.add(type)
        JSONObject state = generateState(stateName)
        json.states.add(state)
        JSONObject listing = generateSimpleServiceItem(listingTitle, profile, type, state)
        listing.launchUrl = "http://"
        json.serviceItems.add(listing)
        json.options.put("importAllProfiles", true)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        then:
        ServiceItem importedItem = ServiceItem.findByUuid(json.serviceItems[0].uuid)

        importedItem != null
        importedItem.title == listingTitle
        importedItem.state.title == stateName
        importedItem.types.id == mappedTypeId
        importedItem.owners[0].username == username
    }

    void testUpdateType() {
        when:
        String typeName = "New Type Name"
        String typeDescription = "New description"

        JSONObject type = generateType(typeName)
        def existingTypeId = findWebAppType().id
        type.uuid = Types.get(existingTypeId).uuid
        type.description = typeDescription
        type.editedDate = futureDate
        JSONObject json = generateEmptyImportStructure()
        json.types.add(type)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        then:
        with(Types.get(existingTypeId)) {
            title == typeName
            description == typeDescription
        }
    }

    private static Types findWebAppType() {
        Types.findByTitle(DefinedDefaultTypes.WEB_APP.title)
    }

    private static Types findAppComponentType() {
        Types.findByTitle(DefinedDefaultTypes.APP_COMPONENT.title)
    }

    void testDoNotUpdateImmutableType() {
        given:
        def existingTypeId = findAppComponentType().id

        assert Types.get(existingTypeId).isPermanent

        when:
        JSONObject type = generateType("New Type Name")
        type.uuid = Types.get(existingTypeId).uuid
        type.description = "New description"
        type.editedDate = futureDate

        JSONObject json = generateEmptyImportStructure()
        json.types.add(type)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        then:
        with(Types.get(existingTypeId)) {
            title == DefinedDefaultTypes.APP_COMPONENT.title
            description == DefinedDefaultTypes.APP_COMPONENT.description
        }
    }

    private JSONObject generateEmptyImportStructure() {
        new JSONObject([serviceItems   : new JSONArray(),
                        states         : new JSONArray(),
                        categories     : new JSONArray(),
                        profiles       : new JSONArray(),
                        types          : new JSONArray(),
                        relationships  : new JSONArray(),
                        customFieldDefs: new JSONArray(),
                        options        : new JSONObject(),
                        tags           : new JSONObject()])
    }

    private JSONObject generateProfile(String username) {
        new JSONObject([username   : username,
                        bio        : "",
                        email      : "",
                        displayName: "",
                        avatar     : null,
                        id         : generateId()])
    }

    private JSONObject generateUserRef(JSONObject profile) {
        new JSONObject([username: profile.username,
                        name    : profile.displayName,
                        id      : profile.id])
    }

    private JSONObject generateType(String name) {
        new JSONObject([title         : name,
                        description   : null,
                        ozoneAware    : false,
                        hasLaunchUrl  : false,
                        hasIcons      : false,
                        image         : null,
                        uuid          : Utils.generateUUID(),
                        isPermanent   : false,
                        needsResolving: true,
                        mapsTo        : "create",
                        id            : generateId()])
    }

    private JSONObject generateTypeRef(JSONObject type) {
        new JSONObject([title       : type.title,
                        uuid        : type.uuid,
                        ozoneAware  : type.ozoneAware,
                        hasLaunchUrl: type.hasLaunchUrl,
                        hasIcons    : type.hasIcons,
                        isPermanent : type.isPermanent,
                        id          : type.id])
    }

    private JSONObject generateState(String name) {
        new JSONObject([title         : name,
                        description   : null,
                        isPublished   : true,
                        uuid          : Utils.generateUUID(),
                        needsResolving: true,
                        id            : generateId()])
    }

    private JSONObject generateStateRef(JSONObject state) {
        new JSONObject([title: state.title,
                        uuid : state.uuid,
                        id   : state.id])
    }

    private JSONObject generateSimpleServiceItem(String title, JSONObject ownerProfile, JSONObject type,
                                                 JSONObject state)
    {
        new JSONObject([title             : title,
                        approvalStatus    : "In Progress",
                        launchUrl         : null,
                        state             : generateStateRef(state),
                        editedDate        : "2013-08-15T18:19:46Z",
                        releasedDate      : "2013-08-28T04:00:00Z",
                        agency            : null,
                        agencyIcon        : null,
                        screenshot1Url    : null,
                        screenshot2Url    : null,
                        imageSmallUrl     : null,
                        imageMediumUrl    : null,
                        imageLargeUrl     : null,
                        requirements      : null,
                        ozoneAware        : false,
                        organization      : null,
                        recommendedLayouts: null,
                        dependencies      : null,
                        description       : null,
                        docUrls           : null,
                        types             : generateTypeRef(type),
                        techPocs          : new JSONArray(),
                        customFields      : new JSONArray(),
                        isPublished       : false,
                        versionName       : null,
                        approvalDate      : null,
                        isEnabled         : false,
                        installUrl        : null,
                        isOutside         : null,
                        owners            : new JSONArray([generateUserRef(ownerProfile)]),
                        categories        : new JSONArray(),
                        uuid              : Utils.generateUUID(),
                        totalComments     : 0,
                        totalRate1        : 0,
                        totalRate2        : 0,
                        totalRate3        : 0,
                        totalRate4        : 0,
                        totalRate5        : 0,
                        totalVotes        : 0,
                        avgRate           : 0,
                        class             : "marketplace.ServiceItem",
                        mapsTo            : "create",
                        id                : generateId(),
                        tags              : new JSONArray()])
    }

    private int generateId() {
        return nextId++
    }

    private def generateImportTaskFromJson(def json) {
        def task = importTaskService.getFileImportTask()
        task.json = JSON.parse(json.toString())
        return task
    }

}
