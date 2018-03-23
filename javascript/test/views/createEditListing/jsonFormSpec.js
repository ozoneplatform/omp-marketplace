define([
    'createEditListing/jsonForm',
    'createEditListing/PrimarySection',
    'createEditListing/fields/TagsInputField',
    'createEditListing/fields/RequiredItemsInputField',
    'collections/CustomFieldsCollection',
    'models/ServiceItem',
    'jquery',
    'underscore',
    'sinon',
    'marketplace'
], function(jsonForm, PrimarySection, TagsInputField, RequiredItemsInputField, CustomFieldsCollection, ServiceItem, $, _, sinon, Marketplace) {

    var JsonForm = jsonForm.Form;
    var FormFieldTypes = jsonForm.FormFieldTypes;

    FormFieldTypes.register('tags', TagsInputField);
    FormFieldTypes.register('tags', TagsInputField);
    FormFieldTypes.register('requiredItems', RequiredItemsInputField);

    describe('JSON form', function() {

        it('renders form from json.', function () {
            var $el = $('<div></div>');
            var model = new ServiceItem({});
            var form = new JsonForm($el, [
                PrimarySection.build(model, model.get('types'), new CustomFieldsCollection([], {}))
            ]);

            form.render();

            var $fieldset = $el.children('fieldset')
            expect($fieldset.length).to.be(1);

            expect($fieldset.children().length).to.be(11);

            expect($fieldset.find('[for="title"]').length).to.be(1);
            expect($fieldset.find('[for="types[id]"]').length).to.be(1);
            expect($fieldset.find('[for="state[id]"]').length).to.be(1);
            expect($fieldset.find('[for="agency[id]"]').length).to.be(1);
            expect($fieldset.find('[for="versionName"]').length).to.be(1);
            expect($fieldset.find('[for="releasedDate"]').length).to.be(1);
            expect($fieldset.find('[for="description"]').length).to.be(1);
            expect($fieldset.find('[for="categories"]').length).to.be(1);
            expect($fieldset.find('[for="tags"]').length).to.be(1);
            expect($fieldset.find('[for="relatedItems"]').length).to.be(1);

            form.remove();
        });

    });

});
