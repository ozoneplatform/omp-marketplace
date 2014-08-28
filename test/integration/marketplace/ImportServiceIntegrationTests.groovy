package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import ozone.marketplace.enums.DefinedDefaultTypes
import ozone.utils.Utils

@TestMixin(IntegrationTestMixin)
class ImportServiceIntegrationTests extends MarketplaceIntegrationTestCase {

    def importService
    def importTaskService
    int nextId = 1
    final String futureDate =  "2051-10-21T04:29:00Z" // Back to the future!!
    final String oldDate =  "1970-01-01T06:00:00Z"

    void setUp() {
        super.setUp()
        (new Profile(username: "testAdmin1")).save()
        switchAdmin("testAdmin1")
    }

    void testImportSimpleListing() {
        JSONObject json = generateEmptyImportStructure()
        String username = "theuser"
        JSONObject profile = generateProfile(username)
        json.profiles.add(profile)
        String typeName = "A Type"
        JSONObject type = generateType(typeName)
        json.types.add(type)
        String stateName = "A State"
        JSONObject state = generateState(stateName)
        json.states.add(state)
        String listingTitle = "The title"
        json.serviceItems.add(generateSimpleServiceItem(listingTitle, profile, type, state))
        json.options.put("importAllProfiles", true)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        ServiceItem importedItem = ServiceItem.findByUuid(json.serviceItems[0].uuid)
        assert null != importedItem
        assert listingTitle == importedItem.title
        assert stateName == importedItem.state.title
        assert typeName == importedItem.types.title
        assert 1 == importedItem.owners.size()
        assert username == importedItem.owners[0].username
    }

    void testUpdateSimpleListing() {
        ServiceItem existingItem = ServiceItem.build(
                owners: [Profile.findByUsername("testAdmin1")],
                state: State.findByTitle("Active")
        )
        existingItem.save(failOnError: true)
        int serviceItemId = existingItem.id

        JSONObject listingJson = existingItem.asJSON()
        String listingTitle = "Updated title"
        listingJson.title = listingTitle
        listingJson.lastActivity = new JSONObject([activityDate: futureDate])
        JSONObject json = generateEmptyImportStructure()
        json.serviceItems.add(listingJson)
        json.profiles.add(existingItem.owners[0].asJSON())
        json.types.add(existingItem.types.asJSON())
        json.types[0].needsResolving = false
        json.types[0].editedDate = oldDate
        json.states.add(existingItem.state.asJSON())
        json.states[0].needsResolving = false
        json.states[0].editedDate = oldDate
        json.options.put("importAllProfiles", true)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        // Make sure that the existing item was updated by fetching it using the old database id.
        ServiceItem updatedItem = ServiceItem.get(serviceItemId)
        assert null != updatedItem
        assert listingTitle == updatedItem.title
    }

    void testMapType() {
        JSONObject json = generateEmptyImportStructure()
        String username = "theuser"
        JSONObject profile = generateProfile(username)
        json.profiles.add(profile)
        String typeName = "A Type"
        JSONObject type = generateType(typeName)
        int mappedTypeId = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title).id
        type.mapsTo = mappedTypeId
        json.types.add(type)
        String stateName = "A State"
        JSONObject state = generateState(stateName)
        json.states.add(state)
        String listingTitle = "The title"
        JSONObject listing = generateSimpleServiceItem(listingTitle, profile, type, state)
        listing.launchUrl = "http://"
        json.serviceItems.add(listing)
        json.options.put("importAllProfiles", true)

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        ServiceItem importedItem = ServiceItem.findByUuid(json.serviceItems[0].uuid)
        assert null != importedItem
        assert listingTitle == importedItem.title
        assert stateName == importedItem.state.title
        assert mappedTypeId == importedItem.types.id
        assert username == importedItem.owners[0].username
    }

    void testUpdateType() {
        JSONObject json = generateEmptyImportStructure()
        String typeName = "New Type Name"
        String typeDescription = "New description"
        JSONObject type = generateType(typeName)
        def existingTypeId = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title).id
        type.uuid = Types.get(existingTypeId).uuid
        type.description = typeDescription
        type.editedDate = futureDate
        json.types.add(type)

        def task = generateImportTaskFromJson(json)
