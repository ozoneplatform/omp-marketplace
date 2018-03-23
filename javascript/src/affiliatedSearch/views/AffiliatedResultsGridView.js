/*
 * Copyright 2013 Next Century Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

define([
    '../../views/BaseView',
    '../../views/GridView',
    'views/LoadMask',
    '../../collections/AppendingPagingCollectionMixin',
    '../collections/AffiliatedSearchResultsCollection',
    'jquery',
    'underscore'
], function(BaseView, GridView, LoadMask, AppendingPagingCollectionMixin,
    AffiliatedSearchResultsCollection, $, _) {

    /*
     * Template for the top part of the view.  Expects the following parameters:
     *  iconUrl - The URL to the icon for this affiliated store
     *  name - The name of this affiliated store
     *  currentCount - The current number of items displayed on the page
     *  totalCount - The total number of items available
     */
    var HEADER_TMPL = _.template('<div class="header">' +
            '<img class="serverIcon" src="<%- iconUrl %>" />' +
            '<h1 class="search_results_title"><%- name %></h1>' +
            '<span class="franchise_search_result_count subtext">' +
                '<%- currentCount %> of <%- totalCount %>' +
            '</span>' +
        '</div>'),

        //html of the load more button
        LOAD_MORE_HTML = '<div class="load_more_link">Show more</div>',
        PAGE_SIZE = 24,
        CollectionClass = AffiliatedSearchResultsCollection.extend(
            _.extend({}, AppendingPagingCollectionMixin, {
                initialize: function(models, options) {
                    AppendingPagingCollectionMixin.initialize.call(this,
                        {pageSize: options.searchParams.max});

                    //both the mixin and the superclass attempt to handle the
                    //page size.  We only want the mixin to handle it, so we remove it
                    //before calling the superclass initialize
                    delete options.searchParams.max;
                    delete options.searchParams.offset;

                    AffiliatedSearchResultsCollection.prototype.initialize.apply(this,
                        arguments);
                }
            })
        );


    return BaseView.extend({
        className: 'affiliated-results-grid-view',

        events: {
            'click .load_more_link': 'loadMore'
        },

        modelEvents: {
            'fetchMore': 'onFetch'
        },

        //the inner GridView
        gridView: null,

        //The AffiliatedServerModel for the server being hit
        server: null,

        //the header element
        $header: null,

        //the load more button element
        $loadMore: null,

        initialize: function(options) {
            var me = this,
                serverId = options.serverId,
                serverCollection = window.Marketplace.affiliatedServers.collection,
                serverPromise = window.Marketplace.affiliatedServers.promise;

            serverPromise.done(function() {
                var server = serverCollection.get(serverId),
                    serverUrl = server.get('serverUrl'),
                    searchParams = _.defaults({max: PAGE_SIZE},
                            JSON.parse(options.searchParamsJSON));

                me.server = server;

                //For its collection, this view uses a subclass of AffiliatedSearchResultsCollection
                //that includes the AppendingPagingCollectionMixin
                me.collection = new CollectionClass(null, {
                    serverUrl: serverUrl,
                    searchParams: searchParams
                });

                //load the first page of results
                me.loadMore();

                me.gridView = new GridView({
                    collection: me.collection,

                    //pass the affiliated store's URL down to the item views
                    subViewParams: {context: serverUrl},
                    pageSize: PAGE_SIZE
                });
            });

            BaseView.prototype.initialize.apply(this, arguments);

            return this;
        },

        render: function() {
            this.renderHeader();
            this.renderGrid();

            return this;
        },

        /**
         * Render the header of the view.  This function is designed to be called
         * repeatedly as needed to update the count parameters
         * @param currentCount [Optional] The current number of items displayed
         * @param totalCount [Optional] The total number of items available
         */
        renderHeader: function(currentCount, totalCount) {
            var tmplParams = {
                currentCount: currentCount !== undefined ? currentCount : '-',
                totalCount: totalCount !== undefined ? totalCount : '-',
                iconUrl: this.server.get('icon').url,
                name: this.server.get('name')
            };

            if (this.$header) {
                this.$header.remove();
            }

            this.$header = $(HEADER_TMPL(tmplParams));
            this.$header.prependTo(this.$el);
        },

        /**
         * Render the grid.  This function should only be called once, when this parent component
         * is rendered
         */
        renderGrid: function() {
            this.gridView.render().$el.insertAfter(this.$header);
        },

        renderLoadMoreBtn:function(doRender) {
            if (this.$loadMoreBtn) {
                this.$loadMoreBtn.remove();
            }

            if (doRender) {
                this.$loadMoreBtn = $(LOAD_MORE_HTML);
                this.$el.append(this.$loadMoreBtn);
            }
            else {
                this.$loadMoreBtn = null;
            }
        },

        loadMore: function() {
            LoadMask.show();
            this.collection.fetchMore().always(function() {
                LoadMask.hide();
            });
        },

        onFetch: function(currentCount, totalCount) {
            this.renderHeader(currentCount, totalCount);
            this.renderLoadMoreBtn(currentCount < totalCount);
        }
    });
});
