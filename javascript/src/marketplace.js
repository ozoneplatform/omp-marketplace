var Marketplace = Marketplace || {};
var context=Marketplace.context;

;(function ($) {
    if($) {
        // turn off caching through cache busting
        $.ajaxSettings.cache = false;

        $.magnificPopup && ($.magnificPopup.defaults.gallery.arrowMarkup = '<a title="%title%" type="button" class="mfp-icon-chevron-%dir% icon-chevron-%dir%" href="javascript:void(0)"></a>');
    }

    // no animations?
    if (!Marketplace.animationsEnabled) {
        // turn off CSS-based animations
        var turnOffTransitionsAndAnimationsCss =
            /**
             * NOTE: Ideally, this would disable animations
             * everywhere except where we want them always
             * enabled (ie, the loading spinner).
             * Unfortunately, I don't know of a way to write
             * that selector until
             * CSS4, when the :not selector gains more
             * capabilities.  So instead, we will have to
             * specifically add each place that we want to
             * disable animations to this selector
             */
            '.switch * {' +
                '-webkit-transition:    none !important;' + // Chrome, Safari
                '-moz-transition:       none !important;' + // Firefox
                '-ms-transition:        none !important;' + // IE
                '-o-transition:         none !important;' + // Opera
                'transition:            none !important;' + // CSS 3
                '-webkit-animation:     none !important;' + // Chrome, Safari
                '-moz-animation:        none !important;' + // Firefox
                '-ms-animation:         none !important;' + // IE
                '-o-animation:          none !important;' + // Opera
                'animation:             none !important;' + // CSS 3
            '}';

        $('head').append($('<style type="text/css">' + turnOffTransitionsAndAnimationsCss + '</style>'));

        // turn off jQuery-based animations
        $.fx.off = true;

        // disable bootstrap animations
        $(function () {
            $.support.transition = false;
        });
    }
})(jQuery);

Marketplace.UrlValidatorRegex = /^(((https|http|ftp|sftp|file):\/)|(\/)){1}(.*)+$/;
Marketplace.TitleBlankValidatorRegex = /^([^\s]|[^\s].*[^\s])$/;

/** add the selected components from the list **/
function addComponents(lhs, rhs)
{
    addSelectedValues(lhs, rhs, false);
    //%todo enable left arrow

}

/** remove the selected components from the list **/
function removeComponents(list)
{
    removeSelected(list);
    //%todo disable left arrow if no items
}

/*
 * limitText - supports the text countdown feature on the description textarea in create.gsp
 */
function limitText(limitField, limitCount, limitNum, elemId)
{
    var textField = limitField.value.replace(/\r/g, '');
    if (textField.length > limitNum)
    {
        limitField.value = textField.substring(0, limitNum);
    }
    else
    {
        document.getElementById(elemId).innerHTML = limitNum - textField.length;
    }
}

Marketplace.getHeaderHeight = function() {
    var $ = jQuery,
        //North Banner
        northBannerContentHeight = (($('#northBanner').outerHeight(true) == undefined) ? 0 : $('#northBanner').outerHeight(true)),
        //Header
        headerContentHeight = (($('#header').outerHeight(true) == undefined) ? 0 : $('#header').outerHeight(true));
        //Top Region (North Banner + Header)
        return ($('#northBanner').is(':visible') ? northBannerContentHeight : 0) + headerContentHeight;
};

Marketplace.getFooterHeight = function() {
    var $ = jQuery,
        //Footer
        footerContentHeight = (($('#footer').outerHeight(true) == undefined) ? 0 : $('#footer').outerHeight(true)),

        //South Banner
        southBannerContentHeight = (($('#southBanner').outerHeight(true) == undefined) ? 0 : $('#southBanner').outerHeight(true));

        //Bottom Region (South Banner + Footer)
        return ($('#southBanner').is(':visible') ? southBannerContentHeight : 0) + footerContentHeight;
};

Marketplace.getAvailableDocumentHeight = function() {

    var $ = jQuery;

    return $(document).height() - Marketplace.getHeaderHeight() - Marketplace.getFooterHeight();

};

Marketplace.hideLink = function(itemId) {
    var e = document.getElementById(itemId);
    if (e) {
        e.style.visibility = "hidden";
        e.style.width = "0px";
        e.style.display = "none";
    }
    return false;
};

Marketplace.applyAutoEllipsis = function(selector, length, isTextAsTooltip) {
    jQuery(selector).each(function(index, element) {
        var text = jQuery(element).text().trim();
        if (text.length > length) {
            jQuery(element).autoEllipsis({forceMaxLettersAllowed: length});
            if(isTextAsTooltip){
                jQuery(element).attr("title",text);
            }
        }
    });
};

Marketplace.error = function(msg) {
    Marketplace.showDialog('Error!',msg);
};

// Use this if a javascript function needs to know the current theme
Marketplace.getCurrentTheme = function(callback) {
    jQuery.ajax({
        url: Marketplace.context + "/theme/getCurrentTheme" ,
        dataType: "JSON"
        }).done(function ( data ) {
            callback(data.currentTheme);
    });
};

