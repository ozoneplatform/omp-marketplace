define([
    './CustomFieldService',
    'underscore'
],
function (CustomFieldService, _) {

    var config = function (model) {
        var installUrl = model.get('installUrl'),
            resources = model.get('docUrls'),
            screenshots = model.get('screenshots');

        return {
            'label': 'Technical Characteristics',
            'type': 'fieldset',
            'attrs': {
                'class': 'technical-characteristics'
            },
            'fields': [{
                'label': 'Installation URL',
                'type': 'url',
                'tooltip': 'Link to installation instructions and necessary files.',
                'attrs': {
                    'name': 'installUrl',
                    'maxlength': 2083,
                    'value': installUrl
                }
            }, {
                'label': 'Resources',
                'type': 'resources',
                'tooltip': 'Link to user help resources like user guides and instructional videos.',
                'attrs': {
                    'value': resources
                }
            }, {
                'label': 'Screenshots',
                'type': 'screenshots',
                'attrs': {
                    'value': screenshots
                }
            }]
        };
    };

    return {
        build: function (model, type, customFieldsCollection) {
            var section = config(model);

            section.fields = section.fields.concat(CustomFieldService.build(model, type, 'technicalProperties', customFieldsCollection));

            return section;
        }
    };
});