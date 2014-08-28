package marketplace.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import org.codehaus.groovy.grails.commons.GrailsApplication

import marketplace.ContactType

@Service
class ContactTypeRestService extends AdminRestService<ContactType> {
    @Autowired
    public ContactTypeRestService(GrailsApplication grailsApplication) {
        super(grailsApplication, ContactType.class, null, null)
    }

    ContactTypeRestService() {}
}
