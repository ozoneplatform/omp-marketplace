<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${session.marketplaceLayout}" />
    <meta name="pageId" content="${controllerName}.${actionName}" />
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
            <h1><g:message code="admin.contactType.list.title" encodeAs="HTML" /></h1>
            <g:if test="${flash.message}">
                <g:if test="${flash.message?.matches(/\b(create|update|delete)\b.success/)}">
                    <div class="successText">
                </g:if>
                <g:else>
                    <div class="errorText">
                </g:else>
                <b><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" encodeAs="HTML"  encodeAs="HTML" /></b>
                </div>
            </g:if>
            <div class="list">
                <table class="listTables table" style="width:auto;" cellpadding="0" cellspacing="0">
                    <thead>
                    <tr>
                        <g:sortableColumn id="title" class="admin_page_link admin_page_list_title"  property="title" title="${g.message(code: 'label.title')}" />
                        <g:sortableColumn id="required" class="admin_page_link admin_page_list_title"  property="required" title="${g.message(code: 'label.isRequired')}" />
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${contactTypeList}" status="i" var="contactType">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td class="admin_page_list"><g:link action="show" id="${contactType.id}">${fieldValue(bean:contactType, field:'title')}</g:link></td>
                            <td class="admin_page_list">${contactType?.required}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="admin_paginate_buttons">
                <g:paginate total="${contactTypeTotal}" />
            </div>
            <span class="menuButton admin_page_link"><g:link class="admin_create" action="create"><g:message code="admin.contactType.create.title" encodeAs="HTML" /></g:link></span>
        </div>
        </div>
    </div>
</body>
</html>
