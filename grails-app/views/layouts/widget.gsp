<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="marketplace.*"%>
<%@ page import="utils.*"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <meta http-equiv="PRAGMA" content="NO-CACHE" />

    <title><g:layoutTitle default="Marketplace" /></title>

    <p:favicon/>

    <!-- ** CSS ** -->
    <!-- base library -->
    <p:css id='theme' name='${marketplaceTheme.defaultCssPath()}'
           absolute='true' />
    <p:css id='theme-bootstrap' name='${marketplaceTheme.defaultThemeBasePath()}css/bootstrap.css' absolute='true'/>
    <p:css id='theme-dataTables' name='${marketplaceTheme.defaultThemeBasePath()}css/dataTables.css' absolute='true'/>


    <!-- ** JavaScript ** -->
    <g:javascript library="../vendor/modernizr" />
    <g:javascript src="../config.js" />
    <p:javascript src="marketplace-js-bundle" />

    <script type="text/javascript">
        jQuery('html').addClass('widget');
    </script>

    <g:layoutHead />

    <%
        def tooltips = [:]
        tooltips["ompHeaderLogo"] = g.message(code: "tooltip.ompHeaderLogo")
    %>
    <script type="text/javascript">
        owfdojo.config.dojoBlankHtmlUrl =
            Marketplace.context + '/vendor/dojo-1.2.3-windowname-only/dojo/resources/blank.html';
        Ozone.eventing.Widget.widgetRelayURL =  '/vendor/eventing/rpc_relay.uncompressed.html';

        jQuery(function() {
            Marketplace.viewPortInit(); //Comes from "config" library

            jQuery(document).ready(function() {
                Marketplace.widget.addWidgetParameter();
                Marketplace.setCustomBannerTooltip('widget_header_logo',
                    Marketplace.headerLogoTooltip, true);
            });
        });

    </script>
</head>

<body class="widget-content">
    <script src="${request.contextPath}/vendor/requirejs/requirejs-2.1.9.js"></script>
    <script src="${request.contextPath}/js/requirejsConfig.js"></script>
    <script src="${request.contextPath}/js/RouterMain.js"></script>
    <div id="omp_content_frame" class="omp_widget">
            <g:render template="/theme/marketplace/content"/>
    </div>
</body>
</html>
