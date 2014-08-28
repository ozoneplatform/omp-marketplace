package marketplace

class OwfWidgetTypesController extends BaseMarketplaceRestController {

    def owfWidgetTypesService

    private retrieveDomain() {
        return owfWidgetTypesService.getOwfWidgetType(params.id)
    }

    private retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        return owfWidgetTypesService.list(params)
    }

    private retrieveDomainCount() { return owfWidgetTypesService.countOwfWidgetTypes() }

    def getListAsJSON = {
        if (!params.max) params.max = 100
        def model
        def total
        try {
            model = retrieveDomainList()
            total = retrieveDomainCount()
        }
        catch (Exception e) {
            handleException(e, "getListAsJSON")
            return
        }
        renderResult(model, total, 200)
    }

    def getItemAsJSON = {
        def model
        try {
            model = retrieveDomain()
            if (!model) {
                handleExpectedException(new Exception("OWF Widget Type with id " + params.id + " does not exist"), "getItemAsJSON", 404)
                return
            }
        } catch (Exception e) {
            handleException(e, "getItemAsJSON")
            return
        }
        renderResult(model, 1, 200)
    }
}
