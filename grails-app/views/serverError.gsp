
<%@ page import="marketplace.*" %>
<html>
  <head>
      <meta name="layout" content="marketplace" />
	  <title><g:message code="marketplace.title" encodeAs="HTML" /> Server Exception</title>
	  <!-- ** CSS ** -->
	  <!-- base library -->
	  <p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='true'/>
	  <myui:bannerBeanCSS/>

	  <!-- ** JavaScript ** -->
	  <myui:bannerBeanJS/>
  </head>

  <body>
    <h1><g:message code="marketplace.title" encodeAs="HTML" /> Server Exception</h1>
    <br>
    <h2> <g:message code="marketplace.server.error" encodeAs="HTML" /></h2>
    <br>
    <div>
  		<strong><g:message code="marketplace.server.error.ref"/></strong> ${errorRef.encodeAsHTML()} <br  encodeAs="HTML" />
  	</div>
  </body>
</html>
