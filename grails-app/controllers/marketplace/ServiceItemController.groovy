package marketplace

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

import grails.converters.JSON

import org.hibernate.SessionFactory

import marketplace.search.SearchCriteria
import org.apache.commons.io.IOUtils
import org.apache.commons.lang.exception.ExceptionUtils
import org.apache.commons.lang.time.FastDateFormat
import org.elasticsearch.action.search.SearchPhaseExecutionException

import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.utils.User


class ServiceItemController extends BaseMarketplaceRestController {

    static SLASHES_DATE_FORMAT = 'MM/dd/yyyy'

    static FastDateFormat dateFormatter = FastDateFormat.getInstance(SLASHES_DATE_FORMAT)

    SearchableService searchableService

    SearchNuggetService searchNuggetService

    AccountService accountService

    ServiceItemService serviceItemService

    ImagesService imagesService

    ProfileService profileService

    GenericQueryService genericQueryService

    ItemCommentService itemCommentService

    ServiceItemActivityService serviceItemActivityService

    TypesService typesService

    CategoryService categoryService

    StateService stateService

    CustomFieldDefinitionService customFieldDefinitionService

    RelationshipService relationshipService

    ImportStackService importStackService

    AggregationsService aggregationsService

    SessionFactory sessionFactory

    def index() {
        if(session.spaEnabled) {
            redirect(uri: '/spa')
            return
        }

        params.sort="editedDate"
        params.order="desc"
        session.sorttype=""
        session.sortid=0
        log.debug "Using accessType: ${session.accessType}"
        processSort()

        if (session.accessType == Constants.VIEW_USER) {
            redirect(action: "shoppe", params: params)
            return
        }

        redirect(action: 'adminView', params: params)
    }

    /** TODO: Are these session properties used? Put them on the request instead of the session. */
    private void processSort() {
        session.sortTitle = "desc"
        session.sortCreatedDate = "desc"
        session.sortRating = "desc"
        session.sortTypes = "desc"
        session.sortState = "desc"
        session.sortApprovalStatus = "desc"

        if (params.order != "desc") return

        switch (params.sort) {
            case "title":
                session.sortTitle = "asc"
                break

            case "createdDate":
                session.sortCreatedDate = "asc"
                break

            case "avgRate":
                session.sortRating = "asc"
                break

            case "types":
                session.sortTypes = "asc"
                break

            case "state":
                session.sortState = "asc"
                break

            case "approvalStatus":
                session.sortApprovalStatus = "asc"
                break
        }
    }

    def getOwfCompatibleItems() {
        params.types_ozoneAware = true
        params.state_isPublished = true
        params.enabled_only = true
        params.statuses = '["Approved"]'
        params.method_name = 'getOwfCompatibleItems'
        //do not use !params.useIndex here as a check for null
        //b/c this value can be false, and we don't want to set it to true!
        if (params.useIndex == null) params.useIndex = true
        getServiceItemsAsJSON()
    }

    /** TODO: Unused? */
    def getListFromIndexAsModel(searchBean) {
        try {
            def result = searchableService.searchListings(searchBean)
            def model = [:]
            def serviceItemList = result.searchResults
            model.put("serviceItemList", serviceItemList)
            model.put("listSize", result?.total)
            model.put("params", params)
            return model
        } catch (Exception e) {
            log.error message(code: "sic.log.error.exceptionOccurred", args: ["${e.getMessage()}"])
            log.error('getListFromIndexAsModel:', e)
            handleNonJSONException(e)
        }
    }

    private SearchResult<ServiceItem> getListFromIndexAsModel2(SearchCriteria searchBean) {
        def result = searchableService.searchListings(searchBean)

        return new SearchResult<ServiceItem>(result?.searchResults, result?.total, params)
    }

