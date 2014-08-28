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
            <h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
            <span class="menuButton admin_page_link"><g:link class="admin_list" action="list"><g:message code="intentDataType.list.title" encodeAs="HTML" /></g:link></span>
            <common:freeTextWarning/>
            <div class="body">
                <h1><g:message code="intentDataType.edit.title" encodeAs="HTML" /></h1>
                <g:if test="${flash.message}">
                    <g:if test="${flash.message?.matches(/\b(create|update|delete)\b.success/)}">
                        <div class="successText">
                    </g:if>
                    <g:else>
                        <div class="errorText">
                    </g:else>
                        <b><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMsg}" encodeAs="HTML" encodeAs="HTML" /></b>
                    </div>
                </g:if>
                <g:hasErrors bean="${intentDataType}">
                <div class="errorText">
                    <g:renderErrors bean="${intentDataType}" as="list" />
                </div>
                </g:hasErrors>
                <g:form method="post" >
                    <input type="hidden" name="id" value="${intentDataType?.id}" />
                    <div>
                        <table class="tableNoBorder table" style="width:auto;" cellpadding="0" cellspacing="0">
                            <tbody>
                                <tr class="prop">
                                    <td  class="nameAdmin" style="vertical-align: top;padding-top: 15px;">
                                        <label for="title"><common:requiredLabel><g:message code="label.title" encodeAs="HTML" /></common:requiredLabel></label>
                                    </td>
                                    <td class="admin_create_field  ${hasErrors(bean:intentDataType,field:'title','errors')}">
                                        <span class="instructions">
                                            (<span id='countdownTitle'>${256 - (intentDataType?.title?.length() ?: 0)}</span> characters remaining)
                                        </span> <br/>
                                        <g:textArea cols="75" rows="5" maxlength="256" class="textAdmin"
                                                    onKeyDown="limitText(this.form.description,this.form.countdownTitle,256,'countdownTitle');"
                                                    onKeyUp="limitText(this.form.description,this.form.countdownTitle,256,'countdownTitle');"
                                                    id="title" name="title" value="${intentDataType.title}"/>
                                    </td>
                                </tr>
                                <tr class="prop">
                                    <td  class="nameAdmin">
                                        <label for="title"><g:message code="label.uuid" encodeAs="HTML" /></label>
                                    </td>
                                    <td  class="admin_create_field">
                                        <label for="title">${intentDataType.uuid.encodeAsHTML()}</label>
                                    </td>
                                </tr>
                                <tr class="prop">
                                    <td  class="nameAdmin" style="vertical-align: top;padding-top: 29px;">
                                        <label for="description"><g:message code="label.description" encodeAs="HTML" /></label>
                                    </td>
                                    <td  class="admin_create_field ${hasErrors(bean:intentDataType,field:'description','errors')}">
                                        <span class="instructions">
                                            (<span id='countdown'>${256 - (intentDataType?.description?.length() ?: 0)}</span> characters remaining)
                                        </span> <br/>
                                        <g:textArea cols="75" rows="5" maxlength="256" class="textAdmin"
                                                    onKeyDown="limitText(this.form.description,this.form.countdown,256,'countdown');"
                                                    onKeyUp="limitText(this.form.description,this.form.countdown,256,'countdown');"
                                                    id="description" name="description" value="${intentDataType.description}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tableNoBorderNoWrap">

                                        <g:actionSubmit class="saveButtonAdmin btn btn-primary" action="Update" value="${g.message(code: 'button.save')}"/>

                                        <input class="cancelButtonAdmin btn" type="button" onclick="javascript:location.href='${createLink(controller:"intentDataType",action:"list")}';" value="<g:message code="button.cancel" encodeAs="HTML"/>" />

                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </g:form>
            </div>
          </div>
       </div>
    </body>
</html>
