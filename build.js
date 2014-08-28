// All options can be found at: https://github.com/jrburke/r.js/blob/master/build/example.build.js
({
    baseUrl: 'web-app/js',

    locale: 'en_us',

    optimize: 'uglify',

    preserveLicenseComments: false,

    waitSeconds: 60,

    dir: 'js-build',

    skipDirOptimize: true,

    modules: [
        {
            name: 'quickview/index',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'jquery.magnific-popup',
                'marketplace'
            ]
        },
        {
            name: 'dataExchange/exportWizard/app',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        },
        {
            name: 'dataExchange/importWizard/app',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        },
        {
            name: 'affiliatedSearch/affiliatedSearch-main',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        },
        {
            name: 'listingManagement/index',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        },
        {
            name: 'createEditListing/index',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        },
        {
            name: 'RouterMain',
            exclude: [
                'profile/Window',
                'quickview/index',
                'jquery',
                'jquery.magnific-popup',
                'jquery.serialize-object',
                'jquery.validate',
                'select2',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ],
            include: [
                'views/LoadMask'
            ]
        },
        {

            name: 'profile/Window',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        },
        {
            name: 'views/filter/FilterMenu',
            exclude: [
                'jquery',
                'jquery.magnific-popup',
                'backbone',
                'backbone.paginator',
                'bootstrap',
                'handlebars',
                'underscore',
                'raty',
                'bxslider',
                'dataTables',
                'bootstrap-select',
                'bootstrap-editable',
                'bootstrap-datepicker',
                'marketplace'
            ]
        }
    ],

    // mainConfigFile: 'web-app/js/requirejsConfig.js',
    // can't use requirejsConfig because of the Marketplace object
    paths: {
        'text'                      : '../vendor/requirejs/text',
        'jquery'                    : '../vendor/jquery/js/jquery-1.10.2',
        'jquery.serialize-object'   : '../vendor/jquery/jquery.serialize-object',
        'jquery.validate'           : '../vendor/jquery-validation-1.11.1/dist/jquery.validate',
        'jquery.magnific-popup'     : '../vendor/jquery.magnific-popup/jquery.magnific-popup',
        'select2'                   : '../vendor/select2-3.4.5/select2',
        'backbone'                  : '../vendor/backbone/backbone-1.0.0',
        'backbone.paginator'        : '../vendor/backbone/backbone.paginator',
        'bootstrap'                 : '../vendor/bootstrap/bootstrap-2.3.2',
        'handlebars'                : '../vendor/handlebars/handlebars-1.0.0',
        'underscore'                : '../vendor/lodash.compat',
        'underscore.string'         : '../vendor/underscore.string',
        'raty'                      : '../vendor/jquery/jquery.raty',
        'bxslider'                  : '../vendor/bxslider/jquery.bxSlider.min',
        'moment'                    : '../vendor/moment',
        'moment-patches'            : '../vendor/moment-patches',
        'dataTables'                : '../vendor/jquery/jquery.dataTables.min',
        'bootstrap-select'          : '../vendor/bootstrap/bootstrap-select',
        'bootstrap-editable'        : '../vendor/bootstrap-editable/bootstrap-editable',
        'bootstrap-datepicker'      : '../vendor/bootstrap-datepicker/bootstrap-datepicker',
        'spin'                      : '../vendor/spin',
        'quickview'                 : 'quickview',
        'marketplace'               : 'marketplace'
    },


    map: {
        // '*' means all modules will get 'moment-patches'
        // for their 'jquery' dependency.
        '*': { 'moment': 'moment-patches' },

        // 'moment-patches' wants the real moment module
        // though. If this line was not here, there would
        // be an unresolvable cyclic dependency.
        'moment-patches': { 'moment': 'moment' }
    },

    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'backbone.paginator': {
            deps: ['backbone'],
            exports: 'Backbone.Paginator'
        },
        'handlebars': {
            exports: 'Handlebars'
        },
        'underscore': {
            exports: '_'
        },
        'raty': {
            deps: ['jquery'],
            exports: '$'
        },
        'bxslider': {
            deps: ['jquery'],
            exports: '$'
        },
        'jquery.serialize-object': {
            deps: ['jquery'],
            exports: '$'
        },
        'select2': {
            deps: ['jquery'],
            exports: '$'
        },
        'marketplace': {
            exports: 'Marketplace'
        },
        'dataTables': {
            deps: ['jquery'],
            exports: 'DataTables'
        },
        'bootstrap-select': {
            deps: ['jquery']
        },
        'bootstrap-editable': {
            deps: ['jquery', 'bootstrap'],
            exports: '$'
        },
        'bootstrap-datepicker': {
            deps: ['jquery', 'bootstrap'],
            exports: '$'
        },
        'spin': {
            exports: 'Spinner'
        }
    }
})
