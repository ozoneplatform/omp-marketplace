package marketplace.rest

import marketplace.AggregationsService
import marketplace.JSONUtil
import marketplace.SearchableService
import marketplace.search.SearchCriteria

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.UriInfo

@Path('/api/search')
class SearchResource extends JsonResource {

    @Context UriInfo uriInfo
    SearchableService searchableService
    SearchRestService searchRestService
    AggregationsService aggregationsService

    @GET
    Map search() {
        Map params = parseParams(uriInfo.getQueryParameters())
        params = searchRestService.buildSearchParams(params)
        doSearch(params)
    }

    @GET
    @Path('/affiliated')
    Map affiliatedSearch() {
        Map params = parseParams(uriInfo.getQueryParameters())
        params = searchRestService.buildAffiliatedSearchParams(params)
        doSearch(params)
    }

    private Map doSearch(Map params) {
        SearchCriteria searchBean = new SearchCriteria(params)
        searchBean.aggregations = true

        def result = searchableService.searchListings(searchBean)
        Collection resultsList = result?.searchResults
        Map aggregations = aggregationsService.extractAggregationInfo(result)

        [
            total: result?.total ?: resultsList.size(),
            data: resultsList,
            aggregations: [
                type:    JSONUtil.getListFromDomainObject(aggregations.types),
                category:  JSONUtil.getListFromDomainObject(aggregations.categories),
                agency: JSONUtil.getListFromDomainObject(aggregations.agencies)
            ]
        ]
    }

}
