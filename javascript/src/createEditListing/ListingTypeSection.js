define([
    './CustomFieldService',
    'underscore'
],
function (CustomFieldService, _) {

    var config = function (model, type) {
        var label,
            imageSmallUrl, imageMediumUrl, imageLargeUrl, launchUrl,
            opensInNewBrowserTab;

        label = type.title + ' Properties';

        imageSmallUrl = model.get('imageSmallUrl');
        imageMediumUrl = model.get('imageMediumUrl');
        imageLargeUrl = model.get('imageLargeUrl');
        launchUrl = model.get('launchUrl');
        opensInNewBrowserTab = model.get('opensInNewBrowserTab');

        var config = {
            'label': label,
            'type': 'fieldset',
            'attrs': {
                'class': label.split(' ').join('-').toLowerCase()
            },
            'fields': [{
                'label': 'Small Icon URL',
                'type': 'image',
                'tooltip': 'Enter the URL to a small icon representing the listing - recommended size 16 x 16',
                'attrs': {
                    'name': 'imageSmallUrl',
                    'maxlength': 2083,
                    'placeholder': 'Enter the URL to a small icon representing the listing - recommended size 16 x 16',
                    'value': imageSmallUrl
                }
            }, {
                'label': 'Medium Icon URL',
                'type': 'image',
                'tooltip': 'Enter the URL to a medium icon representing the listing - recommended size 64 x 64',
                'attrs': {
                    'name': 'imageMediumUrl',
                    'maxlength': 2083,
                    'placeholder': 'Enter the URL to a medium icon representing the listing - recommended size 64 x 64',
                    'value': imageMediumUrl
                }
            }, {
                'label': 'Large Icon URL',
                'type': 'image',
                'tooltip': 'Enter the URL to a large icon representing the listing - recommended size 220 x 137',
                'attrs': {
                    'name': 'imageLargeUrl',
                    'maxlength': 2083,
                    'placeholder': 'Enter the URL to a large icon representing the listing - recommended size 220 x 137',
                    'value': imageLargeUrl
                }
            }]
        };

        if(type.hasLaunchUrl) {
            config.fields.push({
                'label': 'Launch URL',
                'type': 'url',
                'tooltip': 'Enter the URL to access the listing being submitted',
                'attrs': {
                    'name': 'launchUrl',
                    'maxlength': 2083,
                    'placeholder': 'Enter the URL to access the listing being submitted',
                    'required': true,
                    'value': launchUrl
                }
            });
        }
        if(!type.ozoneAware) {
            config.fields.push({
                'label': 'Opens in a new browser tab',
                'type': 'checkbox',
                'attrs': {
                    'name': 'opensInNewBrowserTab',
                    'value': opensInNewBrowserTab,
                    'class': 'brand-success'
                }
            });
        }
        return config;
    };

    return {
        build: function (model, type, customFieldsCollection) {
            if(!type) {
                throw 'type must be passed in to build listing type section config.';
            }
            var section = config(model, type);

            section.fields = section.fields.concat(CustomFieldService.build(model, type, 'typeProperties', customFieldsCollection));

            return section;
        }
    };
});