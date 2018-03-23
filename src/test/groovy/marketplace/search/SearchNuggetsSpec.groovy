package marketplace

import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

//@TestMixin(GrailsUnitTestMixin)
class SearchNuggetsSpec extends Specification {
    SearchNuggets searchNuggets
    public static Map categoryIDs1 = ['id': 1, 'title': "cat1", 'display': "Category 1"]
    public static Map categoryIDs2 = ['id': 2, 'title': "cat2", 'display': "Category 2"]
    public static Map categoryFilters1 = ['id': 3, 'title': "cat3", 'display': "Category 3"]
    public static Map categoryFilters2 = ['id': 4, 'title': "cat4", 'display': "Category 4"]
    def searchNugget1 = new Expando(isFilter:true, isLastSearch:false, values:1, isFirstFilter:false, name:"categoryIDs", label:"category", display:"Category 1")
    def searchNugget2 = new Expando(isFilter:true, isLastSearch:true, values:2, isFirstFilter:false, name:"categoryIDs", label:"category", display:"Category 2")
    def filterNugget1 = new Expando(isFilter:false, isLastSearch:false, values:3, isFirstFilter:true, name:"categoryFilters", label:"category", display:"Category 3")
    def filterNugget2 = new Expando(isFilter:false, isLastSearch:false, values:4, isFirstFilter:false, name:"categoryFilters", label:"category", display:"Category 4")
    public static String QUERY_STRING = "test"

    void setup() {
        searchNuggets = new SearchNuggets()
        searchNuggets.addNugget("categoryIDs", [categoryIDs1, categoryIDs2])
        searchNuggets.addNugget("categoryFilters", [categoryFilters1, categoryFilters2])
        searchNuggets.addNugget("queryString", QUERY_STRING)
    }

    void testGetNugget() {
        expect:
        assert [categoryIDs1, categoryIDs2] == searchNuggets.getNugget("categoryIDs")
        assert [categoryFilters1, categoryFilters2] == searchNuggets.getNugget('categoryFilters')

//        assertEquals([categoryIDs1, categoryIDs2], searchNuggets.getNugget("categoryIDs"))
//        assertEquals([categoryFilters1, categoryFilters2], searchNuggets.getNugget("categoryFilters"))
    }

    void testHasFilter() {
        expect:
        assert searchNuggets.hasFilter('categoryIDs')
        assert searchNuggets.hasFilter('categoryFilters')
        assert searchNuggets.hasFilter('categoryIDs', 1)
        assert searchNuggets.hasFilter('categoryFilters', 3)
        assert !searchNuggets.hasFilter('categoryIDs', 4)
        assert !searchNuggets.hasFilter('categoryFilters', 2)

//        assertTrue(searchNuggets.hasFilter("categoryIDs"))
//        assertTrue(searchNuggets.hasFilter("categoryFilters"))
//        assertTrue(searchNuggets.hasFilter("categoryIDs", 1))
//        assertTrue(searchNuggets.hasFilter("categoryFilters", 3))
//        assertTrue(searchNuggets.hasFilter("categoryIDs", 1))
//        assertTrue(searchNuggets.hasFilter("categoryFilters", 3))
//        assertFalse(searchNuggets.hasFilter("categoryIDs", 4))
//        assertFalse(searchNuggets.hasFilter("categoryFilters", 2))
    }

    void testGetQuery() {
        expect:
        assert QUERY_STRING == searchNuggets.getQuery()
//        assertEquals(QUERY_STRING, searchNuggets.getQuery())

    }

    void testHasQuery() {
        expect:
        assert searchNuggets.hasQuery()

        when:
        searchNuggets = new SearchNuggets()
        searchNuggets.addNugget("categoryIDs", [categoryIDs1, categoryIDs2])
        searchNuggets.addNugget("categoryFilters", [categoryFilters1, categoryFilters2])
        then:
        assert !searchNuggets.hasQuery()
    }

    void testHasNuggets() {
        expect:
        assert searchNuggets.hasNuggets()
        when:
        searchNuggets = new SearchNuggets()
        then:
        assert !searchNuggets.hasNuggets()
    }

    void testGetFilterNuggets() {
        given:
        def filterNuggets = searchNuggets.getFilterNuggets()

        expect:
        assert 2 == filterNuggets.size()
        assert compareObject(filterNugget1, filterNuggets[0])
        assert compareObject(filterNugget2, filterNuggets[1])
    }

    void testGetSearchNuggets() {
        given:
        def searchNuggetsList = searchNuggets.getSearchNuggets()

        expect:
        assert 2 == searchNuggetsList.size()
        assert compareObject(searchNugget1, searchNuggetsList[0])
        assert compareObject(searchNugget2, searchNuggetsList[1])
    }

    void testGetAllNuggets() {
        given:
        def allNuggets = searchNuggets.getAllNuggets()

        expect:
        assert 4 == allNuggets.size()
        assert compareObject(searchNugget1, allNuggets[0])
        assert compareObject(filterNugget2, allNuggets[3])
    }

    void testIsJustQuery() {
        expect:
        assert !searchNuggets.isJustQuery()

        when:
        searchNuggets = new SearchNuggets()
        searchNuggets.addNugget("queryString", QUERY_STRING)

        then:
        assert searchNuggets.isJustQuery()
    }

    private boolean compareObject(obj1, obj2) {
        obj1.properties.each { key, value ->
            if (value != obj2[(key)]) return false
        }
        return true
    }
}
