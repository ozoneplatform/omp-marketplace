<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
        <meta http-equiv="PRAGMA" content="NO-CACHE" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <meta name="pageId" content="${controllerName}.${actionName}" />

        <title><g:message code="default.title" encodeAs="HTML" /></title>

        <!-- ** CSS ** -->
        <!-- base library -->
        <p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='true'/>
        <p:css id='theme-bootstrap' name='${marketplaceTheme.defaultThemeBasePath()}css/bootstrap.css' absolute='true'/>

        <myui:bannerBeanCSS/>

        <!-- ** JavaScript ** -->
        <g:javascript src="../config.js" />
        <p:javascript src="marketplace-js-bundle"/>
        <g:javascript src="../vendor/owf-widget-min.js" />
        <myui:bannerBeanJS/>

        <script type="text/javascript">
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
