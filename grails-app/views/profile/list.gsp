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
        });
        </script>
    </head>
  <body>
        <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
          <div id="marketContent" style="padding-top: 2%; ">
            <h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
            <div class="body">
              <h1><g:message code="profile.list.title" encodeAs="HTML" /></h1>
              <g:if test="${flash.message}">
                <g:if test="${flash.message?.matches(/\b(create|update|delete)\b.success/)}">
                  <div class="successText">
                </g:if>
                <g:else>
                  <div class="errorText">
                </g:else>
                <b><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" encodeAs="HTML"  encodeAs="HTML" /></b>
                <br>
                </div>
              </g:if>
              <div class="list">
                <table class="listTables table" cellpadding="0" cellspacing="0">
                  <thead>
                    <tr>
                      <g:sortableColumn id="username" class="admin_page_link admin_page_list_title" property="username" title="${g.message(code: 'label.username')}" />
                      <g:sortableColumn id="displayName" class="admin_page_link admin_page_list_title" property="displayName" title="${g.message(code: 'label.displayName')}" />
                      <g:sortableColumn id="email" class="admin_page_link admin_page_list_title" property="email" title="${g.message(code:'label.email')}"/>
                      <g:sortableColumn id="bio" class="admin_page_link admin_page_list_title" property="bio" title="${g.message(code:'label.bio')}"/>
                      <g:sortableColumn id="uuid" class="admin_page_link admin_page_list_title" property="uuid" title="${g.message(code:'label.uuid')}"/>
                      <!-- EXTERN PROFILE INFO -->
                      <g:sortableColumn id="systemUri" class="admin_page_link admin_page_list_title" property="systemUri" title="${g.message(code:'label.extern.system_uri')}"/>
                      <g:sortableColumn id="externalId"  class="admin_page_link admin_page_list_title" property="externalId" title="${g.message(code:'label.extern.external_id')}"/>
                      <g:sortableColumn id="externalViewUrl" class="admin_page_link admin_page_list_title" property="externalViewUrl" title="${g.message(code:'label.extern.external_view_url')}"/>
                      <g:sortableColumn id="externalEditUrl" class="admin_page_link admin_page_list_title" property="externalEditUrl" title="${g.message(code:'label.extern.external_edit_url')}"/>
                    </tr>
                  </thead>
                  <tbody>
                  <g:each in="${profiles}" status="i" var="profile">
                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                      <td class="admin_page_list"><g:link action="detail" id="${profile.id}">${fieldValue(bean:profile, field:'username')}</g:link></td>
                      <td class="admin_page_list">${fieldValue(bean:profile, field:'displayName')}</td>
                      <td class="admin_page_list">${fieldValue(bean:profile, field:'email')}</td>
                      <td class="adminListBio_tdCls admin_page_list">${fieldValue(bean:profile, field:'bio')}</td>
                      <td class="admin_page_list">${fieldValue(bean:profile, field:'uuid')}</td>
                      <g:if test="${((profile != null) && (profile instanceof ExtProfile))}">
                          <!-- EXTERN PROFILE INFO -->
                          <td class="admin_page_list">${fieldValue(bean:profile, field:'systemUri')}</td>
                          <td class="admin_page_list">${fieldValue(bean:profile, field:'externalId')}</td>
                          <td class="admin_page_list"><%String  externalViewUrl_Str = (String)fieldValue(bean:profile, field:'externalViewUrl');%>
                             ${externalViewUrl_Str}
                        </td>
                          <td class="admin_page_list"><%String  externalEditUrl_Str = (String)fieldValue(bean:profile, field:'externalEditUrl');%>
                               ${externalEditUrl_Str}
                          </td>
                       </g:if>
                       <g:else>
                         <td class="admin_page_list"></td>
                         <td class="admin_page_list"></td>
                         <td class="admin_page_list"></td>
                         <td class="admin_page_list"></td>
                       </g:else>
                    </tr>
                  </g:each>
                  </tbody>
                </table>
              </div>
              <div class="admin_paginate_buttons">
                <g:paginate total="${total}" />
              </div>
            </div>
          </div>
        </div>
  </body>
</html>