package marketplace.rest

import grails.core.GrailsApplication

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize

import marketplace.Agency


class AgencyRestService extends RestService<Agency> {

    @Autowired
    AgencyRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, Agency.class, null, null)
    }

    AgencyRestService() {}

    List<Agency> getAll(Integer offset, Integer max) {
        Agency.findAll(offset: offset, max: max, sort: "title", order: "asc")
    }

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(Long id) {
        super.deleteById(id)
    }

    @PreAuthorize("hasRole('ADMIN')")
    Agency updateById(Long id, Agency agency, boolean skipValidation = false) {
        super.updateById(id, agency, skipValidation)
    }

    @PreAuthorize("hasRole('ADMIN')")
    Agency createFromDto(Agency agency, boolean skipValidation = false) {
        super.createFromDto(agency, skipValidation)
    }

}
