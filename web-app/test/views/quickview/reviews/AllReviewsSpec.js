define([
    'quickview/reviews/AllReviews',
    'backbone', 'jquery', 'underscore'
], function (AllReviewsView, Backbone, $, _) {

    describe('Quick View: All Reviews View', function () {
        var firstReviewModel, secondReviewModel, collection, view;

        beforeEach(function () {
            firstReviewModel = new Backbone.Model({
                userRate: 5,
                date: '12/12/12'
            });

            secondReviewModel = new Backbone.Model({
                userRate: 1,
                date: '12/11/12'
            });

            serviceItemModel = new Backbone.Model({ 
                avgRate: 5,
                isAffiliated: false,
                totalRate5: 1,
                totalRate4: 0,
                totalRate3: 0,
                totalRate2: 0,
                totalRate1: 0
            });

            collection = new Backbone.Collection([firstReviewModel]);

            view = new AllReviewsView({ collection: collection, serviceItemModel: serviceItemModel });
        });

        afterEach(function () {
            view.remove();
            Marketplace.resetConfigDefaults();
        });

        function assertRatingsAre (ratings) {
            var ratingsCount = collection.length,
                percent, $count, $progress, $rating;

            _.each(ratings, function(value, index) {
                percent = ratingsCount === 0 ? '0%' : Math.round(value * 100 / ratingsCount).toFixed(0) + '%';

                $rating = view.$el.find('.star-rating[data-rating="' + (index + 1) + '"]')
                $count = $rating.find('.count');
                $progress = $rating.find('.progress .bar');

                expect($count.data('count')).to.be(value);
                expect($count.text()).to.be('(' + value + ')');
                expect($progress.css('width')).to.be(percent);
            });
        }

        it('renders correctly', function () {
            view.render();

            assertRatingsAre([0, 0, 0, 0, 1]);
            expect(view.$el.find('.reviews-ratings-count h1').text()).to.be('5.0');
            expect(view.$el.find('.item-review').length).to.be(1);
        });

        it('correctly adjusts the ratings when the service item model data changes', function () {
            view.render();
            collection.unshift(secondReviewModel);
            serviceItemModel.set({ totalRate1: 1, avgRate: 3 })

            assertRatingsAre([1, 0, 0, 0, 1]);
            expect(view.$el.find('.reviews-ratings-count h1').text()).to.be('3.0');
            expect(view.$el.find('.item-review').length).to.be(2);
        });
    });
});
