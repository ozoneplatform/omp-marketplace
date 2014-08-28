<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="ozone.marketplace.enums.CustomFieldSection" %>
<!DOCTYPE html>
<html>
    <head>
    	<meta http-equiv="X-UA-Compatible" content="IE=edge">
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    	<meta name="layout" content="${session.marketplaceLayout}" />
      <script type="text/javascript">
        jQuery(document).ready( function() {
        	 var $ = jQuery;

        	$('.omp_user_search').hide();
        	Marketplace.loadDownCustomFieldDefinitionOptions({${customFieldDefinitionInstance?.instanceOf(DropDownCustomFieldDefinition) ? "SHOW:true" : "HIDE:true"}});
        });

          function onFormSubmit() {
              selectAllList(document.saveForm.types);

              if (document.saveForm.fieldOptions != undefined)
                  selectAllList(document.saveForm.fieldOptions);

              Marketplace.populateFieldValueEnabledList();
          }
      </script>
    </head>
    <body>
    <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar bootstrap-active">
          <div id="marketContent" style="padding-top: 2%; ">
		        <h5 class="admin-home-title inline" ><i class="icon-home"></i><g:link controller="administration"><g:message code="admin.home"  encodeAs="HTML" /></g:link></h5>
          		<span class="menuButton admin_page_link"><g:link class="admin_list" action="list"><g:message code="customFieldDefinition.list.title" encodeAs="HTML" /></g:link></span>
		        <common:freeTextWarning/>
		        <div class="body">
		            <br>
		            <h1><g:message code="customFieldDefinition.edit.title" encodeAs="HTML" /></h1>
		            <br>
		            <g:if test="${failureMessage}">
			           	<div class="errorText">
							<b><g:message code="${failureMessage}" args="${failureArgs}" default="${failureMessage}" encodeAs="HTML"  encodeAs="HTML" /></b>
							<br>
						</div>
					</g:if>
		            <g:hasErrors bean="${customFieldDefinitionInstance}">
		            <div class="errorText">
		                <g:renderErrors bean="${customFieldDefinitionInstance}" as="list" field="name"/>
		                <g:renderErrors bean="${customFieldDefinitionInstance}" as="list" field="label"/>
		                <g:renderErrors bean="${customFieldDefinitionInstance}" as="list" field="tooltip"/>
		                <g:renderErrors bean="${customFieldDefinitionInstance}" as="list" field="description"/>
		                <g:renderErrors bean="${customFieldDefinitionInstance}" as="list" field="isRequired"/>
		                <g:renderErrors bean="${customFieldDefinitionInstance}" as="list" field="fieldValues"/>
		                <br>
		            </div>
		            </g:hasErrors>

		            <g:form method="post" name="saveForm" onsubmit="onFormSubmit();">
		                <input type="hidden" name="id" value="${customFieldDefinitionInstance?.id}" />
		                <input type="hidden" name="version" value="${customFieldDefinitionInstance?.version}" />
		                <input type="hidden" id="valueEnableFlags" name="valueEnableFlags" value="dumbo5" />
		                <div>
		                    <table class="tableNoBorder table" style="width:auto;" cellpadding="0" cellspacing="0">
		                        <tbody>
		                            <tr class="prop">
		                                <td >
		                                    <label for="name"><common:requiredLabel><g:message code="label.name" encodeAs="HTML" /></common:requiredLabel></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'name','errors')}">
		                                    <input size="85" maxlength="50" type="text" id="name" name="name" value="${fieldValue(bean:customFieldDefinitionInstance, field:'name')}" class="textAdmin"/>
		                                </td>
		                            </tr>
		                            <tr class="prop">
		                                <td  >
		                                    <label for="label"><g:message code="label.uuid" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'uuid','errors')}">
		                                    ${customFieldDefinitionInstance.uuid.encodeAsHTML()}
		                                </td>
		                            </tr>
		                            <tr class="prop">
		                                <td >
		                                    <label for="label"><common:requiredLabel><g:message code="label.label" encodeAs="HTML" /></common:requiredLabel></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'label','errors')}">
		                                  <input size="85" maxlength="50" type="text" id="label" name="label" value="${fieldValue(bean:customFieldDefinitionInstance, field:'label')}" class="textAdmin"/>
		                                </td>
		                            </tr>
		                            <tr class="prop">
		                                <td>
		                                    <label for="label"><g:message code="label.tooltip" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'tooltip','errors')}">
		                                  <input size="85" maxlength="50" type="text" id="tooltip" name="tooltip" value="${fieldValue(bean:customFieldDefinitionInstance, field:'tooltip')}" class="textAdmin"/>
		                                </td>
		                            </tr>
		                            <tr class="prop">
		                                <td>
		                                    <label for="allTypes"><g:message code="label.allTypes" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'allTypes','errors')}">
		                                    <g:checkBox name="allTypes" value="${customFieldDefinitionInstance?.allTypes}"
		                                                onChange="Marketplace.enableTypesSelector(this)" class="switch ios brand-success">
		                                    </g:checkBox>
		                                </td>
		                            </tr>
		                            <tr class="prop">
		                                <td>
		                                    <label for="types"><g:message code="label.types" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'types','errors')}">
		                                	<select id="sl" class="shuttleBox left admin_options_box" size="7" multiple="true" name="typeOptions">
		                                		<g:each status="i" var="type" in="${typesList}">
													<option id="availableTypes${i.encodeAsHTML()}" value="${type.id}" title="${type?.title?.encodeAsHTML()}">${type?.titleDisplay()?.encodeAsHTML()}</option>
		                                		</g:each>
		                                	</select>
											<div class="shuttleButton btn-group">
		                    					<a href="javascript:addComponents(document.saveForm.typeOptions,document.saveForm.types);" id="addTypes" class="btn btn-small">&raquo;</a><br>
		                    					<a href="javascript:removeComponents(document.saveForm.types);" id="removeTypes" class="btn btn-small">&laquo;</a>
		                  					</div>
											<select name="types" size="7" multiple="multiple" class="shuttleBox admin_options_box">
												<g:each var="type" in="${customFieldDefinitionInstance?.types}">
													<option value="${type.id}" title="${type?.title?.encodeAsHTML()}">${type?.titleDisplay()?.encodeAsHTML()}</option>
		                                		</g:each>
		                                	</select>
		                                </td>
		                            </tr>

		                            <tr class="prop">
		                                <td  >
		                                    <label for="section"><g:message code="label.section" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'isRequired','errors')}">
		                                    <g:select name="section" from="${CustomFieldSection.values()}"
		                                              value="${customFieldDefinitionInstance?.section?.name()}"
		                                              optionKey="key"
		                                              optionValue="displayName" class="selectpicker"/>
		                                </td>
		                            </tr>

		                        <%-- TODO: finish implementing this! --%>
		                            <tr class="prop">
		                                <td>
		                                    <label for="label"><g:message code="label.fieldType" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'styleType','errors')}">
		                                    ${customFieldDefinitionInstance.styleType.styleTypeName().encodeAsHTML()}
		                                </td>
		                            </tr>

		                           	<g:if test="${customFieldDefinitionInstance.styleType.equals(customFieldDefinitionInstance.styleType.DROP_DOWN)}">
		                           	<%-- The DropDown data type requires different GSP for create vs. edit and
		                           		 has complicated edit logic, so cannot be handled through the standard
		                           		 custom field data type paradigm and must be explicitly put into the
		                           		 create.gsp page --%>

		                           <tr class="prop">
		                               <td  >
		                                   <label for="isMultiSelect"><g:message code="label.isMultiSelect" encodeAs="HTML" /></label>
		                               </td>
		                               <td
		                                   class="admin_create_field ${hasErrors(bean: customFieldDefinitionInstance, field: 'isMultiSelect', 'errors')}">
		                                   <g:checkBox name="isMultiSelect" class="switch ios brand-success"
		                                               value="${customFieldDefinitionInstance?.isMultiSelect}"/>
		                               </td>
		                           </tr>

		                            <tr id="dropdownAddRow">
		                            	<td>
		                            		<label for="label"><common:requiredLabel><g:message code="label.options" encodeAs="HTML" /></common:requiredLabel></label>
		                            	</td>
		                            	<td class="tableNoBorderNoWrap admin_create_field">
		                            		<div id="fieldValueAddOptionButtonContainerId" class="field_val_add_opt_btn_container input-append">

	                                  			<input id="newFieldValueAddOptionInputId" size="32" maxlength="20" type="text" name="newOptionInput" value="" class="optionInputAdmin"/>
	                                  			<input class="cancelButtonAdmin btn" type="button" onclick="Marketplace.addOption(document.saveForm.fieldOptions, document.saveForm.newOptionInput)" value="Add Option" />

						                	</div>
						                	<div id="fieldValueEditOptionMsgContainerId" class="field_val_edit_opt_msg_container">
		                            			<label for="label">
		                            				<p:image src="icons/edit-icon.png" ignore_fix_flag="true"/>
		                            				<g:message code="label.edit.option" encodeAs="HTML" />
		                            				<span id="fieldValueEditOptionMsgContainerLabelId" class="field_val_edit_opt_msg_option_label"></span>
		                            			</label>
		                            		</div>
		                            	</td>
		                            </tr>

		                            <tr id="dropdownAdminRow">
		                                <td></td>
		                            	<td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'fieldValues','errors')}">
		                            	    <div id="fieldValueSelectListContainerId" class="field_val_sel_list_container">
			                            	    <select id="fieldValueList" onChange="Marketplace.fieldValueChanged()" class="shuttleBox left" size="10" multiple="false" name="fieldOptions">
			                                        <g:if test="${customFieldDefinitionInstance?.instanceOf(DropDownCustomFieldDefinition)}">
				                                        <g:each status="idx" var="fieldValue" in="${customFieldDefinitionInstance?.fieldValues}">
				                                          <g:set var="fieldValHandleId" value="fv_cfdid_idx_${idx}" />
			                                 			  <option value="${fieldValue.displayText.encodeAsHTML()}"
			                                              		  id="${fieldValHandleId}"
			                                              		  fieldValueId="${fieldValue.id}"
			                                              		  class="<g:if test="${fieldValue.isEnabled == 0}">OMP_DisabledFieldOption </g:if>OMP_FieldOption"
			                                              		  title="${fieldValue.displayText.encodeAsHTML()}">${fieldValue.displayText.encodeAsHTML()}</option>
				                                        </g:each>
			                                		</g:if>
			                                    </select>
			                          			<div class="shuttleButton2 btn-group" style="margin-left: 12px;">
			                                        <input id="fieldOptionEditButton" class="optionButtonAdmin btn" type="button" disabled="disabled" onclick="Marketplace.handleEditFieldValueOption(document.saveForm.fieldOptions)" value="Edit"/><br>
			                                        <input id="fieldOptionUpButton" class="optionButtonAdmin btn" type="button" disabled="disabled" onclick="Marketplace.listboxMove(document.saveForm.fieldOptions, 'up')" value="Up"/><br>
			                                        <input id="fieldOptionDownButton" class="optionButtonAdmin btn" type="button" disabled="disabled" onclick="Marketplace.listboxMove(document.saveForm.fieldOptions, 'down')" value="Down"/><br>
			                                        <input id="fieldOptionEnableDisableButton" class="optionButtonAdmin btn" type="button" disabled="disabled" onclick="Marketplace.disableFieldOption(document.saveForm.fieldOptions)" value="Disable"/><br>
			                                        <input id="fieldOptionDeleteButton" class="optionButtonAdmin btn btn-danger" type="button" disabled="disabled" onclick="Marketplace.removeFieldValueOptions(document.saveForm.fieldOptions)" value="Delete"/>
			                                    </div>
		                                    </div>
		                                    <div id="fieldValueEditCtrlListContainerId" class="field_val_edit_ctrl_list_container"></div>
		                                    <div id="fieldValueHiddenFVIdListContainerId" class="field_val_hidden_fv_id_list_container"></div>
		                            	</td>
		                            </tr>

		                            </g:if>
		                            <g:else>
		                            	<g:render template="${customFieldDefinitionInstance.styleType.name().encodeAsHTML()}"
		                                          bean="${customFieldDefinitionInstance}" model="[action: 'edit']"/>
		                            </g:else>

		                            <tr class="prop">
		                                <td>
		                                    <label for="description"><g:message code="label.description" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'tooltip','errors')}">
		                                	<span class="instructions">
		                                		(<span id='countdown'><g:if test="${customFieldDefinitionInstance?.description}">${250 - customFieldDefinitionInstance.description.length()}</g:if><g:else>250</g:else></span> characters remaining)
		                                	</span> <br/>
		                                    <g:textArea cols="75" rows="5" maxlength="250" class="textAdmin"
		                                                onKeyDown="limitText(this.form.description,this.form.countdown,250,'countdown');"
		                                                onKeyUp="limitText(this.form.description,this.form.countdown,250,'countdown');"
		                                                id="description" name="description" value="${customFieldDefinitionInstance.description}"/>
		                                </td>
		                            </tr>
		                            <tr class="prop">
		                                <td  >
		                                    <label for="isRequired"><g:message code="label.isRequired" encodeAs="HTML" /></label>
		                                </td>
		                                <td  class="admin_create_field ${hasErrors(bean:customFieldDefinitionInstance,field:'isRequired','errors')}">
		                                    <g:checkBox name="isRequired" value="${customFieldDefinitionInstance?.isRequired}" class="switch ios brand-success" ></g:checkBox>
		                                </td>
		                            </tr>
		                            <tr>
		                            	<td class="tableNoBorderNoWrap">
											<g:actionSubmit class="saveButtonAdmin btn btn-primary" action="Update" value="${g.message(code: 'button.save')}"/>
											<input class="cancelButtonAdmin btn" type="button" onclick="javascript:location.href='${createLink(controller:"customFieldDefinition",action:"list")}';" value="<g:message code="button.cancel" encodeAs="HTML"/>" />
		                            	</td>
		                            </tr>
		                        </tbody>
		                    </table>
		                </div>
		            </g:form>
		        </div>
		     </div>
		  </div>
		  <script>
		  	jQuery('.selectpicker').selectpicker();
		  </script>
    </body>
</html>
