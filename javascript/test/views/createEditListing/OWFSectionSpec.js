define([
    'createEditListing/OWFSection',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    '../../fixtures/listings.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(OWFSection, CustomFieldsCollection, ServiceItem, Listings, $, _, sinon, Marketplace) {

    describe('Create/Edit franchose store section', function() {
        var customFields = new CustomFieldsCollection([], {});

        function getConfigValue (fields, label) {
            return _.findWhere(fields, {label: label}).attrs.value;
        }

        it('builds all fields.', function () {
            var model = new ServiceItem({});
            var fieldset = OWFSection.build(model, model.get('types'), customFields);

            expect(fieldset.label).to.be('OWF Properties');

            var labels = _.pluck(fieldset.fields, 'label');

            expect(labels.length).to.be(8);
            expect(_.contains(labels, 'Universal Name')).to.be(true);
            expect(_.contains(labels, 'Singleton')).to.be(true);
            expect(_.contains(labels, 'Visible')).to.be(true);
            expect(_.contains(labels, 'Runs in Background')).to.be(true);
            expect(_.contains(labels, 'Mobile Ready')).to.be(true);
            expect(_.contains(labels, 'Recommended Layouts')).to.be(true);
            expect(_.contains(labels, 'Approximate Size')).to.be(true);
            expect(_.contains(labels, 'Intents')).to.be(true);

            expect(getConfigValue(fieldset.fields, 'Singleton')).to.be(false);
            expect(getConfigValue(fieldset.fields, 'Visible')).to.be(true);
            expect(getConfigValue(fieldset.fields, 'Runs in Background')).to.be(false);
            expect(getConfigValue(fieldset.fields, 'Mobile Ready')).to.be(false);
            expect(getConfigValue(fieldset.fields, 'Approximate Size')).to.be('MEDIUM');
        });

        it('disables universal name for existing listings.', function () {
            var model = new ServiceItem(Listings.APPROVED_INSIDE_ENABLED_APP_COMPONENT);
            var fieldset = OWFSection.build(model, model.get('types'), customFields);

            var universalName = _.findWhere(fieldset.fields, {label: 'Universal Name'});

            expect(universalName.attrs.disabled).to.be(true);
        });

    });

});
