 // mocha setup
mocha.timeout(5000);
mocha.ignoreLeaks(true);
mocha.setup('bdd');

var tests = [];
for (var file in window.__karma__.files) {
  if (window.__karma__.files.hasOwnProperty(file)) {
    if (/Spec\.js$/.test(file)) {
      tests.push(file);
    }
  }
}

requirejs.config({

    baseUrl: '/base/web-app/js',

    waitSeconds: 60,

    paths: {
        'backbone'                  : '../vendor/backbone/backbone-1.0.0',
        'backbone.paginator'        : '../vendor/backbone/backbone.paginator',
        'bootstrap'                 : '../vendor/bootstrap/bootstrap-2.3.2',
        'bootstrap-select'          : '../vendor/bootstrap/bootstrap-select',
        'bootstrap-editable'        : '../vendor/bootstrap-editable/bootstrap-editable',
        'bootstrap-datepicker'      : '../vendor/bootstrap-datepicker/bootstrap-datepicker',
        'handlebars'                : '../vendor/handlebars/handlebars-1.0.0',
        'raty'                      : '../vendor/jquery/jquery.raty',
        'bxslider'                  : '../vendor/bxslider/jquery.bxSlider.min',
        'moment'                    : '../vendor/moment',
        'moment-patches'            : '../vendor/moment-patches',
        'dataTables'                : '../vendor/jquery/jquery.dataTables.min',
        'bootstrap-select'          : '../vendor/bootstrap/bootstrap-select',
        'bootstrap-editable'        : '../vendor/bootstrap-editable/bootstrap-editable',
        'jquery'                    : '../vendor/jquery/js/jquery-1.10.2',
        'jquery.browser'            : '../vendor/jquery/jquery.browser',
        'jquery.magnific-popup'     : '../vendor/jquery.magnific-popup/jquery.magnific-popup',
        'jquery.serialize-object'   : '../vendor/jquery/jquery.serialize-object',
        'jquery.validate'           : '../vendor/jquery-validation-1.11.1/dist/jquery.validate',
        'sinon'                     : '../vendor/sinon',
        'underscore'                : '../vendor/lodash.compat',
        'underscore.string'         : '../vendor/underscore.string',
        'quickview'                 : 'quickview',
        'marketplace'               : 'marketplace',
        'select2'                   : '../vendor/select2-3.4.5/select2',
        'spin'                      : '../vendor/spin'
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
        'bootstrap': {
            deps: ['jquery'],
            exports: '$'
        },
        'bootstrap-select': {
            deps: ['jquery'],
            exports: '$'
        },
        'bootstrap-editable': {
            deps: ['jquery', 'bootstrap'],
            exports: 'jQuery.fn.editable'
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
        'marketplace': {
            exports: 'Marketplace'
        },
        'dataTables': {
            deps: ['jquery'],
            exports: 'DataTables'
        },
        'jquery.dotdotdot': {
            deps: ['jquery'],
            exports: '$'
        },
        'jquery.borderbox': {
            deps: ['jquery'],
            exports: '$'
        },
        'jquery.deparam': {
            deps: ['jquery'],
            exports: '$'
        },
        'jquery.magnific-popup': {
            deps: ['jquery'],
            exports: '$'
        },
        'jquery.browser': {
            deps: ['jquery'],
            exports: '$'
        },
        'sinon': {
            exports: 'sinon'
        },
        'bootstrap-editable': {
            deps: ['jquery', 'bootstrap'],
            exports: '$'
        },

        'spin': {
            exports: 'Spinner'
        }
    },

    // ask Require.js to load these files (all our tests)
    deps: tests,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});
