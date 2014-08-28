<%@ page import="marketplace.*"%>
<!DOCTYPE html>
<html>
   <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    	<meta name="layout" content="${session.marketplaceLayout}" />
        <meta name="pageId" content="${controllerName.encodeAsHTML()}.${actionName.encodeAsHTML()}" />
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
          	<h5 class="admin-home-title" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
	        <div class="body">
	            <h1><g:message code="types.list.title" encodeAs="HTML" /></h1>
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
	                <table class="listTables table">
	                    <thead>
	                        <tr>
	                   	        <g:sortableColumn id="title" class="admin_page_link admin_page_list_title" property="title" title="${g.message(code: 'label.title')}"/>
	                   	        <th id="image" class="sortable admin_page_link admin_page_list_title"><a>${g.message(code: 'label.typeImage', encodeAs: 'HTML')}</a></th>
	                            <g:sortableColumn id="description" class="admin_page_link admin_page_list_title" property="description" title="${g.message(code: 'label.description')}"/>
	                   	        <g:sortableColumn id="ozoneAware" class="admin_page_link admin_page_list_title" property="ozoneAware" title="${g.message(code:'label.owfAware')}"/>
	                   	        <g:sortableColumn id="hasLaunchUrl" class="admin_page_link admin_page_list_title" property="hasLaunchUrl" title="${g.message(code:'label.hasLaunchUrl')}"/>
	                   	        <g:sortableColumn id="hasIcons" class="admin_page_link admin_page_list_title" property="hasIcons" title="${g.message(code:'label.hasIcons')}"/>
	                   	        <g:sortableColumn id="uuid" class="admin_page_link admin_page_list_title" property="uuid" title="${g.message(code:'label.uuid')}"/>
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${typesInstanceList}" status="i" var="typesInstance">
	                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                        	<td class="admin_page_list"><g:link action="show" id="${typesInstance.id}">${fieldValue(bean:typesInstance, field:'title')}</g:link></td>
	                        	<td class="types_image admin_page_list" style="padding-left: 25px;">
                                    <a href="#" id="preview-${typesInstance.id}" title="click to preview..." onClick="Marketplace.displayThumbnail('${typesInstance.getIconUrl(request.contextPath).encodeAsHTML()}', '${g.message(code:'label.typeImage', encodeAs: 'JavaScript').encodeAsHTML()} Preview'); return false;">
                                        <img src="${typesInstance.getIconUrl(request.contextPath).encodeAsHTML()}" style="width:32px; height: 32px;"/>
                                    </a>
	                        	</td>
	                            <td class="admin_page_list">${fieldValue(bean:typesInstance, field:'description').replaceAll("\n","<br>")}</td>
	                            <td class="admin_page_list">${typesInstance.ozoneAware.encodeAsHTML()}</td>
	                            <td class="admin_page_list">${typesInstance.hasLaunchUrl.encodeAsHTML()}</td>
	                            <td class="admin_page_list">${typesInstance.hasIcons.encodeAsHTML()}</td>
	                            <td class="admin_page_list">${typesInstance.uuid.encodeAsHTML()}</td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
	            </div>
	            <div class="admin_paginate_buttons">
	                <g:paginate total="${typesInstanceTotal}" />
	            </div>
	            <span class="menuButton admin_page_link"><g:link class="admin_create" action="create"><g:message code="types.create.title" encodeAs="HTML" /></g:link></span>
	        </div>
	        </div>
	        </div>
    </body>
</html>
