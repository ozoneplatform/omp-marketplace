package marketplace.rest

import marketplace.ServiceItem
import marketplace.ServiceItemTag
import marketplace.Tag
import org.grails.jaxrs.response.Responses

import javax.ws.rs.core.Response

import javax.ws.rs.*

@Path('/api/serviceItem/{serviceItemId}/tag')
@Consumes(['application/json'])
@Produces(['application/json'])
class ServiceItemTagResource {

    ServiceItemTagRestService serviceItemTagRestService

    @POST
    Response createTags(@PathParam('serviceItemId') long serviceItemId, List<Tag> tags){
        Collection<ServiceItemTag> serviceItemTags = tags.collect { Tag tag ->
            ServiceItemTag serviceItemTag = new ServiceItemTag(tag: tag)
            serviceItemTag.serviceItem = new ServiceItem()
            serviceItemTag.serviceItem.id = serviceItemId
            serviceItemTag
        }
        Responses.ok(serviceItemTagRestService.createFromDtoCollection(serviceItemTags))
    }

    @DELETE
    @Path('/{tagId}')
    void deleteByTagId(@PathParam('serviceItemId') long serviceItemId, @PathParam('tagId') long tagId){
        ServiceItemTag serviceItemTag = serviceItemTagRestService.getByServiceItemIdAndTagId(serviceItemId, tagId)
        if(serviceItemTag)
            serviceItemTagRestService.deleteById(serviceItemTag.id)
    }

    @GET
    Collection<ServiceItemTag> getAllByServiceItemId(@PathParam('serviceItemId') long serviceItemId){
        serviceItemTagRestService.getAllByServiceItemId(serviceItemId)
    }
}
