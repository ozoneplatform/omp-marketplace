<%@ page import="grails.util.Holders" contentType="text/javascript" %>
<%@ page import="grails.converters.JSON" %>
<%@  page import="grails.util.Environment" %>
;
var Marketplace = Marketplace || {};
//externalize any config properties here as javascript properties
//this should be only place where these config properties are exposed to javascript
;(function() {
   //interpolate server values in as JSON
   var config = ${raw((conf as JSON).toString())};
    //auto convert to JSON this will take care of special characters
    Marketplace.url = config.url;
    Marketplace.environment = "${Environment.current}";
    Marketplace.context = config.context;

    //IMAGE resources
    Marketplace.image = Marketplace.image || {};
    Marketplace.image.s = config.blankImage;

    Marketplace.bannerBeanNorth = config.bannerBeanNorth;
    Marketplace.bannerBeanSouth = config.bannerBeanSouth;
    Marketplace.user = config.currUser;

    Marketplace.appconfigs = config.appconfigs;
    Marketplace.defaultState = config.defaultState;
    Marketplace.types = config.types;
    Marketplace.contactTypes = config.contactTypes;

    Marketplace.filterConfig = config.filterConfig;
    Marketplace.theme = config.theme;

    Marketplace.sessionTimeoutConfig = config.sessionTimeoutConfig;
    Marketplace.affiliatedSearchSize = config.affiliatedSearchSize;

    Marketplace.animationsEnabled = config.animationsEnabled;
    Marketplace.accessAlertConfig = config.accessAlertConfig;
    Marketplace.allowImageUpload = config.allowImageUpload;
    Marketplace.importTaskContext = config.importTaskContext;

    //TODO why are these methods defined here?
    Marketplace.viewPortInit = function() {
        jQuery(document).ready(function() {
            var $ = jQuery;

            // Force IE to not cache
            $.ajaxSetup({ cache: false });

            Marketplace.setCustomBannerTooltip('omp_header_logo_img', config.headerLogoTooltip);

            //This was wrapped in a seperate (document).ready


        });
    };

    Marketplace.getBuildNumber = function(){
        return config.buildNumber;
    }
})();
