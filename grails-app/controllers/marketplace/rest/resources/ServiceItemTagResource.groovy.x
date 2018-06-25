package marketplace.rest

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import org.grails.plugins.jaxrs.response.Responses

import marketplace.ServiceItem
import marketplace.ServiceItemTag
import marketplace.Tag


@Path('/api/serviceItem/{serviceItemId}/tag')
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
