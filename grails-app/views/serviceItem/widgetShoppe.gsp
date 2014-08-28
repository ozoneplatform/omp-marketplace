<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${session.marketplaceLayout}" />
    </head>

    <body>

        <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar">
            <div class="body">
                <div id="marketContent">
                        <div id="filter_menu">
                                <g:render template="/filter_menu"/>
                        </div>
                    <g:set var="accessType" value="${session.accessType}" />
                    <g:render template="/serviceItem/widget_shoppe" />
                </div>
            </div>
        </div>
    </body>
</html>
