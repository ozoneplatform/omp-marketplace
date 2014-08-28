<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${session.marketplaceLayout}" />
    <title>Ozone Marketplace - Unknown User</title>

    <!-- ** CSS ** -->
 		<!-- base library -->
		<p:css id='theme' name='${marketplaceTheme.defaultCssPath()}' absolute='true'/>
		<myui:bannerBeanCSS/>
		
		<!-- ** JavaScript ** -->
		<myui:bannerBeanJS/>
  </head>
  
  <body>
   <div id="marketContent">
    <div class="messageContent">
      <p class="messageTitle">Unknown User: PKI Validation Exception</p>
      <p class="messageDetails">Error Details</p>
      <p class="messageTxt">Your PKI certificate may not be configured properly for this browser. Please correct the problem and try again</a>.</p>
    </div>
    </div>
  </body>
</html>
