package marketplace.rest

import grails.testing.gorm.DataTest
import marketplace.*
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import spock.lang.Specification

class ServiceItemResourceSpec extends Specification implements DataTest {
    ServiceItemResource resource

    void setup() {
        resource = new ServiceItemResource()
    }

    void testGetActivitiesForServiceItems() {
        when:
        ServiceItemActivity activity = new ServiceItemActivity()
        def passedOffset, passedMax

        resource.serviceItemActivityRestService = Mock(ServiceItemActivityRestService) {
            getAll(*_) >> { offset, max ->
                passedOffset = offset
                passedMax = max
                [activity]
            }
        }

        then:
        resource.getActivitiesForServiceItems(1,2) == [activity]
        passedOffset == 1
        passedMax == 2
    }

    void testGetServiceItemActivitiesForServiceItem() {
        when:
        ServiceItemActivity activity = new ServiceItemActivity()
        def passedParentId, passedOffset, passedMax

        resource.serviceItemActivityRestService = Mock(ServiceItemActivityRestService) {
            getByParentId(*_) >> { parentId, offset, max ->
                passedParentId = parentId
                passedOffset = offset
                passedMax = max
                [activity]
            }
        }

        then:
        resource.getServiceItemActivitiesForServiceItem(20, 1,2) == [activity]
        passedParentId == 20
        passedOffset == 1
        passedMax == 2
    }

    void testCreateRejectionListing() {
        when:
        RejectionListing rejectionListing = new RejectionListing()
        def passedParentId, passedDto

        resource.rejectionListingRestService = Mock(RejectionListingRestService) {
            createFromParentIdAndDto(*_) >> { parentId, dto ->
                passedParentId = parentId
                passedDto = dto
                dto
            }
        }

        then:
        resource.createRejectionListing(1, rejectionListing) == rejectionListing
        passedParentId == 1
        passedDto == rejectionListing
    }

    void testGetMostRecentRejectionJustification() {
        when:
        RejectionListing rejectionListing = new RejectionListing()
        def passedParentId

        resource.rejectionListingRestService = Mock(RejectionListingRestService) {
            getMostRecentRejectionListing(*_) >> { long parentId ->
                passedParentId = parentId
                rejectionListing
            }
        }

        then:
        resource.getMostRecentRejectionListing(1) == rejectionListing
        passedParentId == 1
    }

    void testGetRequiredServiceItems() {
        when:
        ServiceItem serviceItem = new ServiceItem()
        def passedParentId
        def passedBlockInside, urlA, urlB
        boolean isSameDomainResult = true

        resource.serviceItemRestService = Mock(ServiceItemRestService) {
            getAllRequiredServiceItemsByParentId(*_) >> { parentId, blockInside ->
                passedParentId = parentId
                passedBlockInside = blockInside
                [serviceItem]
            }
        }

        WebUtil.metaClass.static.isSameDomain = { URL a, URL b ->
            urlA = a
            urlB = b

            return isSameDomainResult
        }

        def req = new GrailsMockHttpServletRequest(serverName: 'localhost', scheme: 'https')
        then:
        resource.getRequiredServiceItems(
            req,'https://localhost/', 1
        )[0] == serviceItem
        passedParentId == 1
        passedBlockInside == !isSameDomainResult
        'https' == urlA.protocol
        'https' == urlB.protocol
        'localhost' == urlA.host
        'localhost' == urlB.host

        when:
        isSameDomainResult = false
        resource.getRequiredServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'www.example.com', scheme: 'http',
                serverPort: 443),
            'https://localhost/', 25
        )

        then:
        passedParentId == 25
        passedBlockInside == !isSameDomainResult
    }

    void testGetRequiringServiceItems() {
        when:
        ServiceItem serviceItem = new ServiceItem()
        def passedParentId, passedBlockInside, urlA, urlB
        boolean isSameDomainResult = true

        resource.serviceItemRestService = Mock(ServiceItemRestService) {
            getRequiringServiceItemsByChildId(*_) >> { parentId, blockInside ->
                passedParentId = parentId
                passedBlockInside = blockInside
                [serviceItem]
            }
        }

        WebUtil.metaClass.static.isSameDomain = { URL a, URL b ->
            urlA = a
            urlB = b

            return isSameDomainResult
        }

        then:
        resource.getRequiringServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'localhost', scheme: 'https'),
            'https://localhost/', 1
        )[0] == serviceItem
        passedParentId == 1
        passedBlockInside == !isSameDomainResult
        'https' == urlA.protocol
        'https' == urlB.protocol
        'localhost' == urlA.host
        'localhost' == urlB.host

        when:
        isSameDomainResult = false
        resource.getRequiringServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'www.example.com', scheme: 'http',
                serverPort: 443),
            'https://localhost/', 25
        )
        then:
        passedParentId == 25
        passedBlockInside == !isSameDomainResult
    }

    void testGetItemCommentsByServiceItem() {
        when:
        ItemComment comment = new ItemComment()
        def passedParentId

        resource.itemCommentRestService = Mock(ItemCommentRestService) {
            getByParentId(*_) >> { long parentId ->
                passedParentId = parentId
                [comment]
            }
        }

        then:
        resource.getItemCommentsByServiceItemId(20) == [comment]
        passedParentId == 20
    }

    void testCreateItemComment() {
        when:
        ItemComment comment = new ItemComment()
        def passedParentId, passedDto

        resource.itemCommentRestService = Mock(ItemCommentRestService) {
            createFromParentIdAndDto(*_) >> { parentId, dto ->
                passedParentId = parentId
                passedDto = dto
                dto
            }
        }

        then:
        resource.createItemComment(1, comment) == comment
        passedParentId == 1
        passedDto == comment
    }

    void testUpdateItemComment() {
        when:
        ItemComment comment = new ItemComment()
        def passedParentId, passedId, passedDto

        resource.itemCommentRestService = Mock(ItemCommentRestService) {
            updateByParentId(*_) >> { parentId, id, dto ->
                passedParentId = parentId
                passedId = id
                passedDto = dto
                dto
            }
        }

        then:
        resource.updateItemComment(1, 50, comment) == comment
        passedParentId == 1
        passedId == 50
        passedDto == comment
    }

    void testDeletedItemComment() {
        when:
        def passedId

        resource.itemCommentRestService = Mock(ItemCommentRestService) {
            deleteById(*_ ) >> { long id ->
                passedId = id
            }
        }

        resource.deleteItemComment(1)
        then:
        passedId == 1
    }
}
