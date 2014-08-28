package marketplace

class TextController extends MarketplaceAdminController {

    def textService

    protected retrieveDomain() { return Text.get(params.id) }

    protected String getDomainName() { return "textInstance" }

    protected String getObjectName() { return "text" }

    protected createDomain() { return new Text(params) }

    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'name'
        }
        return textService.list(params)
    }

    protected retrieveDomainCount() { return textService.countTypes() }

}
