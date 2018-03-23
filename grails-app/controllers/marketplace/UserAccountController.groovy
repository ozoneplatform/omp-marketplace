package marketplace

class UserAccountController {

    UserAccountService userAccountService

    AccountService accountService

    def index = {
        redirect(action: 'list', params: params)
        return
    }

    def list = {
        if (!params.max) params.max = 10
        if (params.sort == null) {
            params.sort = 'username'
        }
        [accounts: userAccountService.list(params), total: userAccountService.total()]
    }
}
