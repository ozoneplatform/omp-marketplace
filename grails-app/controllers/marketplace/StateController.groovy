package marketplace

import grails.plugin.cache.CacheEvict
import grails.converters.JSON

class StateController extends MarketplaceAdminWithDeleteController {

    def stateService
    def searchableService

    protected String getDomainName() { return "state" }

    protected String getObjectName() { return "state" }

    protected retrieveDomain() { return State.get(params.id) }

    protected createDomain() { return new State(params) }

    @Override
    protected deleteDomain() {
        stateService.delete(params.id)
    }

    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        return stateService.list(params)
    }

    protected retrieveDomainCount() { return stateService.countTypes() }

    void postUpdateDomain() {
        def id = params.id
        searchableService.reindexServiceItems({ state { eq('id', new Long(id)) } })
        flash.message = "reindex.update"
    }

    def performDoList() {
        def statesList = stateService.getAllStates()
        render ([
            success: (statesList == null) ? false : true,
            totalCount: (statesList == null) ? 0 : statesList.size(),
            records: statesList.collect { state ->
                [
                    id: state?.id,
                    title: state?.title,
                    description: state?.description
                ]
            }
        ] as JSON)
    }

    @CacheEvict("serviceItemBadgeCache")
    def update() {
        super.update()
    }
}
