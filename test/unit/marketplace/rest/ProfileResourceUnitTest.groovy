package marketplace.rest

import org.codehaus.groovy.grails.web.json.JSONArray

import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ItemComment
import marketplace.ServiceItemTag
import marketplace.ServiceItemActivity
import marketplace.Tag

import marketplace.Constants

import marketplace.testutil.FakeAuditTrailHelper

import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.TestMixin

@TestMixin(DomainClassUnitTestMixin)
class ProfileResourceUnitTest {

    def resource

    def currentUser

    void setUp() {
        def testUser = new Profile(username: 'testUser')
        testUser.id = 1

        FakeAuditTrailHelper.install()

        mockDomain(Profile.class, [testUser])

        def profileRestService = [
            getCurrentUserProfile: { currentUser },
            getById: { id ->
                Profile.get(id)
            }
        ] as ProfileRestService


        resource = new ProfileResource(profileRestService)
    }

    void testGetOwnProfile() {
        currentUser = Profile.get(1)
        assert resource.getOwnProfile() == currentUser
    }

    void testGetServiceItemsByAuthorId() {
        def idPassedIn

        def serviceItemRestServiceMock = mockFor(ServiceItemRestService)
        serviceItemRestServiceMock.demand.getAllByAuthorId(1..1) { id ->
            idPassedIn = id
            return null
        }

        resource.serviceItemRestService = serviceItemRestServiceMock.createMock()

        def idToPass = 1

        resource.getServiceItemsByAuthorId(idToPass)

        assert idPassedIn == idToPass
    }

    void testGetOwnServiceItems() {
        currentUser = Profile.get(1)

        def idPassedIn

        def serviceItemRestServiceMock = mockFor(ServiceItemRestService)
        serviceItemRestServiceMock.demand.getAllByAuthorId(1..1) { id ->
            idPassedIn = id
            return null
        }

        resource.serviceItemRestService = serviceItemRestServiceMock.createMock()

        resource.getOwnServiceItems()

        assert idPassedIn == currentUser.id
    }

