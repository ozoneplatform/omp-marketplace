define([
    'createEditListing/ListingTypeSection',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    '../../fixtures/listings.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(ListingTypeSection, CustomFieldsCollection, ServiceItem, Listings, $, _, sinon, Marketplace) {

    describe('Create/Edit listing type section', function() {
        var customFields = new CustomFieldsCollection([], {});

        describe('App Component', function () {
            var model = new ServiceItem(Listings.APPROVED_INSIDE_ENABLED_APP_COMPONENT);

            it('builds all fields.', function () {
                var fieldset = ListingTypeSection.build(model, model.get('types'), customFields);

                expect(fieldset.label).to.be('App Component Properties');

                var labels = _.pluck(fieldset.fields, 'label');

                expect(labels.length).to.be(4);
                expect(_.contains(labels, 'Small Icon URL')).to.be(true);
                expect(_.contains(labels, 'Large Icon URL')).to.be(true);
                expect(_.contains(labels, 'Launch URL')).to.be(true);
            });
        });

        describe('OZONE App', function () {
            var model = new ServiceItem(Listings.APPROVED_INSIDE_ENABLED_APP);

            it('builds all fields.', function () {
                var fieldset = ListingTypeSection.build(model, model.get('types'), customFields);

                expect(fieldset.label).to.be('OZONE App Properties');

                var labels = _.pluck(fieldset.fields, 'label');

                expect(labels.length).to.be(3);
                expect(_.contains(labels, 'Small Icon URL')).to.be(true);
                expect(_.contains(labels, 'Large Icon URL')).to.be(true);
            });
        });

        describe('Web App', function () {
            var model = new ServiceItem(Listings.APPROVED_INSIDE_ENABLED_WEB_APP);

            it('builds all fields.', function () {
                var fieldset = ListingTypeSection.build(model, model.get('types'), customFields);

                expect(fieldset.label).to.be('Web App Properties');

                var labels = _.pluck(fieldset.fields, 'label');

                expect(labels.length).to.be(5);
                expect(_.contains(labels, 'Small Icon URL')).to.be(true);
                expect(_.contains(labels, 'Large Icon URL')).to.be(true);
                expect(_.contains(labels, 'Launch URL')).to.be(true);
                expect(_.contains(labels, 'Opens in a new browser tab')).to.be(true);
            });
        });

    });

});
