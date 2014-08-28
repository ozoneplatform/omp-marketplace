<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="text.create.name" encodeAs="HTML" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="home" controller="administration"><g:message code="admin.home" encodeAs="HTML" /></g:link></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="text.list.name" encodeAs="HTML" /></g:link></span>
        </div>
        <div class="body">
        	<br>
            <h1><g:message code="text.view.name" encodeAs="HTML" /></h1>
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
                <table class="tableNoBorder table" width="auto" cellpadding="0" cellspacing="0">
                    <tbody>
                        <tr class="prop">
                            <td valign="top" class="nameAdmin"><g:message code="label.name" encodeAs="HTML" /></td>
                            <td valign="top" class="value">${fieldValue(bean:textInstance, field:'name')}</td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="nameAdmin"><g:message code="label.created" encodeAs="HTML" /></td>
                            <td valign="top" class="value">${AdminObjectFormatter.createdDateDisplay(textInstance)}</td>
                        </tr>
                         <tr class="prop">
                            <td valign="top" class="nameAdmin"><g:message code="label.createdBy" encodeAs="HTML" /></td>
                            <td valign="top" class="value">
                              <g:set var="creatorId" value="${textInstance.createdBy?.id}" />
                              <g:set var="creatorDisplay" value="${AdminObjectFormatter.creatorNameDisplay(textInstance)}" />
                              <g:if test="${creatorId}">
                                <g:link controller="profile" action="detail" id="${creatorId}">${creatorDisplay?.encodeAsHTML()}</g:link>
                              </g:if>
                              <g:else>
                                ${creatorDisplay?.encodeAsHTML()}
                              </g:else>
                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="nameAdmin"><g:message code="label.lastEdited" encodeAs="HTML" /></td>
                            <td valign="top" class="value">${AdminObjectFormatter.editedDateDisplay(textInstance)}</td>
                        </tr>
                         <tr class="prop">
                            <td valign="top" class="nameAdmin"><g:message code="label.lastEditedBy" encodeAs="HTML" /></td>
                            <td valign="top" class="value">
                              <g:set var="editorId" value="${textInstance.editedBy?.id}" />
                              <g:set var="editorDisplay" value="${AdminObjectFormatter.editorNameDisplay(textInstance)}" />
                              <g:if test="${editorId}">
                                <g:link controller="profile" action="detail" id="${editorId}">${editorDisplay?.encodeAsHTML()}</g:link>
                              </g:if>
                              <g:else>
                                ${editorDisplay?.encodeAsHTML()}
                              </g:else>
                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="nameAdmin"><g:message code="label.value" encodeAs="HTML" /></td>
                            <td valign="top" class="value">${fieldValue(bean:textInstance, field:'value').replaceAll("\n","<br>")}</td>

                        </tr>
                        <tr>
                        	<td></td>
                        	<td class="tableNoBorderNoWrap">


								<table width="auto" cellpadding="0" cellspacing="0" border="0" class="buttonsTable">
                        			<tr>

                					<g:form name="theForm">

                            			<td class="buttonHolder">

                    						<input type="hidden" name="id" value="${textInstance?.id}" />
                                                                <input type="hidden" id="valholder" value="" />
                    						<span class="button"><g:actionSubmit class="editAdmin" action="Edit" value="${g.message(code: 'text.edit.name')}" /></span>

                    					</td>

                					</g:form>


			                        </tr>
			                	</table>


                        	</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
