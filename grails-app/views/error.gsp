<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	  <meta name="layout" content="${session.marketplaceLayout}" />

      <!--  meta name="layout" content="main" /-->
	  <title>Ozone Runtime Exception</title>
	   <!-- ** CSS ** -->
	   <!-- base library -->
	   <p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='true'/>
	   <myui:bannerBeanCSS/>

	   <!-- ** JavaScript ** -->
		<myui:bannerBeanJS/>
  </head>

  <body>
    <h1>Ozone Runtime Exception</h1>
    <h2>Error Details</h2>
  	<div class="message">
  		<strong>Message:</strong> ${exception?.message?.encodeAsHTML()} <br />
  		<strong>Caused by:</strong> ${exception?.cause?.message?.encodeAsHTML()} <br />
  		<strong>Class:</strong> ${exception?.className?.encodeAsHTML()} <br />
  		<strong>At Line:</strong> [${exception?.lineNumber}] <br />
  		<strong>Code Snippet:</strong><br />
  		<div class="snippet">
  			<g:each var="cs" in="${exception?.codeSnippet}">
  				${cs?.encodeAsHTML()}<br />
  			</g:each>
  		</div>
  	</div>
    <h2>Stack Trace</h2>
    <div class="stack">
      <pre><g:each in="${exception.stackTraceLines}">${it?.encodeAsHTML()}<br/></g:each></pre>
    </div>
  </body>
</html>
