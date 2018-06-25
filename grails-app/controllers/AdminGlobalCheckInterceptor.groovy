import marketplace.AccountService

import marketplace.Constants
import marketplace.WebUtil

class AdminGlobalCheckInterceptor {

    int order = 50
    AccountService accountService

    AdminGlobalCheckInterceptor() {
        match( controller: '(administration|category|types|state|text|customFieldDefinition|rejectionJustification|importTask|export|scoreCardItem|userAccount|intentAction|intentDataType|contactType)',
            action: '(index|list|show|delete|softDelete|edit|editme|update|updateme|create|createme|save|saveme|imageDelete|importAll|importFromFile|getTasks|getTaskResults|exportAll|execute)')
        match(controller:'serviceItem', action: '(adminView|adminList)')
        match(controller: 'applicationConfiguration', action: 'show')
        match(controller: 'profile', action: 'list|detail')
        match(controller: 'dataExchange', action: '*')
    }

    boolean before() {
        adminCheck('adminChecks')
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }

    private def adminCheck(filterName) {
        AdminGlobalCheckInterceptor.log.debug "${filterName} before filter - ${controllerName} ${actionName}"/*${filterDelegate.controllerName} ${filterDelegate.actionName}*/

        if (WebUtil.isShowAccessAlertRequiredAndNeeded()) {
            AdminGlobalCheckInterceptor.log.debug "${filterName} filter - returning due to show Access Alert"
            return false;
        }
        boolean returnCode = false
        def username = accountService.getLoggedInUsername()
        session.username = username

        if (!session.isAdmin) {
            if (accountService.isAdmin()) {
                session.isAdmin = true
                session.accessType = Constants.VIEW_ADMIN // "Administrator"
                AdminGlobalCheckInterceptor.log.info "AdminCheck Filter${username} is an Admin"
                returnCode = true
            } else {
                UserInterceptor.log.info "Filter: ${username} is not an admin"
                returnCode = false
                if(session.spaEnabled == true) {
                    redirect(uri: '/spa')
                    return
                }
                else {
                    flash.message = 'admin.access.denied'
                    redirect(controller: "serviceItem", action: "shoppe")
                    return
                }
            }
        } else {
            returnCode = true
        }
        return returnCode
    }
}
