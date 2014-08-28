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
          	<h5 class="admin-home-title" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
	        <div class="body">
	            <h1><g:message code="customFieldDefinition.list.title" encodeAs="HTML" /></h1>
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
	            <div class="list">
	                <table class="listTables table" cellpadding="0" cellspacing="0">
	                    <thead>
	                        <tr>
	                   	        <g:sortableColumn id="name" class="admin_page_link admin_page_list_title" property="name" title="${g.message(code: 'label.name')}" />
	                   	        <g:sortableColumn id="label" class="admin_page_link admin_page_list_title" property="label" title="${g.message(code: 'label.label')}" />
	                   	        <g:sortableColumn id="tooltip" class="admin_page_link admin_page_list_title" property="tooltip" title="${g.message(code: 'label.tooltip')}" />
	                            <th id="types" class="sortable admin_page_link admin_page_list_title"><a>${g.message(code: 'label.types', encodeAs: 'HTML')}</span></th>
	                   	        <g:sortableColumn id="description" class="admin_page_link admin_page_list_title" property="description" title="${g.message(code: 'label.description')}" />
	                            <th id="isRequired" class="sortable admin_page_link admin_page_list_title"><a>${g.message(code: 'label.isRequired', encodeAs: 'HTML')}</span></th>
	                            <th id="section" class="sortable admin_page_link admin_page_list_title"><a>${g.message(code: 'label.section', encodeAs: 'HTML')}</span></th>
	                            <g:sortableColumn id="fieldType" class="admin_page_link admin_page_list_title" property="styleType" title="${g.message(code: 'label.fieldType')}" />
	                            <th id="options" class="sortable admin_page_link admin_page_list_title"><a>${g.message(code: 'label.options', encodeAs: 'HTML')}</span></th>
	                            <g:sortableColumn id="uuid"  class="admin_page_link admin_page_list_title" property="uuid" title="${g.message(code: 'label.uuid')}" />
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${customFieldDefinitionInstanceList}" status="i" var="customFieldDefinitionInstance">
	                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
								<td  class="admin_page_list"><g:link action="show" id="${customFieldDefinitionInstance.id}">${fieldValue(bean:customFieldDefinitionInstance, field:'name')}</g:link></td>
	                            <td  class="admin_page_list">${fieldValue(bean:customFieldDefinitionInstance, field:'label')}</td>
	                            <td  class="admin_page_list">${fieldValue(bean:customFieldDefinitionInstance, field:'tooltip')}</td>
	                            <td  class="admin_page_list">
	                              	<g:if test="${customFieldDefinitionInstance.allTypes}">
	                                  ${g.message(code: 'customFieldDefinition.allTypes', encodeAs: 'HTML')}
	                                </g:if>
	                                <g:else>
	                                  <g:each status="x" var="t" in="${customFieldDefinitionInstance.types}">
	                                      <g:if test="${x != 0}"><g:message code="item.separator" encodeAs="HTML" /></g:if>
	                                      <g:link controller="types" action="show" id="${t.id}">${t?.titleDisplay()?.encodeAsHTML()}</g:link>
	                                  </g:each>
	                                </g:else>
	                            </td>
	                            <td  class="admin_page_list">${fieldValue(bean:customFieldDefinitionInstance, field:'description').replaceAll("\n","<br>")}</td>
	                            <td  class="admin_page_list">${customFieldDefinitionInstance.isRequired}</td>
	                            <td  class="admin_page_list">${customFieldDefinitionInstance?.section?.displayName?.encodeAsHTML()}</td>
	                            <td  class="admin_page_list">${customFieldDefinitionInstance.styleType.styleTypeName().encodeAsHTML()}</td>
	                            <td  class="admin_page_list">
	                            <g:if test="${customFieldDefinitionInstance?.instanceOf(DropDownCustomFieldDefinition)}">
		                           	<g:each status="x" var="fieldValue" in="${customFieldDefinitionInstance?.fieldValues}">
		                        		<g:if test="${x != 0}"><g:message code="item.separator" encodeAs="HTML" /></g:if>
		                        		<g:if test="${fieldValue.isEnabled == 1}">
										${fieldValue?.prettyPrint()?.encodeAsHTML()}
										</g:if>
										<g:else>
	                                     <span class="OMP_DisabledFieldOption">${fieldValue?.prettyPrint()?.encodeAsHTML()}</span>
	                                    </g:else>
		                        	</g:each>
			                    </g:if>
		                        </td>
	                            <td  class="admin_page_list">${customFieldDefinitionInstance.uuid.encodeAsHTML()}</td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
	            </div>
	            <div class="admin_paginate_buttons">
	                <g:paginate total="${customFieldDefinitionInstanceTotal}" />
	            </div>
	            <a class="menuButton admin_page_link"><g:link class="admin_create" action="create"><g:message code="customFieldDefinition.create.title" encodeAs="HTML" /></g:link></span>
	        </div>
	       </div>
	   </div>
    </body>
</html>
