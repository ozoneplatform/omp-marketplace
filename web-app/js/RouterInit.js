define(['MainRouter', 'backbone', 'jquery'], function(MainRouter, Backbone, $) {
    'use strict';

    var $container = $('#marketContentWrapper > .body');

    // admin pages
    if(window.location.href.indexOf('dataExchange') >= 0) {
        $container = $('#marketContentWrapper > .bootstrap-active > .body');
    }
    else if($container.length === 0) {
        $container = $('#marketContent > .body');
    }

    var router = new MainRouter({
        mainContainer: $container
    });

    //we currently have a few other routers that are created synchronously with the
    //page load. They all need to be created before history.start() is called.
    //TODO Consolidate the routers
    $(function() {
        Backbone.history.start();
    });

    //make the router globally accessible
    window.Marketplace.mainRouter = router;

    return router;
});
