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
			Marketplace.showCustomFieldAdmin(jQuery("#styleType").val());
		});
		</script>
  	</head>
    <body>
    <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
          <div id="marketContent" style="padding-top: 2%; ">
          		<h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
          		<span class="menuButton admin_page_link"><g:link class="admin_list" action="list"><g:message code="customFieldDefinition.list.title" encodeAs="HTML" /></g:link></span>
		        <div class="body">
					<br>
		            <h1><g:message code="customFieldDefinition.view.title" encodeAs="HTML" /></h1>
		            <br>
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
		            <div>
		                <table class="tableNoBorder table" style="width:auto;" cellpadding="0" cellspacing="0">
		                    <tbody>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.name" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${fieldValue(bean:customFieldDefinitionInstance, field:'name')}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.uuid" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${fieldValue(bean:customFieldDefinitionInstance, field:'uuid')}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.created" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${AdminObjectFormatter.createdDateDisplay(customFieldDefinitionInstance)}</td>
		                        </tr>
		                         <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.createdBy" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field admin_page_link">
		                              <g:set var="creatorId" value="${customFieldDefinitionInstance.createdBy?.id}" />
		                              <g:set var="creatorDisplay" value="${AdminObjectFormatter.creatorNameDisplay(customFieldDefinitionInstance)}" />
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
		                            <td  class="admin_create_field">${AdminObjectFormatter.editedDateDisplay(customFieldDefinitionInstance)}</td>
		                        </tr>
		                         <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.lastEditedBy" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field admin_page_link">
		                              <g:set var="editorId" value="${customFieldDefinitionInstance.editedBy?.id}" />
		                              <g:set var="editorDisplay" value="${AdminObjectFormatter.editorNameDisplay(customFieldDefinitionInstance)}" />
		                              <g:if test="${editorId}">
		                                <g:link controller="profile" action="detail" id="${editorId}">${editorDisplay?.encodeAsHTML()}</g:link>
		                              </g:if>
		                              <g:else>
		                                ${editorDisplay?.encodeAsHTML()}
		                              </g:else>
		                            </td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.label" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${fieldValue(bean:customFieldDefinitionInstance, field:'label')}</td>
		                        </tr>

		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.section" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${customFieldDefinitionInstance?.section?.displayName?.encodeAsHTML()}</td>
		                        </tr>

		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.fieldType" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${customFieldDefinitionInstance.styleType.styleTypeName().encodeAsHTML()}</td>
		                        </tr>
		                        <%-- dropdown specific block --%>
		                        <g:if test="${customFieldDefinitionInstance?.instanceOf(DropDownCustomFieldDefinition)}">
		                            <tr class="prop">
		                                <td  class="nameAdmin"><g:message code="label.isMultiSelect" encodeAs="HTML" /></td>
		                                <td  class="admin_create_field">${customFieldDefinitionInstance.isMultiSelect}</td>
		                            </tr>

		                            <tr class="prop">
		                        	<td class="nameAdmin"><g:message code="label.options" encodeAs="HTML" /></td>
		                        	<td class="admin_create_field">
		                        		<ul class="unstyled">
			                        	<g:each status="x" var="fieldValue" in="${customFieldDefinitionInstance?.fieldValues}">
			                        		<g:if test="${fieldValue.isEnabled == 1}">
											<li class="listingTableNoStyle">${fieldValue?.prettyPrint()?.encodeAsHTML()}</li>
											</g:if>
											<g:else>
		                                    <li class="listingTableNoStyle"><span class="OMP_DisabledFieldOption">${fieldValue?.prettyPrint()?.encodeAsHTML()}</span></li>
		                                    </g:else>

			                        	</g:each>
			                        	</ul>
		                        	</td>
		                        </tr>
		                        </g:if>
		                        <%-- end of dropdown block --%>
		                        <g:else>
		                            <g:render template="${customFieldDefinitionInstance.styleType.name().encodeAsHTML()}"
		                                                      bean="${customFieldDefinitionInstance}" model="[action: 'show']"/>
		                        </g:else>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.tooltip" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${fieldValue(bean:customFieldDefinitionInstance, field:'tooltip')}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.types" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">
		                                <ul class="unstyled">
		                               	<g:if test="${customFieldDefinitionInstance.allTypes}">
		                                  ${g.message(code: 'customFieldDefinition.allTypes', encodeAs: 'HTML')}
		                                </g:if>
		                                <g:else>
		                                  <g:each status="x" var="t" in="${customFieldDefinitionInstance.types}">
		                                      <li class="listingTableNoStyle"><g:link controller="types" action="show" id="${t.id}">${t?.titleDisplay()?.encodeAsHTML()}</g:link></li>
		                                  </g:each>
		                                </g:else>
		                                </ul>
		                            </td>
		                        </tr>
								<tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.description" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field OMP_CFD_list_description">${fieldValue(bean:customFieldDefinitionInstance, field:'description').replaceAll("\n","<br>")}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.isRequired" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${customFieldDefinitionInstance.isRequired}</td>
		                        </tr>
		                    </tbody>
		                </table>
		                <g:form name="theForm">
							<g:if test="${!customFieldDefinitionInstance.isPermanent}">

							<input type="hidden" name="id" value="${customFieldDefinitionInstance?.id}" />
							<input type="hidden" id="valholder" value="" />
							<g:actionSubmit class="editAdmin btn btn-primary" action="Edit" value="${g.message(code: 'customFieldDefinition.edit.title')}" />
							<g:set var="deleteMsg" value="${g.message(code: 'delete.customFieldDefinition.confirmation')}"/>
							<g:actionSubmit class="deleteAdmin btn btn-danger" action="Delete" onclick="return Marketplace.adminDelete('${deleteMsg.encodeAsHTML()}', this);" value="${g.message(code: 'button.delete')}"/>
							</g:if>

						</g:form>
		            </div>
		        </div>
		    </div>
		 </div>
    </body>
</html>
