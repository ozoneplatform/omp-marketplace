define([
    './PaginatedCollection',
    '../models/Changelog',
    'backbone',
    'underscore',
    'jquery'
],
function(PaginatedCollection, Changelog, Backbone, _, $) {

    return PaginatedCollection.extend({

        model: Changelog,

        initialize: function (models, options) {
            this.context = options.context;
            PaginatedCollection.prototype.initialize.call(this, models, options);
        },

        url: function() {
            return (this.context + '/api/serviceItem/activity/');
        }

    });

});
