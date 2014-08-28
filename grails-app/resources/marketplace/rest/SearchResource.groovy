package marketplace.rest

import marketplace.FacetsService
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
    FacetsService facetsService

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
        searchBean.facets = true

        def result = searchableService.searchListings(searchBean)
        Collection resultsList = result?.searchResults
        Map facets = facetsService.extractFacetInfo(result)

        [
            total: result?.total ?: resultsList.size(),
            data: resultsList,
            facets: [
                type:    JSONUtil.getListFromDomainObject(facets.types),
                category:  JSONUtil.getListFromDomainObject(facets.categories),
                agency: JSONUtil.getListFromDomainObject(facets.agencies)
            ]
        ]
    }

}
