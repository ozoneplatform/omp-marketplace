<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${session.marketplaceLayout}" />
</head>
<body id="adminBkg">
    <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
        <div class="body">
            <div id="marketContent" style="padding-top: 2%;">
                <div id="admin_home" class="bootstrap-active">
                    <h5 class="admin-home-title" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
                    <div id="partner-stores-container"></div>
                </div>
            </div>
        </div>
    </div>
    <script>
        jQuery(document).ready(function() {
            require(['partnerStores/index'], function (PartnerStoresView) {
                (new PartnerStoresView()).render().$el.appendTo('#partner-stores-container')
            });
            jQuery('.omp_user_search').hide();
        });
    </script>
</body>
</html>
