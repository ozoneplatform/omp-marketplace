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
    <br>
    <common:freeTextWarning/>
    <div class="body">
      <h1><g:message code="text.create.name" encodeAs="HTML" /></h1>
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
      <g:hasErrors bean="${textInstance}">
        <div class="errorText">
          <g:renderErrors bean="${textInstance}" as="list" />
          <br>
        </div>
      </g:hasErrors>
      <g:form action="save" method="post" >
        <div>
          <table class="tableNoBorder table" width="auto" cellpadding="0" cellspacing="0">
            <tbody>
              <tr class="prop">
                <td valign="top" class="nameAdmin">
                  <label for="name">* <g:message code="label.name" encodeAs="HTML" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean:textInstance,field:'name','errors')}">
                  <input size="85" maxlength="50" type="text" id="name" name="name" value="${fieldValue(bean:textInstance,field:'name')}" class="textAdmin"/>
                </td>
              </tr>

              <tr class="prop">
                <td valign="top" class="nameAdmin">
                  <label for="value"><g:message code="label.value" encodeAs="HTML" /></label>
                </td>
                <td valign="top" class="value ${hasErrors(bean:textInstance,field:'value','errors')}">
                  <span class="instructions">
                                		(<span id='countdown'><g:if test="${textInstance?.value}">${250 - textInstance.value.length()}</g:if><g:else>250</g:else></span> characters remaining)
                  </span> <br/>
            <g:textArea cols="75" rows="5" id="value" maxlength="250" class="textAdmin"
                        onKeyDown="limitText(this.form.value,this.form.countdown,250,'countdown');"
                        onKeyUp="limitText(this.form.value,this.form.countdown,250,'countdown');"
                        name="value" value="${textInstance.value}"/>
            </td>
            </tr>

            <tr>
              <td></td>
              <td class="tableNoBorderNoWrap">


                <table width="auto" cellpadding="0" cellspacing="0" border="0" class="buttonsTable">
                  <tr>

                    <td class="buttonHolder">

                      <span class="button"><input class="saveButtonAdmin" type="submit" value="<g:message code="button.create" encodeAs="HTML"/>" /></span>

                    </td>
                    <td class="buttonHolder">

                      <span class="button"><input class="cancelButtonAdmin" type="button" onclick="javascript:location.href='${createLink(controller:"text",action:"list")}';" value="<g:message code="button.cancel" encodeAs="HTML"/>" /></span>

                    </td>

                  </tr>
                </table>


              </td>
            </tr>

            </tbody>
          </table>
        </div>
      </g:form>
    </div>
  </body>
</html>
