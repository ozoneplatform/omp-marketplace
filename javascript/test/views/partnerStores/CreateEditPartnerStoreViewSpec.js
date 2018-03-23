define([
    'partnerStores/CreateEditPartnerStoreView',
    'affiliatedSearch/models/AffiliatedServersModel',
    '../../fixtures/partnerStores.js',
    'jquery',
    'sinon',
    'marketplace'
], function (CreateEditPartnerStoreView, AffiliatedServersModel, PartnerStores, $, sinon, Marketplace) {

    describe('Partner stores: Create edit modal view', function () {
        var view, server, spy, stub;

        beforeEach(function () {
            server = sinon.fakeServer.create();
        });

        afterEach(function () {
            server.restore();

            spy && spy.restore();
            spy = null;

            stub && stub.restore();
            stub = null;
        });

        it('shows create window.', function () {
            var modal = CreateEditPartnerStoreView.create();

            expect(modal.$el.find('.free-text-warning').length).to.be(1);

            expect(modal.$el.find('input[name="id"]').length).to.be(0);

            expect(modal.$el.find('input[name="name"]').length).to.be(1);
            expect(modal.$el.find('input[name="name"]').attr('maxlength')).to.be('50');

            expect(modal.$el.find('input[name="serverUrl"]').length).to.be(1);
            expect(modal.$el.find('input[name="serverUrl"]').attr('maxlength')).to.be('2083');

            expect(modal.$el.find('input[name="timeout"]').length).to.be(1);
            expect(modal.$el.find('input[name="active"]').length).to.be(1);
            expect(modal.$el.find('input[name="iconFile"]').length).to.be(1);

            expect(modal.save()).to.be(false);

            modal.remove();
        });

        it('shows edit window.', function () {
            var model = new AffiliatedServersModel(PartnerStores.data[0]);
            var modal = CreateEditPartnerStoreView.edit(model);

            expect(modal.$el.find('input[name="id"]').length).to.be(1);
            expect(modal.$el.find('input[name="timeout"]').attr('min')).to.be('1');

            stub = sinon.stub(modal, '_submit', function () {
                return $.Deferred().resolve(PartnerStores.data[0]).promise();
            });

            spy = sinon.spy(model, 'set');

            modal.save();

            expect(spy.calledOnce).to.be.ok();

            modal.remove();
        });

    });

});