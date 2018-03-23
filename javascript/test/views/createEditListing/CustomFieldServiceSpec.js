define([
    'createEditListing/CustomFieldService',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    '../../fixtures/listings.js',
    '../../fixtures/customFieldDefinitions.js',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(CustomFieldService, CustomFieldsCollection, ServiceItem, Listings, CustomFieldDefinitions, $, _, sinon, Marketplace) {

    describe('Create/Edit custom field service', function() {
        var customFields = new CustomFieldsCollection(CustomFieldDefinitions, {});
        var model = new ServiceItem(Listings.APPROVED_INSIDE_ENABLED_APP_COMPONENT);

        it('builds drop down field config.', function () {
            var fields = CustomFieldService.build(model, model.get('types'), 'primaryCharacteristics', customFields);

            var customField = customFields.findWhere({section: "primaryCharacteristics"});

            expect(customField.get('label')).to.be('<b> a BUG!</b> <button> test </button>');
            expect(customField.get('name')).to.be('<b> a BUG!</b> <button> test </button>');
        });

    });

});
