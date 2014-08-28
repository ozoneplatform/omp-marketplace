package marketplace

import ozone.marketplace.domain.ValidationException

class ContactTypeControllerTests extends MarketplaceAdminControllerTests {

    ContactTypeControllerTests() {
        super(ContactTypeController.class)
    }

    @Override
    void setUp() {
        super.setUp()

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
