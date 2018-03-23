<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<%@ page import="grails.converters.*" %>
<%
    def flashClazz = "message errorText"
    if (flash?.success) { flashClazz = "message successText" }

    def searchParams = params as JSON
    
    def tagSearch = params['queryString']?.contains('tags.tag.title')
    def tagTitle = request.getParameter('queryString')?.replace('tags.tag.title:', '')

    // Use view preference from session if not found in params
    params.searchResultUiViewMode = params.searchResultUiViewMode ?: session.searchResultUiViewMode
%>


%{--<owf:styles>--}%
%{--</owf:styles>--}%
%{--<owf:scripts>--}%
%{--</owf:scripts>--}%

<g:if test="${flash.message}">
  <!-- Message -->
  <div class="${flashClazz}"><g:message code="${flash.message}" args="${flash.args}" default="${flash.default}" encodeAs="HTML"  encodeAs="HTML" /></div>
</g:if>

<g:if test="${0 == listSize}">
    <div class="message no-search-results"><g:message code="blurb.noItems" encodeAs="HTML" /></div>
</g:if>
<g:else>

    <!-- Paging and Sorting-->
    <div class="widget_paging_sorting_listing bootstrap-active">
                
        <g:if test="${tagSearch}">
            <div class="search_results_title_tag">${tagTitle}        
        </g:if>
        <g:else>
            <div class="search_results_title">
            Search Results 
        </g:else>
        <span id="loadedStatus" class="franchise_search_result_count">${numShownResults.encodeAsHTML()} of ${listSize.encodeAsHTML()}</span>

        <p id="numShownResults" style="display: none;">${numShownResults.encodeAsHTML()}</p>
        <p id="numTotalResults" style="display: none;">${listSize.encodeAsHTML()}</p>   </div>

        <div style="float: right; margin-top:5px">
             <div class="search_results_sort search_results_label_text">sort</div>

             <div id="sortByRating"    class="search_results_sort search_results_sort_by_rating">&nbsp</div>
             <div id="sortByAlpha"     class="search_results_sort search_results_sort_by_alpha">&nbsp</div>
             <div id="sortByCalendar"  class="search_results_sort search_results_sort_by_calendar">&nbsp</div>

             <div class="search_results_sort search_results_label_text">view</div>

             <div id="widget_listing_list_button" class="inactive"></div>
             <div id="widget_listing_grid_button" class="inactive"></div>

        </div>
        <div class="clear"></div>

    </div>


    <div id="listView" class="search_result_list_view"  style="display: none;">
        <g:render template="/serviceItem/searchResults/widget_list_view" var='serviceItemList' bean="${serviceItemList}" />
    </div>

    <div id="gridView" style="display: none;">
        <g:render template="/serviceItem/searchResults/widget_grid_view" var='serviceItemList' bean="${serviceItemList}" />
    </div>


    <g:if test="${numShownResults != listSize}">
        <div id="loadMore" class="load_more_link" >Show more</div>
    </g:if>

</g:else>

<script type='text/javascript'>
    var affiliatedMarketplaceSearchParams = ${raw((params as JSON).toString()) ?:  'null'};

    var affiliatedMarketplaceSearchSize = Marketplace.affiliatedSearchSize;
    var sortBy          = affiliatedMarketplaceSearchParams.sort;
    var sortOrder       = affiliatedMarketplaceSearchParams.order;
    var gridOrListView  = affiliatedMarketplaceSearchParams.searchResultUiViewMode;
    var isUserAdmin     = Marketplace.user.isAdmin;
    var username        = Marketplace.user.username;
</script>


<script src="${request.contextPath}/static/js/affiliatedSearch/affiliatedSearch-main.js"></script>