// Get the value of a configuration setting as JSON
// parameter: setting is an ApplicationSetting, such as "ABOUT_BOX_CONTENT"
Marketplace.getApplicationConfiguration = function(setting, callback) {
    jQuery.ajax({
        url: Marketplace.context + "/applicationConfiguration/getApplicationConfiguration/" + setting,
        dataType: "JSON"
    }).done(function ( data ) {
            callback(data.applicationConfiguration);
        });
};

Marketplace.showHideDiv = function(divIdToShow, divIdToHide){
    jQuery(document).ready(function(){
        jQuery("#"+divIdToShow).show();
        jQuery("#"+divIdToHide).hide();
    });
};

Marketplace.deleteTypeImageIcon = function(typesId, updaterContainerId, templatePage, domainInstanceName){
    var onDeleteSuccess = function(responseObj) {
        return jQuery.ajax({
            url: context +'/types/' + templatePage,
            dataType: 'html',
            data: {
                id: typesId,
                template: templatePage
            }
        }).done(function (data, textStatus, jqXHR) {
            jQuery('#' + updaterContainerId).html(data);
        });
    };

    var onDeleteFailure = function(){
        alert('Error deleting image');
    };

    var preformDeleteTypeImageIcon = function(){
        jQuery.ajax({
            url: context+'/types/imageDelete/' + typesId,
            method: 'post',
            data: {
                domainInstanceName: domainInstanceName
            }
        })
        .done(onDeleteSuccess)
        .fail(onDeleteFailure);
    };

    Marketplace.showConfirmationDialog('Save Changes?',
        'Are you sure you want to delete this Type Image Icon?')
        .promise().done(preformDeleteTypeImageIcon);
};

Marketplace.displayThumbnail = function(url, windowTitle, cls) {
        $.magnificPopup.open({
      items: {
        src: url
      },
      type: 'image'
    });
};

Marketplace.getScaledSize = function (maxWidth, maxHeight, currentWidth, currentHeight) {

    var ratio = Math.min( maxWidth / currentWidth, maxHeight/ currentHeight );
    return [Math.floor(ratio * currentWidth), Math.floor(ratio * currentHeight)];
};

Marketplace.adminDelete = function(msg, btn) {

    var handleDeleteYes = function() {

        var f = document.getElementById('theForm');
        var v = document.getElementById('valholder');
//        var b = btn;
        if (f) {
            if (btn && v) {
                v.name = btn.name;
                v.value = btn.value;
            }
            f.submit();
        }
    };


    Marketplace.showConfirmationDialog('Delete',msg).promise().done(handleDeleteYes);

    return false;
};

Marketplace.imageRoll = function(id, src) {
    var e;
    e = document.getElementById(id);
    if (e && src) {
        e.src = src;
    }
};

Marketplace.setCustomBannerTooltip = function(bannerId, tooltipHtml){
    jQuery('#' + bannerId).tooltip({
        html: true,
        title: tooltipHtml,
        placement: 'bottom',
        delay: 500
    });
};

Marketplace.handleShowAccessAlertWindow = function(params){

    var continueProcessingPage = function(){
        var url = params.redirectUrl.replace(/&amp;/g, "&") +
            window.location.hash;
        if(url.match(Marketplace.UrlValidatorRegex))
            window.location = url;
        else
            console.log("Invalid URL. Unable to continue page processing.");
    };

    var sessionShowAccessAlert = params.showAccessAlert;

    if ((sessionShowAccessAlert == "true") || (sessionShowAccessAlert == true)) {
        var accessAlertMsg = params.accessAlertMsg;
        var okButtonHandler = function() {

            var paramsMap = new Object();
            paramsMap["accessAlertShown"] = true;
            paramsMap["actionStatus"] = 200;

            if(params.redirectUrl != undefined) {
                paramsMap["redirectUrl"] = params.redirectUrl.replace(/&amp;/g, "&");
            }

            jQuery.ajax({
                url: context+'/profile/updateSessionAccessAlertShown',
                method: 'post',
                data: paramsMap
            }).done(function () {
                continueProcessingPage();
            }).fail(function () {
                Marketplace.showDialog("Error", params.settingSessionDataErrorMsg);
                continueProcessingPage();
            });
        };

        var okButton = jQuery("#access-alert .btn");

        // NOTE: We allow HTML formatting within the access alert message.
        // Use DOM innerHTML property instead of jQuery.html() to prevent
        // script injection attack. By this method formatting tags, like <i>
        // will still be honored; and <script> will do nothing. Note that we
        // need to take an extra step to guard against the onload attribute
        // that can be provided in <img> tags and such.
        jQuery("#access-alert .modal-body").get(0).innerHTML = '' +
            accessAlertMsg.replace(/on\w+\s*=/gi, '');

        okButton.click(okButtonHandler);

        // Allow any key press to be a click while the button has focus
        okButton.keypress(function() { okButton.click(); });

        jQuery("#access-alert").css('visibility', 'visible');

        var handleEnterOrSpaceKeyInput = function(event) {
            var KEYCODE_ENTER = 13;
            var KEYCODE_SPACE = 32;

            if (event.keyCode == KEYCODE_ENTER ||
                event.keyCode == KEYCODE_SPACE) {
                event.preventDefault();
                okButtonHandler();
            }
        };

        jQuery("body").keypress(function(event) {
            handleEnterOrSpaceKeyInput(event);
        });

        okButton.focus();
    }
    else {
        continueProcessingPage();
    }
};

