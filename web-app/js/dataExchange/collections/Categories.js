define([
    'jquery',
    'underscore',
    'backbone',
    '../models/Category'

], function($, _, Backbone, Category){

    var SuperClass = Backbone.Collection;

    var categories = SuperClass.extend({

        model: Category,

        url: function() {
            return (Marketplace.context + '/public/category');
        },

        parse: function (resp) {
            return resp.data;
        }
    });

    return categories;

});