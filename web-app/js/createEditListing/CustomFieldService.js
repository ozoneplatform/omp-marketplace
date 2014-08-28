define(['underscore'], function (_) {

    var customFieldClasses = {
        'DROP_DOWN' : 'marketplace.DropDownCustomField',
        'TEXT'      : 'marketplace.TextCustomField',
        'TEXT_AREA' : 'marketplace.TextAreaCustomField',
        'IMAGE_URL' : 'marketplace.ImageURLCustomField',
        'CHECK_BOX' : 'marketplace.CheckBoxCustomField'
    };

    function buildCustomFieldConfig (model, customFieldModel, type) {
        var config,
            id = _.uniqueId(),
            value = '', values = [],
            fieldname = 'customFields[' + id +'][value]';

        config = [{
            'label': 'Hidden',
            'type': 'hidden',
            'attrs': {
                'name': 'customFields[' + id +'][class]',
                'value': customFieldClasses[customFieldModel.get('fieldType')]
            }
        }, {
            'label': 'Hidden',
            'type': 'hidden',
            'attrs': {
                'name': 'customFields[' + id +'][customFieldDefinition][id]',
                'value': customFieldModel.get('id')
            }
        }];

        var cf = _.findWhere(model.get('customFields'), {
            customFieldDefinitionUuid: customFieldModel.get('uuid')
        });
        value = cf ? cf.value: '';


        if(customFieldModel.get('fieldType') === 'DROP_DOWN') {
            values = cf ? _.pluck([].concat(cf.fieldValue), 'value') : [];

            config.push({
                'label': customFieldModel.get('label'),
                'type': 'select2',
                'tooltip': customFieldModel.get('tooltip'),
                'attrs': {
                    'name': fieldname,
                    'placeholder': 'Select a value for ' + customFieldModel.get('label'),
                    'required': customFieldModel.get('isRequired'),
                    'data-multiple': customFieldModel.get('isMultiSelect'),
                    'data-allowclear': !customFieldModel.get('isRequired'),
                    'value': values.join(',')
                },
                'data': _.chain(customFieldModel.get('fieldValues')).map(function (fieldValue) {
                    var displayText = fieldValue.displayText;

                    // allow a dropdown option to be selected if it is enabled or if it is already used
                    return (fieldValue.isEnabled || _.contains(values, displayText)) ? {
                        'title': displayText,
                        'id': displayText
                    } : null;
                }).compact().valueOf()
            });
        }
        else if(customFieldModel.get('fieldType') === 'CHECK_BOX') {
            config.push({
                'label': customFieldModel.get('label'),
                'type': 'checkbox',
                'tooltip': customFieldModel.get('tooltip'),
                'attrs': {
                    'name': fieldname,
                    'placeholder': 'Select a value for ' + customFieldModel.get('label'),
                    'required': customFieldModel.get('isRequired'),
                    'class': 'brand-success',
                    'value': (value === true || value === false) ? value : customFieldModel.get('selectedByDefault')
                }
            });
        }
        else if(customFieldModel.get('fieldType') === 'IMAGE_URL') {
            config.push({
                'label': customFieldModel.get('label'),
                'type': 'image',
                'tooltip': customFieldModel.get('tooltip'),
                'attrs': {
                    'name': fieldname,
                    'placeholder': 'Select a value for ' + customFieldModel.get('label'),
                    'required': customFieldModel.get('isRequired'),
                    'maxlength': 2083,
                    'value': value
                }
            });
        }
        else if(customFieldModel.get('fieldType') === 'TEXT') {
            config.push({
                'label': customFieldModel.get('label'),
                'type': 'text',
                'tooltip': customFieldModel.get('tooltip'),
                'attrs': {
                    'name': fieldname,
                    'placeholder': 'Select a value for ' + customFieldModel.get('label'),
                    'required': customFieldModel.get('isRequired'),
                    'maxlength': 256,
                    'value': value
                }
            });
        }
        else if(customFieldModel.get('fieldType') === 'TEXT_AREA') {
            config.push({
                'label': customFieldModel.get('label'),
                'type': 'textarea',
                'tooltip': customFieldModel.get('tooltip'),
                'attrs': {
                    'name': fieldname,
                    'placeholder': 'Select a value for ' + customFieldModel.get('label'),
                    'required': customFieldModel.get('isRequired'),
                    'maxlength': 4000,
                    'rows': 7,
                    'value': value
                }
            });
        }
        return config;
    }

    return {
        'build': function (model, type, section, customFieldsCollection) {
            var fields = customFieldsCollection
                .filter(function (customFieldModel) {
                    if(customFieldModel.get('section') === section &&
                            (customFieldModel.get('allTypes') === true || (type && _.findWhere(customFieldModel.get('types'), {'title': type.title})))) {
                        return true;
                    }
                });

            fields = _.map(fields, function (customFieldModel ,i) {
                return buildCustomFieldConfig(model, customFieldModel, type);
            });

            return _(fields).flatten().compact().valueOf();
        }
    };
});