Marketplace.handleLogout = function(params){
    var getCookie = function(cookie_name){
        var results = document.cookie.match ( '(^|;) ?' + cookie_name + '=([^;]*)(;|$)' );

          if ( results ){
            return ( unescape ( results[2] ) );
          }else{
            return null;
          }
    };

    var deleteCookie = function( cookie_name ) {
      var cookie_date = new Date ( );  // current date & time
      cookie_date.setTime ( cookie_date.getTime() - 1 );
      document.cookie = cookie_name += "=; max-age=0; expires=" + cookie_date.toGMTString();
    };

    var logout = function(){
        if(typeof params.redirectUrl !== "undefined" && params.redirectUrl !== ""){
            //Now Logout...
            window.location = params.redirectUrl;
        }
        //Remove Session Cookies...
        var JSessionIDCookie = getCookie("JSESSIONID");
        if(JSessionIDCookie != null){
            deleteCookie("JSESSIONID");
        }
    };

    owfdojo.xhrPost({
         url: context+'/profile/onBeforeLogout',
         content:{
             isSessionTimeout: (((params.isSessionTimeout != true) && (params.isSessionTimeout != "true")) ? false : true)
         },
         preventCache: true,
         sync: true,
         load: function(data, textStatus) {
             Marketplace.widget.closeWidget();
             logout();
         },
         error: function(_xhr, textStatus, error) {
             if(typeof params.settingSessionDataErrorMsg !== "undefined" && params.settingSessionDataErrorMsg !== ""){
                 Marketplace.showDialog("Error", params.settingSessionDataErrorMsg);
             }
             logout();
         }
   });
};

/***
 * CUSTOM FIELD DEFINITION
 * JAVASCRIPT METHODS
 * Methods used on the create and edit pages for CustomFormDefinitions
 */
Marketplace.loadDownCustomFieldDefinitionOptions = function(viewOptions){
    if(viewOptions.SHOW){
        //Show DropDown...
        Marketplace.showDropdown();
    }else if(viewOptions.HIDE){
        //Hide Drop Down Items
        Marketplace.hideDropdown();
    }
    //Ensure the view is properly set
    Marketplace.switchOptionMode({VIEW:true});

    //Update Hidden Options Ids List
    Marketplace.updateHiddenOptionIdsList();//Update the Hidden Field Value Id's List
};

Marketplace.handleEditFieldValueOption = function(listbox){
    var selIndex = listbox.selectedIndex;
    if(-1 == selIndex) {
        alert("Please select an option to edit.");
        return;
    }
    var optionToEdit = listbox.options[selIndex];
    if(optionToEdit.className.indexOf("OMP_DisabledFieldOption") != -1){
        alert("Please enable option '"+optionToEdit.text+"' to edit.");
        return;
    }
    jQuery("#fieldValueEditCtrlListContainerId").html("");//Reset Edit CTRL LIST
    jQuery("#fieldValueList option").each(function(){
        var currOption = this;
        if(currOption.id == optionToEdit.id){
            //Add Input Item to be Edited (what was selected)...
            Marketplace.updateEditCtrlInputList({
                optionId:this.id,
                inputName:"edit_input_name_"+this.id,
                inputValue:this.value
            });
            //Update Label Info
            jQuery("#fieldValueEditOptionMsgContainerLabelId").html(" '"+this.value+"'");
        }else{
            //Add other Items...
            Marketplace.updateEditCtrlList({
                optionId:this.id,
                inputValue:this.value,
                inputDisabledClass: (this.className.indexOf("OMP_DisabledFieldOption")  != -1) ? "OMP_DisabledFieldOption " : ""
            });
        }
    });
    //Switch to Edit Mode
    Marketplace.switchOptionMode({EDIT:true});
};

Marketplace.updateEditCtrlInputList = function(updateOptions){
    jQuery("#fieldValueEditCtrlListContainerId").append(''+
        '<div id="div_input_'+updateOptions.optionId+'"  class="field_val_text_input">'+
        '<input size="32" maxlength="20" id="fv_edit_input_'+updateOptions.optionId+'" type="text" name="'+updateOptions.inputName+'" value="'+updateOptions.inputValue+'"/>'+
        '<input id="prev_fv_edit_input_'+updateOptions.optionId+'" type="hidden" name="'+updateOptions.inputName+'" value="'+updateOptions.inputValue+'"/>'+
        '<span class="fv-edit-controls">'+
        '   <span title="Click to update" '+
        '         class="field_val_span_input_update"'+
        '         onclick="Marketplace.handleOptionEditUpdate({inputName:\''+updateOptions.inputName+'\', optionId:\''+updateOptions.optionId+'\'});"></span>'+
        '   <span title="Click to cancel" '+
        '         class="field_val_span_input_cancel"'+
        '         onclick="Marketplace.handleOptionEditCancel();"></span>'+
        '</span>'+
        '</div>');
    //Grab the Enter/ESC Key in Edit Mode...
    var handleEnterKeyInput = function(event){
        var KEYCODE_ENTER = 13;
        if (event.keyCode == KEYCODE_ENTER) {//Enter Key Pressed
         event.preventDefault();
         Marketplace.handleOptionEditUpdate({
            inputName: updateOptions.inputName,
            optionId: updateOptions.optionId
         });
       }
    };

    var handleEscKeyInput = function(event){
        var KEYCODE_ESC = 27;
        if (event.keyCode == KEYCODE_ESC) {//ESC Key Pressed
            event.preventDefault();
            Marketplace.handleOptionEditCancel();
       }
    };
    jQuery("#fv_edit_input_"+updateOptions.optionId).unbind('keypress keyup');//Remove prev binds...
    jQuery("#fv_edit_input_"+updateOptions.optionId).keypress(function(event) {
       handleEnterKeyInput(event);
    });
    jQuery("#fv_edit_input_"+updateOptions.optionId).keyup(function(event) {
       handleEscKeyInput(event);
    });
};

