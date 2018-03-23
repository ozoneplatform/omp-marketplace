require ([
    'jquery',
    'underscore',
    'backbone',
    'applicationConfiguration/router/Router',
], function($, _, Backbone,  ApplicationConfigurationRouter){


    jQuery.noConflict();

    $.ajaxSetup({ cache: false });

    //Not sure if this is a good entry pointto the application or not.  For now it will route events on this
    //page to the appropriate place
    var app_router = new ApplicationConfigurationRouter();

    //history.start() is called in RouterInit.js
});
