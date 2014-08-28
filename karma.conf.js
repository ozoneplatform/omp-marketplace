module.exports = function(config) {
  config.set({

    // base path, that will be used to resolve files and exclude
    basePath: '',


    // frameworks to use
    frameworks: ['mocha', 'requirejs'],


    // list of files / patterns to load in the browser
    files: [
      'web-app/vendor/modernizr.js',
      'web-app/vendor/sinon.js',
      'web-app/vendor/expect.js',
      'web-app/vendor/jquery/js/jquery-1.10.2.js',
      'web-app/vendor/jquery/jquery.serialize-object.js',
      'web-app/test/fixtures/config.js',
      'web-app/test/test-main.js',
      {pattern: 'web-app/js/**/*.js', included: false},
      {pattern: 'web-app/spa/**/*.js', included: false},
      {pattern: 'web-app/vendor/**/*.js', included: false},
      {pattern: 'web-app/test/fixtures/**/*.js', included: false},
      {pattern: 'web-app/test/**/*Spec.js', included: false}
    ],


    // list of files to exclude
    exclude: [

    ],


    // test results reporter to use
    // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
    reporters: ['progress'],


    preprocessors: {
      '**/web-app/js/**/*.js': 'coverage'
    },

    coverageReporter: {
      type : 'cobertura',
      dir : 'staging/jscoverage/'
    },


    junitReporter: {
      outputFile: 'staging/js-test-results.xml'
    },


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera (has to be installed with `npm install karma-opera-launcher`)
    // - Safari (only Mac; has to be installed with `npm install karma-safari-launcher`)
    // - PhantomJS
    // - IE (only Windows; has to be installed with `npm install karma-ie-launcher`)
    browsers: ['Chrome'],


    // If browser does not capture in given timeout [ms], kill it
    captureTimeout: 60000,


    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false
  });
};
