package marketplace

class ExternalAccessController {

    def search = {
        params.accessType = Constants.VIEW_EXTERNAL
        params.outside_only = true
        forward(controller: 'search', action: "getListAsJSON", params: params)
    }

}
