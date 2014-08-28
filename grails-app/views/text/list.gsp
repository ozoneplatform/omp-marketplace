<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title><g:message code="text.list.name" encodeAs="HTML" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="home" controller="administration"><g:message code="admin.home" encodeAs="HTML" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="text.list.name" encodeAs="HTML" /></h1>
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
                   	        <g:sortableColumn id="nameAdmin" property="name" title="${g.message(code: 'label.name')}"/>
                   	        <g:sortableColumn id="value" property="value" title="${g.message(code: 'label.value')}"/>
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${textInstanceList}" status="i" var="textInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        	<td><g:link action="show" id="${textInstance.id}">${fieldValue(bean:textInstance, field:'name')}</g:link></td>
                            <td>${fieldValue(bean:textInstance, field:'value').replaceAll("\n","<br>")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${textInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
