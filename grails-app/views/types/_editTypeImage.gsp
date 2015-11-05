<%@ page import="marketplace.*" %>
<%@ page import="static grails.util.Holders.config" %>


 <script>
     var allowImageUpload = Marketplace.allowImageUpload;
 </script>

 <g:if test="${typesInstance?.image?.id}">
    <td  class="nameAdmin">
        <label for="image"><g:message code="label.typeImage" encodeAs="HTML" /></label>
    </td>
    <td  class="types_image_link " id="existing_image_id-${typesInstance?.image?.id}">
        <img src="${request.contextPath}/images/get/${typesInstance?.image?.id}" width="32" height="32"/>
        <div class="btn-group" style="margin-left: 12px;">
            <a href="#" class="btn" id="preview-${typesInstance?.image?.id}" onClick="Marketplace.displayThumbnail('${request.contextPath}/images/get/${typesInstance?.image?.id}', '${g.message(code:'label.typeImage', encodeAs: 'JavaScript').encodeAsHTML()} Preview'); return false;" style="min-width: initial;">
                <i class="icon-picture"></i>
            </a>
            <g:if test="${allowImageUpload.equals('true')}">
                <a href="#" class="btn" onClick="Marketplace.showHideDiv('new_image_id-${typesInstance?.image?.id}','existing_image_id-${typesInstance?.image?.id}')" style="min-width: initial;"><i class="icon-edit"></i></a>
            </g:if>
            <a href="#" class="btn" onClick="Marketplace.deleteTypeImageIcon('${typesInstance?.id}', 'typeImageIconId-${typesInstance?.image?.id}','editTypeImage', 'typesInstance')" style="min-width: initial;"><i class="icon-trash"></i></a>
        </div>
    </td>
    <g:if test="${allowImageUpload.equals('true')}">
        <td  class="types_image_link ${hasErrors(bean:typesInstance,field:'image','errors')}" id="new_image_id-${typesInstance?.image?.id}" style="display:none;">
            <div class="fileupload fileupload-new" data-provides="fileupload">
                <div class="uneditable-input">
                    <span class="fileupload-preview">Choose file</span>
                </div>
                <span class="btn btn-file">
                    <span class="fileupload-new">Browse</span>
                    <input type="file" name="typeImage" value="${fieldValue(bean:typesInstance,field:'image')}"/>
                </span>
                <a href="#" class="btn" onClick="Marketplace.showHideDiv('existing_image_id-${typesInstance?.image?.id}', 'new_image_id-${typesInstance?.image?.id}')">&times;</a>
            </div>

        </td>
    </g:if>
</g:if>
<g:else>
<g:if test="${allowImageUpload.equals('true')}">
    <td  class="nameAdmin ">
        <label for="image"><g:message code="label.typeImage" encodeAs="HTML" /></label>
    </td>
    <td  class="types_image_link admin_create_field ${hasErrors(bean:typesInstance,field:'image','errors')}">
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
   </g:if>
</g:else>
