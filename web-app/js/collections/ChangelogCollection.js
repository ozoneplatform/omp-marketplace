define([
    './PaginatedCollection',
    '../models/Changelog',
    'underscore',
    'jquery'
],
function(PaginatedCollection, Changelog, _, $) {

    return PaginatedCollection.extend({

        model: Changelog,

        initialize: function (models, options) {
            PaginatedCollection.prototype.initialize.call(this, models, options);

            this.id = options.id;
            this.context = options.context;
        },

        url: function() {
            return (this.context + '/api/serviceItem/' + this.id + '/activity/');
        },

        /*jshint camelcase: false */
        server_api: _.extend({}, PaginatedCollection.prototype.server_api, {
            sort: 'activityDate',
            dir: 'desc'
        })

    });

});
