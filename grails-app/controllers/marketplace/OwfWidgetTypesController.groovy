package marketplace

class OwfWidgetTypesController extends BaseMarketplaceRestController {

    OwfWidgetTypesService owfWidgetTypesService

    def getListAsJSON() {
        if (!params.max) params.max = 100
        def model
        def total
        try {
            if (params.sort == null) {
            params.sort = 'title'
        }
            model = owfWidgetTypesService.list(params)
            total = owfWidgetTypesService.countOwfWidgetTypes()
        }
        catch (Exception e) {
            handleException(e, "getListAsJSON")
            return
        }
        renderResult(model, total, 200)
    }

    def getItemAsJSON() {
        def model
        try {
            model = owfWidgetTypesService.getOwfWidgetType(params.id)
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
