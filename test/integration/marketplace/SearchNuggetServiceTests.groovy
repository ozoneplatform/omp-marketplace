package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.junit.Ignore
import marketplace.search.SearchCriteria

@TestMixin(IntegrationTestMixin)
class SearchNuggetServiceTests {
    def searchNuggetService
    def searchBean
    def typeId1 = Types.findByTitle("App Component").id
    def typeId2 = Types.findByTitle("Web App").id
    def stateId1 = State.findByTitle("Beta").id
    def stateId2 = State.findByTitle("Planned").id
    def categoryId1 = Category.findByTitle("Query").id
    def agency1 = "AgencyA"
    void setUp() {
        searchBean = new SearchCriteria(['queryString':'test', 'typeIDs':[typeId1,typeId2], 'stateIDs': [stateId1,stateId2]])
        searchBean.addSearch("categoryFilters", categoryId1 as String)
        searchBean.addSearch("rating", "5")
        searchBean.addSearch("agencyFilters", agency1)
    }

    @Ignore
    void testNuggetize() {
        SearchNuggets searchNuggets = searchNuggetService.nuggetize(searchBean)
        assert true == searchNuggets.hasNuggets()
        assert true == searchNuggets.hasQuery()
        assert true == searchNuggets.hasFilter("categoryFilters")
        assert true == searchNuggets.hasFilter("typeIDs")
        assert true == searchNuggets.hasFilter("agencyFilters")
        assert "test" == searchNuggets.getQuery()
        assert 5 == searchNuggets.getNugget("rating")[0].id
        assert categoryId1 == searchNuggets.getNugget("categoryFilters")[0].id
        assert typeId2 == searchNuggets.getNugget("typeIDs")[1].id
        assert agency1 == searchNuggets.getNugget("agencyFilters")
        assert 6 == searchNuggets.getAllNuggets().size()
        assert 3 == searchNuggets.getFilterNuggets().size()
        assert 3 == searchNuggets.getSearchNuggets().size()
    }
}
