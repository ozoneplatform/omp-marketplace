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
  <body>
    <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
    	<div id="marketContent" style="padding-top: 2%; ">
	    	<h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
		    <div class="body">
		      <h1><g:message code="userAccount.list.title" encodeAs="HTML" /></h1>
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
		              <g:sortableColumn id="lastLogin" class="admin_page_link admin_page_list_title" property="lastLogin" title="${g.message(code:'label.lastLogin')}"/>
		            </tr>
		          </thead>
		          <tbody>
		          <g:each in="${accounts}" status="i" var="account">
		            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
		              <td class="admin_page_list">${fieldValue(bean:account, field:'username')}</td>
		              <td class="admin_page_list"><g:formatDate format="MM/dd/yyyy h:mm:ss aa z" date="${account.lastLogin}"/></td>
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