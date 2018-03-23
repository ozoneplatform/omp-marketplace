package marketplace.rest

import spock.lang.Unroll

import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONObject

import org.springframework.http.HttpStatus

import marketplace.ChangeDetail
import marketplace.Constants
import marketplace.ItemComment
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemTag
import marketplace.Tag
import marketplace.Types

import static marketplace.rest.JsonMatchers.*
import static ozone.utils.Utils.generateUUID


@Integration
class ProfileRestControllerSpec extends RestSpec {

    Profile testAdmin1
    Profile testUser1
    JSONObject testUser1Json

    List<Long> defaultProfileIds

    List<Profile> profiles
    List<JSONObject> profilesJson

    List<JSONObject> allProfilesJson

    void setupSpec() {
        marshaller ServiceItem
        marshaller Types
    }

    @Transactional
    def setup() {
        assert ServiceItem.count() == 0
        assert ItemComment.count() == 0
        assert Tag.count() == 0
        assert ServiceItemTag.count() == 0
        assert ServiceItemActivity.count() == 0

        loginAsUser()

        testAdmin1 = Profile.findByUsername('testAdmin1')
        testUser1 = Profile.findByUsername('testUser1')
        testUser1Json = testUser1.asJSON()

        def defaultProfiles = Profile.findAll(sort: 'id', order: 'asc')
        defaultProfileIds = defaultProfiles*.id

        def profile1 = createProfile('profile1', 'Profile 1')
        def profile2 = createProfile('profile2', 'Profile 2')
        def profile3 = createProfile('profile3', 'Profile 3')

        profiles = [profile1, profile2, profile3]
        profilesJson = profiles.collect { it.asJSON() }

        def defaultProfilesJson = defaultProfiles.collect { it.asJSON() }
        allProfilesJson = defaultProfilesJson + profilesJson
    }

    @Transactional
    int countProfiles() {
        Profile.count()
    }

    @Transactional
    void cleanup() {
        ServiceItemActivity.findAll().each { it.delete() }

        ServiceItemTag.findAll().each { it.delete() }
        Tag.findAll().each { it.delete() }

        ItemComment.findAll().each { it.delete() }

        ServiceItem.findAll().each { it.delete() }

        Profile.findAll()
               .findAll { !defaultProfileIds.contains(it.id) }
               .each { it.delete() }

        logout()
    }

