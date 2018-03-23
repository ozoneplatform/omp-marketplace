define(['underscore'], function (_) {
    var SIZES = [{
        width: 480,
        height: 350,
        size: 'XSMALL'
    }, {
        width: 768,
        height: 350,
        size: 'SMALL'
    }, {
        width: 1050,
        height: 650,
        size: 'MEDIUM'
    }, {
        width: 1050,
        height: 1050,
        size: 'LARGE'
    }];

    var getSize = function (owfProperties) {
        var size = _.findWhere(SIZES, {
            width: owfProperties.width,
            height: owfProperties.height
        });

        return owfProperties.size || (size ? size.size : 'MEDIUM');
    };

    var config = function (model) {
        var singleton, visibleInLaunch, background, mobileReady,
            recommendedLayouts, universalName, size, intents, owfProperties;

        owfProperties = model.get('owfProperties') || {
            singleton: false,
            visibleInLaunch: true,
            background: false,
            mobileReady: false,
            size: 'MEDIUM',
            intents: []
        };

        universalName = owfProperties.universalName;
        singleton = owfProperties.singleton;
        visibleInLaunch = owfProperties.visibleInLaunch;
        background = owfProperties.background;
        mobileReady = owfProperties.mobileReady;
        recommendedLayouts = model.get('recommendedLayouts');
        intents = owfProperties.intents;
        size = getSize(owfProperties);

        return {
            'label': 'OWF Properties',
            'type': 'fieldset',
            'attrs': {
                'class': 'owf-properties'
            },
            'fields': [{
                'label': 'Universal Name',
                'type': 'text',
                'tooltip': 'Use as a necessary global identifier that provides consistency between OWF and Stores.',
                'attrs': {
                    'name': 'owfProperties[universalName]',
                    'maxlength': 255,
                    'value': universalName,
                    'disabled': !model.isNew()
                }
            }, {
                'label': 'Singleton',
                'type': 'checkbox',
                'tooltip': 'Designate if the listing can have only one instance launched in OWF.',
                'attrs': {
                    'name': 'owfProperties[singleton]',
                    'class': 'brand-success',
                    'value': singleton
                }
            }, {
                'label': 'Visible',
                'type': 'checkbox',
                'tooltip': 'Designate if the listing will appear in the OWF App Component Menu or if it will be hidden.',
                'attrs': {
                    'name': 'owfProperties[visibleInLaunch]',
                    'class': 'brand-success',
                    'value': visibleInLaunch
                }
            }, {
                'label': 'Runs in Background',
                'type': 'checkbox',
                'tooltip': 'Designate if the listing will run in the background or run on the screen where users can see it in OWF.',
                'attrs': {
                    'name': 'owfProperties[background]',
                    'class': 'brand-success',
                    'value': background
                }
            }, {
                'label': 'Mobile Ready',
                'type': 'checkbox',
                'tooltip': 'Designate if the listing is compatible with the MONO Mobile OWF',
                'attrs': {
                    'name': 'owfProperties[mobileReady]',
                    'class': 'brand-success',
                    'value': mobileReady
                }
            }, {
                'label': 'Recommended Layouts',
                'type': 'buttons-checkbox',
                'tooltip': 'Suggest application layouts that complement the listing.',
                'buttons': [{
                    'id': 'ACCORDION',
                    'value': 'Accordion'
                }, {
                    'id': 'DESKTOP',
                    'value': 'Desktop'
                }, {
                    'id': 'PORTAL',
                    'value': 'Portal'
                }, {
                    'id': 'TABBED',
                    'value': 'Tabbed'
                }],
                'attrs': {
                    'name': 'recommendedLayouts',
                    'value': recommendedLayouts
                }
            }, {
                'label': 'Approximate Size',
                'type': 'buttons-radio',
                'tooltip': 'Designate the pixel size of the window where the listing will open in OWF.',
                'buttons': [{
                    'id': 'XSMALL',
                    'value': 'Extra Small'
                }, {
                    'id': 'SMALL',
                    'value': 'Small'
                }, {
                    'id': 'MEDIUM',
                    'value': 'Medium'
                }, {
                    'id': 'LARGE',
                    'value': 'Large'
                }],
                'attrs': {
                    'name': 'owfProperties[size]',
                    'value': size
                }
            }, {
                'label': 'Intents',
                'type': 'intents',
                'tooltip': 'If this listing uses intents, enter them here. Intents are instructions developers add to app components for carrying out intentions.',
                'attrs': {
                    'value': intents
                }
            }]
        };
    };

    return {
        build: function (model, type) {
            return config(model);
        }
    };
});
