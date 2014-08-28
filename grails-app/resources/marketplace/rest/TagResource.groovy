package marketplace.rest

import javax.ws.rs.Path
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemTag

import org.springframework.beans.factory.annotation.Autowired

import marketplace.Tag
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.GET
import javax.ws.rs.DELETE
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response


@Path('/api/tag')
class TagResource extends DomainResource<Tag>{ 

    TagResource(){}
    
    @Autowired
    TagRestService tagRestService
    
    @Autowired
    public TagResource(TagRestService tagRestService) {
        super(Tag.class, tagRestService)
    }
  
    @GET
    @Path('/search')
    Collection<Tag> findByTitle(@QueryParam("title") String title){
        tagRestService.findAllLikeTitle(title)
    }
}