    /**
     * Advanced search method to return serviceItem models as JSON objects
     */
    def getServiceItemsAsJSON() {
        if (!params.max) params.max = 10
        if (!params.method_name) params.method_name = 'getServiceItemsAsJSON'
        params.accessType = Constants.VIEW_USER

        try {
            HttpSession session = getSession()
            SearchResult<ServiceItem> result
            if (params.boolean('useIndex')) {
                prepDefaultParamsForIndex(params)
                // Create new Search Bean
                session.searchBean = new SearchCriteria(params)
                result = getListFromIndexAsModel2(session.searchBean)
            } else {
                String username = session?.getAttribute('username')
                String accessType = session?.getAttribute('accessType')

                result = genericQueryService.serviceItems(params, username, accessType)
            }

            def model = result.items.collect { it.asJSON() }
            renderResult(model, result.total, HttpServletResponse.SC_OK)
        }
        catch (Exception e) {
            handleException(e, params.method_name)
        }
    }

    def shoppe() {
        def defaultParams = [:]
        prepDefaultParamsForIndex(defaultParams)
        session.searchBean = new SearchCriteria(defaultParams)

        try{
            params.offset = "0"
            params.max = "5"
            params.sort = "score"
            params.order = "auto"
            params.accessType = session.accessType ?: Constants.VIEW_USER
            params.username = session.username
            def filterSearchBean = new SearchCriteria(params)
            filterSearchBean.aggregations = true
            def filterSearch = searchableService.searchListings(filterSearchBean)


            params.statuses = [Constants.APPROVAL_STATUSES["APPROVED"]]
            params.accessType = session.accessType
            params.username = session.username
            params.offset = 0
            params.max = ((params.widget) ? config.discoveryWidgetMaxPerRow : config.discoveryMaxPerRow)
            params.order = 'desc'
            params.enabled_only = true
            params.max = grailsApplication.config.marketplace.defaultLandingPageSize

            //RECENTLY ADDED LISTINGS
            params.sort = 'approvalDate'
            def recentlyAdded = searchableService.searchListings(new SearchCriteria(params))
            log.debug "${params.max} ${recentlyAdded.searchResults.size()}"

            //HIGHEST RATED LISTINGS
            params.sort = 'avgRate'
            def higestRated = searchableService.searchListings(new SearchCriteria(params))

            def aggregations = aggregationsService.extractAggregationInfo(filterSearch);

            def modelData = [
                recentlyAdded: recentlyAdded.searchResults,
                best: higestRated.searchResults]

            modelData['carouselContent'] = ["carousel_content/page1"]
            if (higestRated?.searchResults) modelData['carouselContent'] << "carousel_content/page2"

            //video page for carousel can be removed
//            modelData['carouselContent'] << "carousel_content/page4"

            modelData['typeAggregations'] =    JSONUtil.getListFromDomainObject(aggregations.types) as JSON
            modelData['categoriesAggregations'] =  JSONUtil.getListFromDomainObject(aggregations.categories) as JSON
            modelData['domainAggregations'] = JSONUtil.getListFromDomainObject(aggregations.domain) as JSON
            modelData['agenciesAggregations'] = JSONUtil.getListFromDomainObject(aggregations.agencies) as JSON


            render(view: 'widgetShoppe', model: modelData)

        } catch (Exception e) {
            log.error message(code: "sic.log.error.exceptionOccurred", args: ["${e.getMessage()}"])
            log.error('shoppe:', e)
            handleNonJSONException(e)
        }
    }

    def list() {
        if ((session.modelMap) && (session.modelMap['flashMessage'])) {
            flash.message = session.modelMap['flashMessage']
            session.modelMap = null
        }
        prepDefaultParamsForIndex(params)
        session.searchBean = new SearchCriteria(params)
        getListFromIndex(false, session.searchBean)
    }


    def search() {
        if (!params.sort) {
            //if there is a search query, default sort is by relevance score,
            //otherwise, default sort is by rating
            params.sort = params.queryString ? "score" : 'avgRate'
        }

        if (!params.order) params.order = "desc"

        prepDefaultParamsForIndex(params)
        session.searchBean = new SearchCriteria(params)
        getListFromIndex(false, session.searchBean)
    }

