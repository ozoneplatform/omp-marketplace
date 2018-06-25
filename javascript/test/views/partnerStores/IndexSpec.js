define([
    'partnerStores/index',
    'partnerStores/StoresView',
    '../../fixtures/partnerStores.js',
    'sinon',
    'marketplace'
], function (IndexView, StoresView, PartnerStores, sinon, Marketplace) {

    describe('Partner stores: Index view', function () {
        var view, model, server, spy, stub, showMaskStub, hideMaskStub;

        beforeEach(function () {
            server = sinon.fakeServer.create();
            showMaskStub = sinon.stub(Marketplace, 'showLoadMask');
            hideMaskStub = sinon.stub(Marketplace, 'hideLoadMask');

            view = new IndexView();
        });

        afterEach(function () {
            view.remove();

            showMaskStub.restore();
            hideMaskStub.restore();

            server.restore();

            spy && spy.restore();
            spy = null;

            stub && stub.restore();
            stub = null;
        });

        it('binds to add event if collection is empty.', function () {
            view.render();

            spy = sinon.spy(view, 'listenToOnce');

            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify([])
            );

            expect(spy.calledOnce).to.be.ok();
            expect(spy.calledWith(view.collection, 'add', view.renderStoresView)).to.be.ok();
        });

        it('renders list view when request completes.', function () {
            view.render();

            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify(PartnerStores)
            );

            expect(view.$el.find('.affiliated-stores > tbody > tr').length).to.be(1);
        });

    });

});