//task.addToRuns(new ImportTaskResult())
        importService.execute(task)

        assert typeName == Types.get(existingTypeId).title
        assert typeDescription == Types.get(existingTypeId).description
    }

    void testDoNotUpdateImmutableType() {
        JSONObject json = generateEmptyImportStructure()
        String typeName = "New Type Name"
        String typeDescription = "New description"
        JSONObject type = generateType(typeName)
        def existingTypeId = Types.findByTitle(DefinedDefaultTypes.APP_COMPONENT.title).id
        type.uuid = Types.get(existingTypeId).uuid
        type.description = typeDescription
        type.editedDate = futureDate
        json.types.add(type)

        assert true ==  Types.get(existingTypeId).isPermanent

        def task = generateImportTaskFromJson(json)
        importService.execute(task)

        assert DefinedDefaultTypes.APP_COMPONENT.title == Types.get(existingTypeId).title
        assert DefinedDefaultTypes.APP_COMPONENT.description == Types.get(existingTypeId).description

    }

    // testImportAppComponent()
    // testUpdateAppComponent()
    // testImport7_3Format()
    // testMapCategory()
    // testImportListingWithNewCustomField()
    // testImportListingWithExistingCustomField()

    private JSONObject generateEmptyImportStructure() {
        return new JSONObject([
                serviceItems: new JSONArray(),
                states: new JSONArray(),
                categories: new JSONArray(),
                profiles: new JSONArray(),
                types: new JSONArray(),
                relationships: new JSONArray(),
                customFieldDefs: new JSONArray(),
                options: new JSONObject(),
                tags: new JSONObject()
        ])
    }

    private JSONObject generateProfile(String username) {
        return new JSONObject([
                username: username,
                bio: "",
                email: "",
                displayName: "",
                avatar: null,
                id: generateId()
        ])
    }

    private JSONObject generateUserRef(JSONObject profile) {
        return new JSONObject([
            username: profile.username,
            name: profile.displayName,
            id: profile.id
        ])
    }

    private JSONObject generateType(String name) {
        return new JSONObject([
                title: name,
                description: null,
                ozoneAware: false,
                hasLaunchUrl: false,
                hasIcons: false,
                image: null,
                uuid: Utils.generateUUID(),
                isPermanent: false,
                needsResolving: true,
                mapsTo: "create",
                id: generateId()
        ])
    }

    private JSONObject generateTypeRef(JSONObject type) {
        return new JSONObject([
                title: type.title,
                uuid: type.uuid,
                ozoneAware: type.ozoneAware,
                hasLaunchUrl: type.hasLaunchUrl,
                hasIcons: type.hasIcons,
                isPermanent: type.isPermanent,
                id: type.id
        ])
    }

    private JSONObject generateState(String name) {
        return new JSONObject([
                title: name,
                description: null,
                isPublished: true,
                uuid: Utils.generateUUID(),
                needsResolving: true,
                id: generateId()
        ])
    }

    private JSONObject generateStateRef(JSONObject state) {
        return new JSONObject([
                title: state.title,
                uuid: state.uuid,
                id: state.id
        ])
    }

    private JSONObject generateSimpleServiceItem(String title, JSONObject ownerProfile, JSONObject type, JSONObject state) {
        return new JSONObject([
                title: title,
                approvalStatus: "In Progress",
                launchUrl: null,
                state: generateStateRef(state),
                editedDate: "2013-08-15T18:19:46Z",
                releaseDate: "2013-08-28T04:00:00Z",
                agency: null,
                agencyIcon: null,
                screenshot1Url: null,
                screenshot2Url: null,
                imageSmallUrl: null,
                imageMediumUrl: null,
                imageLargeUrl: null,
                requirements: null,
                ozoneAware: false,
                organization: null,
                recommendedLayouts: null,
                dependencies: null,
                description: null,
                docUrls: null,
                types: generateTypeRef(type),
                techPocs: new JSONArray(),
                customFields: new JSONArray(),
                isPublished: false,
                versionName: null,
                approvedDate: null,
                isEnabled: false,
                installUrl: null,
                isOutside: null,
                owners: new JSONArray([generateUserRef(ownerProfile)]),
                categories: new JSONArray(),
                uuid: Utils.generateUUID(),
                totalComments: 0,
                totalRate1: 0,
                totalRate2: 0,
                totalRate3: 0,
                totalRate4: 0,
                totalRate5: 0,
                totalVotes: 0,
                avgRate: 0,
                class: "marketplace.ServiceItem",
                mapsTo: "create",
                id: generateId(),
                tags: new JSONArray()
        ])
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
