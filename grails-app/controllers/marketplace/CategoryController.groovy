package marketplace

import grails.plugin.cache.CacheEvict
import grails.converters.JSON

class CategoryController extends MarketplaceAdminWithDeleteController {

    def categoryService
    def searchableService

    protected String getDomainName() { return "category" }

    protected String getObjectName() { return "category" }

    protected retrieveDomain() { return Category.get(params.id) }

    protected createDomain() { return new Category(params) }

    @Override
    protected deleteDomain() {
        categoryService.delete(params.id)
    }

    protected retrieveDomainList() {
        if (params.sort == null) {
            params.sort = 'title'
        }
        return categoryService.list(params)
    }

    protected retrieveDomainCount() { return categoryService.countTypes() }

    void postUpdateDomain() {
        def id = params.id
        searchableService.reindexServiceItems({ categories { eq('id', new Long(id)) } })
        flash.message = "reindex.update"
    }

    def performDoList() {
        def categoriesList = categoryService.getAllCategories()
        render ([
            success: (categoriesList == null) ? false : true,
            totalCount: (categoriesList == null) ? 0 : categoriesList.size(),
            records: categoriesList.collect { category ->
                [
                    id: category?.id,
                    title: category?.title,
                    description: category?.description
                ]
            }
        ] as JSON)
    }

    def search = {
        if (!params.max) params.max = 100
        render categoryService.search(params) as JSON;
    }

    @CacheEvict("serviceItemBadgeCache")
    def update() {
        super.update()
    }
}
