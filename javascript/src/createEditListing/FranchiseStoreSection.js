define([
    './CustomFieldService',
    'underscore',
    'marketplace'
],
function (CustomFieldService, _, Marketplace) {

    var config = function (model) {
        var owners = model.get('owners'),
            requirements = model.get('requirements'),
            dependencies = model.get('dependencies'),
            techPocs = model.get('techPocs'),
            organization = model.get('organization'),
            contacts = model.get('contacts'),
            config;

        config = {
            'label': 'Franchise Store Characteristics',
            'type': 'fieldset',
            'attrs': {
                'class': 'franchise-store-characteristics'
            },
            'fields': [{
                'label': 'Owners',
                'type': 'select2',
                'tooltip': 'Designate the people responsible for the listing.',
                'data': owners,
                'attrs': {
                    'name': 'owners',
                    'placeholder': 'Select one or more owners of this listing',
                    'required': true,
                    'data-multiple': true,
                    'data-search': true,
                    'data-ajax': Marketplace.context + '/profile/search',
                    'value': _.map(owners, function (owner) { return owner.id; }).join(',')
                }
            }, {
                'label': 'Technical POC',
                'type': 'array',
                'tooltip': 'Designate technical point(s)-of-contact.',
                'attrs': {
                    'name': 'techPocs[]',
                    'value': techPocs,
                    'maxlength': 256
                }
            }, {
                'label': 'Organization',
                'type': 'text',
                'attrs': {
                    'name': 'organization',
                    'placeholder': 'Enter the sponsoring organization or program office for the listing being submitted',
                    'maxlength': 256,
                    'value': organization
                }
            }, {
                'label': 'Requirements',
                'type': 'textarea',
                'attrs': {
                    'name': 'requirements',
                    'placeholder': 'Enter prerequisites necessary to utilize the listing',
                    'maxlength': 1000,
                    'rows': 7,
                    'value': requirements
                }
            }, {
                'label': 'Dependencies',
                'type': 'textarea',
                'attrs': {
                    'name': 'dependencies',
                    'maxlength': 1000,
                    'placeholder': 'Enter dependencies (external systems, technologies, etc) for the listing',
                    'rows': 7,
                    'value': dependencies
                }
            }]
        };

        // only show contacts field if a contact type exists
        if(Marketplace.contactTypes.length) {
            config.fields.splice(2, 0, {
                'label': 'Contacts',
                'type': 'contacts',
                'attrs': {
                    'value': contacts
                }
            });
        }

        return config;
    };

    return {
        build: function (model, type, customFieldsCollection) {
            var section = config(model);

            section.fields = section.fields.concat(CustomFieldService.build(model, type, 'marketplaceReferences', customFieldsCollection));

            return section;
        }
    };
});