package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.plugins.elasticsearch.ElasticSearchAdminService
import grails.plugins.elasticsearch.ElasticSearchService

import marketplace.Category
import marketplace.Profile
import marketplace.SearchableService
import marketplace.ServiceItem
import marketplace.Types
import marketplace.search.SearchCriteria
import spock.lang.Specification

import static marketplace.Constants.APPROVAL_STATUSES

@Integration
@Rollback
class SearchableServiceTests extends Specification{

    SearchableService searchableService
    ElasticSearchService elasticSearchService
    ElasticSearchAdminService elasticSearchAdminService

    Profile testOwner
    String queryString = 'liuwehflkjsdfasdkfjuha'

    private ServiceItem makeServiceItem(Map params) {
        def serviceItem = new ServiceItem(params)

        //defaults
        serviceItem.with {
            approvalStatus = params?.approvalStatus ?: APPROVAL_STATUSES['APPROVED']
            types = params?.types ?: new Types(title: 'Some Type').save()
            owners = params?.owners ?: [testOwner]
            title = params?.title ?: 'Test Listing'
            launchUrl = params?.launchUrl ?: 'https://'
        }

        serviceItem.save(failOnError: true)

        elasticSearchService.index(serviceItem)

        return serviceItem
    }

    private Map search(SearchCriteria searchCriteria) {
        elasticSearchAdminService.refresh()
        searchableService.searchListings(searchCriteria)
    }

    void setupData() {
        // Unrelated tests are leaking data somewhere
        ServiceItem.findAll().each { it.delete(flush: true) }

        testOwner = new Profile(username: 'testOwner').save(failOnError: true)
    }

    void tearDown() {
        elasticSearchService.unindex(ServiceItem.class)
        elasticSearchAdminService.refresh()
    }

    void testDefaultSearchReturnsAllItems() {
        setup:
        setupData()
        (1..5).each {
            makeServiceItem()
        }

        when:
        def searchCriteria = new SearchCriteria([:])
        def results = search(searchCriteria)
        then:
        results.total == 5
    }

    void testFilterByType() {
        setup:
        setupData()
        when:
        def typeToFilter = new Types(title: queryString).save(failOnError: true)
        def itemToFind = makeServiceItem(types: typeToFilter)
        makeServiceItem(types: new Types(title: queryString).save(failOnError: true))
        makeServiceItem(description: queryString)

        def searchCriteria = new SearchCriteria(queryString: queryString, typeFilters: "${typeToFilter.id}")
        
        def results = search(searchCriteria)

        then:
        results.total == 1
        results.searchResults*.id == [itemToFind.id]
    }

    void testFilterByCategory() {
        setup:
        setupData()
        when:
        def categoryToFilter = new Category(title: queryString).save(failOnError: true)
        def categoryNotToFind1 = new Category(title: queryString).save(failOnError: true)
        def categoryNotToFind2 = new Category(title: 'foo').save(failOnError: true)
        def itemToFind1 = makeServiceItem(categories: [categoryToFilter, categoryNotToFind1])
        def itemToFind2 = makeServiceItem(categories: [categoryToFilter])
        makeServiceItem(categories: [categoryNotToFind1])
        makeServiceItem(categories: [categoryNotToFind2], description: queryString)

        def searchCriteria = new SearchCriteria(queryString: queryString, categoryFilters: "${categoryToFilter.id}")
        def results = search(searchCriteria)

        then:
        results.total == 2
        results.searchResults*.id as Set == [itemToFind1.id, itemToFind2.id] as Set
    }

//    public void testFilterByAgency() {
//        def agencyToFilter = new Agency(title: queryString, iconUrl: 'http://').save(failOnError: true)
//        def itemToFind = makeServiceItem(agency: agencyToFilter)
//        makeServiceItem(description: queryString)
//
//        def searchCriteria = new SearchCriteria(queryString: queryString, agencyFilters: "${agencyToFilter.id}")
//        def results = search(searchCriteria)
//
//        assert results.total == 1
//        assert results.searchResults*.id == [itemToFind.id]
//    }
}
