define([
    'jquery',
    'underscore',
    'backbone',
    '../../serviceItem/models/ServiceItemModel'
], function($, _, Backbone, ServiceItemModel){
    var SHOW_LISTING_URL_PATH = 'serviceItem/show';
    var CLIENT_SIDE_AFFILIATED_SEARCH_URL = 'public/search';

    //A specialization of ServiceItemModel that has isAffiliated defaulted to true
    var AffiliatedServiceItemModel = ServiceItemModel.extend({
        defaults: {isAffiliated: true}
    });

    return Backbone.Collection.extend({

        initialize: function(models, options){
            var serverUrl = options.serverUrl;

            if (serverUrl.charAt(serverUrl.length - 1) !== '/') {
                serverUrl += '/';
            }

            this.url = serverUrl + CLIENT_SIDE_AFFILIATED_SEARCH_URL +
                '?' + $.param(options.searchParams);

            this.on('reset', _.bind(this.setDetailListingUrl, this));
        },

        model: AffiliatedServiceItemModel,

        //The root is in the 'data' key so we need to override parse
        parse: function(response){
            return response.data;
        },

        //Override of the base sync method so that when its a read we can use datatype of 'jsonp'
        sync:   function(method, model, options) {
            if(method == 'read')
                options.dataType = 'jsonp';
            return Backbone.sync(method, model, options);
        },

        //Iterate through the models and set the detail listing link
        setDetailListingUrl: function(baseUrl){
            var showURL = SHOW_LISTING_URL_PATH;
            _.each(this.models, function(model) {
                model.set({
                    'baseUrl': baseUrl,
                    'detailListingUrl': baseUrl + showURL + '/' + model.get('id') + '?accessType=User'
                });
            });
        }
    });
});
