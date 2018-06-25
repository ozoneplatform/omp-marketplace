define([
    'jquery',
    'underscore',
    'backbone',
    '../models/CustomFieldDefinition'

], function($, _, Backbone, CustomFieldDefinition){

    var SuperClass = Backbone.Collection;

    var customFieldDefinitions = SuperClass.extend({

        model: CustomFieldDefinition,

        url: function() {
            return (Marketplace.context + '/public/customFieldDefinition');
        },

        parse: function (resp) {
            return resp.data;
        }
    });

    return customFieldDefinitions;

});