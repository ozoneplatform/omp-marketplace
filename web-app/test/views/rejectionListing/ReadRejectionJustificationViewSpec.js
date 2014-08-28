define([
    'rejectionListing/ReadRejectionJustificationView',
    'models/ServiceItem',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(ReadRejectionJustificationView, ServiceItem, $, _, sinon, Marketplace) {

    describe('Read service item rejection justification view', function() {
        var view, model, spy, server;

        beforeEach(function () {
            server = sinon.fakeServer.create();

            model = new ServiceItem({});

            view = new ReadRejectionJustificationView({
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
            view.render();

            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify({
                    justification: {
                        title: 'title'
                    },
                    description: 'description'
                })
            );

            expect($(view.$('.controls').get(0)).html()).to.be('title');
            expect($(view.$('.controls').get(1)).html()).to.be('description');
        });

        it('shows "None" when title and description are missing.', function () {
            view.render();

            server.requests[0].respond(
                200,
                { "Content-Type": "application/json" },
                JSON.stringify({
                    justification: {
                        title: ''
                    },
                    description: ''
                })
            );

            expect($(view.$('.controls').get(0)).html()).to.be('None');
            expect($(view.$('.controls').get(1)).html()).to.be('None');
        });

    });

});