Marketplace.updateEditCtrlList = function(updateOptions){
  jQuery("#fieldValueEditCtrlListContainerId").append(''+
        '<div id="div_text_'+updateOptions.optionId+'"'+
        '  title="'+updateOptions.inputValue + (updateOptions.inputDisabledClass ? ' is Disabled' : '') + '"' +
        '  class="'+updateOptions.inputDisabledClass+'field_val_text_input">'+
        ''+updateOptions.inputValue+
        '</div>');
};

Marketplace.updateHiddenOptionIdsList = function(){
    jQuery("#fieldValueHiddenFVIdListContainerId").html("");//Reset Edit CTRL LIST
    jQuery("#fieldValueList option").each(function(){
        var fieldValueId = jQuery(this).attr("fieldValueId");
        jQuery("#fieldValueHiddenFVIdListContainerId").append(''+
            '<input type="hidden" name="_fv_hidden_id_'+this.value+'" value="'+ (fieldValueId ? fieldValueId : '-1')+'"/>'+
            '');
    });
};

Marketplace.switchOptionMode = function(modeOptions){
    if(modeOptions.EDIT){
        jQuery("#fieldValueSelectListContainerId").hide(); //Hide the Select List Container
        jQuery("#fieldValueAddOptionButtonContainerId").hide();//Hide the Add Option Button Container
        jQuery("#fieldValueEditOptionMsgContainerId").show();//Show the Edit Option Message Container
        jQuery("#fieldValueEditCtrlListContainerId").show();//Show the Edit Ctrl List Container
        jQuery(".field_val_text_input input:text:visible:first").focus();//Set focus on input
    }else if(modeOptions.VIEW){
        jQuery("#fieldValueEditCtrlListContainerId").html("");//Reset Edit CTRL LIST
        Marketplace.updateHiddenOptionIdsList();//Update the Hidden Field Value Id's List
        jQuery("#fieldValueEditOptionMsgContainerId").hide();//Hide the Edit Option Message Container
        jQuery("#fieldValueEditCtrlListContainerId").hide();//Hide the Edit Ctrl List Container
        jQuery("#fieldValueSelectListContainerId").show(); //Show the Select List Container
        jQuery("#fieldValueAddOptionButtonContainerId").show();//Show the Add Option Button Container
        jQuery("#fieldValueList option").each(function(){
            Marketplace.updateEnableDisableOptionTitle(jQuery(this));
        });
        //Handle Add Option Input Enter Key Presses...
        var handleEnterKeyInput = function(event){
            var KEYCODE_ENTER = 13;
            if (event.keyCode == KEYCODE_ENTER) {//Enter Key Pressed
                 event.preventDefault();
                 Marketplace.addOption(
                    document.saveForm.fieldOptions,
                    document.saveForm.newOptionInput
                 );
           }
        };
        jQuery("#newFieldValueAddOptionInputId").unbind('keypress');//Remove prev binds...
        jQuery("#newFieldValueAddOptionInputId").keypress(function(event) {
            handleEnterKeyInput(event);
        });
    }
};

Marketplace.updateEnableDisableOptionTitle = function(optionByJQuery) {
    var replaceAbleDisableTitleText = " is Disabled";
    var prevTitle = optionByJQuery.attr("title");
    prevTitle = jQuery.trim(prevTitle);
    prevTitle = prevTitle.replace(replaceAbleDisableTitleText, "");
    if(optionByJQuery.hasClass("OMP_DisabledFieldOption")){
        optionByJQuery.attr("title", prevTitle+replaceAbleDisableTitleText);
    }else{
        optionByJQuery.attr("title", prevTitle);
    }
};

Marketplace.handleOptionEditCancel = function() {
    //Perform no update...return
    //Switch to View Mode
    Marketplace.switchOptionMode({VIEW:true});
};

Marketplace.handleOptionEditUpdate = function(updateOptions){
    var updatedVal = jQuery("#fv_edit_input_"+updateOptions.optionId).val();
    var prevVal = jQuery("#prev_fv_edit_input_"+updateOptions.optionId).val();
    updatedVal = jQuery.trim(updatedVal);
    prevVal = jQuery.trim(prevVal);
    if(updatedVal == ''){
        jQuery("#fv_edit_input_"+updateOptions.optionId).val(prevVal);
        alert("Please enter text for the option '"+prevVal+"' being edited.");
        jQuery("#fv_edit_input_"+updateOptions.optionId).focus();
    }else{
        if((updatedVal != prevVal) && Marketplace.containsOption(jQuery("#fieldValueList")[0], updatedVal))
        {
          alert("The option list already contains " + updatedVal + ".");
          return;
        }
        jQuery("#"+updateOptions.optionId).val(updatedVal);
        jQuery("#"+updateOptions.optionId).attr("title", updatedVal);
        jQuery("#"+updateOptions.optionId).html(updatedVal);
        Marketplace.switchOptionMode({VIEW:true});
    }
};

