package marketplace

class IntentDataTypeController extends MarketplaceAdminWithDeleteController {

    IntentDataTypeService intentDataTypeService

    @Override
    protected String getDomainName() {
        'intentDataType'
    }

    @Override
    protected String getObjectName() {
        'intentDataType'
    }

    @Override
    protected retrieveDomain() {
        intentDataTypeService.lookupById(params.id)
    }

    @Override
    protected createDomain() {
        intentDataTypeService.create(params)
    }

    @Override
    protected deleteDomain() {
        intentDataTypeService.delete(params.id as Long)
    }

    @Override
    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        intentDataTypeService.list(params)
    }

    @Override
    protected retrieveDomainCount() {
        intentDataTypeService.countTypes()
    }
}
