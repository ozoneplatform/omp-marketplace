<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
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
            Marketplace.showCustomFieldAdmin(jQuery("#styleType").val());
        });
        </script>
    </head>
  <body>
  <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
          <div id="marketContent" style="padding-top: 2%; ">
            <div class="nav">
              <span class="menuButton admin_page_link"><g:link class="admin_home" controller="administration"><g:message code="admin.home" encodeAs="HTML" /></g:link></span>
              <span class="menuButton admin_page_link"><g:link class="admin_list" action="list"><g:message code="profile.list.title" encodeAs="HTML" /></g:link></span>
            </div>
            <div class="body">
              <h1><g:message code="profile.view.title" encodeAs="HTML" /></h1>
              <g:if test="${flash.message}">
                <g:if test="${flash.message?.matches(/\b(create|update|delete)\b.success/)}">
                  <div class="successText">
                </g:if>
                <g:else>
                  <div class="errorText">
                </g:else>
                <b><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" encodeAs="HTML" encodeAs="HTML" /></b>
                <br>
                </div>
              </g:if>
              <div>
                <table class="tableNoBorder table" style="width:auto;" cellpadding="0" cellspacing="0">
                  <tbody>
                    <tr class="prop">
                      <td  class="nameAdmin"><g:message code="label.username" encodeAs="HTML" /></td>
                      <td  class="admin_create_field">${fieldValue(bean:profile, field:'username')}</td>
                    </tr>
                    <tr class="prop">
                      <td  class="nameAdmin"><g:message code="label.uuid" encodeAs="HTML" /></td>
                      <td  class="admin_create_field">${fieldValue(bean:profile, field:'uuid')}</td>
                    </tr>
                    <tr class="prop">
                        <td  class="nameAdmin"><g:message code="label.created" encodeAs="HTML" /></td>
                        <td  class="admin_create_field">${AdminObjectFormatter.createdDateDisplay(profile)}</td>
                    </tr>
                     <tr class="prop">
                        <td  class="nameAdmin"><g:message code="label.createdBy" encodeAs="HTML" /></td>
                        <td  class="admin_create_field admin_page_link">
                          <g:set var="creatorId" value="${profile.createdBy?.id}" />
                          <g:set var="creatorDisplay" value="${AdminObjectFormatter.creatorNameDisplay(profile)}" />
                          <g:if test="${creatorId}">
                            <g:link controller="profile" action="detail" id="${creatorId}">${creatorDisplay?.encodeAsHTML()}</g:link>
                          </g:if>
                          <g:else>
                            ${creatorDisplay?.encodeAsHTML()}
                          </g:else>
                        </td>
                    </tr>
                    <tr class="prop">
                        <td  class="nameAdmin"><g:message code="label.lastEdited" encodeAs="HTML" /></td>
                        <td  class="admin_create_field">${AdminObjectFormatter.editedDateDisplay(profile)}</td>
                    </tr>
                     <tr class="prop">
                        <td  class="nameAdmin"><g:message code="label.lastEditedBy" encodeAs="HTML" /></td>
                        <td  class="admin_create_field">
                          <g:set var="editorId" value="${profile.editedBy?.id}" />
                          <g:set var="editorDisplay" value="${AdminObjectFormatter.editorNameDisplay(profile)}" />
                          <g:if test="${editorId}">
                            <g:link controller="profile" action="detail" id="${editorId}">${editorDisplay?.encodeAsHTML()}</g:link>
                          </g:if>
                          <g:else>
                            ${editorDisplay?.encodeAsHTML()}
                          </g:else>
                        </td>
                    </tr>
                    <tr class="prop">
                      <td  class="nameAdmin"><g:message code="label.displayName" encodeAs="HTML" /></td>
                      <td  class="admin_create_field">${fieldValue(bean:profile, field:'displayName')}</td>
                    </tr>
                    <tr class="prop">
                      <td  class="nameAdmin"><g:message code="label.email" encodeAs="HTML" /></td>
                      <td  class="admin_create_field">${fieldValue(bean:profile, field:'email')}</td>
                    </tr>
                    <tr class="prop">
                      <td  class="nameAdmin"><g:message code="label.bio" encodeAs="HTML" /></td>
                      <td  class="admin_create_field adminDetailBio_tdCls">${fieldValue(bean:profile, field:'bio').replaceAll("\n","<br>")}</td>
                    </tr>
                    <g:if test="${((profile != null) && (profile instanceof ExtProfile))}">
                        <!-- EXTERN PROFILE INFO -->
                        <tr class="prop">
                          <td  class="nameAdmin"><g:message code="label.extern.system_uri" encodeAs="HTML" /></td>
                          <td  class="admin_create_field">${fieldValue(bean:profile, field:'systemUri')}</td>
                        </tr>
                        <tr class="prop">
                          <td  class="nameAdmin"><g:message code="label.extern.external_id" encodeAs="HTML" /></td>
                          <td  class="admin_create_field">${fieldValue(bean:profile, field:'externalId')}</td>
                        </tr>
                        <tr class="prop">
                          <td  class="nameAdmin"><g:message code="label.extern.external_view_url" encodeAs="HTML" /></td>
                          <td  class="admin_create_field"><%String  externalViewUrl_Str = (String)fieldValue(bean:profile, field:'externalViewUrl');%>
                              ${externalViewUrl_Str.encodeAsHTML()}
                        </td>
                        </tr>
                        <tr class="prop">
                          <td  class="nameAdmin"><g:message code="label.extern.external_edit_url" encodeAs="HTML" /></td>
                          <td  class="admin_create_field"><%String  externalEditUrl_Str = (String)fieldValue(bean:profile, field:'externalEditUrl');%>
                             ${externalEditUrl_Str.encodeAsHTML()}
                          </td>
                        </tr>
                    </g:if>
                    <tr>
                    <td class="tableNoBorderNoWrap" style="padding-top:50px;">
                        <g:form name="theForm">
                            <input type="hidden" name="id" value="${profile?.id}" />
                            <input type="hidden" id="valholder" value="" />
                            <g:actionSubmit class="editAdmin btn btn-primary" action="Edit" value="${g.message(code: 'profile.edit.title')}" />
                        </g:form>
                    </td>
                  </tr>
                </tbody>
                </table>
              </div>
            </div>
         </div>
      </div>
  </body>
</html>