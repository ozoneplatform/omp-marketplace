define([
    'quickview/reviews/ReviewItemView',
    'backbone', 'jquery', 'underscore'
], function (ReviewItemView, Backbone, $, _) {

    describe('QuickView: Review Item View', function () {
        var model, view,
            isAffiliated = false;

        beforeEach(function () {
            model = new Backbone.Model({
                text: 'review text',
                displayName: 'reviewer',
                date: '12/12/12',
                userRate: '2'
            });
        });

        afterEach(function () {
            view && view.remove();
            Marketplace.resetConfigDefaults();
        });

        function render () {
            view = new ReviewItemView({
                model: model,
                isAffiliated: isAffiliated
            }).render();
        }

        function assertEditableIs (editable) {
            expect(view.$el.hasClass('reviews-editable')).to.be(editable);
            expect(!!view.$el.find('.review-text').data('editable')).to.be(editable);
        }

        it('renders correctly', function () {
            render();

            expect(view.$el.find('.review-text').text()).to.be('review text');
            expect(view.$el.find('.item-review-author').text()).to.be('reviewer');
            expect(view.$el.find('.icon-star').length).to.be(2);
        });

        it('renders placeholder text if the comment is empty', function () {
            model.set({ text: '' });

            render();

            expect(view.$el.find('.review-text').text()).to.be('No comment provided.');
        });

        it('responds to changes on the model', function () {
            render();

            model.set({
                text: 'new text',
                userRate: '4'
            });

            expect(view.$el.find('.review-text').text()).to.be('new text');
            expect(view.$el.find('.icon-star').length).to.be(4);
        });

        it('removes itself when the model is destroyed', function () {
            render();

            sinon.spy(view, "remove");

            model.destroy();

            expect(view.remove.calledOnce);

            view.remove.restore();
        });

        it('is not editable if user is not an admin', function () {
            Marketplace.user.isAdmin = false;
            render();

            assertEditableIs(false);
        });

        it('is editable when a user is an admin', function () {
            Marketplace.user.isAdmin = true;

            render();

            assertEditableIs(true);
        });

        it('is not editable if this is an affiliated listing', function () {
            Marketplace.user.isAdmin = true;
            isAffiliated = true;

            render();

            assertEditableIs(false);
        });

    });
});
