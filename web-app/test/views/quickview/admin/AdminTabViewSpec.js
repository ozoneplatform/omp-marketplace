define([
    'quickview/admin/AdminTabView',
    'models/ServiceItem',
    '../../../fixtures/listings.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(AdminTabView, ServiceItem, ListingFixtures, $, _, sinon, Marketplace) {

    describe('Quickview: Admin tab', function() {
        var view, model;

        beforeEach(function () {
            model = new ServiceItem({});
            view = new AdminTabView({
                model: model
            });
            view.render();
        });

        afterEach(function () {
            view.remove();
        });

        it('renders changelog and setting views.', function() {
            expect(view.settingsView).to.be.ok();
            expect(view.changelogView).to.be.ok();
            expect(view.$('.span4').children().data('view')).to.be(view.settingsView);
            expect(view.$('.span8').children().data('view')).to.be(view.changelogView);
        });

    });

});