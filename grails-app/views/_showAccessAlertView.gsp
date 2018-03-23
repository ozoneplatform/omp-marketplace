<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
        <meta http-equiv="PRAGMA" content="NO-CACHE" />
        %{--<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--}%

        <meta name="pageId" content="${controllerName}.${actionName}" />

        <title><g:message code="default.title" encodeAs="HTML" /></title>

        <!-- ** CSS ** -->
        <!-- base library -->
        <marketplaceTheme:themeStylesheet/>
        <marketplaceTheme:bootstrapStylesheet/>
        %{--<p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='false'/>--}%
        %{--<p:css id='theme-bootstrap' name='${marketplaceTheme.defaultThemeBasePath()}css/bootstrap.css' absolute='false'/>--}%

        <!-- ** JavaScript ** -->
        <g:javascript src="../config.js"/>

        <g:javascript src="../static/js/marketplace-js-bundle.js"/>
        <g:javascript src="../static/vendor/owf-widget-min.js" />

        <myui:bannerBeanCSS/>
        <myui:bannerBeanJS/>

        <script type="text/javascript">
            console.log('initializing viewport')
            Marketplace.viewPortInit(); //Comes from "config" library
        </script>
    </head>
    <body class="access_alert_view_body">
        <g:set var="comingFromAccessAlert" value="true" scope="request"/>
        <g:render template="/theme/marketplace/content"
                  model="[showAccessAlert: showAccessAlert,
                  accessAlertMsg: accessAlertMsg,
                  initialUrl: initialUrl.encodeAsHTML()]"/>
    </body>
</html>
