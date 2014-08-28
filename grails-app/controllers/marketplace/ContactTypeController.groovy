package marketplace

import ozone.marketplace.domain.ValidationException

class ContactTypeController extends MarketplaceAdminWithDeleteController {

    @Override
    protected String getDomainName() { 'contactType' }

    @Override
    protected String getObjectName() { 'contactType' }

    @Override
    protected ContactType retrieveDomain() { ContactType.get(params.id) }

    @Override
    protected ContactType createDomain() { new ContactType(params) }

    @Override
    protected Integer retrieveDomainCount() { ContactType.count() }

    @Override
    protected retrieveDomainList() { ContactType.list(params) }

    @Override
    protected deleteDomain() {
        ContactType contactType = ContactType.get(params.id)
        if(!contactType) {
            throw new ValidationException(message: 'objectNotFound', args: [params.id])
        }

        ContactType.withTransaction {
            contactType.delete()
        }
    }
}
