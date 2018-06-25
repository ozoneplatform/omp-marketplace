define([
    'partnerStores/StoreView',
    'affiliatedSearch/models/AffiliatedServersModel',
    '../../fixtures/partnerStores.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function (StoreView, AffiliatedServersModel, PartnerStores, $, _, sinon, Marketplace) {

    describe('Partner stores: Store item view', function () {
        var view, model, stub;

        beforeEach(function () {
            model = new AffiliatedServersModel(PartnerStores.data[0]);
            view = new StoreView({
                model: model
            });
        });

        afterEach(function () {
            view.remove();
        });

        it('renders correctly.', function () {
            var stub = sinon.stub($.fn, 'svitch');

            view.render();
            expect(view.$el.find('td').length).to.be(5);
            expect(view.$el.find('span').text()).to.be(model.get('name'));

            expect(stub.calledOnce).to.be.ok();

            stub.restore();
        });

    });

});