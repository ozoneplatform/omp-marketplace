package marketplace.search

import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.Client
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.builder.SearchSourceBuilder

import org.grails.plugins.elasticsearch.ElasticSearchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.InternalMax;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;

class MarketplaceElasticSearchService extends ElasticSearchService {

    static final Logger LOG = LoggerFactory.getLogger(this)

    private static final int INDEX_REQUEST = 0
    private static final int DELETE_REQUEST = 1

    static transactional = false

    /**
     *   We need support for aggregations, which is not provided by the elasticsearch
     *   grails plugin. This method allows for adding extra search request source.
     *
     * @param query
     * @param params
     * @param aggregations
     * @return
     */
    def search(Map params, Closure query, SearchSourceBuilder source) {
        SearchRequest request = buildSearchRequest(query, null, params)

        request.extraSource(source)

        search(request, params)
    }

    def search(SearchRequest request, Map params) {
        resolveIndicesAndTypes(request, params)
        elasticSearchHelper.withElasticSearch { Client client ->
            LOG.debug 'Executing search request.'
            def response = client.search(request).actionGet()
            LOG.debug 'Completed search request.'

            def searchHits = response.getHits()
            def result = [:]
            result.total = searchHits.totalHits()

            LOG.debug 'Search returned ${result.total ?: 0} result(s).'

            result.searchResults = domainInstancesRebuilder.buildResults(searchHits)

            if (params.highlight) {
                def highlightResults = []

                for (SearchHit hit : searchHits) {
                    highlightResults << hit.highlightFields
                }

                result.highlight = highlightResults
            }

            LOG.debug 'Adding score information to results.'
            //Extract score information
            //Records a map from hits of (hit.id, hit.score) returned in 'scores'
            if (params.score) {
                def scoreResults = [:]
                for (SearchHit hit : searchHits) {
                    scoreResults[(hit.id)] = hit.score
                }
                result.scores = scoreResults
            }

            if (params.sort) {
                def sortValues = [:]
                searchHits.each { SearchHit hit ->
                    sortValues[hit.id] = hit.sortValues
                }
                result.sort = sortValues
            }

            if (response.getAggregations()) {
                Map<String, Aggregation> aggregations = response.getAggregations().asMap()
                if (aggregations) {
                    result.aggregations = aggregations
                }
            }            

            result
        }
    }
}
