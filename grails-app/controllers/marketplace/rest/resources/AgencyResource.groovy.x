package marketplace.rest

import javax.ws.rs.Path

import org.springframework.beans.factory.annotation.Autowired

import marketplace.Agency

@Path('/api/agency')
class AgencyResource extends DomainResource<Agency> {

    @Autowired
    AgencyResource(AgencyRestService agencyRestService) {
        super(Agency.class, agencyRestService)
    }

    AgencyResource() {}

}
