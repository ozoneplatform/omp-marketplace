define([
    'rejectionListing/CreateRejectionJustificationView',
    'models/ServiceItem',
    '../../fixtures/rejection-justifications.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(RejectionJustificationsView, ServiceItem, RejectionJustifications, $, _, sinon, Marketplace) {

    describe('Create service item rejection justification view', function() {
        var view, model, spy, server;

        beforeEach(function () {
            server = sinon.fakeServer.create();

            model = new ServiceItem({});

            view = new RejectionJustificationsView({
                model: model
            });
        });

        afterEach(function () {
            view && view.remove();

            server.restore();

            spy && spy.restore();
            spy = null;
        });

        it('renders correctly.', function () {
            spy = sinon.spy($.fn, 'selectpicker');
            view.render();

            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify(RejectionJustifications.RESPONSE)
            );

            expect(spy.calledOnce).to.be.ok();
            expect(view.$('.selectpicker').children().length).to.be(5);
        });

        it('rejects a listing.', function () {

            spy = sinon.spy(model, 'reject');
            view.render();

            view.$('textarea').val('description');
            view.submit();

            expect(spy.calledWith({
                'id': 3,
                'reason': 'Data Content',
                'description': 'description'
            })).to.be.ok();
        });

        it('updates remaining count on keyup.', function () {
            view.render();
            var description = 'description';
            view.$('textarea').val(description);
            view.$('textarea').trigger('keyup');

            expect(parseInt(view.$('.remaining-count').html(), 10)).to.be(2000 - description.length);
        });

    });

});
