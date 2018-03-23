package marketplace

import grails.converters.JSON
import grails.plugins.feeds.FeedBuilder

import com.sun.syndication.feed.module.opensearch.OpenSearchModule
import com.sun.syndication.feed.module.opensearch.entity.OSQuery
import com.sun.syndication.feed.module.opensearch.impl.OpenSearchModuleImpl
import com.sun.syndication.feed.synd.SyndContent
import com.sun.syndication.feed.synd.SyndContentImpl
import com.sun.syndication.feed.synd.SyndFeed
import com.sun.syndication.feed.synd.SyndImageImpl
import com.sun.syndication.io.SyndFeedOutput
import marketplace.search.SearchCriteria

import ozone.marketplace.controller.MarketplaceException
import ozone.marketplace.enums.MarketplaceApplicationSetting


class SearchController extends BaseMarketplaceRestController {

    final static String DEFAULT_FEED_TYPE = 'atom'
    final static List VALID_FEED_TYPES = ['atom', 'rss']
    final static String DEFAULT_FEED_PAGE_SIZE = '10'

    SearchableService searchableService

    SearchNuggetService searchNuggetService

    ServiceItemService serviceItemService

    AggregationsService aggregationsService

    // TODO: Remove this once I get logging from the integration test working.
    def logIt(def strIn) {
        log.info strIn
    }

    /**
     * This action is mapped to the Advanced Search page.
     */
    def index = {}

    def openSearchDescriptor = {
        def model = [:]
        model.openSearchTitleMessage = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OPEN_SEARCH_TITLE_MESSAGE)
        model.openSearchDescriptionMessage = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OPEN_SEARCH_DESCRIPTION_MESSAGE)
        model.openSearchFavIcon = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OPEN_SEARCH_FAV_ICON)
        model.openSearchPluginSiteIcon = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OPEN_SEARCH_SITE_ICON)
        render(template: "/search/openSearchDescriptor", contentType: "text/xml", model: model)
    }

    def filterQuery = {
        filter('queryString', params.id ?: null)
    }

    // AML-680  agency attribute for a listing
    def filterAgency = {
        filter('agencyFilters', params.id ?: null, true)
    }

    // AML-726  domain attribute for a listing
    def filterDomain = {
        filter('domainFilters', params.id ?: null, null)
    }

    def filterType = {
        filter('typeFilters', params.id ?: null, true)
    }

    def filterCategory = {
        filter('categoryFilters', params.id ?: null)
    }

    def filterState = {
        filter('stateFilters', params.id ?: null, true)
    }

    def filterRate = {
        filter('rating', params.id ?: null)
    }

    def filterStatus = {
        filter('statuses', params.id ? [params.id] : null)
    }

    def filterRelTo = {
        filter('releasedToDate', params.id ?: null)
    }

