<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="marketplace.*"%>
<%
    def flashClazz = "message errorText"
    if (flash?.success) { flashClazz = "message successText" }
    def seeAllArrowLink = p.imageLink(src:"seeAllArrow.png").replaceAll("'", "")
%>


<g:if test="${flash.message}">
    <!-- Message -->
    <div class="${flashClazz}">
        <g:message code="${flash.message}" args="${flash.args}"
            default="${flash.default}" encodeAs="HTML" />
    </div>
</g:if>


<div class="landingPageRows" style="display: none;" id="landingPageRowSpan">

    <div id="landingPageTopCarousel">
        <myui:carousel class="multi-page" id="my-carousel" content="${carouselContent}" />
    </div>

    <h1 class="highestRated">Highest Rated</h1>
    <div class="franchise_discover_see_more_text">
        <g:link action="search" controller="serviceItem"
            params="[sort:'avgRate',order:'desc',offset:0,accessType:session.accessType,status_any_checkbox:'on']">
            <span class="see-all-text">see all</span>
        </g:link>
    </div>
    <div class="listings_line"></div>
    <g:render template="/serviceItem/widget_shoppe_items" var="items" bean="${best}" model="[size:'6', name:'highestRatedCarousel']" />
    <g:render template="/serviceItem/widget_shoppe_items" var="items" bean="${best}" model="[size:'3', name:'miniHighestRatedCarousel']" />

    <h1 class="newArrivals">New Arrivals</h1>
    <div class="franchise_discover_see_more_text">
        <g:link action="search" controller="serviceItem"
                params="[sort:'approvedDate',order:'desc',offset:0,accessType:session.accessType,status_any_checkbox: 'on']">
            <span class="see-all-text">see all</span>
        </g:link>
    </div>
    <div class="listings_line"></div>
    <g:render   template="/serviceItem/widget_shoppe_items" var="items" bean="${recentlyAdded}" model="[size:'6', name:'recentlyAddedCarousel']" />
    <g:render   template="/serviceItem/widget_shoppe_items" var="items" bean="${recentlyAdded}" model="[size:'3', name:'miniRecentlyAddedCarousel']" />
</div>

<p:javascript src='service-item-js-bundle' />
<script type="text/javascript">


    $(document).ready(function() {
        configureCarousels();
    });
</script>
