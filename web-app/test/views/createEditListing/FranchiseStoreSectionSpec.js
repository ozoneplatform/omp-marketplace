define([
    'createEditListing/FranchiseStoreSection',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(FranchiseStoreSection, CustomFieldsCollection, ServiceItem, $, _, sinon, Marketplace) {

    describe('Create/Edit franchose store section', function() {
        var customFields = new CustomFieldsCollection([], {});
        var model = new ServiceItem({});

        it('builds all fields.', function () {
            var fieldset = FranchiseStoreSection.build(model, model.get('types'), customFields);

            expect(fieldset.label).to.be('Franchise Store Characteristics');

            var labels = _.pluck(fieldset.fields, 'label');

            expect(labels.length).to.be(6);
            expect(_.contains(labels, 'Owners')).to.be(true);
            expect(_.contains(labels, 'Technical POC')).to.be(true);
            expect(_.contains(labels, 'Organization')).to.be(true);
            expect(_.contains(labels, 'Requirements')).to.be(true);
            expect(_.contains(labels, 'Dependencies')).to.be(true);
            expect(_.contains(labels, 'Contacts')).to.be(true);
            expect(_.contains(labels, 'Contacts')).to.be(true);
        });

    });

});
