package marketplace.rest

import grails.testing.gorm.DataTest
import marketplace.*
import org.grails.web.json.JSONArray
import spock.lang.Specification

class ProfileResourceSpec extends Specification implements DataTest{

    def resource

    def currentUser

    void setup() {
        def testUser = new Profile(username: 'testUser')
        testUser.id = 1

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
        when:
        currentUser = Profile.get(1)
        then:
        resource.getOwnProfile() == currentUser
    }

    void testGetServiceItemsByAuthorId() {
        when:
        int idPassedIn

        resource.serviceItemRestService = Mock(ServiceItemRestService) {
            getAllByAuthorId(*_) >> { long id ->
                idPassedIn = id
                return null
            }
        }

        int idToPass = 1

        resource.getServiceItemsByAuthorId(idToPass)

        then:
        idPassedIn == idToPass
    }

    void testGetOwnServiceItems() {
        when:
        currentUser = Profile.get(1)

        def idPassedIn

        resource.serviceItemRestService = Mock(ServiceItemRestService) {
            getAllByAuthorId(*_) >> { long id ->
                idPassedIn = id
                return null
            }
        }

        resource.getOwnServiceItems()

        then:
        idPassedIn == currentUser.id
    }

    void testGetItemCommentsByAuthorId() {
        when:
        def idPassedIn

        resource.itemCommentRestService = Mock(ItemCommentRestService) {
            getAllByAuthorId(*_) >> { long id ->
                idPassedIn = id
                return [new ItemComment(
                        author: Profile.get(1),
                        text: 'blah blah',
                        rate: 1,
                        serviceItem: new ServiceItem(title: 'test serviceItem')
                )] as List
            }
        }

        def idToPass = 1

        def dtos = resource.getItemCommentsByAuthorId(idToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray


        then:
        idPassedIn == idToPass
        json.size() == 1
        json[0].text == 'blah blah'
        json[0].rate == 1
    }

    void testGetOwnItemComments() {
        when:
        currentUser = Profile.get(1)

        def idPassedIn

        resource.itemCommentRestService = Mock(ItemCommentRestService) {
            getAllByAuthorId(*_) >> { long id ->
                idPassedIn = id
                return [new ItemComment(
                        author: Profile.get(1),
                        text: 'blah blah',
                        rate: 1,
                        serviceItem: new ServiceItem(title: 'test serviceItem')
                )] as List
            }
        }


        def dtos = resource.getOwnItemComments()
        def json = dtos.collect { it.asJSON() } as JSONArray

        then:
        idPassedIn == currentUser.id
        json.size() == 1
        json[0].text == 'blah blah'
        json[0].rate == 1
    }

    void testGetTagsByProfileId() {
        when:
        def idPassedIn

        resource.serviceItemTagRestService = Mock(ServiceItemTagRestService) {
            getAllByProfileId(*_) >> { long id ->
                idPassedIn = id

                def tag = new Tag(title: 'tag 1')
                tag.id = 1

                def serviceItem = new ServiceItem(title: 'listing 1')
                serviceItem.id = 2

                return [new ServiceItemTag(
                        createdBy: Profile.get(id),
                        tag: tag,
                        serviceItem: serviceItem
                )] as List
            }
        }

        def idToPass = 1

        def dtos = resource.getTagsByProfileId(idToPass)
        def json = dtos.collect {
            assert it instanceof ProfileServiceItemTagDto
            it.asJSON()
        } as JSONArray


        then:
        idPassedIn == idToPass
        json.size() == 1
        json[0].tag.title == 'tag 1'
        json[0].tag.id == 1
        json[0].serviceItem.title == 'listing 1'
        json[0].serviceItem.id == 2
    }

    void testOwnTags() {
        when:
        currentUser = Profile.get(1)

        def idPassedIn

        resource.serviceItemTagRestService = Mock(ServiceItemTagRestService) {
            getAllByProfileId(*_) >> { long id ->
                idPassedIn = id

                def tag = new Tag(title: 'tag 1')
                tag.id = 1

                def serviceItem = new ServiceItem(title: 'listing 1')
                serviceItem.id = 2

                return [new ServiceItemTag(
                        createdBy: Profile.get(id),
                        tag: tag,
                        serviceItem: serviceItem
                )] as List
            }
        }

        def dtos = resource.getOwnTags()
        def json = dtos.collect {
            assert it instanceof ProfileServiceItemTagDto
            it.asJSON()
        } as JSONArray

        then:
        idPassedIn == currentUser.id
        json.size() == 1
        json[0].tag.title == 'tag 1'
        json[0].tag.id == 1
        json[0].serviceItem.title == 'listing 1'
        json[0].serviceItem.id == 2
    }

    void testGetServiceItemActivitiesByProfileId() {
        when:
        def idPassedIn
        def offsetPassedIn
        def maxPassedIn
        resource.serviceItemActivityRestService = Mock(ServiceItemActivityRestService) {
            getAllByProfileId(*_) >> { id, offset, max ->
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
        }
        def idToPass = 1
        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getServiceItemActivitiesByProfileId(idToPass, offsetToPass, maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        then:
        idPassedIn == idToPass
        offsetPassedIn == offsetToPass
        maxPassedIn == maxToPass

        json.size() == 1
        json[0].action == Constants.Action.CREATED.asJSON()
        json[0].author.id == 1
        json[0].serviceItem.title == 'listing 1'
        json[0].serviceItem.id == 2
    }

    void testGetOwnServiceItemActivities() {
        when:
        currentUser = Profile.get(1)

        def idPassedIn, offsetPassedIn, maxPassedIn

        resource.serviceItemActivityRestService = Mock(ServiceItemActivityRestService) {
            getAllByProfileId(*_ ) >> { id, offset, max ->
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
        }

        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getOwnServiceItemActivities(offsetToPass, maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        then:
        idPassedIn == currentUser.id
        offsetPassedIn == offsetToPass
        maxPassedIn == maxToPass
        json.size() == 1
        json[0].action == Constants.Action.CREATED.asJSON()
        json[0].author.id == 1
        json[0].serviceItem.title == 'listing 1'
        json[0].serviceItem.id == 2
    }

    void testGetServiceItemActivitiesByServiceItemOwnerId() {
        when:
        def idPassedIn
        def offsetPassedIn
        def maxPassedIn

        resource.serviceItemActivityRestService = Mock(ServiceItemActivityRestService) {
            getAllByServiceItemOwnerId(*_ ) >> {  id, offset, max ->
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
        }

        def idToPass = 1
        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getServiceItemActivitiesByServiceItemOwnerId(idToPass, offsetToPass,
                maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        then:
        idPassedIn == idToPass
        offsetPassedIn == offsetToPass
        maxPassedIn == maxToPass
        json.size() == 1
        json[0].action == Constants.Action.CREATED.asJSON()
        json[0].author.id == 1
        json[0].serviceItem.title == 'listing 1'
        json[0].serviceItem.id == 2
    }

    void testGetServiceItemActivitiesOnOwnServiceItems() {
        when:
        currentUser = Profile.get(1)

        def idPassedIn, offsetPassedIn, maxPassedIn

        resource.serviceItemActivityRestService = Mock(ServiceItemActivityRestService) {
            getAllByServiceItemOwnerId(*_ ) >> {  id, offset, max ->
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
        }

        def offsetToPass = 10
        def maxToPass = 5

        def dtos = resource.getServiceItemActivitiesOnOwnServiceItems(offsetToPass, maxToPass)
        def json = dtos.collect { it.asJSON() } as JSONArray

        then:
        idPassedIn == currentUser.id
        offsetPassedIn == offsetToPass
        maxPassedIn == maxToPass
        json.size() == 1
        json[0].action == Constants.Action.CREATED.asJSON()
        json[0].author.id == 1
        json[0].serviceItem.title == 'listing 1'
        json[0].serviceItem.id == 2
    }
}
