<%@ page contentType="text/html;charset=UTF-8" %>

<%
    /***
     * This template assumes one passes in a 'button' object with the following fields (i.e. button.*):
     * where * is:
     * 
     * button.
     *      disabled : defines a boolean 'true' if this button is rendered disabled
     *      id : defines the base 'id' of this button to be rendered into the DOM
     *      qtip : defines the button's data-qtip contents
     *      iconCls : defines the button's icon class (i.e. in addition to 'x-btn-icon')
     *      cls : defines the button's top level class
     *      paramz : defines the params for the remoteLink 
     * 
     * AND
     *
     * controller : defines the controller for the remoteLink
     * action : defines the action for the remoteLink
     */


    boolean isDisabled = Boolean.valueOf(button.disabled)
    def innerButtonContents = "" +
    "<span id=\"${button.id}\" class=\"extjs-toolbar-page-button ${button.cls}  " +
        "${isDisabled ? 'x-item-disabled extjs-toolbar-page-button-disabled' : 'x-item-enabled'}\" title=\"${button.qtip}\">" +
        "<span class=\"extjs-toolbar-page-button-wrap\">" +
            "<span class=\"extjs-page-button-icon ${button.iconCls}\" id=\"${button.id}-btnIconEl\">&nbsp;</span>" +
        "</span>" +
    "</span>"
    
 %>
<g:if test="${!isDisabled}">
    <g:link controller="${controller}" 
                  action="${action}"
                  params="${button.paramz}">
        ${innerButtonContents}
    </g:link>
</g:if>
<g:else>
    ${innerButtonContents}
</g:else>