package marketplace.rest

import javax.ws.rs.Path

import org.springframework.beans.factory.annotation.Autowired

import marketplace.ContactType
import org.springframework.beans.factory.annotation.Autowired

@Path('api/contactType')
class ContactTypeResource extends DomainResource<ContactType> {

    @Autowired
    public ContactTypeResource(ContactTypeRestService contactTypeRestService) {
        super(ContactType.class, contactTypeRestService)
    }

    ContactTypeResource() {}
}
