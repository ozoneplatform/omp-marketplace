package marketplace

import grails.test.GrailsUnitTestCase
import grails.test.mixin.TestMixin
import marketplace.search.SearchCriteria
import org.elasticsearch.common.xcontent.ToXContent
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import ozone.utils.ApplicationContextHolder

@TestMixin(GrailsUnitTestCase)
class SearchCriteriaTests {

    def defaultSearchParams = [
            sort: 'score',
            order: 'desc',
            facets: 'true',
            max: '24',
            offset: '0'
    ]

    def config = [marketplace: [customFields:[domain: [values:['domain1', 'domain2']]]]]

    public void setUp() {
        ApplicationContextHolder.metaClass.static.getConfig = { config }
    }

    public void testFacetsAreAdded() {
        Integer facetCount = 0

        SearchSourceBuilder.metaClass.facet = { ToXContent facetBuilder ->
            facetCount += 1
        }

        def criteria = new SearchCriteria(defaultSearchParams)
        criteria.facets = true

        SearchSourceBuilder source = criteria.extraSearchSource

        assert source != null
        assert facetCount == SearchCriteria.TERM_FACETS.size()
    }

    public void testSortByScore() {
        Boolean scoreSort = false
        Integer secondarySortCount = 0

        SearchSourceBuilder.metaClass.sort = { ScoreSortBuilder sort ->
            scoreSort = true
        }

        SearchSourceBuilder.metaClass.sort = { FieldSortBuilder sort ->
            secondarySortCount += 1
        }

        def criteria = new SearchCriteria(defaultSearchParams)
        SearchSourceBuilder source = criteria.extraSearchSource

        assert source != null
        assert scoreSort
        assert secondarySortCount == 2
    }
}
