package marketplace.search

import org.elasticsearch.common.xcontent.ToXContent
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import ozone.utils.ApplicationContextHolder
import spock.lang.Specification

//@TestMixin(GrailsUnitTestCase)
class SearchCriteriaSpec extends Specification {

    def defaultSearchParams = [
            sort: 'score',
            order: 'desc',
            aggregations: 'true',
            max: '24',
            offset: '0'
    ]

    def config = [marketplace: [customFields:[domain: [values:['domain1', 'domain2']]]]]

    void setup() {
        ApplicationContextHolder.metaClass.static.getConfig = { config }
    }

    void testAggregationsAreAdded() {
        given:
        Integer aggregationCount = 0

        SearchSourceBuilder.metaClass.aggregation = { ToXContent aggregationBuilder ->
            aggregationCount += 1
        }

        def criteria = new SearchCriteria(defaultSearchParams)
        criteria.aggregations = true

        SearchSourceBuilder source = criteria.extraSearchSource

        expect:
        assert source != null
        assert aggregationCount == SearchCriteria.TERM_AGGREGATIONS.size()
    }

    void testSortByScore() {
        given:
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

        expect:
        assert source != null
        assert scoreSort
        assert secondarySortCount == 2
    }
}
