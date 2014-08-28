package marketplace

import ozone.marketplace.domain.ValidationException

abstract class MarketplaceAdminWithDeleteController extends MarketplaceAdminController {

    abstract protected deleteDomain()

    def delete = {
        try {
            def title = retrieveDomain()?.title
            deleteDomain()
            flash.message = 'delete.success'
            flash.args = [title]
            redirect(action: 'list')
        }
        catch (ValidationException ve) {
            flash.message = ve.message
            flash.args = ve.args
            redirect(action: 'show', id: params.id)
        }
    }
}
