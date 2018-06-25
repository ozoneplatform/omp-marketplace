define([
    './LoadMoreListView',
    '../serviceItem/views/badge/ItemBadgeBlockView',
    './BaseView',
    '../collections/ServiceItemsCollection',
    'underscore',
    'handlebars',
    'jquery',
    'marketplace'
],
function(LoadMoreListView, ItemBadgeBlockView, BaseView, ServiceItemsCollection, _, Handlebars, $, Marketplace) {


    return BaseView.extend({

        className: 'list-container',

        template: Handlebars.compile(
                    '<h3 class="list-container-title">{{title}}<span class="result-count"><span class="count"></span> of <span class="total"></span></span></h3>' +
                    '<div class="list-container-body"></div>'
                ),

        noResultsHtml: '<span class="no-results">No Results</span>',

        data: function () {
            return this.options;
        },

        ItemView: null,

        // options to pass to ItemView instance
        itemViewOptions: null,

        collection: null,

        loadMoreListOptions: function () {
            if(!this.ItemView) {
                throw 'ItemView must be specified.';
            }
            return {
                tagName: 'ul',
                ItemView: this.ItemView,
                collection: this.collection,
                emptyText: '<span class="no-results">No Results</span>',
                itemViewOptions: _.result(this, 'itemViewOptions')
            };
        },

        initializeListView: function () {
            this.listView = new LoadMoreListView({
                el: this.$('.list-container-body'),
                collection: this.collection,
                defaultListViewOptions: this.loadMoreListOptions()
            });
            this.listenTo(this.collection, 'sync', this.updateResultsCounter);
            return this.listView;
        },

        render: function () {
            this.$el.append(this.template(this.data()));
            this.initializeListView();
            this.listView.render();
            this.updateResultsCounter();
            return this;
        },

        remove: function () {
            this.listView && this.listView.remove();
            return BaseView.prototype.remove.call(this);
        },

        updateResultsCounter: function (collection, response, options) {
            if(this.collection.length) {
                this.$('.count').text(this.collection.length);
                this.$('.total').text(this.collection.totalCount);
                this.$('.result-count').show();
            }
            else {
                this.$('.result-count').hide();
            }
        }

    });

});