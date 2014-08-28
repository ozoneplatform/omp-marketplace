define([
    'createEditListing/TechnicalSection',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(TechnicalSection, CustomFieldsCollection, ServiceItem, $, _, sinon, Marketplace) {

    describe('Create/Edit primary section', function() {
        var customFields = new CustomFieldsCollection([], {});
        var model = new ServiceItem({});

        it('builds all fields.', function () {
            var fieldset = TechnicalSection.build(model, model.get('types'), customFields);

            expect(fieldset.label).to.be('Technical Characteristics');

            var labels = _.pluck(fieldset.fields, 'label');

            expect(labels.length).to.be(3);
            expect(_.contains(labels, 'Installation URL')).to.be(true);
            expect(_.contains(labels, 'Resources')).to.be(true);
            expect(_.contains(labels, 'Screenshots')).to.be(true);
        });

    });

});
