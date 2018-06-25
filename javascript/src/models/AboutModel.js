define([
    'marketplace',
    'backbone'
], function(Marketplace, Backbone) {
    'use strict';

    var SuperClass = Backbone.Model;

    return SuperClass.extend({
        url: Marketplace.context + '/api'
    });
});
