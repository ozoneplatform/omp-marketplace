
<%@ page import="marketplace.*" %>
<html>
<head>
    <meta name="layout" content="${session.marketplaceLayout}" />
    <title>Maintenance Warning</title>
    <!-- ** CSS ** -->
    <!-- base library -->
    <p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='true'/>
    <myui:bannerBeanCSS/>

    <!-- ** JavaScript ** -->
    <myui:bannerBeanJS/>
</head>

<body>
<h1>Maintenance Warning</h1>
<br>
<h2> <g:message code="application.configuration.maintenance.required" encodeAs="HTML" /></h2><br><br><br><br>
<br>
</body>
</html>
