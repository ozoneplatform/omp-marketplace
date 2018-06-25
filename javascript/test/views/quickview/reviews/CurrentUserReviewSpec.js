define([
    'quickview/reviews/CurrentUserReview',
    'backbone', 'jquery', 'underscore'
], function (CurrentUserReviewView, Backbone, $, _) {

    describe('Quickview: Current User Review', function () {
        var view, model, serviceItemModel;

        beforeEach(function () {
            serviceItemModel = new Backbone.Model({
                isAffiliated: false
            });

            model = null;

            view = null;
        });

        afterEach(function () {
            view.remove();
            Marketplace.resetConfigDefaults();
        });

        function render () {
            view = new CurrentUserReviewView({
                model: model,
                serviceItemModel: serviceItemModel
            }).render();
        };

        function initModel () {
            model = new Backbone.Model({
                text: 'a review!',
                userRate: 4
            });
        };

        it('renders correctly with no review model', function () {
            render();

            expect(view.$el.find('.current-user-comment').val()).to.be('');
            expect(view.$el.find('.current-user-comment').attr('placeholder')).to.be('free text!');
            expect(view.$el.find('.free-text-warning').text()).to.be('free text!');
        });

        it('renders correctly with a review model', function () {
            initModel();
            render();

            expect(view.$el.find('.current-user-comment').val()).to.be('a review!');
            expect(view.$el.find('.current-user-comment').attr('placeholder')).to.be('free text!');
            expect(view.$el.find('.current-user-rating input').val()).to.be('4');
            expect(view.$el.find('.free-text-warning').text()).to.be('free text!');
        });

        it('responds correctly to a change in the review model', function () {
            initModel();
            render();

            model.set({
                text: 'another review!',
                userRate: 2
            });

            expect(view.$el.find('.current-user-comment').val()).to.be('another review!');
            expect(view.$el.find('.current-user-rating input').val()).to.be('2');
        });

        it('renders correctly when the service item is an affiliated listing', function () {
            serviceItemModel.set({ isAffiliated: true });
            render();

            expect(view.$el.find('.mask-text').text()).to.be('You cannot review listings from Affiliated Stores.');
            expect(view.$el.find('.current-user-comment').val()).to.be('');
        });
    });
});
