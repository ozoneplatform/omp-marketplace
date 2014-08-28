<%
  def selectedByDefault = false
  def readOnly = false
  switch(action){
    case 'create':
      break
    case 'edit':
      selectedByDefault = customFieldDefinitionInstance.selectedByDefault
      break
    case 'show':
      selectedByDefault = customFieldDefinitionInstance.selectedByDefault
      readOnly = true
      break
    default:
      break
  }
%>
<tr class="customFieldAdmin CHECK_BOX">
    <td valign="top" class="nameAdmin">
        <label for="checkBoxDefault">
            <g:message code="Selected by Default:" encodeAs="HTML"/>
        </label>
    </td>
    <g:if test="${readOnly}">
      <td valign="top" class="value">${selectedByDefault.encodeAsHTML()}</td>
    </g:if>
    <g:else>
      <td valign="top" class="value admin_create_field">
          <g:checkBox name="selectedByDefault" value="${selectedByDefault}" class="switch ios brand-success"></g:checkBox>
      </td>
    </g:else>
</tr>
