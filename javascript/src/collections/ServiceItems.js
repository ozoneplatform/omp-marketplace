define([
    '../models/ServiceItem',
    'backbone',
    'jquery',
    'underscore'
],
function(ServiceItem, Backbone, $, _) {

    var SuperClass = Backbone.Collection;

    return SuperClass.extend({

        model: ServiceItem,

        url: function() {
            return (Marketplace.context + '/api/serviceItem/');
        },

        parse: function (resp) {
            return resp.data;
        }

    });

});