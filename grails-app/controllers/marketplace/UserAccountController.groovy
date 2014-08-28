package marketplace

import ozone.marketplace.util.event.*
import grails.converters.JSON

class UserAccountController {

    def userAccountService
    def accountService

    def index = { redirect(action: list, params: params) }

    def list = {
        if (!params.max) params.max = 10
        if (params.sort == null) {
            params.sort = 'username'
        }
        [accounts: userAccountService.list(params), total: userAccountService.total()]
    }
}
