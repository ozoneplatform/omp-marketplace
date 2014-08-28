define(
[
    '../views/tabbable/TabPaneView',
    './reviews/AllReviews',
    './reviews/CurrentUserReview',
    '../models/Review',
    'backbone',
    'jquery',
    'underscore'
],
function(TabPaneView, AllReviews, CurrentUserReview, Backbone, $, _) {

    var SuperClass = TabPaneView;

    return SuperClass.extend({

        title: 'Reviews',

        href: function () {
            return 'quickview-' + this.title.toLowerCase();
        },

        serviceItemModel: null,

        affiliatedError: 'Reviews are not available for Affiliated Stores versions 7.3 and earlier.',

        render: function() {
            this.$el.append(this.markup);
            return this.fetchAndRender();
        },

        fetchAndRender: function () {
            var me = this,
                reviews = me.serviceItemModel.reviews();

            reviews.fetch({data: {accessAlertShown: true}})
                .done(function () {
                    me.allReviews = new AllReviews({
                        collection: reviews,
                        serviceItemModel: me.serviceItemModel
                    });

                    me.currentUserReview = new CurrentUserReview({
                        serviceItemModel: me.serviceItemModel,
                        collection: reviews,
                        model: reviews.findWhere({username: Marketplace.user.username})
                    });

                    me.$el.append(
                        me.allReviews.render().el,
                        me.currentUserReview.render().el
                    );
                })
                .fail(function () {
                    me.$el.append('Unable to retrieve reviews.');
                });

            return me;
        },

        remove: function () {
            this.allReviews && this.allReviews.remove();
            this.currentUserReview && this.currentUserReview.remove();
            return SuperClass.prototype.remove.call(this);
        }

    });
});
