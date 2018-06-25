define([
    '../../views/BaseView',
    '../../views/CollectionView',
    'views/Dialog',
    '../../serviceItem/ServiceItemCarouselView',
    '../collections/AffiliatedSearchResultsCollection',
    'jquery',
    'underscore',
    'backbone'
], function (BaseView, CollectionView, Dialog, ServiceItemCarouselView,
        AffiliatedSearchResultsCollection, $, _, Backbone) {

    var HEADER_HTML = '<h1 class="search-results-title">Our Partners</h1>',
        AFFILIATED_SERVERS_VIEW_ID = 'affiliated_marketplace_widget_grid_view_table';
        //the view used to render each affiliated server.  It is a specialization
        //of ServiceItemCarouselView that includes a custom header
        AffiliatedServerView = ServiceItemCarouselView.extend({
            className: ServiceItemCarouselView.prototype.className +
                " affiliated-store-search-results",

            //the model for the affiliated server being connected to
            serverModel: null,

            headerTemplate: _.template('<div class="header affiliated-store-info">' +
                '<img class="serverIcon" src="<%- iconUrl %>" />' +
                '<h2 class="search_results_title"><%- name %></h2>' +
                '<a href="#affiliated/<%- id %>/<%- searchParamsJSON %>" class="see-more subtext">see more</a>' +
            '</div>'),

            initialize: function(options) {
                ServiceItemCarouselView.prototype.initialize.apply(this, arguments);

                this.subViewParams = {context: this.serverModel.get('serverUrl')};
            },

            render: function() {
                var headerHtml = this.headerTemplate({
                        id: this.model.get('id'),
                        iconUrl: this.model.get('icon').url,
                        name: this.model.get('name'),
                        searchParamsJSON: encodeURIComponent(JSON.stringify(this.searchParams || {}))
                    });

                this.$el.empty().append(headerHtml);
                ServiceItemCarouselView.prototype.render.apply(this, arguments);

                return this;
            }
        });

    return CollectionView.extend({

        id: AFFILIATED_SERVERS_VIEW_ID,
        className: AFFILIATED_SERVERS_VIEW_ID,

        CollectionItemView: AffiliatedServerView,

        initialize: function (options) {
            CollectionView.prototype.initialize.apply(this, arguments);

            this.searchParams = _.defaults({
                max: options.searchResultSize
            }, options.searchParams);

            if (this.searchParams.accessType === 'User') {
                this.searchParams.accessType = 'Developer';
            }
        },


        render: function () {
            var me = this;

            $(HEADER_HTML).appendTo(this.$el);

            return CollectionView.prototype.render.apply(this, arguments);
        },


        //This will handle each server by executing the search then delegating to renderAffiliatedSearch
        createOne: function (model) {
            var me = this,
                fetchOpts = {
                    timeout: model.get("timeout")
                },

                //The collection for this server's listings
                resultsCollection = new AffiliatedSearchResultsCollection(null, {
                    serverUrl: model.get("serverUrl"),
                    searchParams: this.searchParams
                });


            resultsCollection.fetch(fetchOpts).fail(function (result) {
                if(isUserAdmin)
                    Dialog.show('Unexpected Error', "Error sending query to:  '" +
                        model.get('serverUrl') + "'.");
            });

            //the rendered view for this model
            return CollectionView.prototype.createOne.call(this, model, {
                collection: resultsCollection,
                serverModel: model,
                searchParams: this.searchParams
            });
        }
    });
});