    void testGetItemCommentsByAuthorId() {
        def idPassedIn

        def itemCommentRestServiceMock = mockFor(ItemCommentRestService)
        itemCommentRestServiceMock.demand.getAllByAuthorId(1..1) { id ->
            idPassedIn = id

            return [new ItemComment(
                author: Profile.get(1),
                text: 'blah blah',
                rate: 1,
                serviceItem: new ServiceItem(title: 'test serviceItem')
            )] as Set
        }

        resource.itemCommentRestService = itemCommentRestServiceMock.createMock()

        def idToPass = 1

        def dtos = resource.getItemCommentsByAuthorId(idToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        assert idPassedIn == idToPass

        assert json.size() == 1
        assert json[0].text == 'blah blah'
        assert json[0].rate == 1
    }

    void testGetOwnItemComments() {
        currentUser = Profile.get(1)

        def idPassedIn

        def itemCommentRestServiceMock = mockFor(ItemCommentRestService)
        itemCommentRestServiceMock.demand.getAllByAuthorId(1..1) { id ->
            idPassedIn = id

            return [new ItemComment(
                author: Profile.get(1),
                text: 'blah blah',
                rate: 1,
                serviceItem: new ServiceItem(title: 'test serviceItem')
            )] as Set
        }

        resource.itemCommentRestService = itemCommentRestServiceMock.createMock()

        def dtos = resource.getOwnItemComments()
        def json = dtos.collect { it.asJSON() } as JSONArray

        assert idPassedIn == currentUser.id

        assert json.size() == 1
        assert json[0].text == 'blah blah'
        assert json[0].rate == 1
    }

    void testGetTagsByProfileId() {
        def idPassedIn

        def serviceItemTagRestServiceMock = mockFor(ServiceItemTagRestService)
        serviceItemTagRestServiceMock.demand.getAllByProfileId(1..1) { id ->
            idPassedIn = id

            def tag = new Tag(title: 'tag 1')
            tag.id = 1

            def serviceItem = new ServiceItem(title: 'listing 1')
            serviceItem.id = 2

            return [new ServiceItemTag(
                createdBy: Profile.get(id),
                tag: tag,
                serviceItem: serviceItem
            )] as Set
        }

        resource.serviceItemTagRestService = serviceItemTagRestServiceMock.createMock()

        def idToPass = 1

        def dtos = resource.getTagsByProfileId(idToPass)
        def json = dtos.collect {
            assert it instanceof ProfileServiceItemTagDto
            it.asJSON()
        } as JSONArray

        assert idPassedIn == idToPass

        assert json.size() == 1
        assert json[0].tag.title == 'tag 1'
        assert json[0].tag.id == 1
        assert json[0].serviceItem.title == 'listing 1'
        assert json[0].serviceItem.id == 2
    }

    void testOwnTags() {
        currentUser = Profile.get(1)

        def idPassedIn

        def serviceItemTagRestServiceMock = mockFor(ServiceItemTagRestService)
        serviceItemTagRestServiceMock.demand.getAllByProfileId(1..1) { id ->
            idPassedIn = id

            def tag = new Tag(title: 'tag 1')
            tag.id = 1

            def serviceItem = new ServiceItem(title: 'listing 1')
            serviceItem.id = 2

            return [new ServiceItemTag(
                createdBy: Profile.get(id),
                tag: tag,
                serviceItem: serviceItem
            )] as Set
        }

        resource.serviceItemTagRestService = serviceItemTagRestServiceMock.createMock()

        def dtos = resource.getOwnTags()
        def json = dtos.collect {
            assert it instanceof ProfileServiceItemTagDto
            it.asJSON()
        } as JSONArray

        assert idPassedIn == currentUser.id

        assert json.size() == 1
        assert json[0].tag.title == 'tag 1'
        assert json[0].tag.id == 1
        assert json[0].serviceItem.title == 'listing 1'
        assert json[0].serviceItem.id == 2
    }

    void testGetServiceItemActivitiesByProfileId() {
        def idPassedIn, offsetPassedIn, maxPassedIn

        def serviceItemActivityRestServiceMock = mockFor(ServiceItemActivityRestService)
        serviceItemActivityRestServiceMock.demand.getAllByProfileId(1..1) { id, offset, max ->
            idPassedIn = id
            offsetPassedIn = offset
            maxPassedIn = max

            def serviceItem = new ServiceItem(title: 'listing 1')
            serviceItem.id = 2

            return [new ServiceItemActivity(
                action: Constants.Action.CREATED,
                author: Profile.get(1),
                serviceItem: serviceItem
            )]
        }

        resource.serviceItemActivityRestService = serviceItemActivityRestServiceMock.createMock()

        def idToPass = 1
        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getServiceItemActivitiesByProfileId(idToPass, offsetToPass, maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        assert idPassedIn == idToPass
        assert offsetPassedIn == offsetToPass
        assert maxPassedIn == maxToPass

        assert json.size() == 1
        assert json[0].action == Constants.Action.CREATED.asJSON()
        assert json[0].author.id == 1
        assert json[0].serviceItem.title == 'listing 1'
        assert json[0].serviceItem.id == 2
    }

    void testGetOwnServiceItemActivities() {
        currentUser = Profile.get(1)

        def idPassedIn, offsetPassedIn, maxPassedIn

        def serviceItemActivityRestServiceMock = mockFor(ServiceItemActivityRestService)
        serviceItemActivityRestServiceMock.demand.getAllByProfileId(1..1) { id, offset, max ->
            idPassedIn = id
            offsetPassedIn = offset
            maxPassedIn = max

            def serviceItem = new ServiceItem(title: 'listing 1')
            serviceItem.id = 2

            return [new ServiceItemActivity(
                action: Constants.Action.CREATED,
                author: Profile.get(1),
                serviceItem: serviceItem
            )]
        }

        resource.serviceItemActivityRestService = serviceItemActivityRestServiceMock.createMock()

        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getOwnServiceItemActivities(offsetToPass, maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        assert idPassedIn == currentUser.id
        assert offsetPassedIn == offsetToPass
        assert maxPassedIn == maxToPass

        assert json.size() == 1
        assert json[0].action == Constants.Action.CREATED.asJSON()
        assert json[0].author.id == 1
        assert json[0].serviceItem.title == 'listing 1'
        assert json[0].serviceItem.id == 2
    }

    void testGetServiceItemActivitiesByServiceItemOwnerId() {
        def idPassedIn, offsetPassedIn, maxPassedIn

        def serviceItemActivityRestServiceMock = mockFor(ServiceItemActivityRestService)
        serviceItemActivityRestServiceMock.demand.getAllByServiceItemOwnerId(1..1) { id, offset,
                max ->

            idPassedIn = id
            offsetPassedIn = offset
            maxPassedIn = max

            def serviceItem = new ServiceItem(title: 'listing 1')
            serviceItem.id = 2

            return [new ServiceItemActivity(
                action: Constants.Action.CREATED,
                author: Profile.get(1),
                serviceItem: serviceItem
            )]
        }

        resource.serviceItemActivityRestService = serviceItemActivityRestServiceMock.createMock()

        def idToPass = 1
        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getServiceItemActivitiesByServiceItemOwnerId(idToPass, offsetToPass,
                maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        assert idPassedIn == idToPass
        assert offsetPassedIn == offsetToPass
        assert maxPassedIn == maxToPass

        assert json.size() == 1
        assert json[0].action == Constants.Action.CREATED.asJSON()
        assert json[0].author.id == 1
        assert json[0].serviceItem.title == 'listing 1'
        assert json[0].serviceItem.id == 2
    }

    void testGetServiceItemActivitiesOnOwnServiceItems() {
        currentUser = Profile.get(1)

        def idPassedIn, offsetPassedIn, maxPassedIn

        def serviceItemActivityRestServiceMock = mockFor(ServiceItemActivityRestService)
        serviceItemActivityRestServiceMock.demand.getAllByServiceItemOwnerId(1..1) { id, offset,
                max ->

            idPassedIn = id
            offsetPassedIn = offset
            maxPassedIn = max

            def serviceItem = new ServiceItem(title: 'listing 1')
            serviceItem.id = 2

            return [new ServiceItemActivity(
                action: Constants.Action.CREATED,
                author: Profile.get(1),
                serviceItem: serviceItem
            )]
        }

        resource.serviceItemActivityRestService = serviceItemActivityRestServiceMock.createMock()

        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getServiceItemActivitiesOnOwnServiceItems(offsetToPass, maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        assert idPassedIn == currentUser.id
        assert offsetPassedIn == offsetToPass
        assert maxPassedIn == maxToPass

        assert json.size() == 1
        assert json[0].action == Constants.Action.CREATED.asJSON()
        assert json[0].author.id == 1
        assert json[0].serviceItem.title == 'listing 1'
        assert json[0].serviceItem.id == 2
    }
}
