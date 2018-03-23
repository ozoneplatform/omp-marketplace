define([
    'jquery',
    'underscore',
    'backbone',
    '../models/Type'

], function($, _, Backbone, Type){

    var SuperClass = Backbone.Collection;

    var types = SuperClass.extend({

        model: Type,

        url: function() {
            return (Marketplace.context + '/public/types');
        },

        parse: function (resp) {
            return resp.data;
        }
    });

    return types;

});