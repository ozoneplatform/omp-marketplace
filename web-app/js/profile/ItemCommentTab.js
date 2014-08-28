define([
    '../views/tabbable/TabPaneView',
    './MyReviewsView',
    './MyTagsView'
], function(TabPaneView, MyReviewsView, MyTagsView) {
    'use strict';

    var SuperClass = TabPaneView;

    return SuperClass.extend({
        className: 'item-comment-tab',

        title: 'Reviews & Tags',

        reviewsView: null,
        tagsView: null,

        initialize: function(options) {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.reviewsView = new MyReviewsView(options);
            this.tagsView = new MyTagsView(options);
        },

        render: function() {
            this.$el
                //the child views will render themselves
                .append(this.reviewsView.$el)
                .append(this.tagsView.$el);

            return this;
        },

        remove: function() {
            this.reviewsView.remove();
            this.tagsView.remove();

            return SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