    /** TODO: Unused? */
    def lastSearch() {
        prepDefaultParamsForIndex(params)
        SearchCriteria searchCriteria = session.searchBean
        if (searchCriteria) {
            searchCriteria.updateParams(params)
        } else {
            searchCriteria = new SearchCriteria(params)
        }
        getListFromIndex(false, searchCriteria)
    }

    /**
     * Retrieve the detail-listing of a service item as a JSON object
     * TODO: Unused?
     */
    def relist() {
        prepDefaultParamsForIndex(params)
        if (session.searchBean) {
            session.searchBean.updateBean(params)
        } else {
            session.searchBean = new SearchCriteria(params)
        }
        getListFromIndex(true, session.searchBean)
    }

    def getDetailListingForServiceItemAsJSON()  {
        def model

        try {
            model = serviceItemService.getServiceItemListing(params, true)
            if (!model) {
                // TODO: serviceItemService.getServiceItemListing never returns null. It throws an
                // exception if there is no listing for the specified id. So, this code should be changed.
                def exceptionMsg = message(code: "sic.exception.getDetailListingForServiceItemAsJSON.serviceItemDoesNotExist", args: ["${params.id}"])
                handleException(new Exception(exceptionMsg), "getDetailListingForServiceItemAsJSON", HttpServletResponse.SC_FORBIDDEN)
            } else {
                def count = 1
                renderResult(model.asJSON(), count, HttpServletResponse.SC_OK)
            }
        }
        catch (AccessControlException ae) {
            handleException(ae, "getDetailListingForServiceItemAsJSON", HttpServletResponse.SC_FORBIDDEN)
        }
        catch (Exception e) {
            handleException(e, "getDetailListForServiceItemAsJSON")
        }
    }

    /**
     * Imports a stack from a stack descriptor file
     * TODO: Unused?
     */
    def importStack() {
        try {
            def descriptorFile = request.getFile('descriptorFile')
            def descriptorText = IOUtils.toString(descriptorFile.getInputStream(), "UTF-8")
            User user = accountService.getLoggedInUser()

            ServiceItem stack = importStackService.importStackDescriptor(descriptorText, user)

            Profile profile = profileService.findByUsername(session.username)
            render(view: "/serviceItem/create", model: [serviceItem: stack, profile: profile, act: "Create"])
        } catch (e) {
            log.error message(code: "sic.log.error.cannotParseStackFile", args: ["${e.getMessage()}"])
            flash.message = "sic.flash.msg.cannotParseStackFile"
            redirect(action: 'list')
            return
        }
    }

    def beforeInterceptor = {
        if (session.baseUrl == null) {
            session.baseUrl = request.scheme + "://" + request.serverName + ":" + request.serverPort + request.getContextPath()
        }
    }

    def afterInterceptor = { model, view ->
        if (view?.viewName == "/${controllerName}/create") {
            // No matter how the create view is shown (e.g., create, edit, or copy actions) it needs some global lists.
            // Populate the lists here so that they can't be missed.
            model['types'] = typesService.getAllTypes()
            model['categories'] = categoryService.getAllCategories()
            model['states'] = stateService.getAllStates()
            model['customFieldDefinitions'] = customFieldDefinitionService.list()
            model['intentDataTypes'] = IntentDataType.findAll()
            model['intentActions'] = IntentAction.findAll()
            model['contactTypes'] = ContactType.list()
        }
    }

    /**
     * TODO: this is used by the my listings page to populate the recent activities. It can be removed when that feature is migrated to the REST API
     */
    def adminView() {
        // render(view: "adminView")
    }