    def 'index - GET /api/profile/'() {
        when:
        def response = get("/api/profile/")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            allProfilesJson.collect { it + hasUserPrefs() }
        }
    }

    def 'show - GET /api/profile/$ID'() {
        when:
        def response = get("/api/profile/${profiles[0].id}")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            profilesJson[0] + hasUserPrefs()
        }
    }

    def 'show - GET /api/profile/$ID - not found'() {
        when:
        def response = get("/api/agency/-1")

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def 'show - GET /api/profile/self'() {
        given:
        loginAsUser()

        when:
        def response = get("/api/profile/self")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            testUser1Json + hasUserPrefs()
        }
    }

    def 'create - POST /api/profile/'() {
        given:
        loginAsAdmin()

        int initialProfileCount = countProfiles()

        when:
        def profile = [username   : "picardj",
                       displayName: "Jean-Luc Picard"]

        def response = post("/api/profile/", profile)

        then:
        response.statusCode == HttpStatus.CREATED

        matches(response.json) {
            profile + hasId() + hasUserPrefs() + hasAuditStamp(testAdmin1)
        }

        countProfiles() == initialProfileCount + 1
    }

    def 'create - POST /api/profile/ - without ADMIN role should fail'() {
        given:
        loginAsUser()

        int initialProfileCount = countProfiles()

        when:
        def profile = [username   : "picardj",
                       displayName: "Jean-Luc Picard"]

        def response = post("/api/profile/", profile)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countProfiles() == initialProfileCount
    }

    def 'update - PUT /api/profile/$ID'() {
        given:
        loginAsUser(profiles[0])

        when:
        def profile = [email: "user1@foo.com"]

        def response = put("/api/profile/${profiles[0].id}", profile)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            profilesJson[0] + [email: "user1@foo.com"] + hasUserPrefs() + hasAuditStamp(testUser1, profiles[0])
        }
    }

    def 'update - PUT /api/profile/$ID - with ADMIN role, can update another Profile'() {
        given:
        loginAsAdmin()

        when:
        def profile = [email: "user1@foo.com"]

        def response = put("/api/profile/${profiles[0].id}", profile)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            profilesJson[0] + [email: "user1@foo.com"] + hasUserPrefs() + hasAuditStamp(testUser1, testAdmin1)
        }
    }

    def 'update - PUT /api/profile/$ID - with USER role, updating another Profile should fail'() {
        given:
        loginAsUser(profiles[1])

        when:
        def profile = [displayName: "John Q. Public"]

        def response = put("/api/profile/${profiles[0].id}", profile)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'delete - DELETE /api/profile/$ID - can delete own Profile'() {
        given:
        loginAsUser(profiles[0])

        int initialProfileCount = countProfiles()

        when:
        def response = delete("/api/profile/${profiles[0].id}")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countProfiles() == initialProfileCount - 1
    }

    def 'delete - DELETE /api/profile/$ID - with ADMIN role, can delete other Profile'() {
        given:
        loginAsAdmin()

        int initialProfileCount = countProfiles()

        when:
        def response = delete("/api/profile/${profiles[0].id}")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countProfiles() == initialProfileCount - 1
    }

    def 'delete - DELETE /api/profile/$ID - with USER role, deleting another Profile should fail'() {
        given:
        loginAsUser(profiles[1])

        int initialProfileCount = countProfiles()

        when:
        def response = delete("/api/profile/${profiles[0].id}")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countProfiles() == initialProfileCount
    }

    @Unroll
    def 'index - GET /api/profile/#id/serviceItem'(String id) {
        given:
        loginAsUser(profiles[0])

        def serviceItems = setupServiceItems(profiles[0].id)

        when:
        def profileId = (id == 'self') ? 'self' : profiles[0].id
        def response = get("/api/profile/$profileId/serviceItem")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, serviceItems)

        where:
        id << ['$ID', 'self']
    }

    @Unroll
    def 'index - GET /api/profile/#id/itemComment'(String id) {
        given:
        loginAsUser(profiles[0])

        def expectedItemComments = setupItemComments(profiles[0].id)

        when:
        def profileId = (id == 'self') ? 'self' : profiles[0].id
        def response = get("/api/profile/$profileId/itemComment")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, expectedItemComments)

        where:
        id << ['$ID', 'self']
    }

    @Unroll
    def 'index - GET /api/profile/#id/tag'(String id) {
        given:
        loginAsUser(profiles[0])

        def expectedItemTags = setupItemTags(profiles[0].id)

        when:
        def profileId = (id == 'self') ? 'self' : profiles[0].id
        def response = get("/api/profile/$profileId/tag")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, expectedItemTags)

        where:
        id << ['$ID', 'self']
    }

    @Unroll
    def 'index - GET /api/profile/#id/activity'(String id) {
        given:
        loginAsUser(profiles[0])

        def expectedActivities = setupServiceItemActivities(profiles[0].id)

        when:
        def profileId = (id == 'self') ? 'self' : profiles[0].id
        def response = get("/api/profile/$profileId/activity")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, expectedActivities)

        where:
        id << ['$ID', 'self']
    }

    @Unroll
    def 'index - GET /api/profile/#id/serviceItem/activity'(String id) {
        given:
        loginAsUser(profiles[0])

        def expectedActivities = setupServiceItemActivities(profiles[0].id)

        when:
        def profileId = (id == 'self') ? 'self' : profiles[0].id
        def response = get("/api/profile/$profileId/serviceItem/activity")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, expectedActivities)

        where:
        id << ['$ID', 'self']
    }

    private static Map<String, ?> hasUserPrefs() {
        [theme            : isAnyValue(),
         animationsEnabled: isAnyValue()]
    }

    @Transactional
    private List<JSONObject> setupServiceItems(Long ownerId) {
        Profile owner = Profile.findById(ownerId)

        createServiceItems(owner).collect { it.asJSON() }
    }

    @Transactional
    private List<JSONObject> setupItemComments(Long authorProfileId) {
        Profile author = Profile.findById(authorProfileId)

        def serviceItems = createServiceItems(author)

        def itemComment1 = save new ItemComment([author     : author,
                                                 serviceItem: serviceItems[0],
                                                 rate       : 1F,
                                                 text       : "Horrible"])

        def itemComment2 = save new ItemComment([author     : author,
                                                 serviceItem: serviceItems[1],
                                                 rate       : 3F,
                                                 text       : "Just OK"])

        def itemComment3 = save new ItemComment([author     : author,
                                                 serviceItem: serviceItems[2],
                                                 rate       : 5F,
                                                 text       : "Great!"])

        // Reverse sorted by ServiceItem name
        [itemComment3, itemComment2, itemComment1].collect {
            new ItemCommentServiceItemDto(it).asJSON()
        }
    }

    @Transactional
    private List<JSONObject> setupItemTags(Long profileId) {
        Profile user = Profile.findById(profileId)

        def tags = createTags()
        def serviceItems = createServiceItems(user)

        def itemTag1 = save new ServiceItemTag([createdBy  : user,
                                                tag        : tags[0],
                                                serviceItem: serviceItems[0]])

        def itemTag2 = save new ServiceItemTag([createdBy  : user,
                                                tag        : tags[1],
                                                serviceItem: serviceItems[1]])

        def itemTag3 = save new ServiceItemTag([createdBy  : user,
                                                tag        : tags[2],
                                                serviceItem: serviceItems[2]])

        [itemTag1, itemTag2, itemTag3].collect {
            new ProfileServiceItemTagDto(it).asJSON()
        }
    }

    @Transactional
    private List<JSONObject> setupServiceItemActivities(Long ownerId) {
        Profile owner = Profile.findById(ownerId)

        def serviceItems = createServiceItems(owner)

        def activity1 = new ServiceItemActivity([author           : owner,
                                                 activityTimestamp: new Date(System.currentTimeMillis() - 30000),
                                                 serviceItem      : serviceItems[0],
                                                 action           : Constants.Action.CREATED])

        def activity2 = new ServiceItemActivity([author           : owner,
                                                 activityTimestamp: new Date(System.currentTimeMillis() - 20000),
                                                 serviceItem      : serviceItems[1],
                                                 action           : Constants.Action.CREATED])

        def activity3 = new ServiceItemActivity([author           : owner,
                                                 activityTimestamp: new Date(System.currentTimeMillis() - 10000),
                                                 serviceItem      : serviceItems[2],
                                                 action           : Constants.Action.CREATED])

        // Extra ServiceItemActivity to make sure querying by user works
        Profile owner2 = Profile.findById(testUser1.id)
        def type = Types.findByTitle("App Component")
        def otherServiceItem = createServiceItem(owner2, "Listing 4", type, "http://woof.biz/item4")

        def otherActivity = new ServiceItemActivity([author     : owner2,
                                                     serviceItem: otherServiceItem,
                                                     action     : Constants.Action.CREATED])

        [activity1, activity2, activity3, otherActivity].each { activity ->
            def detail = new ChangeDetail(fieldName: "foo", oldValue: "bar", newValue: "biz")
            activity.addToChangeDetails(detail)
            activity.save(failOnError: true)
        }

        // Sorted by activityTimestamp, descending
        [activity3, activity2, activity1].collect { it.asJSON() }
    }

    private static List<ServiceItem> createServiceItems(Profile owner) {
        def type = Types.findByTitle("App Component")

        [createServiceItem(owner, "Listing 1", type, "http://foo.biz/item1"),
         createServiceItem(owner, "Listing 2", type, "http://foo.biz/item2"),
         createServiceItem(owner, "Listing 3", type, "http://foo.biz/item3")]
    }

    private static List<Tag> createTags() {
        save([new Tag(title: "Alpha"),
              new Tag(title: "Bravo"),
              new Tag(title: "Charlie")])
    }

    private static Profile createProfile(String username, String displayName) {
        save new Profile([username   : username,
                          displayName: displayName,
                          uuid       : generateUUID()])
    }

    private static ServiceItem createServiceItem(Profile owner, String title, Types types,
                                                 String launchUrl, boolean isApproved = false)
    {
        save new ServiceItem([title         : title,
                              types         : types,
                              launchUrl     : launchUrl,
                              owners        : [owner],
                              approvalStatus: Constants.APPROVAL_STATUSES[isApproved ? 'APPROVED' : 'IN_PROGRESS']])
    }

}
