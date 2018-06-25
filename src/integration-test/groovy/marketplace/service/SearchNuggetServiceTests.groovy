package marketplace.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import marketplace.Category
import marketplace.SearchNuggets
import marketplace.State
import marketplace.Types
import marketplace.search.SearchCriteria
import spock.lang.Ignore
import spock.lang.Specification

@Integration
@Rollback
class SearchNuggetServiceTests extends Specification{
    def searchNuggetService
    def searchBean
    def typeId1
    def typeId2
    def stateId1
    def stateId2
    def categoryId1
    def agency1
    void setupData() {
        searchBean = new SearchCriteria(['queryString':'test', 'typeIDs':[typeId1,typeId2], 'stateIDs': [stateId1,stateId2]])
       // searchBean.addSearch("categoryFilters", categoryId1 as String)
        searchBean.addSearch("rating", "5")
        searchBean.addSearch("agencyFilters", agency1)

        typeId1 = Types.findByTitle("App Component").id
        typeId2 = Types.findByTitle("Web App").id
        stateId1 = State.findByTitle("Beta").id
        stateId2 = State.findByTitle("Planned").id
        //categoryId1 = Category.findByTitle("Query").id
        agency1 = "AgencyA"
    }

    @Ignore
    void testNuggetize() {
        setup:
        setupData()
        when:
        SearchNuggets searchNuggets = searchNuggetService.nuggetize(searchBean)
        then:
        searchNuggets.hasNuggets()
        searchNuggets.hasQuery()
       // searchNuggets.hasFilter("categoryFilters")
        searchNuggets.hasFilter("typeIDs")
        searchNuggets.hasFilter("agencyFilters")
        "test" == searchNuggets.getQuery()
        5 == searchNuggets.getNugget("rating")[0].id
        //categoryId1 == searchNuggets.getNugget("categoryFilters")[0].id
        typeId2 == searchNuggets.getNugget("typeIDs")[1].id
        agency1 == searchNuggets.getNugget("agencyFilters")
        6 == searchNuggets.getAllNuggets().size()
        3 == searchNuggets.getFilterNuggets().size()
        3 == searchNuggets.getSearchNuggets().size()
    }
}
