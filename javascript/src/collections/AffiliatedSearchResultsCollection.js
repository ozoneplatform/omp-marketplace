define([
    './PaginatedCollection',
    'models/ServiceItem',
    'jquery',
    'underscore',
    'backbone'
], function (PaginatedCollection, ServiceItemModel, $, _, Backbone) {
    var SHOW_LISTING_URL_PATH = 'serviceItem/show';
    var CLIENT_SIDE_AFFILIATED_SEARCH_URL = 'public/search';

    //A specialization of ServiceItemModel that has isAffiliated defaulted to true
    var AffiliatedServiceItemModel = ServiceItemModel.extend({
        defaults: {isAffiliated: true}
    });

    return PaginatedCollection.extend({

        model: AffiliatedServiceItemModel,

        /*jshint camelcase: false */
        paginator_core: {
            type: 'GET',
            dataType: 'jsonp',
            url: function () {
                return _.result(this, 'url');
            }
        },

        initialize: function (models, options) {
            var serverUrl = options.serverUrl;

            if (serverUrl.charAt(serverUrl.length - 1) !== '/') {
                serverUrl += '/';
            }

            this.url = serverUrl + CLIENT_SIDE_AFFILIATED_SEARCH_URL +
                '?' + $.param(options.searchParams);

            PaginatedCollection.prototype.initialize.call(this, models, options);
        }

    });

});
