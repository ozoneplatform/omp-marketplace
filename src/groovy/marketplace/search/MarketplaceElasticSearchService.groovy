package marketplace.search

import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.Client
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.facet.Facet
import org.elasticsearch.search.facet.Facets
import org.elasticsearch.search.facet.query.QueryFacet
import org.elasticsearch.search.facet.range.RangeFacet
import org.elasticsearch.search.facet.terms.TermsFacet
import org.grails.plugins.elasticsearch.ElasticSearchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MarketplaceElasticSearchService extends ElasticSearchService {

    static final Logger LOG = LoggerFactory.getLogger(this)

    private static final int INDEX_REQUEST = 0
    private static final int DELETE_REQUEST = 1

    static transactional = false

    /**
     *   We need support for facets, which is not provided by the elasticsearch
     *   grails plugin. This method allows for adding extra search request source.
     *
     * @param query
     * @param params
     * @param facets
     * @return
     */
    def search(Map params, Closure query, SearchSourceBuilder source) {
        SearchRequest request = buildSearchRequest(query, null, params)

        request.extraSource(source)

        search(request, params)
    }

    /**
     * We need to get the facets out of the response.
     * This method is otherwise the same as the
     * one it overrides.
     *
     * @param request The SearchRequest to compute
     * @param params Search parameters
     * @return A Map containing the search results
     */
    @Override
    def search(SearchRequest request, Map params) {
        resolveIndicesAndTypes(request, params)
        elasticSearchHelper.withElasticSearch { Client client ->
            LOG.debug 'Executing search request.'
            def response = client.search(request).actionGet()
            LOG.debug 'Completed search request.'
            def searchHits = response.getHits()
            def result = [:]
            result.total = searchHits.totalHits()

            LOG.debug "Search returned ${result.total ?: 0} result(s)."

            // Convert the hits back to their initial type
            result.searchResults = domainInstancesRebuilder.buildResults(searchHits)

            if (response.getFacets()) {
                result.facets = [:]
                Facets facets = response.getFacets()
                facets.facetsAsMap().each { entry ->
                    def facetInfo = new Expando(name: entry.key)
                    Facet facet = entry.value
                    facetInfo.termCounts = []
                    facetInfo.rangeCounts = []
                    if (facet instanceof TermsFacet) {
                        facetInfo.type = "term"
                        facetInfo.missing = facet.getMissingCount()
                        facetInfo.total = facet.getTotalCount()
                        facet.getEntries().each { countEntry ->
                            facetInfo.termCounts << [term: "${countEntry.getTerm()}", count: countEntry.getCount()]
                        }
                    } else if (facet instanceof RangeFacet) {
                        (facet as RangeFacet).getEntries().each { countEntry ->
                            facetInfo.type = "range"
                            facetInfo.rangeCounts << [from: countEntry.getFrom(), to: countEntry.getTo(), count: countEntry.getCount()]
                        }
                    } else if (facet instanceof QueryFacet)  {
                        facetInfo.type = "query"
                        facetInfo.termCounts << [term: facet.getName(), count: facet.getCount()]
                    }

                    result.facets[(entry.key)] = facetInfo
                }
            }

            // Extract highlight information.
            // Right now simply give away raw results...
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

            result
        }
    }
}
