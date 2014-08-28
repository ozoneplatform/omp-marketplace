package marketplace.rest

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import marketplace.AccountService
import marketplace.Constants
import ozone.utils.ApplicationContextHolder
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly=true)
@Service
class SearchRestService {

    @Autowired AccountService accountService

    public buildSearchParams(final Map params, Boolean isAdmin = accountService.isAdmin()) {
        if (!params.offset) params.offset = 0
        if (!params.max) params.max = ApplicationContextHolder.config.marketplace.defaultSearchPageSize
        if (!params.sort) params.sort = "score"
        if (!params.order) params.order = "asc"

        if(!isAdmin) {
            params.enabled_only = true
            params.state_isPublished = true
            params.statuses = [Constants.APPROVAL_STATUSES.APPROVED]
        }

        params.accessType = isAdmin ? Constants.VIEW_ADMIN : Constants.VIEW_USER
        params.username = accountService.getLoggedInUsername()

        params
    }

    public buildAffiliatedSearchParams(Map params) {
        buildSearchParams(params, false)
    }

}
