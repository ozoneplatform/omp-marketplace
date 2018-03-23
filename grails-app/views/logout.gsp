<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="utils.*" %>
<%@ page import="javax.servlet.http.Cookie" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE" />

    <%
        def cookies = request.getCookies()
        def cssValue = marketplaceTheme.defaultCssPath()

        for(cookie in cookies) {
            String cssCookieName = "${request.contextPath?.replaceAll('/','')}_theme_css"
            if(cookie.getName()?.equals(cssCookieName)){
                cssValue = cookie.getValue()
                break
            }
        }

    %>

    <title><g:message code="default.title" encodeAs="HTML" /></title>

    <p:favicon/>

    <!-- ** CSS ** -->
    <!-- base library -->
    <p:css id='theme' name='${cssValue}' absolute='true'/>
    <myui:bannerBeanCSS/>

    <!-- ** JavaScript ** -->
    <p:javascript src="marketplace-js-bundle"/>
    <myui:bannerBeanJS/>

    <script type="text/javascript">
        Marketplace.viewPortInit();//Comes from "config" library
    </script>

</head>
<body>
<g:set var="comingFromLogout" value="true" scope="request"/>
<g:render template="/theme/marketplace/content"/>
</body>
</html>
