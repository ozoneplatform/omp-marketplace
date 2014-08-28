package marketplace.rest

import java.net.URL

import org.codehaus.groovy.grails.plugins.testing.GrailsMockHttpServletRequest

import marketplace.ServiceItem
import marketplace.RejectionListing
import marketplace.ItemComment
import marketplace.ServiceItemActivity

import marketplace.WebUtil

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

@TestMixin(DomainClassUnitTestMixin)
class ServiceItemResourceUnitTest {
    ServiceItemResource resource

    void setUp() {
        resource = new ServiceItemResource()
    }

    void testGetActivitiesForServiceItems() {
        ServiceItemActivity activity = new ServiceItemActivity()
        def passedOffset, passedMax

        def serviceItemActivityRestServiceMock = mockFor(ServiceItemActivityRestService)
        serviceItemActivityRestServiceMock.demand.getAll(1..1) { offset, max ->
            passedOffset = offset
            passedMax = max
            [activity]
        }
        resource.serviceItemActivityRestService = serviceItemActivityRestServiceMock.createMock()

        assert resource.getActivitiesForServiceItems(1,2) == [activity]
        assert passedOffset == 1
        assert passedMax == 2
    }

    void testGetServiceItemActivitiesForServiceItem() {
        ServiceItemActivity activity = new ServiceItemActivity()
        def passedParentId, passedOffset, passedMax

        def serviceItemActivityRestServiceMock = mockFor(ServiceItemActivityRestService)
        serviceItemActivityRestServiceMock.demand.getByParentId(1..1) { parentId, offset, max ->
            passedParentId = parentId
            passedOffset = offset
            passedMax = max
            [activity]
        }
        resource.serviceItemActivityRestService = serviceItemActivityRestServiceMock.createMock()

        assert resource.getServiceItemActivitiesForServiceItem(20, 1,2) == [activity]
        assert passedParentId == 20
        assert passedOffset == 1
        assert passedMax == 2
    }

    void testCreateRejectionListing() {
        RejectionListing rejectionListing = new RejectionListing()
        def passedParentId, passedDto

        def rejectionListingRestServiceMock = mockFor(RejectionListingRestService)
        rejectionListingRestServiceMock.demand.createFromParentIdAndDto(1..1) { parentId, dto ->
            passedParentId = parentId
            passedDto = dto
            dto
        }
        resource.rejectionListingRestService = rejectionListingRestServiceMock.createMock()

        assert resource.createRejectionListing(1, rejectionListing) == rejectionListing
        assert passedParentId == 1
        assert passedDto == rejectionListing
    }

    void testGetMostRecentRejectionJustification() {
        RejectionListing rejectionListing = new RejectionListing()
        def passedParentId

        def rejectionListingRestServiceMock = mockFor(RejectionListingRestService)
        rejectionListingRestServiceMock.demand.getMostRecentRejectionListing(1..1) {
                parentId ->
            passedParentId = parentId
            rejectionListing
        }
        resource.rejectionListingRestService = rejectionListingRestServiceMock.createMock()

        assert resource.getMostRecentRejectionListing(1) == rejectionListing
        assert passedParentId == 1
    }

