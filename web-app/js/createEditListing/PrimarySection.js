define([
    './CustomFieldService',
    'underscore',
    'marketplace'
],
function (CustomFieldService, _, Marketplace) {

    var config =  function (model) {
        var title = model.get('title'),
            type = model.get('types'),
            state = model.get('state'),
            agency = model.get('agency'),
            version = model.get('versionName'),
            releaseDate = model.get('releaseDate'),
            description = model.get('description'),
            categories = model.get('categories'),
            serviceItemModel = model,
            isOzoneApp = type ? type.title === 'OZONE App' : false,
            requiredItems;

        var relationships = model.get('relationships');
        if(relationships && relationships.length) {
            requiredItems = _.map(relationships[0].relatedItems, function (obj) {
                return obj.id;
            }).join(',');
        }

        return {
            'label': 'Primary Characteristics',
            'type': 'fieldset',
            'attrs': {
                'class': 'primary-characteristics'
            },
            'fields':  [{
                'label': 'Name',
                'type': 'text',
                'value': '',
                'attrs': {
                    'name': 'title',
                    'placehoder': 'Enter the name of the listing being submitted',
                    'required': true,
                    'maxlength': 256,
                    'value': title
                }
            }, {
                'label': 'Type',
                'type': 'select2',
                'data': isOzoneApp ?  Marketplace.types : _.reject(Marketplace.types, {title: 'OZONE App'}),
                'attrs': {
                    'name': 'types[id]',
                    'placeholder': 'Select a type',
                    'required': true,
                    'value': type ? type.id : '',
                    'readonly': isOzoneApp
                }
            }, {
                'label': 'State',
                'type': 'select2',
                'attrs': {
                    'name': 'state[id]',
                    'data-ajax': Marketplace.context + '/state',
                    'value': state ? state.id : ''
                }
            }, {
                'label': 'Company',
                'type': 'select2',
                'attrs': {
                    'name': 'agency[id]',
                    'placeholder': 'Select a company',
                    'data-ajax': Marketplace.context + '/api/agency',
                    'value': agency ? agency.id : ''
                }
            }, {
                'label': 'Version',
                'type': 'text',
                'attrs': {
                    'name': 'versionName',
                    'class': 'auto-width',
                    'maxlength': 256,
                    'value': version
                }
            }, {
                'label': 'Release Date',
                'type': 'datepicker',
                'attrs': {
                    'name': 'releaseDate',
                    'class': 'auto-width',
                    'value': releaseDate
                }
            }, {
                'label': 'Description',
                'type': 'textarea',
                'attrs': {
                    'name': 'description',
                    'maxlength': 4000,
                    'rows': 15,
                    'value': description
                }
            }, {
                'label': 'Categories',
                'type': 'select2',
                'data': categories,
                'attrs': {
                    'name': 'categories',
                    'placeholder': 'Select one or more categories',
                    'data-multiple': true,
                    'data-search': true,
                    'data-ajax': Marketplace.context + '/public/category/search',
                    'value': _.map(categories, function (category) { return category.id; }).join(',')
                }
            }, {
                'label': 'Tags',
                'type': 'tags',
                'serviceItemModel': serviceItemModel,
                'attrs': {
                    'name': 'tags'
                }
            }, {
                'label': 'Required Items',
                'type': 'requiredItems',
                'tooltip': 'Add items this listing needs to function.',
                'model': model,
                'attrs': {
                    'name': 'relatedItems',
                    'placeholder': 'Select listings that are required by this listing',
                    'data-multiple': true,
                    'data-search': true,
                    'data-ajax': Marketplace.context + '/relationship/getListings',
                    'value': requiredItems,
                    'readonly': isOzoneApp
                }
            }]
        };
    };

    return {
        build: function (model, type, customFieldsCollection) {
            var section = config(model);

            section.fields = section.fields.concat(CustomFieldService.build(model, type, 'primaryCharacteristics', customFieldsCollection));

            return section;
        }
    };
});