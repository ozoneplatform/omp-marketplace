define('jquery', function () {
   return jQuery;
});
define('backbone', function () {
   return Backbone;
});
define('bootstrap', function () {
   return jQuery;
});
define('handlebars', function () {
   return Handlebars;
});
define('underscore', function () {
   return _;
});
define('raty', function () {
   return jQuery;
});
define('bxslider', function () {
   return jQuery;
});
define('dataTables', function () {
   return jQuery;
});
define('bootstrap-select', function () {
   return jQuery;
});
define('bootstrap-editable', function () {
   return jQuery;
});
define('bootstrap-datepicker', function () {
   return jQuery;
});
define('marketplace', function () {
   return Marketplace;
});
define('jquery.magnific-popup', function () {
   return jQuery;
});
define('jquery.serialize-object', function () {
   return jQuery;
});
define('jquery.validate', function () {
   return jQuery;
});
define('jquery.browser', function () {
   return jQuery;
});

require.config({

    // urlArgs : 'bust=' + Marketplace.getBuildNumber(),

    baseUrl: Marketplace.context + '/js',

    waitSeconds: 60,

    paths: {
        'text'                      : '../vendor/requirejs/text',
        'jquery'                    : '../vendor/jquery/js/jquery-1.10.2',
        'jquery.serialize-object'   : '../vendor/jquery/jquery.serialize-object',
        'jquery.validate'           : '../vendor/jquery-validation-1.11.1/dist/jquery.validate',
        'jquery.browser'            : '../vendor/jquery/jquery.browser',
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
        '*': {
            'jquery': 'jquery.browser',
            'moment': 'moment-patches'
        },

        'jquery.browser': { 'jquery': 'jquery' },
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
        'jquery.browser': {
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
});
