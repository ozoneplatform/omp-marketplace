package marketplace

import grails.config.Config

// import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.util.Holders

import ozone.decorator.JSONDecoratorService

import static ozone.marketplace.enums.MarketplaceApplicationSetting.*
import javax.annotation.PostConstruct

class MyUITagLib {

    static namespace = "myui"

    Config config = Holders.config

    CategoryService categoryService

    ItemCommentService itemCommentService

    ServiceItemService serviceItemService

    SearchNuggetService searchNuggetService

    AccountService accountService

    def groovyPagesTemplateEngine

    JSONDecoratorService JSONDecoratorService

    CustomHeaderFooterService customHeaderFooterService

    def bannerBean

    @PostConstruct
    protected void init() {
        bannerBean = JSONDecoratorService.getBean("mp_BannerBeanService")
    }


    def filterDisplay = { attr ->
        def filterMap = attr.filterMap
        SearchNuggets nuggets = attr.nuggets
        filterMap.each { mapEntry ->
            def filterItem = mapEntry.key
            def count = mapEntry.value
            def titleDisplay = filterItem?.titleDisplay()?.encodeAsHTML()
            out << '<div class="nav_item_text">'
            if (nuggets.hasFilter(attr.nuggetName, filterItem.id)) {
                out << "<span class='filter_selected'>${titleDisplay} <span class='nav_item_count'> (${count})</span></span>"
            } else {
                out << "<a href='${createLink(controller: 'search', action: 'filter' + attr.filterKind, id: filterItem.id)}' title='${filterItem?.title?.encodeAsHTML()}'>"
                out << "<span>${titleDisplay}<span class='nav_item_count'> (${count})</span></span>"
                out << "</a>"
            }
            out << '</div>'
        }
    }

    def ratingFilterDisplay = { attr ->
        def filterMap = attr.filterMap
        def nuggets = attr.nuggets
        filterMap.each { mapEntry ->
            def rating = mapEntry.key
            def label = g.message(code: "filter.rating." + rating, encodeAs: 'HTML')
            def count = mapEntry.value
            out << '<div class="nav_item_text">'
            if (nuggets.hasFilter(attr.nuggetName, rating as String)) {
                //if (rating == nuggets as int) {
                out << "<span class='filter_selected'>${label}<span class='nav_item_count'> (${count})</span></span>"
            } else {
                out << "<a href='${createLink(controller: 'search', action: 'filterRate', id: rating)}' title='${label}'>"
                out << "<span>${label}<span class='nav_item_count'> (${count})</span></span>"
                out << "</a>"
            }
            out << '</div>'
        }
    }

    def displayAny = { attr ->
        out << '<div class="nav_item_text">'
        def label = attr.label

        // TODO Refactor so that plurals aren't determined by checking hardcoded values
        if ((attr.label.equals("Type")) || (attr.label.equals("State")) || (attr.label.equals("Rating")) || (attr.label.equals("Domain"))) {
            label = label + 's'
        } else if (attr.label.equals("Category")) {
            label = "Categories"
        } else if(attr.label.equals("Company")){
            label = "Companies"
        }

        def nuggets = attr.nuggets
        if (nuggets.hasFilter(attr.nuggetName)) {
            out << "<a href='${createLink(controller: 'search', action: 'filter' + attr.filterKind)}' title='All'>"
            out << 'All ' + label
            out << "</a>"
        } else {
            out << "<span class='filter_selected'>All ${label}</span>"
        }
        out << '</div>'

    }

    /**
     * Only display affiliated marketplace search results if nugget map
     * contains no parameters or a single 'query' parameter.
     */
    def shouldSearchAffiliatedMarketplaces = { attrs, body ->
        if (AffiliatedMarketplace.countByActive(1) > 0) {
            SearchNuggets nuggets = searchNuggetService.nuggetize(session.searchBean)
            if (nuggets.justQuery || !nuggets.hasNuggets()) {
                out << body()
            }
        }
    }

    /**
     * Only display affiliated marketplace search results if nugget map
     * contains no parameters or a single 'query' parameter.
     */
    def doIfNotSearchResutsPage = { attrs, body ->
        def nuggets = searchNuggetService.nuggetize(session.searchBean)
        if (nuggets.isEmpty()) {
            out << body()
        }
    }


