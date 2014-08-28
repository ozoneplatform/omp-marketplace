define([
    '../models/ServiceItem',
    'backbone'
], function(ServiceItemModel, Backbone) {
    'use strict';

    var SuperClass = Backbone.Collection;

    return SuperClass.extend({
        model: ServiceItemModel,

        profileUrl: null,

        initialize: function(models, options) {
            this.profileUrl = options.profileUrl;

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        url: function() {
            return this.profileUrl + '/serviceItem';
        }
    });
});
