define([
    '../jsonForm',
    'jquery',
    'underscore',
    'handlebars'
], function (JsonForm, $, _, Handlebars) {

    var FormFieldTypes = JsonForm.FormFieldTypes,
        Select2InputField = FormFieldTypes.get('select2');

    var RequiredItemsField = function (options) {
        this.init('requiredItems', options);
    };

    _.extend(RequiredItemsField.prototype, Select2InputField.prototype, {

        multiInitSelection: function (element, callback) {
            var me = this,
                ids = $(element).val().split(','),
                includedItems = me.options.model.includedItems();

            includedItems.size() ?
                callback(me.findMultiSelections(ids, includedItems.toJSON())) :
                includedItems
                    .fetch()
                    .done(function (response) {
                        me.findMultiSelections(ids, response);
                    });
        }

    });

    FormFieldTypes.register('requiredItems', RequiredItemsField);

    return RequiredItemsField;

});