    /**
     * TODO: this is used by the my listings page. It can be removed when that feature is migrated to the REST API
     */
    def myListingView() {
        def activities

        try {
            // Restrict activities to the current user regardless of his role. This will only display that user's listings' activities.
            activities = serviceItemActivityService.getLatestActivity(['limit': 5, 'start': 0], session?.username, Constants.VIEW_USER)
            session.displayMarketplace = 'marketplace'
        } catch (Exception e) {
            def eMessage = e?.message
            log.error message(code: "sic.log.error.ExceptionMyListingView", args: ["${eMessage}", "${e}"])
            flash.message = "sic.flash.msg.ExceptionDefaultMessage"
            flash.defaultMsg = message(code: "sic.flash.default.msg.ExceptionErrorOccurred", args: [])
        }
        [activities: activities]
    }

    /**
     * TODO: this is used by the my listings page. It can be removed when that feature is migrated to the REST API
     */
    def getActiveListings() {
        HttpSession session = getSession()

        params.offset = params.start
        params.max = params.limit;
        params.order = params?.dir?.toLowerCase() ?: '';
        params.author_username = session.username
        params.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]
        if (params.sort == "types") params.sort = "types_title"
        if (params.sort == "state") params.sort = "state_title"
        def json

        String username = session?.getAttribute('username')
        String accessType = session?.getAttribute('accessType')

