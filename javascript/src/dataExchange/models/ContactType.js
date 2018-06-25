define([
    'jquery',
    'underscore',
    'backbone'
], function($, _, Backbone){

    var SuperClass = Backbone.Model;

    return SuperClass.extend({
        url: function() {
            return (Marketplace.context + '/api/contactType/' + this.id);
        }
    });
});
