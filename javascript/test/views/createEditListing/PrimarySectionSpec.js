define([
    'createEditListing/PrimarySection',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    '../../fixtures/listings.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(PrimarySection, CustomFieldsCollection, ServiceItem, Listings, $, _, sinon, Marketplace) {

    describe('Create/Edit primary section', function() {
        var customFields = new CustomFieldsCollection([], {});

        describe('New model', function () {
            var model = new ServiceItem({});

            it('builds all fields.', function () {
                var fieldset = PrimarySection.build(model, model.get('types'), customFields);

                expect(fieldset.label).to.be('Primary Characteristics');

                var labels = _.pluck(fieldset.fields, 'label');

                expect(labels.length).to.be(10);
                expect(_.contains(labels, 'Name')).to.be(true);
                expect(_.contains(labels, 'Type')).to.be(true);
                expect(_.contains(labels, 'State')).to.be(true);
                expect(_.contains(labels, 'Company')).to.be(true);
                expect(_.contains(labels, 'Version')).to.be(true);
                expect(_.contains(labels, 'Release Date')).to.be(true);
                expect(_.contains(labels, 'Description')).to.be(true);
                expect(_.contains(labels, 'Categories')).to.be(true);
                expect(_.contains(labels, 'Tags')).to.be(true);
                expect(_.contains(labels, 'Required Items')).to.be(true);
            });

            it('state is set to Active.', function () {
                var fieldset = PrimarySection.build(model, model.get('types'), customFields);

                var state = _.findWhere(fieldset.fields, {label: 'State'});
                expect(state.attrs.value).to.be(Marketplace.defaultState.id);
            });

            it('OZONE App is not an option for type.', function () {
                var fieldset = PrimarySection.build(model, model.get('types'), customFields);

                var type = _.findWhere(fieldset.fields, {label: 'Type'});

                expect(_.findWhere(type.data, {title: 'OZONE App'})).to.not.be.ok();
            });
        });

        describe('OZONE App', function () {
            var model = new ServiceItem(Listings.APPROVED_INSIDE_ENABLED_APP);

            it('OZONE App is an option for type and is disabled.', function () {
                var fieldset = PrimarySection.build(model, model.get('types'), customFields);

                var type = _.findWhere(fieldset.fields, {label: 'Type'});
                expect(_.findWhere(type.data, {title: 'OZONE App'})).to.be.ok();
                expect(type.attrs.readonly).to.be(true);
            });
        });

    });

});
