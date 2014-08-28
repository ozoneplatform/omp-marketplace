<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${session.marketplaceLayout}" />
        <script type="text/javascript" >
            jQuery(document).ready(function() {
                var $ = jQuery;

                $('.omp_user_search').hide();
            });
        </script>
    </head>

    <body id="adminBkg">
    <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
        <div class="body">
          <div id="marketContent" style="padding-top: 2%; ">
            <div id="admin_home" class="bootstrap-active">
                <h5 class="admin-home-title" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
                <h6 class="label-faded"><g:message code="admin.description" encodeAs="HTML" /></h6>

                <table class="admin_home_table">
                    <tbody>
                        <tr>
                            <td><h4 class="span2"><g:link controller="serviceItem" action="adminView" class="admin_nav_link"><g:message code="listingManagement"  encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.listingManagement.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="types" action="list" class="admin_nav_link"><g:message code="types"  encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.types.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="state"  action="list"  class="admin_nav_link"><g:message code="state" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.state.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="category" class="admin_nav_link" action="list"><g:message code="category" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.category.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><a href="#agency" class="admin_nav_link"><g:message code="agency" encodeAs="HTML" /></a></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.agency.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="contactType" class="admin_nav_link"><g:message code="admin.contactType" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.contactType.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="customFieldDefinition" class="admin_nav_link"><g:message code="customFieldDefinition" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.customFieldDefinition.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="rejectionJustification" class="admin_nav_link"><g:message code="rejectionJustification" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.rejectionJustification.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="profile" class="admin_nav_link"><g:message code="profile" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.profile.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="userAccount" class="admin_nav_link"><g:message code="userAccount" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.userAccount.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="intentAction" class="admin_nav_link"><g:message code="intentAction" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.intentAction.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="intentDataType" class="admin_nav_link"><g:message code="intentDataType" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.intentDataType.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="dataExchange" class="admin_nav_link"><g:message code="dataExchange" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.dataExchange.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><g:link controller="applicationConfiguration" class="admin_nav_link"><g:message code="applicationConfiguration" encodeAs="HTML" /></g:link></h4></td>
                            <td><h5 class="label-faded"><g:message code="admin.applicationConfiguration.description" encodeAs="HTML" /></h5></td>
                        </tr>
                        <tr>
                            <td><h4 class="span2"><a href="./admin/partner-stores" class="admin_nav_link">Partner Stores</a></h4></td>
                            <td><h5 class="label-faded">Add, remove or modify information about partner stores. Search results from these partner stores will be displayed below the current store's search results.</td>
                        </tr>
                    </tbody>
                </table>
            </div>
         </div>
        </div>
      </div>
    </body>
</html>
