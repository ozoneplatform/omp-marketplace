<%@ page import="marketplace.*" %>
<%@ page import="static org.codehaus.groovy.grails.commons.ConfigurationHolder.config"%>
<%@ page import="org.springframework.context.ApplicationContext" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="${session.marketplaceLayout}" />
        <meta name="pageId" content="${controllerName.encodeAsHTML()}.${actionName.encodeAsHTML()}" />
        <script type="text/javascript" >

        var allowImageUpload = Marketplace.allowImageUpload;

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
            <common:freeTextWarning/>
            <div class="body">
                <h1><g:message code="types.create.title" encodeAs="HTML" /></h1>
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
                <g:hasErrors bean="${typesInstance}">
                <div class="errorText">
                    <g:renderErrors bean="${typesInstance}" as="list" />
                    <br>
                </div>
                </g:hasErrors>
                <g:form action="save" method="post" enctype="multipart/form-data" >
                    <div>
                        <table class="tableNoBorder table" style="width: auto;" cellpadding="0" cellspacing="0">
                            <tbody>
                                <tr class="prop">
                                    <td class="nameAdmin" style="vertical-align: top;padding-top: 15px;">
                                        <label for="title"><common:requiredLabel><g:message code="label.title" encodeAs="HTML" /></common:requiredLabel></label>
                                    </td>
                                    <td class="admin_create_field ${hasErrors(bean:typesInstance,field:'title','errors')}">
                                        <input size="85" maxlength="50" type="text" id="title" name="title" value="${fieldValue(bean:typesInstance,field:'title')}" class="textAdmin"/>
                                    </td>
                                </tr>

                                <tr class="prop">
                                    <td class="nameAdmin" style="vertical-align: top;padding-top: 29px;">
                                        <label for="description"><g:message code="label.description" encodeAs="HTML" /></label>
                                    </td>
                                    <td class="admin_create_field ${hasErrors(bean:typesInstance,field:'description','errors')}">
                                        <span class="instructions">
                                            (<span id='countdown'><g:if test="${types?.description}">${250 - types.description.length()}</g:if><g:else>250</g:else></span> characters remaining)
                                        </span> <br/>
                                        <g:textArea cols="75" rows="5" maxlength="250" class="textAdmin"
                                                    onKeyDown="limitText(this.form.description,this.form.countdown,250,'countdown');"
                                                    onKeyUp="limitText(this.form.description,this.form.countdown,250,'countdown');"
                                                    id="description" name="description" value="${typesInstance?.description}"/>
                                    </td>
                                </tr>

                                <tr class="prop">
                                    <td class="nameAdmin">
                                        <label for="ozoneAware"><g:message code="label.owfAware" encodeAs="HTML" /></label>
                                    </td>
                                    <td class="admin_create_field ${hasErrors(bean:typesInstance,field:'ozoneAware','errors')}">
                                        <g:checkBox name="ozoneAware" value="${typesInstance?.ozoneAware}" class="switch ios brand-success"></g:checkBox>
                                    </td>
                                </tr>

                                <tr class="prop">
                                    <td class="nameAdmin">
                                        <label for="hasLaunchUrl"><g:message code="label.hasLaunchUrl" encodeAs="HTML" /></label>
                                    </td>
                                    <td class="admin_create_field ${hasErrors(bean:typesInstance,field:'hasLaunchUrl','errors')}">
                                        <g:checkBox name="hasLaunchUrl" value="${typesInstance?.hasLaunchUrl}" class="switch ios brand-success"></g:checkBox>
                                    </td>
                                </tr>

                                <tr class="prop">
                                    <td class="nameAdmin">
                                        <label for="hasIcons"><g:message code="label.hasIcons" encodeAs="HTML" /></label>
                                    </td>
                                    <td class="admin_create_field ${hasErrors(bean:typesInstance,field:'hasIcons','errors')}">
                                        <g:checkBox name="hasIcons" value="${typesInstance?.hasIcons}" class="switch ios brand-success"></g:checkBox>
                                    </td>
                                </tr>

                                <g:if test="${allowImageUpload.equals('true')}">
                                    <tr class="prop">
                                        <td class="nameAdmin">
                                            <label for="image"><g:message code="label.typeImage" encodeAs="HTML" /></label>
                                        </td>
                                        <td class="admin_create_field ${hasErrors(bean:typesInstance,field:'image','errors')}">
                                            <div class="fileupload fileupload-new" data-provides="fileupload">
                                                <div class="uneditable-input">
                                                    <span class="fileupload-preview">Choose file</span>
                                                </div>
                                                <span class="btn btn-file">
                                                    <span class="fileupload-new">Browse</span>
                                                    <input type="file" name="typeImage" />
                                                </span>
                                            </div>
                                        </td>
                                    </tr>
                                </g:if>

                                <input name="isDeleted" type="hidden" value="0"/>

                                <tr>
                                    <td class="tableNoBorderNoWrap">

                                        <input class="saveButtonAdmin btn btn-primary" type="submit" value="<g:message code="button.create" encodeAs="HTML"/>" />

                                        <input class="cancelButtonAdmin btn" type="button" onclick="javascript:location.href='${createLink(controller:"types",action:"list")}';" value="<g:message code="button.cancel" encodeAs="HTML"/>" />


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
