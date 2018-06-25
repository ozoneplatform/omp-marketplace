package marketplace.rest

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.HeaderParam
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response

import grails.gorm.transactions.Transactional
import grails.plugins.elasticsearch.ElasticSearchAdminService

import org.springframework.beans.factory.annotation.Autowired

import marketplace.ExtServiceItem
import marketplace.ItemComment
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.WebUtil


@Path('/api/serviceItem')
class ServiceItemResource extends DomainResource<ServiceItem> {
    ServiceItemRestService serviceItemRestService
    ServiceItemActivityRestService serviceItemActivityRestService
    RejectionListingRestService rejectionListingRestService
    ItemCommentRestService itemCommentRestService
    ElasticSearchAdminService elasticSearchAdminService

    @Autowired
    ServiceItemResource(ServiceItemRestService service) {
        super(ServiceItem.class, service)
    }

    ServiceItemResource() {}

    @GET
    @Path('/activity')
    Collection<ServiceItemActivity> getActivitiesForServiceItems(
            @QueryParam('offset') Integer offset,
            @QueryParam('max') Integer max) {
        serviceItemActivityRestService.getAll(offset, max)
    }

    @GET
    @Path('/{serviceItemId}/activity')
    Collection<ServiceItemActivity> getServiceItemActivitiesForServiceItem(
            @PathParam('serviceItemId') long serviceItemId,
            @QueryParam('offset') Integer offset,
            @QueryParam('max') Integer max) {
        serviceItemActivityRestService.getByParentId(serviceItemId, offset, max)
    }

    @POST
    @Path('/{serviceItemId}/rejectionListing')
    RejectionListing createRejectionListing(@PathParam('serviceItemId') long serviceItemId,
                                            RejectionListing dto) {
        rejectionListingRestService.createFromParentIdAndDto(serviceItemId, dto)
    }

    @GET
    @Path('/{serviceItemId}/rejectionListing')
    RejectionListing getMostRecentRejectionListing(
            @PathParam('serviceItemId') long serviceItemId) {
        rejectionListingRestService.getMostRecentRejectionListing(serviceItemId)
    }

    //add JSONP support.  javascript has to be first in the list because browsers
    //send */* Accept headers for script tags, which is quite unhelpful
    @GET
    @Path('/{serviceItemId}/requiredServiceItems')
    @Produces(['application/javascript', 'text/javascript', 'application/json'])
    Collection<ServiceItem> getRequiredServiceItems(
            @Context HttpServletRequest request,
            @HeaderParam('referer') String referrer,
            @PathParam('serviceItemId') long serviceItemId) {

        boolean isSameDomain = referrer && WebUtil.isSameDomain(new URL(referrer),
                new URL(request.requestURL as String))
        serviceItemRestService.getAllRequiredServiceItemsByParentId(serviceItemId, !isSameDomain)
    }

    @GET
    @Path('/{serviceItemId}/requiringServiceItems')
    @Produces(['application/javascript', 'text/javascript', 'application/json'])
    Collection<ServiceItem> getRequiringServiceItems(
            @Context HttpServletRequest request,
            @HeaderParam('referer') String referrer,
            @PathParam('serviceItemId') long serviceItemId) {

        boolean isSameDomain = referrer && WebUtil.isSameDomain(new URL(referrer),
                new URL(request.requestURL as String))
        serviceItemRestService.getRequiringServiceItemsByChildId(serviceItemId, !isSameDomain)
    }

    @GET
    @Path('/{serviceItemId}/itemComment')
    Collection<ItemComment> getItemCommentsByServiceItemId(
            @PathParam('serviceItemId') long serviceItemId) {
        itemCommentRestService.getByParentId(serviceItemId)
    }

    @POST
    @Path('/{serviceItemId}/itemComment')
    ItemComment createItemComment(@PathParam('serviceItemId') long serviceItemId,
                                  ItemComment dto) {
        itemCommentRestService.createFromParentIdAndDto(serviceItemId, dto)
    }

    @PUT
    @Path('/{serviceItemId}/itemComment/{itemCommentId}')
    ItemComment updateItemComment(@PathParam('serviceItemId') long serviceItemId,
                                  @PathParam('itemCommentId') long id, ItemComment dto) {
        itemCommentRestService.updateByParentId(serviceItemId, id, dto)
    }

    @DELETE
    @Path('/{serviceItemId}/itemComment/{itemCommentId}')
    void deleteItemComment(@PathParam('itemCommentId') long itemCommentId) {
        itemCommentRestService.deleteById(itemCommentId)
    }

    @Override
    void delete(long id) {
        super.delete(id)
        refreshElasticSearch()
    }

    @Override
    @Transactional
    Response create(ServiceItem dto) {
        Response retval = super.create(dto)
        refreshElasticSearch()
        return retval
    }

    @Override
    ServiceItem update(long id, ServiceItem dto) {
        ServiceItem retval = super.update(id, dto)
        refreshElasticSearch()
        return retval
    }

    private void refreshElasticSearch() {
        //ensure elastic search is finished updating before returning response

        elasticSearchAdminService.refresh(ServiceItem, ExtServiceItem)
    }

}
