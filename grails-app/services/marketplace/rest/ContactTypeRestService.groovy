package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize

import grails.core.GrailsApplication

import marketplace.ContactType


class ContactTypeRestService extends RestService<ContactType> {

    @Autowired
    ContactTypeRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, ContactType.class, null, null)
    }

    ContactTypeRestService() {}

    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(Long id) {
        super.deleteById(id)
    }

    @PreAuthorize("hasRole('ADMIN')")
    ContactType updateById(Long id, ContactType agency, boolean skipValidation = false) {
        super.updateById(id, agency, skipValidation)
    }

    @PreAuthorize("hasRole('ADMIN')")
    ContactType createFromDto(ContactType agency, boolean skipValidation = false) {
        super.createFromDto(agency, skipValidation)
    }
    
}
