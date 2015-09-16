package marketplace

import grails.util.Holders
import marketplace.search.SearchCriteria
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.exception.ExceptionUtils
import org.apache.commons.lang.time.FastDateFormat
import org.elasticsearch.action.search.SearchPhaseExecutionException
import org.hibernate.FlushMode
import ozone.marketplace.domain.ValidationException
import ozone.marketplace.enums.MarketplaceApplicationSetting
import ozone.utils.User
import grails.converters.JSON

import javax.servlet.http.HttpServletResponse

class ServiceItemController extends BaseMarketplaceRestController {
    def config = Holders.config
    def sessionFactory

    def searchableService
    def searchNuggetService
    def accountService
    def serviceItemService
    def imagesService
    def profileService
    def genericQueryService
    def itemCommentService
    def serviceItemActivityService
    def typesService
    def categoryService
    def stateService
    def customFieldDefinitionService
    def relationshipService
    def importStackService
    def facetsService

    static SLASHES_DATE_FORMAT = 'MM/dd/yyyy'

    static FastDateFormat dateFormatter = FastDateFormat.getInstance(SLASHES_DATE_FORMAT)


    def index = {
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

        if (session.accessType == Constants.VIEW_USER)
            redirect(action: "shoppe", params: params)
        else
            redirect(action:'adminView', params:params)
    }