Marketplace.showDropdown = function() {
  jQuery("#dropdownAdminRow").show();
  jQuery("#dropdownAddRow").show();
};

Marketplace.hideDropdown = function() {
  jQuery("#dropdownAdminRow").hide();
  jQuery("#dropdownAddRow").hide();
};
Marketplace.showCustomFieldAdmin = function(name) {
    jQuery(".customFieldAdmin").hide();
    jQuery("."+name).show();
};
Marketplace.enableTypesSelector = function(element) {
console.log('Marketplace.enableTypesSelector: element.checked = ', element.checked);
//jQuery(".shuttleBox").attr('disabled', element.checked);
jQuery("select[name=typeOptions]").attr('disabled', element.checked);
jQuery("select[name=types]").attr('disabled', element.checked);
};

Marketplace.listboxMove = function(listbox, direction)
{
    var selIndex = listbox.selectedIndex;
    if(-1 == selIndex) {
        alert("Please select an option to move.");
        return;
    }
    var increment = -1;
    if(direction == 'up'){
        increment = -1;
    }else if(direction == 'down'){
        increment = 1;
   }

    if((selIndex + increment) < 0 || (selIndex + increment) > (listbox.options.length-1))
    {
        return;
    }
    var selValue = listbox.options[selIndex].value;
    var selText = listbox.options[selIndex].text;
    var selId = listbox.options[selIndex].id;
    var selTitle = listbox.options[selIndex].title;
    var selClassName = listbox.options[selIndex].className;

    listbox.options[selIndex].id = listbox.options[selIndex + increment].id;
    listbox.options[selIndex].value = listbox.options[selIndex + increment].value;
    listbox.options[selIndex].text = listbox.options[selIndex + increment].text;
    listbox.options[selIndex].title = listbox.options[selIndex + increment].title;
    listbox.options[selIndex].className = listbox.options[selIndex + increment].className;

    listbox.options[selIndex + increment].id = selId;
    listbox.options[selIndex + increment].value = selValue;
    listbox.options[selIndex + increment].text = selText;
    listbox.options[selIndex + increment].title = selTitle;
    listbox.options[selIndex + increment].className = selClassName;

    listbox.selectedIndex = selIndex + increment;

    if(direction == 'up'){
        if(jQuery("#fieldOptionDownButton").attr("disabled")){
            jQuery("#fieldOptionDownButton").attr("disabled", false);
        }
        if(jQuery("#fieldValueList").attr("selectedIndex") == 0){
            jQuery("#fieldOptionUpButton").attr("disabled", true);
        }
    }else if(direction == 'down'){
        if(jQuery("#fieldOptionUpButton").attr("disabled")){
            jQuery("#fieldOptionUpButton").attr("disabled", false);
        }
        if(jQuery("#fieldValueList").attr("selectedIndex") == (jQuery("#fieldValueList")[0].length - 1)){
            jQuery("#fieldOptionDownButton").attr("disabled", true);
        }
    }
};

Marketplace.containsOption = function(selectItem, newOption) {
  var returnValue=false;
  for (var i=0; i < selectItem.options.length; i++) {
     if (selectItem.options[i].text == newOption) {
       returnValue=true;
       break;
     }
  }
  return returnValue;
};

Marketplace.addOption = function(selectbox, optionInput)
{
    var optn = document.createElement("OPTION");
    var newValue = optionInput.value;
    newValue = jQuery.trim(newValue);
    if(newValue == '')
    {
      alert("Please enter text for the option to add.");
      return;
    }
    if(Marketplace.containsOption(selectbox, newValue))
    {
      alert("The option list already contains " + newValue + ".");
      return;
    }
    var currOptnsLength = selectbox.options.length;
    optn.id = "fv_cfdid_idx_"+currOptnsLength;
    optn.text = newValue;
    optn.value = newValue;
    optn.title = newValue;
    optn.className = "OMP_FieldOption";
    selectbox.options.add(optn);
    optionInput.value = '';
    //Update Hidden Options Ids List
    Marketplace.updateHiddenOptionIdsList();//Update the Hidden Field Value Id's List
    Marketplace.fieldValueChanged();
};

