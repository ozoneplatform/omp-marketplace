define([
    'partnerStores/StoresView',
    'affiliatedSearch/collections/AffiliatedServersCollection',
    'affiliatedSearch/models/AffiliatedServersModel',
    '../../fixtures/partnerStores.js',
    'underscore',
    'sinon',
    'marketplace'
], function (StoresView, AffiliatedServersCollection, AffiliatedServersModel, PartnerStores, _, sinon, Marketplace) {

    describe('Partner stores: Store list view', function () {
        var view, collection;

        beforeEach(function () {
            collection = new AffiliatedServersCollection(PartnerStores.data);
            view = new StoresView({
                collection: collection
            });
        });

        afterEach(function () {
            view.remove();
        });

        it('renders correctly.', function () {
            view.render();
            expect(view.$el.find('tr').length).to.be(2);
        });

    });

});