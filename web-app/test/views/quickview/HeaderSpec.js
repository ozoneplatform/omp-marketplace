define([
    'quickview/header',
    'models/ServiceItem',
    '../../fixtures/listings.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(Header, ServiceItem, ListingFixtures, $, _, sinon, Marketplace) {

    describe('Quickview: header', function() {
        var view, model, stub, isWidgetStub;

        function initModelAndView (modelAttrs) {
            view && view.remove();

            model = new ServiceItem(modelAttrs);
            view = new Header({
                serviceItemModel: model
            });
        }

        function isWidget (inOWF) {
            isWidgetStub = sinon.stub(Marketplace.widget, 'isWidget');
            isWidgetStub.returns(inOWF === false ? false: true);
        }

        beforeEach(function () {
            initModelAndView(ListingFixtures.APPROVED_INSIDE_ENABLED_APP_COMPONENT);
        });

        afterEach(function () {
            view.remove();
            stub && stub.restore();
            stub = null;

            isWidgetStub && isWidgetStub.restore();
            isWidgetStub = null;
        });

        it('shows start/add button for only Approved listings.', function() {
            view.render();
            expect(view.$('.btn-add')[0]).to.be.ok(0);

            initModelAndView(ListingFixtures.UNAPPROVED_OUTSIDE_ENABLED_APP_COMPONENT);
            view.render();
            expect(view.$('.btn-add')[0]).to.not.be.ok();
        });

        it('shows add button when in OWF.', function() {
            isWidget();
            view.render();
            expect(view.$('.btn-add').html()).to.be('Add');
        });

        it('does not show add button for Apps when not in OWF.', function() {
            isWidget(false);
            initModelAndView(ListingFixtures.APPROVED_INSIDE_ENABLED_APP);
            view.render();
            expect(view.$('.btn-add').length).to.be(0);
        });

        it('adds App Component and App to OWF when Add button is clicked.', function () {
            isWidget();
            stub = sinon.stub(Marketplace.widget, 'addWidgetToOwf');

            view.render();

            var evt = { target: '.btn-add' };

            view.addOrStart(evt);

            expect(stub.calledOnce).to.be(true);
            stub.restore();

            initModelAndView(ListingFixtures.APPROVED_INSIDE_ENABLED_APP);
            stub = sinon.stub(Marketplace.widget, 'addStackToOwf');

            view.render();
            view.addOrStart(evt);

            expect(stub.calledOnce).to.be(true);
        });

        it('opens listings with launch url in tab when not in OWF.', function () {
            isWidget(false);

            stub = sinon.stub(window, 'open');

            view.render();
            view.addOrStart();

            expect(stub.calledOnce).to.be(true);
        });

    });

});