package marketplace

import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class SearchNuggetsTests {
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

    void setUp() {
        searchNuggets = new SearchNuggets()
        searchNuggets.addNugget("categoryIDs", [categoryIDs1, categoryIDs2])
        searchNuggets.addNugget("categoryFilters", [categoryFilters1, categoryFilters2])
        searchNuggets.addNugget("queryString", QUERY_STRING)
    }

    void testGetNugget() {
        assertEquals([categoryIDs1, categoryIDs2], searchNuggets.getNugget("categoryIDs"))
        assertEquals([categoryFilters1, categoryFilters2], searchNuggets.getNugget("categoryFilters"))
    }

    void testHasFilter() {
        assertTrue(searchNuggets.hasFilter("categoryIDs"))
        assertTrue(searchNuggets.hasFilter("categoryFilters"))
        assertTrue(searchNuggets.hasFilter("categoryIDs", 1))
        assertTrue(searchNuggets.hasFilter("categoryFilters", 3))
        assertTrue(searchNuggets.hasFilter("categoryIDs", 1))
        assertTrue(searchNuggets.hasFilter("categoryFilters", 3))
        assertFalse(searchNuggets.hasFilter("categoryIDs", 4))
        assertFalse(searchNuggets.hasFilter("categoryFilters", 2))
    }

    void testGetQuery() {
        assertEquals(QUERY_STRING, searchNuggets.getQuery())

    }

    void testHasQuery() {
        assertTrue(searchNuggets.hasQuery())
        searchNuggets = new SearchNuggets()
        searchNuggets.addNugget("categoryIDs", [categoryIDs1, categoryIDs2])
        searchNuggets.addNugget("categoryFilters", [categoryFilters1, categoryFilters2])
        assertFalse(searchNuggets.hasQuery())
    }

    void testHasNuggets() {
        assertTrue(searchNuggets.hasNuggets())
        searchNuggets = new SearchNuggets()
        assertFalse(searchNuggets.hasNuggets())
    }

    void testGetFilterNuggets() {
        def filterNuggets = searchNuggets.getFilterNuggets()
        assertEquals(2, filterNuggets.size())
        assertTrue(compareObject(filterNugget1, filterNuggets[0]))
        assertTrue(compareObject(filterNugget2, filterNuggets[1]))
    }

    void testGetSearchNuggets() {
        def searchNuggetsList = searchNuggets.getSearchNuggets()
        assertEquals(2, searchNuggetsList.size())
        assertTrue(compareObject(searchNugget1, searchNuggetsList[0]))
        assertTrue(compareObject(searchNugget2, searchNuggetsList[1]))
    }

    void testGetAllNuggets() {
        def allNuggets = searchNuggets.getAllNuggets()
        assertEquals(4, allNuggets.size())
        assertTrue(compareObject(searchNugget1, allNuggets[0]))
        assertTrue(compareObject(filterNugget2, allNuggets[3]))
    }

    void testIsJustQuery() {
        assertFalse(searchNuggets.isJustQuery())
        searchNuggets = new SearchNuggets()
        searchNuggets.addNugget("queryString", QUERY_STRING)
        assertTrue(searchNuggets.isJustQuery())
    }

    private boolean compareObject(obj1, obj2) {
        obj1.properties.each { key, value ->
            if (value != obj2[(key)]) return false
        }
        return true
    }
}
