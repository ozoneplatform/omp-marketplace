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
          		<h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
		        <span class="menuButton admin_page_link"><g:link class="admin_list" action="list"><g:message code="types.list.title" encodeAs="HTML" /></g:link></span>
		        <div class="body">
		            <h1><g:message code="types.view.title" encodeAs="HTML" /></h1>
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
		                            <td  class="nameAdmin"><g:message code="label.title" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${fieldValue(bean:typesInstance, field:'title')}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.uuid" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${typesInstance.uuid.encodeAsHTML()}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.created" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${AdminObjectFormatter.createdDateDisplay(typesInstance)}</td>
		                        </tr>
		                         <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.createdBy" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">
		                              <g:set var="creatorId" value="${typesInstance.createdBy?.id}" />
		                              <g:set var="creatorDisplay" value="${AdminObjectFormatter.creatorNameDisplay(typesInstance)}" />
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
		                            <td  class="admin_create_field">${AdminObjectFormatter.editedDateDisplay(typesInstance)}</td>
		                        </tr>
		                         <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.lastEditedBy" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">
		                              <g:set var="editorId" value="${typesInstance.editedBy?.id}" />
		                              <g:set var="editorDisplay" value="${AdminObjectFormatter.editorNameDisplay(typesInstance)}" />
		                              <g:if test="${editorId}">
		                                <g:link controller="profile" action="detail" id="${editorId}">${editorDisplay?.encodeAsHTML()}</g:link>
		                              </g:if>
		                              <g:else>
		                                ${editorDisplay?.encodeAsHTML()}
		                              </g:else>
		                            </td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.description" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${fieldValue(bean:typesInstance, field:'description').replaceAll("\n","<br>")}</td>

		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.owfAware" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${typesInstance.ozoneAware.encodeAsHTML()}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.hasLaunchUrl" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${typesInstance.hasLaunchUrl.encodeAsHTML()}</td>
		                        </tr>
		                        <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.hasIcons" encodeAs="HTML" /></td>
		                            <td  class="admin_create_field">${typesInstance.hasIcons.encodeAsHTML()}</td>
		                        </tr>
		                         <tr class="prop">
		                            <td  class="nameAdmin"><g:message code="label.typeImage" encodeAs="HTML" /></td>
		                            <td  class="types_image_link admin_create_field">
                                        <a href="#" id="preview-${typesInstance.id}" onClick="Marketplace.displayThumbnail('${typesInstance.getIconUrl(request.contextPath).encodeAsHTML()}', '${g.message(code:'label.typeImage', encodeAs: 'JavaScript').encodeAsHTML()} Preview'); return false;">
                                            <img src="${typesInstance.getIconUrl(request.contextPath).encodeAsHTML()}" style="width:32px; height: 32px;"/> (preview)
                                        </a>
		                            </td>
		                        </tr>
		                        <tr>
		                        	<td class="tableNoBorderNoWrap">

		                					<g:form name="theForm">
		                						<g:if test="${!typesInstance.isPermanent}">

		                    						<input type="hidden" name="id" value="${typesInstance?.id}" />
		                                                                <input type="hidden" id="valholder" value="" />
		                    						<g:actionSubmit class="editAdmin btn btn-primary" action="Edit" value="${g.message(code: 'types.edit.title')}" />


		                    						<g:set var="message" value="${g.message(code: 'delete.object.confirmation')}"/>
		                    						<g:actionSubmit class="deleteAdmin btn btn-danger" action="delete" onclick="return Marketplace.adminDelete('${message.encodeAsHTML()}', this);" value="${g.message(code: 'button.delete')}"/>

												</g:if>
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
