package marketplace.rest

import static org.grails.plugins.jaxrs.response.Responses.created

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import org.springframework.security.access.AccessDeniedException


/**
 * Parent class of jaxrs rest controllers for domain
 * objects.  It contains basic CRUD functionality
 */
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
abstract class DomainResource<T> {

    private Class<T> DomainClass

    protected RestService<T> service

    /**
     * Constructor that should be called by subclasses in order
     * to initialize the subclass-specific fields of this class.
     * @param DomainClass The class of the domain object that this
     * resource is for
     * @param servicePropertyName The string name of an instance
     * property on this object that will hold the service needed
     * for doing data access on this domain object.  This needs
     * to be passed in as a string because the object itself
     * will not be available until spring bean wiring, which
     * happens after the Constructor is called
     */
    protected DomainResource(Class<T> DomainClass,
            RestService<T> service) {
        this.DomainClass = DomainClass
        this.service = service
    }

    protected DomainResource() {}

    @POST
    @Path('/')
    Response create(T dto) {
        try {
			created service.createFromDto(dto)
        } catch (AccessDeniedException ade) {
			throw new WebApplicationException(Response.Status.FORBIDDEN)
        } catch (DomainObjectNotFoundException ade) {
			throw new WebApplicationException(Response.Status.NOT_FOUND)
		}
    }

    /**
     * GET all of the domain objects of type T, optionally
     * with paging parameters to limit the size of the return
     */
    @GET
    @Path('/')
    Collection<T> readAll(@QueryParam('offset') Integer offset,
            @QueryParam('max') Integer max) {
        try {
			service.getAll(offset, max)
        } catch (AccessDeniedException ade) {
			throw new WebApplicationException(Response.Status.FORBIDDEN)
        } catch (DomainObjectNotFoundException ade) {
			throw new WebApplicationException(Response.Status.NOT_FOUND)
		}
    }

    @GET
    @Path('/{id}')
    T read(@PathParam('id') long id) {
        try {
			service.getById(id)
        } catch (AccessDeniedException ade) {
			throw new WebApplicationException(Response.Status.FORBIDDEN)
        } catch (DomainObjectNotFoundException ade) {
			throw new WebApplicationException(Response.Status.NOT_FOUND)
		}
    }

    @PUT
    @Path('/{id}')
    T update(@PathParam('id') long id, T dto) {
        try {
			service.updateById(id, dto)
        } catch (AccessDeniedException ade) {
			throw new WebApplicationException(Response.Status.FORBIDDEN)
        } catch (DomainObjectNotFoundException ade) {
			throw new WebApplicationException(Response.Status.NOT_FOUND)
		}
    }

    @DELETE
    @Path('/{id}')
    void delete(@PathParam('id') long id) {
        try {
			service.deleteById(id)
        } catch (AccessDeniedException ade) {
			throw new WebApplicationException(Response.Status.FORBIDDEN)
        } catch (DomainObjectNotFoundException ade) {
			throw new WebApplicationException(Response.Status.NOT_FOUND)
		}
    }
}