    def itemDescBlock = { attrs, body ->
        def description = ""
        def expandable = false
        def readMore = false
        def itemId = ""

        int truncateAt = Integer.parseInt(attrs.truncateAt)
        int lineCount = Integer.parseInt(attrs?.lineCount ?: '0')
        def item = attrs.item
        if (item) {
            description = Helper.shorten(item.description, truncateAt, "...", lineCount)
            if (item.description?.size() > description.size()) {
                itemId = item.id
                readMore = attrs?.readMore?.toBoolean()
                if (!readMore) {
                    expandable = true
                }
            }
        }
        out << render(template: "/itemDescBlock", model: [itemId: itemId, desc: description, expandable: expandable, collapsable: false, readMore: readMore])
    }

    def itemCategoriesBlock = { attrs, body ->
        def categoriesShortened = ""
        def seeAll = false
        def itemId = ""
        def linkText = "more"

        int truncateAt = Integer.parseInt(attrs.truncateAt)
        def item = attrs.item
        if (item) {
            def categories = item?.categories?.join(", ")
            if (categories?.size() > truncateAt) {
                // when truncating, account for length of link text that will be appended
                categoriesShortened = Helper.shorten(categories, (truncateAt - linkText.size()), "...", 0)
                if (categories?.size() > categoriesShortened.size()) {
                    itemId = item.id
                    seeAll = true
                }
            } else {
                categoriesShortened = categories
            }
        }
        out << render(template: "/itemCategoriesBlock", model: [itemId: itemId, categories: categoriesShortened, seeAll: seeAll, linkText: linkText])
    }

    def yourRating = { attrs, body ->
        out << body(itemCommentService.yourRating(session.profileID, attrs.serviceItemID))
    }

    def calcOrder = { attrs, body ->
        def dflt = "desc"
        if (attrs?.sort == attrs?.field) {
            if ("desc" == attrs?.order) {
                dflt = "asc"
            }
        }
        out << dflt
    }

    def showOrder = { attrs, body ->
        def dflt = ""
        if (attrs?.sort == attrs?.field) {
            dflt = p.image(src: 'skin/sorted_desc.gif')
            if ("asc" == attrs?.order) {
                dflt = p.image(src: 'skin/sorted_asc.gif')
            }
        }
        out << dflt
    }

    def classOrder = { attrs, body ->
        def dflt = ""
        if (attrs?.sort == attrs?.field) {
            dflt = "selected"
        }
        out << dflt
    }

    def repeat = { attrs, body ->
        def i = Integer.valueOf(attrs["times"])
        def current = 0
        i.times {
            // pass the current iteration as the groovy default arg "it"
            // then pass the result to "out" to send it to the view
            out << body(++current)
        }
    }

    def carousel = { attrs, body ->
        out << "<div class=\"carousel carousel-visible ${attrs?.class ?: ''}\">"
        out << "<div class=\"border-top\"></div>"
        out << "<ul id=\"${attrs['id']}\">"
        def templates = attrs["content"]
        templates.each {
            out << "<li><div class=\"carousel-content\">" << g.render(template: "${it}") << "</div></li>"
        }
        out << "</ul>"
        out << "<div class=\"border-bottom\"></div>"
        out << "</div>"

    }

    def itemIcon = { attrs, body ->
        ServiceItem si = attrs.item
        def css = attrs?.clss ?: ''
        def height = attrs?.height ?: 128
        def type = si?.types?.title?.encodeAsHTML()
        def width = attrs?.width ?: 128
        def alt = si?.title?.encodeAsHTML() ?: 'Service Item'
        def url = null
        def contentType = null
        if (attrs.getSmallUrl && si.imageSmallUrl) {
            url = si.imageSmallUrl
        } else if (si.imageMediumUrl) {
            url = si.imageMediumUrl
        } else {
            def serviceItemIconImage = serviceItemService.getServiceItemIconImage([serviceItem: si, type: type, contextPath: request.contextPath,
                getSmallUrl: attrs.getSmallUrl])
            url = serviceItemIconImage.url
            contentType = serviceItemIconImage.contentType
        }

        def imgServerPng = url?.toLowerCase().endsWith(".png") || contentType?.equalsIgnoreCase("image/png")
        out << "<img class=\"${css}\" height=\"${height}\" width=\"${width}\" alt=\"${alt}\" server_png=\"${imgServerPng}\" src=\"${url?.encodeAsHTML()}\" />"
    }

    def serviceItemBadge = { attrs, body ->

        if (attrs.template) {
            out << render(template: attrs.template, model: attrs.model)
        } else {
            out << render(template: "/serviceItem/badge/item_badge_with_actions", model: attrs.model)
        }
    }

    def bannerBeanCSS = { attr ->
        out << includeBannerBeanCSS()
    }

    def bannerBeanJS = { attr ->
        out << includeBannerBeanJS()
    }


