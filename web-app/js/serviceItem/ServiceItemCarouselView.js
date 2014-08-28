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


/**
 * The Client-side carousel Backbone component
 */
define([
    '../views/CollectionView',
    './views/badge/ItemBadgeBlockView',
    'jquery',
    'spin',
    'bxslider'
], function(CollectionView, ItemBadgeBlockView, $, Spinner) {
    var ITEMS_PER_PAGE = 6,
        EMPTY_HTML = '<div class="no-results"><span>No Results</span></div>',
        WAITING_HTML = '<div class="carousel-waiting loading-mask-content">' +
            '<div class="loading-mask-spinner-container"></div>' +
            '<div class="loading-mask-message">Loading...</div>' +
        '</div>',
        PAGE_HTML = '<li class="carousel-page"></li>',
        PAGES_HTML = '<ul class="bxslider"></ul>',
        loadMaskOptions = {
            color: '#FFF',
            width: 2,
            length: 12,
            radius: 15,
            className: 'loading-spinner'
        };

    return CollectionView.extend({
        className: 'carousel carousel-invisible',

        CollectionItemView: ItemBadgeBlockView,

        //a wrapper around the main elements in order to allow subclasses
        //to separately include a header
        $body: null,

        //a promise that will resolve when the data from the collection
        //has returned.
        dataPromise: null,

        //the total size of the result set as reported by the server
        dataTotal: null,

        modelEvents: _.defaults({
            sync: 'onSync',
            error: 'onSyncError'
        }, CollectionView.prototype.modelEvents),

        initialize: function() {
            this.dataPromise = new $.Deferred();

            CollectionView.prototype.initialize.apply(this, arguments);
        },

        onSync: function(collection, response) {
            this.dataTotal = response.total;

            this.dataPromise.resolve();
        },

        onSyncError: function() {
            this.dataPromise.reject();
        },

        renderEmpty: function() {
            this.$body.empty().append($(EMPTY_HTML));
            return this;
        },

        //create and return a jquery element containing the carousel-inner element
        //with all of its pages and children rendered
        renderPages: function(models) {
            //the models for the current page, and the rest of them
            var $pageContainer = $(PAGES_HTML),
                $currentPage,
                currentPageModels,
                remainingModels = models,
                me = this,
                addPageFn = function(model) {
                    me.addOne(model, $currentPage);
                };

            //this loop executes once per page
            while (remainingModels.length > 0) {
                currentModels = remainingModels.slice(0, ITEMS_PER_PAGE);
                remainingModels = remainingModels.slice(ITEMS_PER_PAGE);

                //create the current page and add its models
                $currentPage = $(PAGE_HTML);
                _.each(currentModels, addPageFn);

                $pageContainer.append($currentPage);
            }

            return $pageContainer;
        },

        render: function() {
            var me = this;

            this.$body = $('<div class="carousel-body">');
            this.$el.append(this.$body);

            //wait until we have data before rendering
            this.dataPromise.done(function() {
                me.allowAdd = true;
                me.addAll();
            }).fail(function() {
                me.renderEmpty();
            });

            $(WAITING_HTML).appendTo(this.$body);
            this.spinner = new Spinner(loadMaskOptions);
            this.spinner.spin(this.$('.loading-mask-spinner-container')[0]);
            return this;
        },

        addAll: function() {
            var $pageContainer,
                multiplePages;

            if (this.collection.size() === 0) {
                this.renderEmpty();
            }
            else {
                $pageContainer = this.renderPages(this.collection.models);
                this.$body.empty().append($pageContainer);

                //add the multi-page class iff the total number of available results is greater than
                //one carousel page in size
                multiplePages = this.dataTotal > ITEMS_PER_PAGE;
                this.$el[multiplePages ? 'addClass' : 'removeClass']('multi-page');

                $pageContainer.bxSlider({
                    pager: true,
                    buildPager: function(slideIndex) {
                        // return an empty link so just the background image for the page
                        // will show but it will be properly styled
                        return '<a href=""></a>';
                    },
                    prevText: '',
                    nextText: ''
                });
            }
        }
    });
});
