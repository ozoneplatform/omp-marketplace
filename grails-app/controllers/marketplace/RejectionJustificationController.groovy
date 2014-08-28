package marketplace

import grails.converters.JSON

class RejectionJustificationController extends MarketplaceAdminWithDeleteController {

    def rejectionJustificationService

    protected retrieveDomain() { return RejectionJustification.get(params.id) }

    protected String getDomainName() { return "rejectionJustification" }

    protected String getObjectName() { return "rejection justification" }

    protected createDomain() { return new RejectionJustification(params) }

    @Override
    protected deleteDomain() {
        rejectionJustificationService.delete(params.id)
    }

    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        return rejectionJustificationService.list(params)
    }

    protected retrieveDomainCount() { return rejectionJustificationService.countJustifications() }

    def getListAsExt = {
        def model, json
        try {
            model = retrieveDomainList()

            json = [
                success: true,
                totalCount: retrieveDomainCount(),
                data: model.collect { item ->
                    [
                        id: item.id,
                        title: item.title,
                        description: item.description
                    ]
                }
            ]
        }
        catch (Exception e) {
            json = [
                success: false,
                totalCount: 0,
                msg: e.getMessage()
            ]
        }
        render (json as JSON)
    }
}
