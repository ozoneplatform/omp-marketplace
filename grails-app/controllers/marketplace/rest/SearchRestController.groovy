package marketplace.rest

import grails.converters.JSON

import marketplace.AggregationsService
import marketplace.JSONUtil
import marketplace.SearchableService
import marketplace.search.SearchCriteria


class SearchRestController implements RestExceptionHandlers {

    static namespace = 'api'

    static responseFormats = ['json']

    SearchableService searchableService
    SearchRestService searchRestService
    AggregationsService aggregationsService

    def search() {
        Map searchParams = searchRestService.buildSearchParams(queryParameters)
        Map results = doSearch(searchParams)

        render(results as JSON)
    }

    def affiliatedSearch() {
        Map searchParams = searchRestService.buildAffiliatedSearchParams(queryParameters)
        Map results = doSearch(searchParams)

        render(results as JSON)
    }

    private Map doSearch(Map params) {
        SearchCriteria criteria = new SearchCriteria(params)
        criteria.aggregations = true

        def result = searchableService.searchListings(criteria)
        Collection resultsList = result?.searchResults
        Map aggregations = aggregationsService.extractAggregationInfo(result)

        [total       : result?.total ?: resultsList.size(),
         data        : resultsList,
         aggregations: [type    : JSONUtil.getListFromDomainObject(aggregations.types),
                        category: JSONUtil.getListFromDomainObject(aggregations.categories),
                        agency  : JSONUtil.getListFromDomainObject(aggregations.agencies)]]
    }

    private Map getQueryParameters() {
        Map<String, Object> queryParameters = [:]
        request.parameterMap.forEach { String key, String[] values -> queryParameters.put(key, Arrays.asList()) }
        parseParams(queryParameters)
    }

    private static Map parseParams(Map<String, Object> params) {
        Map returnParams = [:]

        params.each { key, val ->
            // handle jquery style list values
            String formattedKey = key.endsWith('[]') ? (key.substring(0, key.length() - 2)) : key

            if (val instanceof List && val.size() == 1) {
                returnParams[formattedKey] = (val as List).get(0)
            }
            else {
                returnParams[formattedKey] = val
            }
        }
        returnParams
    }

}
