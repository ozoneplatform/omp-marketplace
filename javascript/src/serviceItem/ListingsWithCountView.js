define([
    '../views/LoadMoreListWithCountView',
    '../views/LoadMoreListView',
    '../serviceItem/views/badge/ItemBadgeBlockView',
    'underscore'
],
function(LoadMoreListWithCount, LoadMoreListView, ItemBadgeBlockView, _) {


    return LoadMoreListWithCount.extend({

        title: 'Listings',

        ItemView: ItemBadgeBlockView,

        //the sub-route on the quickview that should be opened when
        //the listing is clicked
        quickviewTab: null,

        loadMoreListOptions: function () {
            return {
                tagName: 'div',
                className: 'listings-grid-view',
                ItemView: ItemBadgeBlockView,
                collection: this.collection,
                itemViewOptions: _.result(this, 'itemViewOptions'),
                emptyText: '<span class="no-results">No Results</span>'
            };
        },

        itemViewOptions: function() {
            return {
                quickviewTab: this.quickviewTab
            };
        }
    });
});
