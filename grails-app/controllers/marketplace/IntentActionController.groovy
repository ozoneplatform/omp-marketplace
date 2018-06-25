package marketplace

class IntentActionController extends MarketplaceAdminWithDeleteController {

    IntentActionService intentActionService

    @Override
    protected String getDomainName() { "intentAction" }

    @Override
    protected String getObjectName() { "intentAction" }

    @Override
    protected retrieveDomain() { IntentAction.get(params.id) }

    @Override
    protected createDomain() { new IntentAction(params) }

    @Override
    protected deleteDomain() {
        intentActionService.deleteIntentActionById(params.id as Long)
    }

    @Override
    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        return IntentAction.list(params)
    }

    @Override
    protected retrieveDomainCount() { IntentAction.count() }

}