// Changes the enable/disable button when the selected option is changed so that
// the button will say 'Enable' if the selected option is disabled and will say
// 'Disable' if the selected option is enabled.
Marketplace.fieldValueChanged = function()
{
    if(jQuery("#fieldValueList option:selected").length == 1) {
        if(jQuery("#fieldOptionEnableDisableButton").length == 1){
            jQuery("#fieldOptionEnableDisableButton").attr("disabled", false);
        }
        jQuery("#fieldOptionEditButton").attr("disabled", false);
        jQuery("#fieldOptionUpButton").attr("disabled", false);
        jQuery("#fieldOptionDownButton").attr("disabled", false);
        jQuery("#fieldOptionDeleteButton").attr("disabled", false);

        if(jQuery("#fieldValueList").attr("selectedIndex") == 0){
            jQuery("#fieldOptionUpButton").attr("disabled", true);
        }

        if(jQuery("#fieldValueList").attr("selectedIndex") == (jQuery("#fieldValueList")[0].length - 1)){
            jQuery("#fieldOptionDownButton").attr("disabled", true);
        }

        Marketplace.updateEnableDisable();
   }else{
        if(jQuery("#fieldOptionEnableDisableButton").length == 1){
            jQuery("#fieldOptionEnableDisableButton").attr("disabled", true);
        }
        jQuery("#fieldOptionEditButton").attr("disabled", true);
        jQuery("#fieldOptionUpButton").attr("disabled", true);
        jQuery("#fieldOptionDownButton").attr("disabled", true);
        if(jQuery("#fieldValueList option:selected").length == 0){
            jQuery("#fieldOptionDeleteButton").attr("disabled", true);
        }else{
            jQuery("#fieldOptionDeleteButton").attr("disabled", false);
        }

        Marketplace.updateEnableDisable();
   }
};

Marketplace.isOnlySelectedOptionEnabled = function(){
    //No point in checking, the Enabled/Disabled button does not exist
    if(jQuery("#fieldOptionEnableDisableButton").length == 0){return null;}
    //No point in checking, there is no option selected
    if(jQuery("#fieldValueList option:selected").length == 0) {return null;}
    //There is only 1 option selected, and it's disabled
    if(jQuery("#fieldValueList option:selected").hasClass("OMP_DisabledFieldOption")) {return false;}
    //There is only 1 option selected, and it's the only option available
    if((jQuery("#fieldValueList option:selected").length == 1)
        && (jQuery("#fieldValueList")[0].length == 1)
        && (!jQuery("#fieldValueList option:selected").hasClass("OMP_DisabledFieldOption"))) {return true;}

    var disabledEnabledMarkupChain = "";
    //Search
    var selOptionMap = {};
    var prevCheckedMap = {};
    jQuery("#fieldValueList option:selected").each(function(){
        selOptionMap[""+jQuery(this)[0].id] = true; //Add to Map of existing ones
    });
    jQuery("#fieldValueList option:selected").each(function(){
        var currSelOption = jQuery(this);
        jQuery("#fieldValueList option").each(function(){
            var currOption = jQuery(this);
            if((currOption[0].id != currSelOption[0].id)
                && (!selOptionMap[""+currOption[0].id])
                && (!prevCheckedMap[""+currOption[0].id])){
                if(currOption.hasClass("OMP_DisabledFieldOption")){
                    disabledEnabledMarkupChain = disabledEnabledMarkupChain + " disabled";
                }else{
                    disabledEnabledMarkupChain = disabledEnabledMarkupChain + " enabled";
                }
                prevCheckedMap[""+currOption[0].id] = true;
            }
        });
    });

    if(disabledEnabledMarkupChain.indexOf("enabled") == -1){//Only Disabled values exist
        return true;
    }else{
        return false;
    }
};

Marketplace.updateEnableDisable = function(){
    if(jQuery("#fieldOptionEnableDisableButton").length == 0){return;}
    if(jQuery("#fieldValueList option:selected").length == 0) {return;}
    var disabledEnabledMarkupChain = "";
    jQuery("#fieldValueList option:selected").each(function(){
        if(jQuery(this).hasClass("OMP_DisabledFieldOption")){
            disabledEnabledMarkupChain = disabledEnabledMarkupChain + " disabled";
        }else{
            disabledEnabledMarkupChain = disabledEnabledMarkupChain + " enabled";
        }
        Marketplace.updateEnableDisableOptionTitle(jQuery(this));
    });
    if(disabledEnabledMarkupChain.indexOf("enabled") == -1){//Only Disabled values exist
        jQuery("#fieldOptionEnableDisableButton").val("Enable");
        jQuery("#fieldOptionEnableDisableButton").attr("disabled", false);
        //Cannot Edit Disabled Options
        jQuery("#fieldOptionEditButton").attr("disabled", true);
    }else if(disabledEnabledMarkupChain.indexOf("disabled") == -1){//Only Enabled values exist
        if((jQuery("#fieldValueList option:selected").length == jQuery("#fieldValueList")[0].length)
            || Marketplace.isOnlySelectedOptionEnabled()){
            //Cannot Disable All Options in the List
            jQuery("#fieldOptionEnableDisableButton").attr("disabled", true);
        }else{
            jQuery("#fieldOptionEnableDisableButton").val("Disable");
            jQuery("#fieldOptionEnableDisableButton").attr("disabled", false);
        }
        //Can only edit 1 enabled value at a time
        if(jQuery("#fieldValueList option:selected").length == 1) {
            jQuery("#fieldOptionEditButton").attr("disabled", false);
        }
    }
};

// Marks the selected option as disabled.
Marketplace.disableFieldOption = function(listbox)
{
  if(jQuery("#fieldValueList option:selected").length == 0) {
    // this shouldn't happen.
      alert("Please select an option to disable.");
      return;
  }
  jQuery("#fieldValueList option:selected").toggleClass("OMP_DisabledFieldOption");
  Marketplace.updateEnableDisable();
};