        try {
            def result = genericQueryService.serviceItems(params, username, accessType)
            json = [
                success: true,
                totalCount: result.total,
                data: result.items.collect { serviceItem ->
                    [
                        id: serviceItem.id,
                        title: serviceItem.title,
                        state: serviceItem.state?.title,
                        types: serviceItem.types?.title,
                        lastActivity: serviceItem.lastActivityString(),
                        isHidden: serviceItem.isHidden
                    ]
                }
            ]
        }
        catch (Exception e) {
            String eMessage = ExceptionUtils.getRootCauseMessage(e)
            log.error message(code: "sic.log.error.ExceptionGetActiveListings", args: ["${eMessage}", "${e}"])
            json = [
                success: false,
                totalCount: 0,
                msg: e.getMessage()
            ]
        }
        render (json as JSON)
    }


    def getInactiveListings() {
        HttpSession session = getSession()

        params.offset = params.start
        params.max = params.limit;
        params.order = params?.dir?.toLowerCase() ?: '';
        params.author_username = session.username
        if (params.sort == "types") params.sort = "types_title"
        String[] strArr = new String[3]
        params.approvalStatus = [Constants.APPROVAL_STATUSES["IN_PROGRESS"], Constants.APPROVAL_STATUSES["PENDING"], Constants.APPROVAL_STATUSES["REJECTED"]].toArray(strArr)

        String username = session?.getAttribute('username')
        String accessType = session?.getAttribute('accessType')

        def json
        try {
            def result = genericQueryService.serviceItems(params, username, accessType)

            json = [
                success: true,
                totalCount: result.total,
                data: result.items.collect { serviceItem ->
                    [
                        id: serviceItem.id,
                        title: serviceItem.title,
                        approvalStatus: serviceItem.approvalStatus,
                        types: serviceItem.types?.title,
                        lastActivity: serviceItem.lastActivityString(),
                        isHidden: serviceItem.isHidden
                    ]
                }
            ]
        }
        catch (Exception e) {
            String eMessage = ExceptionUtils.getRootCauseMessage(e)
            log.error message(code: "sic.log.error.ExceptionGetInactiveListings", args: ["${eMessage}", "${e}"])
            json = [
                success: false,
                totalCount: 0,
                msg: e.getMessage()
            ]
        }
        render (json as JSON)
    }

    private void getListFromIndex (boolean remote, SearchCriteria searchBean) {
        log.debug "getListFromIndex:"
        try {
            searchBean.aggregations = true
            def resultsList
            def result
            def modelData = [:]
            try {
                result = searchableService.searchListings(searchBean)
                resultsList = result?.searchResults
            }
            catch (SearchPhaseExecutionException e) {
                // Retry search with quoted search string
                try {
                    searchBean.queryString = "\"${searchBean.queryString}\""
                    result = searchableService.searchListings(searchBean)
                    resultsList = result?.searchResults
                }
                catch (Exception e1) {
                    throw e1
                }
            }

            def listContainerTemplate = 'widget_list_container'
            def listView = 'widgetList'

            modelData['affiliatedMarketplaceSearchSize'] = this.marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.AMP_SEARCH_RESULT_SIZE)

            if (remote) {
                render(template: "/serviceItem/${listContainerTemplate}",
                    model: [serviceItemList: resultsList, listSize: result?.total,
                        numShownResults: resultsList.size(), searchCriteria: searchBean])
            } else {
                def nuggets = searchNuggetService.nuggetize(searchBean)
                if (resultsList) {
                    log.debug "render list with ${resultsList.size()} items"
                    log.debug "SearchBean before search list ${searchBean}"
					def aggregations = aggregationsService.extractAggregationInfo(result)

                    modelData['serviceItemList'] = resultsList
                    modelData['aggregations'] = aggregations
                    modelData['nuggets'] = nuggets
                    modelData['listSize'] = result?.total
                    modelData['numShownResults'] = resultsList.size()
                    //to fix bug AML-1085
                    modelData['queryString'] = searchBean.queryString?.encodeAsHTML()
                    modelData['searchCriteria'] = searchBean

					modelData['typeAggregations'] =    JSONUtil.getListFromDomainObject(aggregations.types) as JSON
					modelData['categoriesAggregations'] =  JSONUtil.getListFromDomainObject(aggregations.categories) as JSON
					modelData['domainAggregations'] = JSONUtil.getListFromDomainObject(aggregations.domain) as JSON
					modelData['agenciesAggregations'] = JSONUtil.getListFromDomainObject(aggregations.agencies) as JSON
                    // Changed to fix bug AML-1085
                    render(view: "/serviceItem/${listView}", model: modelData)
                } else {

                    def searchText = message(code: "sic.searchText", args: [])
                    render(view: "/serviceItem/${listView}",
                        model: [listSize: 0, searchCriteria: searchBean, noResultsText: searchText, numShownResults: resultsList.size(),
                            queryString: searchBean.queryString?.encodeAsHTML(), nuggets: nuggets,
                            affiliatedMarketplaceSearchSize: this.marketplaceApplicationConfigurationService.valueOf(MarketplaceApplicationSetting.AMP_SEARCH_RESULT_SIZE)])
                }
            }
        }
        catch (Exception e) {
            log.error message(code: "sic.log.error.exceptionOccurred", args: ["${e.getMessage()}"])
            log.error('getListFromIndex:', e)
            handleNonJSONException(e, "sic.log.error.cannotParseSearchString", [searchBean.queryString])
        }
        catch (Exception e) {
            log.error message(code: "sic.log.error.exceptionOccurred", args: ["${e.getMessage()}"])
            log.error('getListFromIndex:', e)
            handleNonJSONException(e)
        }
    }

    protected handleNonJSONException(Exception e, String message, List args) {
        flash.message = message ?: "sic.flash.msg.exceptionOccurred"
        flash.args = args ?: [e?.getMessage()]
        def requestReferer = request.getHeader("referer")
        def requestServletPath = request.getServletPath()
        def grailsUrl = "/grails"
        def requestServletPathClean = requestServletPath?.substring(grailsUrl.length(), requestServletPath?.lastIndexOf(".dispatch"))
        if (requestReferer) {
            if (!requestReferer.contains(requestServletPathClean)) {
                redirect(url: requestReferer)
                return
            }
        }

        if (requestServletPathClean?.contains("/serviceItem/list")) {
            forward(action: 'index')
            return
        } else {
            redirect(action: 'list')
            return
        }
    }

    def handleNonJSONException(Exception e) {
        handleNonJSONException(e, null, null)
    }
}
