define([
    'views/GlobalTagList',
    'backbone',
    'underscore',
    'jquery'
], function (GlobalTagListView, Backbone, _, $) {
    describe('GlobalTagList', function () {
        var view,
            collectionData = [{
                id: 1,
                title: "zTag",
                itemCount: "1"
            },{
                id: 2,
                title: "cTag",
                itemCount: "6"
            },{
                id: 3,
                title: "CZTag",
                itemCount: "1"
            },{
                id: 4,
                title: "mTag",
                itemCount: "4"
            }],
            collection,
            sortedTagTitles = ['cTag', 'CZTag', 'mTag', 'zTag'];

        beforeEach(function() {
            collection = new Backbone.Collection(collectionData);

            view = new GlobalTagListView({ collection: collection });

            view.render();
        });

        afterEach(function() {
            view.remove();
        });

        it('Creates a list entry for each tag in the collection, indexed and sorted correctly', function () {
            var tagIndexes = view.$('.tag-list-index'),
                tagTitles = _.map(view.$('.tag-list-item a'), function (el) {
                    return $(el).html();
                });

            expect(tagTitles.length).to.be(4);
            expect(tagIndexes.length).to.be(3);
            expect(_.isEqual(tagTitles, sortedTagTitles)).to.be(true);
        });

        it('Renders a cloud with the correct number of items', function () {
            view.renderAsCloud();

            var tagTitles = view.$('.cloud-item');

            expect(tagTitles.length).to.be(4);
        });
    });
});
