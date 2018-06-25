package marketplace.rest

import javax.ws.rs.Path

import org.springframework.beans.factory.annotation.Autowired

import marketplace.ContactType


@Path('/api/contactType')
class ContactTypeResource extends DomainResource<ContactType> {

    @Autowired
    ContactTypeResource(ContactTypeRestService contactTypeRestService) {
        super(ContactType.class, contactTypeRestService)
    }

    ContactTypeResource() {}

}
