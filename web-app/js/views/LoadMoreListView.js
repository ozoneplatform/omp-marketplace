define([
    './ListView',
    './BaseView',
    'underscore'
],
function(ListView, BaseView, _) {

    return BaseView.extend({

        tpl: '<a href="#" class="btn btn-link load-more"><h4>Show More</h4></a>',

        events: {
            'click .load-more': 'loadMore'
        },

        className: 'list-group-load-more',

        ItemView: null,

        ListView: null,

        defaultListViewOptions: function () {
            return {
                tagName: 'ul',
                ItemView: this.ItemView,
                collection: this.collection,
                itemViewOptions: this.itemViewOptions
            };
        },

        initialize: function (options) {
            BaseView.prototype.initialize.call(this, options);
            this.initializeListView();
            this.listenTo(this.collection, 'sync', this.updateLoadMore);
        },

        initializeListView: function () {
            var listOptions = this.ListView ? {
                collection: this.collection
            } : _.result(this, 'defaultListViewOptions');

            var ListViewImpl = this.ListView || ListView;
            this.listView = new ListViewImpl(listOptions);
        },

        render: function () {
            this.$el.append(this.listView.render().$el, this.tpl);
            this.updateLoadMore();
            return this;
        },

        loadMore: function (evt) {
            evt.preventDefault();
            this.collection.nextPage().always(_.bind(this.updateLoadMore, this));
        },

        updateLoadMore: function () {
            this.$el.children('.load-more')[this.collection.canLoadMore() ? 'show' : 'hide']();
        },

        remove: function () {
            this.listView.remove();
            return BaseView.prototype.remove.call(this);
        }

    });

});