    void testGetRequiredServiceItems() {
        ServiceItem serviceItem = new ServiceItem()
        def passedParentId, passedBlockInside, urlA, urlB
        boolean isSameDomainResult = true

        def serviceItemRestServiceMock = mockFor(ServiceItemRestService)
        serviceItemRestServiceMock.demand.getAllRequiredServiceItemsByParentId(1..2) {
                parentId, blockInside ->
            passedParentId = parentId
            passedBlockInside = blockInside
            [serviceItem]
        }
        resource.serviceItemRestService = serviceItemRestServiceMock.createMock()

        WebUtil.metaClass.static.isSameDomain = { URL a, URL b ->
            urlA = a
            urlB = b

            return isSameDomainResult
        }

        assert resource.getRequiredServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'localhost', scheme: 'https'),
            'https://localhost/', 1
        ) == [serviceItem]
        assert passedParentId == 1
        assert passedBlockInside == !isSameDomainResult
        assert 'https' == urlA.protocol
        assert 'https' == urlB.protocol
        assert 'localhost' == urlA.host
        assert 'localhost' == urlB.host

        isSameDomainResult = false
        resource.getRequiredServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'www.example.com', scheme: 'http',
                serverPort: 443),
            'https://localhost/', 25
        )
        assert passedParentId == 25
        assert passedBlockInside == !isSameDomainResult
    }

    void testGetRequiringServiceItems() {
        ServiceItem serviceItem = new ServiceItem()
        def passedParentId, passedBlockInside, urlA, urlB
        boolean isSameDomainResult = true

        def serviceItemRestServiceMock = mockFor(ServiceItemRestService)
        serviceItemRestServiceMock.demand.getRequiringServiceItemsByChildId(1..2) {
                parentId, blockInside ->
            passedParentId = parentId
            passedBlockInside = blockInside
            [serviceItem]
        }
        resource.serviceItemRestService = serviceItemRestServiceMock.createMock()

        WebUtil.metaClass.static.isSameDomain = { URL a, URL b ->
            urlA = a
            urlB = b

            return isSameDomainResult
        }

        assert resource.getRequiringServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'localhost', scheme: 'https'),
            'https://localhost/', 1
        ) == [serviceItem]
        assert passedParentId == 1
        assert passedBlockInside == !isSameDomainResult
        assert 'https' == urlA.protocol
        assert 'https' == urlB.protocol
        assert 'localhost' == urlA.host
        assert 'localhost' == urlB.host

        isSameDomainResult = false
        resource.getRequiringServiceItems(
            new GrailsMockHttpServletRequest(serverName: 'www.example.com', scheme: 'http',
                serverPort: 443),
            'https://localhost/', 25
        )
        assert passedParentId == 25
        assert passedBlockInside == !isSameDomainResult
    }

    void testGetItemCommentsByServiceItem() {
        ItemComment comment = new ItemComment()
        def passedParentId

        def itemCommentRestServiceMock = mockFor(ItemCommentRestService)
        itemCommentRestServiceMock.demand.getByParentId(1..1) { parentId ->
            passedParentId = parentId
            [comment]
        }
        resource.itemCommentRestService = itemCommentRestServiceMock.createMock()

        assert resource.getItemCommentsByServiceItemId(20) == [comment]
        assert passedParentId == 20
    }

    void testCreateItemComment() {
        ItemComment comment = new ItemComment()
        def passedParentId, passedDto

        def itemCommentRestServiceMock = mockFor(ItemCommentRestService)
        itemCommentRestServiceMock.demand.createFromParentIdAndDto(1..1) { parentId, dto ->
            passedParentId = parentId
            passedDto = dto
            dto
        }
        resource.itemCommentRestService = itemCommentRestServiceMock.createMock()

        assert resource.createItemComment(1, comment) == comment
        assert passedParentId == 1
        assert passedDto == comment
    }

    void testUpdateItemComment() {
        ItemComment comment = new ItemComment()
        def passedParentId, passedId, passedDto

        def itemCommentRestServiceMock = mockFor(ItemCommentRestService)
        itemCommentRestServiceMock.demand.updateByParentId(1..1) { parentId, id, dto ->
            passedParentId = parentId
            passedId = id
            passedDto = dto
            dto
        }
        resource.itemCommentRestService = itemCommentRestServiceMock.createMock()

        assert resource.updateItemComment(1, 50, comment) == comment
        assert passedParentId == 1
        assert passedId == 50
        assert passedDto == comment
    }

    void testDeletedItemComment() {
        def passedId

        def itemCommentRestServiceMock = mockFor(ItemCommentRestService)
        itemCommentRestServiceMock.demand.deleteById(1..1) { id ->
            passedId = id
        }
        resource.itemCommentRestService = itemCommentRestServiceMock.createMock()

        resource.deleteItemComment(1)
        assert passedId == 1
    }
}
