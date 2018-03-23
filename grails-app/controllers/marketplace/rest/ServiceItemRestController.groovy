package marketplace.rest

import groovy.transform.CompileStatic

import grails.converters.JSON
import grails.plugins.elasticsearch.ElasticSearchAdminService

import org.springframework.http.HttpStatus

import marketplace.ExtServiceItem
import marketplace.ItemComment
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemTag
import marketplace.Tag
import marketplace.WebUtil


@CompileStatic
class ServiceItemRestController implements RestExceptionHandlers {

    static namespace = 'api'

    static responseFormats = ['json']

    ServiceItemRestService serviceItemRestService

    ServiceItemActivityRestService serviceItemActivityRestService
    ServiceItemTagRestService serviceItemTagRestService
    RejectionListingRestService rejectionListingRestService
    ItemCommentRestService itemCommentRestService
    ElasticSearchAdminService elasticSearchAdminService

    def index(Integer offset, Integer max) {
        Collection<ServiceItem> results = serviceItemRestService.getAll(offset, max)

        render(results as JSON)
    }

    def show(Long id) {
        ServiceItem result = serviceItemRestService.getById(id)

        render(result as JSON)
    }

    def save(ServiceItem serviceItem) {
        ServiceItem result = serviceItemRestService.createFromDto(serviceItem)

        refreshElasticSearch()

        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def update(ServiceItem serviceItem) {
        ServiceItem result = serviceItemRestService.updateById(serviceItem.id, serviceItem)

        refreshElasticSearch()

        render(result as JSON)
    }

    def delete(Long id) {
        serviceItemRestService.deleteById(id)

        refreshElasticSearch()

        render(status: HttpStatus.NO_CONTENT.value())
    }

    def activities(Integer offset, Integer max) {
        Collection<ServiceItemActivity> results = serviceItemActivityRestService.getAll(offset, max)

        render(results as JSON)
    }

    def serviceItemActivities(Long id, Integer offset, Integer max) {
        Collection<ServiceItemActivity> results = serviceItemActivityRestService.getByParentId(id, offset, max)

        render(results as JSON)
    }

    def saveRejectionListing(Long serviceItemId, RejectionListing dto) {
        RejectionListing result = rejectionListingRestService.createFromParentIdAndDto(serviceItemId, dto)

        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def latestRejectionListing(Long serviceItemId) {
        RejectionListing result = rejectionListingRestService.getMostRecentRejectionListing(serviceItemId)

        render(result as JSON)
    }

    def requiredServiceItems(Long id) {
        Set<ServiceItem> results = serviceItemRestService.getAllRequiredServiceItemsByParentId(id, !isSameDomain())

        render(results as JSON)
    }

    def requiringServiceItems(Long id) {
        Set<ServiceItem> results = serviceItemRestService.getRequiringServiceItemsByChildId(id, !isSameDomain())

        render(results as JSON)
    }

    def itemComments(Long serviceItemId) {
        List<ItemComment> results = itemCommentRestService.getByParentId(serviceItemId)

        render(results as JSON)
    }

    def saveItemComment(Long serviceItemId, ItemComment itemComment) {
        ItemComment result = itemCommentRestService.createFromParentIdAndDto(serviceItemId, itemComment)

        response.status = HttpStatus.CREATED.value()
        render(result as JSON)
    }

    def updateItemComment(Long serviceItemId, Long itemCommentId, ItemComment itemComment) {
        ItemComment result = itemCommentRestService.updateByParentId(serviceItemId, itemCommentId, itemComment)

        render(result as JSON)
    }

    def deleteItemComment(Long serviceItemId, Long itemCommentId) {
        itemCommentRestService.deleteById(itemCommentId)

        render(status: HttpStatus.NO_CONTENT.value())
    }

    def tags(Long id) {
        Collection<ServiceItemTag> results = serviceItemTagRestService.getAllByServiceItemId(id)

        render(results as JSON)
    }

    def saveTags() {
        // set to true if want to accept request input for multiple tags with delimiter.
        boolean allowMultipleTagInput = false
        String delimiter = ","

        // get info from request body.
        def json = request.JSON

        List<Long> serviceItemIdList = (List<Long>) json["serviceItemId"]
        Long serviceItemId = serviceItemIdList[0]

        List<String> titleList = (List<String>) json["title"]
        String tagTitles = titleList[0]

        List<String> indivTagTitles = []
        if (allowMultipleTagInput) {
            indivTagTitles = tagTitles.tokenize(delimiter)
        } else {
            indivTagTitles = [tagTitles]
        }

        // create domain objects.
        Collection<ServiceItemTag> serviceItemTags = indivTagTitles.collect { String tagTitle ->
            Tag tag = new Tag(title: tagTitle)
            ServiceItemTag serviceItemTag = new ServiceItemTag(tag: tag)
            serviceItemTag.serviceItem = new ServiceItem()
            serviceItemTag.serviceItem.id = serviceItemId
            serviceItemTag
        }
        Collection<ServiceItemTag> results = serviceItemTagRestService.createFromDtoCollection(serviceItemTags)

        response.status = HttpStatus.CREATED.value()
        render(results as JSON)
    }

    def deleteTag(Long serviceItemId, Long tagId) {
        ServiceItemTag serviceItemTag = serviceItemTagRestService.getByServiceItemIdAndTagId(serviceItemId, tagId)

        serviceItemTagRestService.deleteById(serviceItemTag.id)

        render(status: HttpStatus.NO_CONTENT.value())
    }

    /** Ensure ElasticSearch is finished updating before returning response */
    private void refreshElasticSearch() {
        elasticSearchAdminService.refresh(ServiceItem, ExtServiceItem)
    }

    private boolean isSameDomain() {
        String referrer = request.getHeader('referer')
        referrer && WebUtil.isSameDomain(new URL(referrer), new URL(request.requestURL.toString()))
    }
}