//    def filterScore = {
//        filter('scoreCardValue', params.id ?: null)
//    }

    // AML-2608 owfWidgetType
    def filterOwfWidgetType = {
        filter('owfWidgetType', params.id ?: null)
    }

    def filter = { field, val, replaceSearch = false ->

        prepDefaultParams(params)
        SearchCriteria searchBean = session.searchBean
        if (searchBean) {
            applySaved(params, searchBean)
            searchBean.updateBean(params)
        } else {
            searchBean = new SearchCriteria(params)
            session.searchBean = searchBean
        }
        if (val)
            replaceSearch ? searchBean.replaceSearch(field, val) : searchBean.addSearch(field, val)
        else
            searchBean.clearSearch(field, "")

        getList(searchBean)
    }

    //Use in the search results page to load more results based on the current offset
    def getSearchResults = {

        //Get the search bean and increment the offset
        SearchCriteria searchBean = session.searchBean

        def initialOffset = searchBean.offset ?: "0"

        if (params.offset)
            searchBean.offset = (initialOffset as int) + (params.offset as int)

        def initialMax = searchBean.max
        if (params.max)
            searchBean.max = params.max

        if (params.sort)
            searchBean.sort = params.sort

        if (params.order)
            searchBean.order = params.order


        session.sortBy = searchBean.sort
        session.orderBy = searchBean.order

        def modelData = [:]
        def result = searchableService.searchListings(searchBean)

        //If the loaded record count is < than the total then we return the results
        //Otherwise register an error and reset the count
        if ((params.loaded as Integer) < result?.total) {
            modelData = [serviceItemList: result?.searchResults]
        } else {
            render(status: 500)
            return
        }

        //Since this is incoming parameter we can reset it on each request
        searchBean.offset = initialOffset
        searchBean.max = initialMax


        if (params.viewGridOrList == 'list') {
            render(template: "/serviceItem/searchResults/widget_list_view", model: modelData)
        } else {
            render(template: "/serviceItem/searchResults/widget_grid_view", model: modelData)
        }

    }

    //This will totally replace the search bean
    def clear = {
        prepDefaultParams(params)
        session.searchBean = new SearchCriteria(params)
        getList(session.searchBean)
    }

    //This will remove a filter from the search bean
    def removeFilter = {
        prepDefaultParams(params)
        SearchCriteria searchBean = session.searchBean
        if (searchBean) {
            applySaved(params, searchBean)
            searchBean.updateBean(params)
            searchBean.clearSearch(params.field, params.values)
        }
        getList(searchBean)
    }

    def back = {
        if ((session.modelMap) && (session.modelMap['flashMessage'])) {
            flash.message = session.modelMap['flashMessage']
            if (Boolean.valueOf(session.modelMap['flashSuccess'])) {
                flash.success = true
            }
            session.modelMap = null
        }
        prepDefaultParams(params)
        if (session.searchBean) {
            applySaved(params, session.searchBean)
            session.searchBean.updateBean(params)
        } else {
            session.searchBean = new SearchCriteria(params)
        }
        getList(session.searchBean)
    }

    def getList = { searchBean ->

        searchBean.aggregations = true

        def result = searchableService.searchListings(searchBean)
        def resultsList = result?.searchResults
		def aggregations = aggregationsService.extractAggregationInfo(result);

        def modelData = [serviceItemList:resultsList, listSize: result?.total,
                 numShownResults: resultsList.size(), numShownResultsparams:searchBean, queryString:searchBean.queryString]

	    modelData['typeAggregations'] =    JSONUtil.getListFromDomainObject(aggregations.types) as JSON
	    modelData['categoriesAggregations'] =  JSONUtil.getListFromDomainObject(aggregations.categories) as JSON
		modelData['domainAggregations'] = JSONUtil.getListFromDomainObject(aggregations.domain) as JSON
		modelData['agenciesAggregations'] = JSONUtil.getListFromDomainObject(aggregations.agencies) as JSON
		modelData['nuggets'] =  searchNuggetService.nuggetize(searchBean).nuggetMap as JSON

        render(view: "/serviceItem/widgetList", model: modelData)

    }

    /**
     * Returns service item search results as a JSON list
     */
    def getListAsJSON = {
        try {
            params.outside_only = true
            params.accessType = Constants.VIEW_EXTERNAL
            def result = getServiceItemSearchResults()
            def model
            if (params.client == 'amp') {
                // smaller JSON for affiliated marketplaces
                model = result.searchResults?.collect { it.asJSONRef() }
            } else {
                model = result.searchResults?.collect { it.asJSON() }
            }
            renderResult(model, result.total as int, 200)
        }
        catch (Exception e) {
            handleException(e, "search", 500)
        }
    }

    private getServiceItemSearchResults() {
        def accessTypeIn = params.accessType ?: Constants.VIEW_USER
        prepDefaultParamsForIndex(params)
        params.accessType = accessTypeIn
        if (params.accessType == Constants.VIEW_ADMIN && !session.isAdmin) {
            log.info "user is not an admin so view is being reset to user"
            params.accessType = Constants.VIEW_USER
        }
        log.debug "params.accessType = ${params.accessType}"
        def result = searchableService.searchListings(new SearchCriteria(params))
        result
    }

    /**
     * Returns service item search results as Atom or RSS feed.
     */
    def openSearch = {
        log.debug "openSearch: params = ${params}"
        updateOpenSearchParams(params)

        // Perform Lucene search
        def result = searchableService.searchListings(new SearchCriteria(params))

        SyndFeed feed = buildOpenSearchFeed(result, params)

        // Output the feed XML to the response
        SyndFeedOutput output = new SyndFeedOutput()
        response.contentType = "application/${params.format}+xml; charset=UTF-8"
        output.output(feed, response.writer)
    }

    def setResultUiViewSettings = {
        session.searchResultUiViewMode = params.viewGridOrList
        render ([success: true] as JSON)
    }


    def getAffiliated = {

        long affiliatedMarketplaceId = params.id as int
        AffiliatedMarketplace affiliatedMarketplace = AffiliatedMarketplace.get(affiliatedMarketplaceId)
        String affiliatedUrl = grailsApplication?.config?.marketplace?.is?.franchise?.store ? "/public/outsideSearch" : "/public/search"
        // Trim trailing slash
        String serverUrl = affiliatedMarketplace.serverUrl?.endsWith('/') ? affiliatedMarketplace.serverUrl[0..-2] : affiliatedMarketplace.serverUrl
        affiliatedUrl = "${serverUrl}$affiliatedUrl"

        def queryString =
            params.collect { key, value ->
                if (key)
                    "${URLEncoder.encode(key)}=${value instanceof String ? URLEncoder.encode(value ?: '') : value}"
            }.join('&')

        affiliatedUrl = "$affiliatedUrl?${queryString}"

        log.info "Affiliated search URL: $affiliatedUrl"

        try {
            HttpURLConnection affiliatedConnection = (HttpURLConnection) new URL(affiliatedUrl).openConnection()
            affiliatedConnection.requestMethod = 'GET'
            affiliatedConnection.addRequestProperty("Accept", "application/json")
            if (affiliatedConnection.responseCode == HttpURLConnection.HTTP_OK) {
                String json = affiliatedConnection.content.text
                if (json?.trim()?.startsWith('{'))
                    render json
                else
                    throw new MarketplaceException(message: "Error searching affiliated marketplace ${affiliatedMarketplace.serverUrl}: invalid JSON - ${json}")
            } else {
                // Handle invalid response
                throw new MarketplaceException(message: "Error searching affiliated marketplace ${affiliatedMarketplace.serverUrl}: response code - ${affiliatedConnection.responseCode}, response message - ${affiliatedConnection.responseMessage}")
            }
        }
        catch (Exception e) {
            log.error("Error searching affiliated marketplace ${affiliatedMarketplace.serverUrl}", e)
            render ([
                success: false,
                totalCount: 0,
                msg: e.toString().encodeAsHTML()
            ] as JSON)
        }
    }

    /**
     * Set parameters for Lucene search
     * @param params
     */
    private void updateOpenSearchParams(Map params) {
        if (!params.max) params.max = DEFAULT_FEED_PAGE_SIZE
        prepDefaultParamsForIndex(params)

        // Logic for restricting the results to those published, approved and enabled
        params.state_isPublished = true
        params.enabled_only = true
        params.statuses = ["Approved"]
        // Restrict to only outside listings
        //params.outside_only = true

        // Cache base url
        params.baseUrl = request.scheme + "://" + request.serverName + ":" + request.serverPort

        if (!params.format || !VALID_FEED_TYPES.contains(params.format)) params.format = DEFAULT_FEED_TYPE
    }

    /**
     * Build the feed object containing the search results
     * @param result Lucene search results
     * @param params request parameters
     * @return
     */
    private SyndFeed buildOpenSearchFeed(def result, Map params) {
        def builder = new FeedBuilder()
        def searchLink = params.baseUrl + g.createLink(controller: "search", action: "openSearch", params: params)
        def imageUrl = g.resource(dir: '/images/themes/default', file: 'market_64x64.png')
        def marketplaceUrl = g.resource(dir: '/')
        def resultMap = [:]
        // Use Feeds plugin to create the feed object
        String openSearchMessage = marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.OPEN_SEARCH_TITLE_MESSAGE)
        builder.feed(title: openSearchMessage,
            description: Text.findByName(Constants.TEXT_NAME_ABOUT)?.value,
            link: searchLink) {
            for (listing in result.searchResults) {
                resultMap[(listing.title)] = listing // create a map of search results for later use
                image = new SyndImageImpl(url: imageUrl, title: 'Marketplace Logo', link: marketplaceUrl)
                def entryLink = params.baseUrl + g.createLink(controller: "serviceItem", action: "show", id: listing.id)

                entry(listing.title) {
                    link = entryLink
                    author = listing.owners?.join(", ")
                    publishedDate = listing.releaseDate
                    content() {
                        type = "text/html"
                        listing.description
                    }
                }
            }
        }

        // Generate a ROME syndicated feed
        SyndFeed feed = builder.makeFeed(params.format)

        // Add entry description (Feeds plugin does not support it)
        addEntryDescriptions(feed, resultMap)

        // Add open-search-specific information to the feed
        addOpenSearchModule(feed, result, params)
    }

    /**
     * Iterate through feed entries adding listing description to each of them. This is a workaround for the Feeds
     * plugin limitation - it does not allow setting entry's description field.
     * @param feed
     * @param resultMap
     * @return
     */
    private addEntryDescriptions(SyndFeed feed, Map resultMap) {
        feed.entries.each { entry ->
            SyndContent content = new SyndContentImpl()
            content.value = resultMap[(entry.title)]?.description
            entry.description = content
        }
    }

    /**
     * Add parameters specific to ROME OpenSearch module
     * @param feed
     * @return
     */
    private SyndFeed addOpenSearchModule(SyndFeed feed, def result, Map params) {
        List mods = feed.getModules()
        OpenSearchModule osm = new OpenSearchModuleImpl()
        int paramsMax = params.max as int
        int paramsOffset = params.offset as int
        osm.setItemsPerPage(paramsMax)
        osm.setStartIndex(paramsOffset)
        osm.setTotalResults(result.total as int)

        OSQuery query = new OSQuery()
        query.setRole("request")
        query.setSearchTerms(params.queryString)
        if (result.searchResults) {
            int startPage = paramsOffset / paramsMax + 1
            query.setStartPage(startPage)
        }
        osm.addQuery(query)

        mods.add(osm)
        feed.setModules(mods)
        feed
    }


    private prepDefaultParams(def params) {

        params.sort = session.sortBy
        params.order = session.orderBy

        // Set up Default params if necessary
        if (params) {
            if (!params.offset) params.offset = "0"
            if (!params.max) params.max = grailsApplication.config.marketplace.defaultSearchPageSize
            if (!params.sort) params.sort = "title"
            if (!params.order) params.order = "asc"
            params.accessType = session.accessType ?: Constants.VIEW_USER
            params.username = session.username
        }
    }

    private applySaved(def params, def bean) {
        if (bean.sort) {
            params.sort = bean.sort
        }
        if (bean.max) {
            params.max = bean.max
        }
        if (bean.order) {
            params.order = bean.order
        }
    }


}