// sets the value of the hidden field 'valueEnableFlags' to a list of 1s and 0s
// indicating whether or not the corresponding option is enabled or disabled.
Marketplace.populateFieldValueEnabledList = function()
{
     var tmpString = "";
 var options = jQuery("#fieldValueList option");

 for(var i=0; i < options.length; i++) {
     if(i != 0){
       tmpString += ",";
     }
     var currentOption = options.eq(i);
     if(currentOption.hasClass("OMP_DisabledFieldOption")){
       tmpString += "0";
     }
     else {
       tmpString += "1";
     }
 }
  //alert("FieldValueEnabled = " + tmpString);
  jQuery("#valueEnableFlags").val(tmpString);
};


Marketplace.removeFieldValueOptions = function(list) {
    removeSelected(list);
    Marketplace.fieldValueChanged();
};

/**
 * @namespace
 */
Marketplace.widget = {
    back: function() {
        self.history.back();
    },

    forward: function() {
        self.history.forward();
    },

    addWidgetToOwf: function (widgetId, imageEl, url, doLaunch, callback) {
        url = url || Marketplace.url;
        Marketplace.widget.addWidget(widgetId, imageEl, url, false, !!doLaunch, callback);
    },

    // OP-712 Skip the check for required items for webapps (aka listings that are launchable but not addable to OWF)
    startWebappInOwf: function (widgetId, imageEl, url, doLaunch, callback)   {
        url = url || Marketplace.url;
        Marketplace.widget.addWidget(widgetId, imageEl, url, false, !!doLaunch, callback);
    },

    // If running as a OWF Widget, add 'widget' parameter to all URLs and forms
    addWidgetParameter: function() {
        if (Marketplace.widget.isWidget()) {
            jQuery('a').each(function() {
                if (this.href.indexOf('#') === -1 && (this.href.indexOf('http') >= 0 || this.href.charAt(0) == '/')) {
                    var href = Marketplace.widget.addWidgetParameterToUrl(this.href);
                    jQuery(this).attr('href', href);
                }
            });

            var input = jQuery("<input>").attr("type", "hidden").attr("name", "widget").val("true");
            jQuery('form').append(jQuery(input));
        }
    },

    addWidgetParameterToUrl: function(url) {
        if (Marketplace.widget.isWidget()) {
            if (url.indexOf('widget=true') < 0) {
                if (url.indexOf('?') >= 0) {
                    url += '&widget=true';
                }
                else {
                    if (!url.charAt(url.length - 1) == '/') {
                        url += "/";
                    }
                    url += '?widget=true';
                }
            }
        }
        return url;
    },

    _imageOffset: function (imageEl) {
        var $imageEl = jQuery(imageEl),
            $window = jQuery(window),
            image = $imageEl.offset();
        image.left = image.left - $window.scrollLeft();
        image.top = image.top - $window.scrollTop();
        image.width = $imageEl.width();
        image.height = $imageEl.height();
        image.URL = $imageEl.attr('src');
        return image;
    },

    /**
     * Add the specified widget and all its OWF-compatible dependencies to OWF container. Only used by the Marketplace Widget.
     * @param widgetId id of the widget selected by the user
     * @param {IMG-HTMLElement} imageEl image element of the widget selected by user
     * @param baseMarketplaceUrl the base Marketplace URL the OWF can use to get the list of widgets to add
     * @param hasDependencies - true, if the service item has dependent items
     * @param callback the function to be executed after the server received the request.
     */
    addWidget: function(widgetId, imageEl, baseMarketplaceUrl, hasDependencies, doLaunch, callback) {

        Ozone.eventing.Widget.widgetRelayURL = '/vendor/eventing/rpc_relay.uncompressed.html';

        var widgetEventingController = Ozone.eventing.Widget.getInstance();
        var addWidgetClient = Ozone.marketplace.AddWidgetClient.getInstance(widgetEventingController);

        var widgetData;
        if(this.isContainerVersionGreaterThan(7, 1)) {
            widgetData = {
                baseUrl: baseMarketplaceUrl,
                data: {
                    id: widgetId,
                    image: this._imageOffset(imageEl)
                },
                hasDependencies: hasDependencies,
                doLaunch: doLaunch
            };
        }
        else {
            widgetData = {
                widgetsJSON: {
                    baseUrl: baseMarketplaceUrl,
                    itemId: widgetId,
                    hasDependencies: hasDependencies
                }
            };
        }

        addWidgetClient.addWidget(widgetData, callback);
    },

    // AML-3225 Add stack stub
    addStackToOwf: function(stackUuid, imageEl, callback) {

        var widgetEventingController = Ozone.eventing.Widget.getInstance();
        var addWidgetClient = Ozone.marketplace.AddWidgetClient.getInstance(widgetEventingController);

        var serviceItem = { itemUuid: stackUuid };

        addWidgetClient.addStack({
            widgetsJSON: serviceItem,
            data: {
                id: stackUuid,
                image: this._imageOffset(imageEl)
            }
        }, callback);
    },

    /**
     * Launch a given stack within OWF.
     */
    launchStack: function(stackServiceItem, imageEl, callback) {
        var widgetEventingController = Ozone.eventing.Widget.getInstance();
        var addWidgetClient = Ozone.marketplace.AddWidgetClient.getInstance(widgetEventingController);

        addWidgetClient.launchStack({
            stackContext: stackServiceItem.owfProperties.stackContext,
            data: {
                id: stackServiceItem.uuid,
                image: this._imageOffset(imageEl)
            }
        }, callback);
    },

    isWidget: function() {
        return this.isRunningInOWF();
    },

    isRunningInOWF: function() {
        try {
            var configParams = JSON.parse(window.name);
            if (configParams && configParams.owf) {
                return true;
            }
        }
        catch(e) {}

        return false;
    },

    closeWidget: function() {
        if (Marketplace.widget.isWidget()) {
            Ozone.state.WidgetState.getInstance().closeWidget({guid: Ozone.Widget.getInstanceId()});
        }
    },

    initOwfAPI: function() {
        if (Marketplace.widget.isRunningInOWF()) {
            jQuery(function() {
                OWF.ready(function() {
                    //when an id is received on this channel, redirect to the detail page for
                    //the corresponding service item
                    OWF.Eventing.subscribe('ozone.marketplace.show', function(snd, id) {
                        if(typeof id == 'number') {
                            window.location = window.location.href.replace(/#$/, '') + '#quickview/' + id + '/admin';
                            window.location.reload();
                        } else {
                            console.log('Unable to reload window. ID ' + id + ' was not parsed correctly.');
                        }
                    });

                    //notify when a new page is loaded
                    OWF.Eventing.publish('ozone.marketplace.pageLoaded', window.location.toString());
                    OWF.notifyWidgetReady();
                });
            });
        }
    },

    /**
     * Uses RPC to test if the current user already has a given App (Stack)
     * or App Component (widget) in OWF.
     * @private
     * @param searchId Provide stackContext for a Stack and uuid otherwise.
     * @param callback Function executed upon completion. Receives a single
     *                 boolean argument.
     */
    isAlreadyInOwf: function(searchId, callback) {
        var me = this;

        var doCallback = function(result) {
            callback && callback(result);
        };

        // Check if we are running in OWF and can RPC to the container
        if (Marketplace.widget.isRunningInOWF() &&
            typeof gadgets !== 'undefined' && gadgets.rpc) {
            var serviceName = '_MARKETPLACE_LISTING_CHECK';

            // Stacks added to OWF from Marketplace WILL NOT be present in
            // OWF's stores until the My Apps window is open. As a result
            // the RPC call below will return false immediately after a
            // successful add (and until the user goes to OWF). We need a
            // stop gap hack to ensure the favorites icon can be displayed
            // immediately after the App is added to OWF. A simple solution
            // is a cache of recently added stack contexts.
            if (Marketplace.widget._recentlyAddedStackCache &&
                Marketplace.widget._recentlyAddedStackCache.indexOf(searchId) >= 0) {
                doCallback(true);
                return; // DO NOT do RPC below in this case
            }

            // Ask the OWF container if the user already has this listing
            OWF.ready(function() {
                gadgets.rpc.call('..', serviceName, doCallback, {guid: searchId});
            });
        } else {
            doCallback(false);
        }
    },

    /**
     * Gets the OWF container version.
     * @private
     * @returns {Object} Object with major and minor properties. Both are
     * numeric.
     */
    getContainerVersion: function() {
        // Only do string parsing once
        if (this._containerVersion) {
            return this._containerVersion;
        } else if (this.isWidget() && OWF) {
            var version = OWF.getContainerVersion();

            this._containerVersion = { major: 0, minor: 0 };

            if (version) {
                version = version.split('.');

                if (version[0]) {
                    this._containerVersion.major = Number(version[0]);
                }

                if (version[1]) {
                    this._containerVersion.minor = Number(version[1]);
                }
            }

            return this._containerVersion;
        } else {
            this._containerVersion = { major: 0, minor: 0 };

            return this._containerVersion;
        }
    },

    /**
     * Checks if the OWF container version is greater than the version specified
     * @param major the major version number
     * @param minor the minor version number
     * @private
     * @returns {Boolean} True only if the OWF container meets a specific
     * minimum version requirement.
     */
    isContainerVersionGreaterThan: function(major, minor){
        var version = this.getContainerVersion();
        return version.major > major ||
                (version.major === major && version.minor >= minor);
    },


    /**
     * Checks if the OWF container supports adding a stack from the Store.
     * @private
     * @returns {Boolean} True only if the OWF container meets a specific
     * minimum version requirement.
     */
    isStackAddable: function() {
        var version = this.getContainerVersion();

        return version.major > 7 ||
               (version.major === 7 && version.minor >= 3);
    },

    /**
     * Checks if the OWF container supports launching a stack from the
     * Store.
     * @private
     * @returns {Boolean} True only if the OWF container meets a specific
     * minimum version requirement.
     */
    isStackLaunchable: function() {
        var version = this.getContainerVersion();

        return version.major > 7 ||
               (version.major === 7 && version.minor >= 12);
    },

    canCheckIfUserHasListing: function () {
        var version = this.getContainerVersion();

        return version.major > 7 ||
               (version.major === 7 && version.minor >= 11);
    }
};


jQuery(function () {
    var $pagination = jQuery('.admin_paginate_buttons').addClass('pagination');
    if($pagination.children().length === 0) {
        return;
    }
    $pagination.wrapInner('<ul>').find('span, a').wrap('<li>').filter('.currentStep').parent().addClass('active');
    $pagination.find('.prevLink').html('&laquo;');
    $pagination.find('.nextLink').html('&raquo;');
});
