package marketplace.grails.controller

import grails.testing.gorm.DataTest
import marketplace.Contact
import marketplace.ContactType
import marketplace.ContactTypeController


class ContactTypeControllerSpec extends MarketplaceAdminControllerTests implements DataTest {

    ContactTypeControllerTests() {
        super(ContactTypeController.class)
    }

    @Override
    void setup() {
        super.setup()

        mockDomain(Contact)

        ContactType.metaClass.'static'.withTransaction = { Closure callable ->
            callable.call(null)
        }

        ContactType.metaClass.'static'.withNewSession = { Closure callable ->
            callable.call(null)
        }
    }

    @Override
    protected void mocksForTestDeleteFailure() {
        validationExceptionMessage = 'objectNotFound'
    }
}