    def bannerBeanNorth = { attr ->
        if (!customHeaderFooterService.hasCustomHeaderOrFooter() && bannerBean?.enableBanner) {
            if (!bannerBean.northBanner)
                bannerBean.northBanner = getBannerText(Constants.NORTH_BANNER, bannerBean.getBannerBeanUINorthAsStream())
            out << bannerBean.northBanner
        } else
            out << getBannerText(Constants.NORTH_BANNER)
    }

    def bannerBeanSouth = { attr ->
        if (!customHeaderFooterService.hasCustomHeaderOrFooter() && bannerBean?.enableBanner) {
            if (!bannerBean.southBanner)
                bannerBean.southBanner = getBannerText(Constants.SOUTH_BANNER, bannerBean.getBannerBeanUISouthAsStream())
            out << bannerBean.southBanner
        } else
            out << getBannerText(Constants.SOUTH_BANNER)
    }

    def bannersAvailable = {
        //if either banner has contents, return true
        out << bannerBean.hasBanner()
    }

    private String includeBannerBeanCSS() {

        if (customHeaderFooterService.hasCustomHeaderOrFooter()) {

            def headerHeight = customHeaderFooterService.customHeaderHeight
            def footerHeight = customHeaderFooterService.customFooterHeight
            def cssIncludesFromConfig = customHeaderFooterService.cssImportsAsList
            def cssLinkList = ""
            cssIncludesFromConfig.each {
                cssLinkList += "<link rel='stylesheet' type='text/css' href='${it}'/>"
            }

            return """
            <style type="text/css">
                .hasBanners #header { top: ${headerHeight}px; }
                .hasBanners #footer { bottom: ${footerHeight}px; }

                /* Alternative to host everything that is not part of the custom header/footer */
                .hasBanners .marketplace-container {
                    top: ${headerHeight}px;
                    bottom: ${footerHeight}px;
                    position: fixed;
                    width: 100%;
                }
            </style>
            ${cssLinkList}
            """
        }
        /*
                if we get here, then the header/footer information is
                not in the dbconfig and we do things the old way via the banner bean
         */

        if (bannerBean) {
            def cssContents = bannerBean.getCssContents()
            if (cssContents) {
                StringBuffer cssContentsBuffer = new StringBuffer()
                for (String cssContent : cssContents) {
                    cssContentsBuffer.append(cssContent)
                    cssContentsBuffer.append("\n")
                }
                return cssContentsBuffer.toString()
            }
        }
        return null
    }

    private String includeBannerBeanJS() {

        if (customHeaderFooterService.hasCustomHeaderOrFooter()) {
            String jQueryLoads = "", jsLinkList = ""
            def jsIncludesFromConfig = customHeaderFooterService.jsImportsAsList

            if (customHeaderFooterService.hasCustomHeader())
                jQueryLoads += "jQuery('#northBanner').load('${customHeaderFooterService.customHeaderUrl}');"
            if (customHeaderFooterService.hasCustomFooter())
                jQueryLoads += "jQuery('#southBanner').load('${customHeaderFooterService.customFooterUrl}');"

            jsIncludesFromConfig.each {
                jsLinkList += "<script type='text/javascript' src='${it}'></script>"
            }

            return """
            <script type="text/javascript">
                jQuery(document).ready(function() {
                    if (!Marketplace.widget.isWidget()) {
                    ${jQueryLoads}
                        jQuery('.marketplaceBody').addClass('hasBanners');
                        jQuery('#northBanner').show();
                        jQuery('#southBanner').show();
                    }
                });
            </script>
            ${jsLinkList}
            """
        }

        /*
                if we get here, then the header/footer information is
                not in the config and we do things the old way via the banner bean
         */
        if (bannerBean) {
            def jsContents = bannerBean.getJsContents()
            if (jsContents) {
                StringBuffer jsContentBuffer = new StringBuffer()
                for (String jsContent : jsContents) {
                    jsContentBuffer.append(jsContent)
                    jsContentBuffer.append("\n")
                }
                return jsContentBuffer.toString()
            }
        }
        return null
    }


    private String getBannerText(String bannerId, InputStream inputStream = null) {
        def outputVal = new StringBuffer()
        outputVal.append("<div id=\"${bannerId}\" >")
        if (!inputStream)
            return outputVal.append("</div>").toString()

        try {
            def output = new StringWriter()
            groovyPagesTemplateEngine.createTemplate(inputStream).make().writeTo(output)
            outputVal.append(output.toString()).append("</div>")
        } catch (IOException ex) {
            log.info(ex, ex)
        }
        return outputVal.toString()
    }

}
