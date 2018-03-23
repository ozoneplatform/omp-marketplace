package ${packageName}

import static org.grails.plugins.jaxrs.response.Responses.*

import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.PUT
import javax.ws.rs.core.Response

@Consumes(['application/xml','application/json'])
@Produces(['application/xml','application/json'])
class ${simpleName}Resource {

    def ${modelName}ResourceService
    def id

    @GET
    Response read() {
        ok ${modelName}ResourceService.read(id)
    }

    @PUT
    Response update(${simpleName} dto) {
        dto.id = id
        ok ${modelName}ResourceService.update(dto)
    }

    @DELETE
    void delete() {
        ${modelName}ResourceService.delete(id)
    }
}
