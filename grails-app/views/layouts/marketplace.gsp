<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="grails.converters.JSON; marketplace.*" %>
<%@ page import="utils.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
    String query = request.getQueryString();

    if (query && query.toLowerCase().contains("csp-debug=true")) {
        response.addHeader("Content-Security-Policy-Report-Only",
                           "default-src 'self';")
    }
 %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
        <meta http-equiv="PRAGMA" content="NO-CACHE" />

        <meta name="pageId" content="${controllerName}.${actionName}" />

        <title><g:layoutTitle default="${g.message(code: 'default.title')}"/></title>

        %{--<p:favicon/>--}%

        <!-- ** CSS ** -->
        <!-- base library -->
        %{--<p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='true'/>--}%
        %{--<p:css id='theme-bootstrap' name='${marketplaceTheme.defaultThemeBasePath()}css/bootstrap.css' absolute='true'/>--}%
        %{--<p:css id='theme-dataTables' name='${marketplaceTheme.defaultThemeBasePath()}css/dataTables.css' absolute='true'/>--}%
        <marketplaceTheme:themeStylesheet/>
        <marketplaceTheme:bootstrapStylesheet/>
        <marketplaceTheme:dataTablesStylesheet/>

        <!-- ** JavaScript ** -->
        <g:javascript src="../static/vendor/modernizr.js" />
        <g:javascript src="../config.js"/>
        <g:javascript src="../static/js/marketplace-js-bundle.js"/>
        <g:javascript src="../static/vendor/owf-widget-min.js" />

        <myui:bannerBeanCSS/>
        <myui:bannerBeanJS/>

        <script type="text/javascript">
                owfdojo.config.dojoBlankHtmlUrl =
                    Marketplace.context +
                        '/vendor/dojo-1.2.3-windowname-only/dojo/resources/blank.html';
                Ozone.eventing.Widget.widgetRelayURL =
                    '/vendor/eventing/rpc_relay.uncompressed.html';
            <mpwidget:doInWidget/>

            jQuery(function () {
                jQuery('html').addClass('website');
                if (Marketplace.widget.isWidget()) {
                    Marketplace.widget.addWidgetParameter();
                }

                // Do not do redirection in AppMall since it is not using widget.gsp template for MP Widget (MS)
                <mpwidget:skipInAmlWidget>
                    // If this template was opened from OWF widget, redirect the request to the Marketplace Widget home page
                    if (Marketplace.widget.isWidget()) {
                        var newUrl
                        if (window.location) {
                            newUrl = Marketplace.widget.addWidgetParameterToUrl(window.location.href)
                        }
                        else {
                            newUrl = '<g:createLink controller="serviceItem" action="list" params="[widget: true, accessType: 'Developer', themeName: params.themeName, themeContrast: params.themeContrast, themeFontSize: params.themeFontSize]"/>';
                        }
                        window.location = newUrl;
                    }
                </mpwidget:skipInAmlWidget>

                Marketplace.viewPortInit(); //Comes from "config" library
            });

        </script>

        <g:layoutHead/>

        <!-- ENABLE OPEN SEARCH DISCOVERY -->
        <%
            def openSearchPluginTitleMessage = "${openSearchTitleMessage}".encodeAsHTML()
            if(StringUtils.isBlank(openSearchPluginTitleMessage)){
                openSearchPluginTitleMessage = g.message(code:'search.opensearch.main.title', encodeAs: 'HTML')
            }
        %>

        <link rel="search"
              href="${createLink(controller:'search',action:'openSearchDescriptor')}"
              type="application/opensearchdescription+xml"
              title="${openSearchPluginTitleMessage}"/>
    </head>

    <body class="marketplaceBody">
        <script src="${request.contextPath}/static/vendor/requirejs/requirejs-2.1.9.js"></script>
        <script src="${request.contextPath}/static/js/requirejsConfig.js"></script>
        <script src="${request.contextPath}/static/js/RouterMain.js"></script>
        <g:render template="/theme/marketplace/content"/>
    </body>
</html>
