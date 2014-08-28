<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
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
          <div class="nav">
            <span class="menuButton admin_page_link"><g:link class="admin_home" controller="administration"><g:message code="admin.home" encodeAs="HTML" /></g:link></span>
            <span class="menuButton admin_page_link"><g:link class="admin_list" action="list"><g:message code="profile.list.title" encodeAs="HTML" /></g:link></span>
          </div>
          <common:freeTextWarning/>
          <div class="body">
            <h1><g:message code="profile.edit.title" encodeAs="HTML" /></h1>
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
            <g:hasErrors bean="${profile}">
              <div class="errorText">
                <g:renderErrors bean="${profile}" as="list" />
                <br>
              </div>
            </g:hasErrors>
            <g:form method="post" >
              <input type="hidden" name="id" value="${profile?.id}" />
              <div c>
                <table class="tableNoBorder table" width="auto" cellpadding="0" cellspacing="0">
                  <tbody>
                    <tr class="prop">
                      <td  >
                        <label for="username"><g:message code="label.username" encodeAs="HTML" /></label>
                      </td>
                      <td  class="admin_create_field ${hasErrors(bean:profile, field:'username', 'errors')}">
                        <span class="textAdmin">${fieldValue(bean:profile, field:'username')}</span>
                        <input type="hidden" name="username" value="${fieldValue(bean:profile, field:'username')}" />
                      </td>
                    </tr>
                    <tr class="prop">
                      <td  >
                        <label for="uuid"><g:message code="label.uuid" encodeAs="HTML" /></label>
                      </td>
                      <td  class="admin_create_field ${hasErrors(bean:profile, field:'uuid', 'errors')}">
                        <span class="textAdmin">${fieldValue(bean:profile, field:'uuid')}</span>
                        <input type="hidden" name="uuid" value="${fieldValue(bean:profile, field:'uuid')}" />
                      </td>
                    </tr>
                    <tr class="prop">
                      <td  style="vertical-align: top;padding-top: 15px;">
                        <label for="displayName"><g:message code="label.displayName" encodeAs="HTML" /></label>
                      </td>
                      <td  class="admin_create_field ${hasErrors(bean:profile, field:'displayName', 'errors')}">
                        <input size="85" maxlength="250" type="text" id="displayName" name="displayName" value="${fieldValue(bean:profile, field:'displayName')}" class="textAdmin"/>
                      </td>
                    </tr>
                    <tr class="prop">
                      <td style="vertical-align: top;padding-top: 15px;">
                        <label for="email"><g:message code="label.email" encodeAs="HTML" /></label>
                      </td>
                      <td  class="admin_create_field ${hasErrors(bean:profile, field:'email', 'errors')}">
                        <input size="85" maxlength="250" type="text" id="email" name="email" value="${fieldValue(bean:profile, field:'email')}" class="textAdmin"/>
                      </td>
                    </tr>
                    <tr class="prop">
                      <td style="vertical-align: top;padding-top: 29px;" >
                        <label for="description"><g:message code="label.bio" encodeAs="HTML" /></label>
                      </td>
                      <td  class="admin_create_field ${hasErrors(bean:profile, field:'bio', 'errors')}">
                        <span class="instructions">
                          <span id='countdown'><g:if test="${profile?.bio}">${1000 - profile.bio.length()}</g:if><g:else>1000</g:else></span> characters remaining
                        </span> <br/>
                        <g:textArea cols="75" rows="5" maxlength="1000" class="textAdmin"
                              onKeyDown="limitText(this.form.bio, this.form.countdown, 1000, 'countdown');"
                              onKeyUp="limitText(this.form.bio, this.form.countdown, 1000, 'countdown');"
                              id="bio" name="bio" value="${profile.bio}"/>
                       </td>
                    </tr>
                    <g:if test="${((profile != null) && (profile instanceof ExtProfile))}">
                      <!-- EXTERN PROFILE INFO -->
                      <tr class="prop"><!-- SYSTEM URI -->
                        <td  >
                          <label for="systemUri"><g:message code="label.extern.system_uri" encodeAs="HTML" /></label>
                        </td>
                        <td  class="admin_create_field ${hasErrors(bean:profile, field:'systemUri', 'errors')}">
                          <span class="textAdmin">${fieldValue(bean:profile, field:'systemUri')}</span>
                          <input type="hidden" name="systemUri" value="${fieldValue(bean:profile, field:'systemUri')}" />
                        </td>
                      </tr>
                      <tr class="prop"><!-- SYSTEM ID -->
                        <td  >
                          <label for="externalId"><g:message code="label.extern.external_id" encodeAs="HTML" /></label>
                        </td>
                        <td  class="admin_create_field ${hasErrors(bean:profile, field:'externalId', 'errors')}">
                          <span class="textAdmin">${fieldValue(bean:profile, field:'externalId')}</span>
                          <input type="hidden" name="externalId" value="${fieldValue(bean:profile, field:'externalId')}" />
                        </td>
                      </tr>
                      <tr class="prop"><!-- SYSTEM VIEW URL -->
                        <td  >
                          <label for="externalViewUrl"><g:message code="label.extern.external_view_url" encodeAs="HTML" /></label>
                        </td>
                        <td  class="admin_create_field ${hasErrors(bean:profile, field:'externalViewUrl', 'errors')}">
                          <span class="textAdmin">${fieldValue(bean:profile, field:'externalViewUrl')}</span>
                          <input type="hidden" name="externalViewUrl" value="${fieldValue(bean:profile, field:'externalViewUrl')}" />
                        </td>
                      </tr>
                      <tr class="prop"><!-- SYSTEM EDIT URL -->
                          <label for="externalEditUrl"><g:message code="label.extern.external_edit_url" encodeAs="HTML" /></label>
                        </td>
                        <td  class="admin_create_field ${hasErrors(bean:profile, field:'externalEditUrl', 'errors')}">
                          <span class="textAdmin">${fieldValue(bean:profile, field:'externalEditUrl')}</span>
                          <input type="hidden" name="externalEditUrl" value="${fieldValue(bean:profile, field:'externalEditUrl')}" />
                        </td>
                      </tr>
                    </g:if>
                    <tr>
                      <td class="tableNoBorderNoWrap">
                        <g:actionSubmit class="saveButtonAdmin btn btn-primary" action="Update" value="${g.message(code: 'button.save')}"/>
                        <input class="cancelButtonAdmin btn" type="button" onclick="javascript:location.href='${createLink(controller:"profile",action:"list")}';" value="<g:message code="button.cancel" encodeAs="HTML"/>" />
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