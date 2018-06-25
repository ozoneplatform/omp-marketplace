define(
[
    '../../views/BaseView',
    '../../views/ListView',
    './ReviewItemView',
    'backbone',
    'jquery',
    'underscore',
    'handlebars',
    'raty'
],
function (BaseView, ListView, ReviewItemView, Backbone, $, _, Handlebars, Raty) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'all-reviews',

        // collection of reviews
        collection: null,

        markup:     Handlebars.compile('<div class="reviews-header">' +
                        '<div class="reviews-ratings-count">' +
                            '<h1></h1><p>User Rating</p>' +
                        '</div>' +
                        '<div class="reviews-filter"></div>' +
                    '</div>' +
                    '<div class="reviews-body"></div>'),

        ratingTpl:  Handlebars.compile('<div class="star-rating" data-rating="{{score}}">' +
                        '<span class="rating" data-rating="{{score}}"></span>' +
                        '<div class="progress">' +
                            '<div class="bar" style="width: 0%;"></div>' +
                        '</div>' +
                        '<span class="count" data-count=0>(0)</span>' +
                        '<a href="#" class="view-all hide">View All</a>' +
                    '</div>'),

        events: {
            'mouseenter .star-rating': 'showViewAll',
            'mouseleave .star-rating': 'hideViewAll',
            'click .star-rating': 'viewAllByRating',
            'click .view-all': 'viewAll'
        },

        serviceItemModel: null,

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);

            this.listenTo(this.serviceItemModel, 'change', this.updateHeader);
        },

        render: function () {
            this.$el.append(this.markup());

            this.renderHeader();
            this.renderBody();

            return this;
        },

        renderHeader: function () {
            var $header = this.$el.find('.reviews-filter'),
                i = 5;
            while (i > 0) {
                var html = this.ratingTpl({ score: i });
                $header.append(html);
                i--;
            }

            $header.find('.rating').raty({
                hints: ['', '', '', '', ''],
                readOnly: true,
                score: function() {
                    return $(this).attr('data-rating');
                }
            });

            this.updateHeader();
        },

        updateHeader: function () {
            var $header = this.$el.find('.reviews-filter'),
                totalRatings = this.collection.length,
                avgRate = this.serviceItemModel.get('avgRate').toFixed(1);

            var i = 5,
                width, count, $rating, $count, $progress;

            while (i > 0) {
                count = this.serviceItemModel.get('totalRate' + i);
                width = this.collection.length === 0 ? '0' : Math.round(count * 100 / totalRatings).toFixed(2);

                $rating = this.$el.find('.star-rating[data-rating="' + i + '"]');
                $count = $rating.find('.count');
                $progress = $rating.find('.bar');

                $progress.width( width + '%');
                $count.html('(' + count + ')').data('count', count).attr('data-count', count);

                i--;
            }

            this.$('.reviews-ratings-count h1').text(avgRate);
        },

        renderBody: function () {
            var $body = this.$el.find('.reviews-body');
            var isAffiliated = this.serviceItemModel && this.serviceItemModel.get('isAffiliated');
            this.listView = new ListView({
                el: $body,
                collection: this.collection,
                ItemView: ReviewItemView,
                emptyText: '<div class="empty">No Reviews Found!</div>',
                itemViewOptions: {
                    isAffiliated: isAffiliated
                }
            });
            this.listView.render();
        },

        showViewAll: function (evt) {
            var $el = $(evt.currentTarget);

            $el.removeClass('disabled-star-rating');
        },

        hideViewAll: function (evt) {
            var $el = $(evt.currentTarget);

            $el.data('disabled') === true && $el.addClass('disabled-star-rating');
        },

        viewAllByRating: function (evt) {
            evt.preventDefault();
            var $el = $(evt.currentTarget),
                score = $el.data('rating');

            // disable other star reviews
            this.$el
                .find('.star-rating')
                .addClass('disabled-star-rating')
                .data('disabled', true)
                .find('a')
                .hide();

            $el
                .removeClass('disabled-star-rating')
                .data('disabled', false)
                .find('a')
                .show();

            this.listView.filter(function (model) {
                return model.get('userRate') === score;
            });
        },

        viewAll: function (evt) {
            this.$el
                .find('.star-rating')
                .removeClass('disabled-star-rating')
                .data('disabled', false)
                .find('a')
                .hide();

            this.listView.removeFilters();
            return false;
        },

        remove: function () {
            this.listView.remove();
            this.$el.find('.rating').raty('destroy');
            return SuperClass.prototype.remove.call(this);
        }

    });
});
