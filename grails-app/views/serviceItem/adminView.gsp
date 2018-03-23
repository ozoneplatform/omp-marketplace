<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%
  def flashClazz = "message errorText"
  if (flash?.success) { flashClazz = "message successText" }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="layout" content="${session.marketplaceLayout}" />
</head>
<body>
    <div id="marketContentWrapper">
        <div class="body">
            <div id="marketContent" class="bootstrap-active listing-management">
                <h5 class="admin-home-title" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
                <h1 class="page-title">Listing Management</h1>
                <div id="recentActivity"></div>
                <div id="pendingListings"></div>
            </div>
        </div>
    </div>

    <script>
        jQuery(function() {
            require(['listingManagement/index']);
        });
    </script>
</body>
</html>

