define([
    'views/LoadMoreListWithCountView',
    'collections/PaginatedCollection',
    'jquery',
    'underscore',
    'sinon'
], function(LoadMoreListWithCountView, PaginatedCollection, $, _, sinon) {

    describe('Load more list with count view', function() {
        var view, collection, ItemView, spy;

        ItemView = Backbone.View;

        beforeEach(function () {
            collection = new PaginatedCollection([{
                user: 'John Doe'
            }, {
                user: 'John Doe2'
            }], {
                parse: true
            });

            view = new LoadMoreListWithCountView({
                title: 'Test Title',
                collection: collection,
                ItemView: ItemView
            });
        });

        afterEach(function () {
            view.remove();
            spy && spy.restore();
            spy = null;
        });

        it('does not render without ItemView option.', function() {
            delete view.ItemView;
            expect(_.bind(view.render, view)).to.throwException();
        });

        it('renders with title, count and children.', function() {
            spy = sinon.spy(ItemView.prototype, 'render');
            view.render();

            expect(spy.calledTwice).to.be.ok();
            expect(view.$('.list-container-title').html()).to.contain('Test Title');
            expect(view.$('.count').html()).to.contain(2);
            expect(view.$('.total').html()).to.contain(2);
        });

        it('updates counter on sync.', function() {
            spy = sinon.spy(view, 'updateResultsCounter');
            view.render();

            collection.pop();
            collection.trigger('sync');

            expect(view.$('.count').html()).to.contain(1);
        });

    });

});