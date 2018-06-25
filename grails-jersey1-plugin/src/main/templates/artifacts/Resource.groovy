package ${packageName}

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path('/api/${modelName}')
class ${simpleName}Resource {
    @GET
    @Produces('text/plain')
    String get${simpleName}Representation() {
        '${simpleName}'
    }
}