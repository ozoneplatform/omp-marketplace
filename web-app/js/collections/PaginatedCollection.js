define([
    'backbone.paginator',
    'jquery',
    'underscore',
    'marketplace'
],
function(Paginator, $, _, Marketplace) {

    var SuperClass = Paginator.requestPager,
        resultSize = Marketplace.appconfigs && _.findWhere(Marketplace.appconfigs, {code: 'store.amp.search.result.size'}).value,
        perPage = resultSize ? parseInt(resultSize, 10) : 12;


    return SuperClass.extend({

        /*jshint camelcase: false */
        paginator_core: {
            type: 'GET',
            dataType: 'json',
            url: function () {
                return _.result(this, 'url');
            }
        },

        /*jshint camelcase: false */
        paginator_ui: {
            firstPage: 0,
            currentPage: 0,
            perPage: 24,
            totalPages: 10
        },

        /*jshint camelcase: false */
        server_api: {
            // number of items to return per request/page
            'max': function() { return this.perPage; },
            // how many results the request should skip ahead to
            'offset': function() { return this.currentPage * this.perPage; }
        },

        // flag to indicate whether to only add and not remove on each fetch for 'Show more' behaviour
        infiniteMode: true,

        totalCount: 0,

        // last response from server
        lastResponse: null,

        initialize: function (models, options) {
            var server_api =  _.extend({}, this.server_api, options && options.server_api);
            _.defaults(this, options);
            this.server_api = server_api;
        },

        parse: function (response) {
            this.totalCount = response.total || response.totalCount || (_.isArray(response) ? response.length : 0);
            this.totalPages = Math.ceil(this.totalCount / this.perPage);
            return (response.data || response.rows || response);
        },

        fetch: function (options) {
            var colllection = this;

            if(this.infiniteMode === true) {
                options = _.extend({
                    remove: false
                }, options);
            }
            return SuperClass.prototype.fetch.call(this, options).done(function (response) {
                colllection.lastResponse = response;
            });
        },

        refresh: function () {
            if(this.infiniteMode !== true) {
                this.fetch();
            }
            else {
                this.fetch({
                    reset: true,
                    remove: true,
                    data: {
                        offset: 0,
                        max: this.perPage * (this.currentPage + 1)
                    }
                });
            }
        }

    });

});