    def getOwfCompatibleItems = {
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

    def getListFromIndexAsModel = { searchBean ->
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

    def getListFromIndexAsModel2 = { searchBean ->
        def result = searchableService.searchListings(searchBean)
        def model = [:]
        def serviceItemList = result.searchResults
        model.put("serviceItemList", serviceItemList)
        model.put("listSize", result?.total)
        model.put("params", params)
        return model
    }

    /**
     * Advanced search method to return serviceItem models as JSON objects
     */
    def getServiceItemsAsJSON = {
        log.debug "getServiceItemsAsJSON: params = ${params}"
        if (!params.max) params.max = 10
        if (!params.method_name) params.method_name = 'getServiceItemsAsJSON'
        params.accessType = Constants.VIEW_USER
        def result
        def model = []
        int total
        try {
            if (Boolean.valueOf(params.useIndex)) {
                prepDefaultParamsForIndex(params)
                // Create new Search Bean
                session.searchBean = new SearchCriteria(params)
                result = getListFromIndexAsModel2(session.searchBean)
            } else {
                result = genericQueryService.serviceItems(params)
            }
            result['serviceItemList']?.collect { model.add(it.asJSON()) }
            total = result['listSize']
            renderResult(model, total, HttpServletResponse.SC_OK)
        }
        catch (Exception e) {
            handleException(e, params.method_name)
        }
    }

    def shoppe = {

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
            filterSearchBean.facets = true
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
            params.sort = 'approvedDate'
            def recentlyAdded = searchableService.searchListings(new SearchCriteria(params))
            log.debug "${params.max} ${recentlyAdded.searchResults.size()}"

            //HIGHEST RATED LISTINGS
            params.sort = 'avgRate'
            def higestRated = searchableService.searchListings(new SearchCriteria(params))

            def facets = facetsService.extractFacetInfo(filterSearch);

            def modelData = [
                recentlyAdded: recentlyAdded.searchResults,
                best: higestRated.searchResults]

            modelData['carouselContent'] = ["carousel_content/page1"]
            if (higestRated?.searchResults) modelData['carouselContent'] << "carousel_content/page2"

            //video page for carousel can be removed
//            modelData['carouselContent'] << "carousel_content/page4"

            modelData['typeFacets'] =    JSONUtil.getListFromDomainObject(facets.types) as JSON
            modelData['categoriesFacets'] =  JSONUtil.getListFromDomainObject(facets.categories) as JSON
            modelData['domainFacets'] = JSONUtil.getListFromDomainObject(facets.domain) as JSON
            modelData['agenciesFacets'] = JSONUtil.getListFromDomainObject(facets.agencies) as JSON


            render(view: 'widgetShoppe', model: modelData)

        } catch (Exception e) {
            log.error message(code: "sic.log.error.exceptionOccurred", args: ["${e.getMessage()}"])
            log.error('shoppe:', e)
            handleNonJSONException(e)
        }
    }



    def list = {
        log.debug "list:"
        if ((session.modelMap) && (session.modelMap['flashMessage'])) {
            flash.message = session.modelMap['flashMessage']
            session.modelMap = null
        }
        prepDefaultParamsForIndex(params)
        session.searchBean = new SearchCriteria(params)
        getListFromIndex(false, session.searchBean)
    }

    def search = {
        log.debug "search: params = ${params}"

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

    def lastSearch = {
        prepDefaultParamsForIndex(params)
        SearchCriteria searchCriteria = session.searchBean
        if (searchCriteria) {
            searchCriteria.updateParams(params)
        } else {
            searchCriteria = new SearchCriteria(params)
        }
        getListFromIndex(false, searchCriteria)
    }

    def relist = {
        prepDefaultParamsForIndex(params)
        if (session.searchBean) {
            session.searchBean.updateBean(params)
        } else {
            session.searchBean = new SearchCriteria(params)
        }
        getListFromIndex(true, session.searchBean)
    }

    /*
     * Retrieve the detail-listing of a service item as a JSON object
     */
    def getDetailListingForServiceItemAsJSON = {
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
     */
    def importStack = {
        try {
            def descriptorFile = request.getFile('descriptorFile')
            def descriptorText = org.apache.commons.io.IOUtils.toString(descriptorFile.getInputStream(), "UTF-8")
            User user = accountService.getLoggedInUser()

            ServiceItem stack = importStackService.importStackDescriptor(descriptorText, user)

            Profile profile = profileService.findByUsername(session.username)
            render(view: "/serviceItem/create", model: [serviceItem: stack, profile: profile, act: "Create"])
        } catch (e) {
            log.error message(code: "sic.log.error.cannotParseStackFile", args: ["${e.getMessage()}"])
            flash.message = "sic.flash.msg.cannotParseStackFile"
            redirect(action: 'list')
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

    def adminView = {
        log.debug "adminView: params = ${params}"
        []
    }

    //TODO: this is used by the my listings page to populate the recent activities. It can be removed when that feature is migrated to the REST API
    def myListingView = {

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

    //TODO: this is used by the my listings page. It can be removed when that feature is migrated to the REST API
    def getActiveListings = {
        params.offset = params.start
        params.max = params.limit;
        params.order = params?.dir?.toLowerCase() ?: '';
        params.author_username = session.username
        params.approvalStatus = Constants.APPROVAL_STATUSES["APPROVED"]
        if (params.sort == "types") params.sort = "types_title"
        if (params.sort == "state") params.sort = "state_title"
        def json

        try {
            def items = genericQueryService.serviceItems(params)['serviceItemList']
            json = [
                success: true,
                totalCount: items.totalCount,
                data: items.collect { si ->
                    [
                        id: si.id,
                        title: si.title,
                        state: si.state?.title,
                        types: si.types?.title,
                        lastActivity: si.lastActivityString(),
                        isHidden: si.isHidden
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

    //TODO: this is used by the my listings page. It can be removed when that feature is migrated to the REST API
    def getInactiveListings = {
        params.offset = params.start
        params.max = params.limit;
        params.order = params?.dir?.toLowerCase() ?: '';
        params.author_username = session.username
        if (params.sort == "types") params.sort = "types_title"
        String[] strArr = new String[3]
        params.approvalStatus = [Constants.APPROVAL_STATUSES["IN_PROGRESS"], Constants.APPROVAL_STATUSES["PENDING"], Constants.APPROVAL_STATUSES["REJECTED"]].toArray(strArr)
        def json
        try {
            def items = genericQueryService.serviceItems(params)['serviceItemList']

            json = [
                success: true,
                totalCount: items.totalCount,
                data: items.collect { si ->
                    [
                        id: si.id,
                        title: si.title,
                        approvalStatus: si.approvalStatus,
                        types: si.types?.title,
                        lastActivity: si.lastActivityString(),
                        isHidden: si.isHidden
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

    def processSort = {

        // Title
        session.sortTitle = "desc"
        if ("title" == params.sort && "desc" == params.order) {
            session.sortTitle = "asc"
        }

        // Create Date
        session.sortCreatedDate = "desc"
        if ("createdDate" == params.sort && "desc" == params.order) {
            session.sortCreatedDate = "asc"
        }

        // Average Rating
        session.sortRating = "desc"
        if ("avgRate" == params.sort && "desc" == params.order) {
            session.sortRating = "asc"
        }

        // Type
        session.sortTypes = "desc"
        if ("types" == params.sort && "desc" == params.order) {
            session.sortTypes = "asc"
        }

        // State
        session.sortState = "desc"
        if ("state" == params.sort && "desc" == params.order) {
            session.sortState = "asc"
        }

        // Approval Status
        session.sortApprovalStatus = "desc"
        if ("approvalStatus" == params.sort && "desc" == params.order) {
            session.sortApprovalStatus = "asc"
        }

    }

    private void getListFromIndex (boolean remote, def searchBean) {
        log.debug "getListFromIndex:"
        try {
            searchBean.facets = true
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
					def facets = facetsService.extractFacetInfo(result)

                    modelData['serviceItemList'] = resultsList
                    modelData['facets'] = facets
                    modelData['nuggets'] = nuggets
                    modelData['listSize'] = result?.total
                    modelData['numShownResults'] = resultsList.size()
                    //to fix bug AML-1085
                    modelData['queryString'] = searchBean.queryString?.encodeAsHTML()
                    modelData['searchCriteria'] = searchBean

					modelData['typeFacets'] =    JSONUtil.getListFromDomainObject(facets.types) as JSON
					modelData['categoriesFacets'] =  JSONUtil.getListFromDomainObject(facets.categories) as JSON
					modelData['domainFacets'] = JSONUtil.getListFromDomainObject(facets.domain) as JSON
					modelData['agenciesFacets'] = JSONUtil.getListFromDomainObject(facets.agencies) as JSON
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

    public handleNonJSONException(Exception e) {
        handleNonJSONException(e, null, null)
    }

    def beforeInterceptor = {
        if(session.baseUrl == null)
            session.baseUrl = request.scheme + "://" + request.serverName + ":" + request.serverPort + request.getContextPath()
    }
}
