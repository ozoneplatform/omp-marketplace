module.exports = function (grunt) {

    grunt.initConfig({

        // See: http://www.jshint.com/docs/
        jshint: {
            all: {
                src: [
                    'web-app/js/models/**/*.js',
                    'web-app/js/collections/**/*.js',
                    'web-app/js/views/**/*.js',
                    'web-app/js/quickview/**/*.js',
                    'web-app/js/recentActivity/**/*.js',
                    'web-app/js/listingManagement/**/*.js',
                    'web-app/js/createEditListing/**/*.js',
                    'web-app/js/partnerStores/**/*.js',
                ],
                options: {
                    globals: {
                        Ext: false,
                        jQuery: false,
                        define: false,
                        Marketplace: false,
                        require: false,
                        window: false,
                        document: false,
                        Modernizr: false,
                        console: false,
                        setTimeout: false,
                        clearTimeout: false,
                        setInterval: false,
                        clearInterval: false,
                        gadgets:false
                    },
                    bitwise: true,
                    camelcase: true,
                    curly: true,
                    eqeqeq: true,
                    forin: true,
                    immed: true,
                    indent: 4,
                    latedef: true,
                    newcap: true,
                    noarg: true,
                    noempty: true,
                    nonew: true,
                    quotmark: 'single',
                    regexp: true,
                    undef: true,
                    expr: true,
                    unused: false,
                    trailing: false,
                    es3: true
                }
            }
        },

        karma: {
            options: {
                configFile: 'karma.conf.js',
                browsers: ['Chrome']
            },
            continuous: {
                singleRun: true,
                reporters: ['progress', 'junit'],
                browsers: ['PhantomJS']
            },
            dev: {}
        },

        // Run shell commands
        shell: {
            hooks: {
                command: [
                    'cp -rf git-hooks/* .git/hooks/',
                    'chmod -R 755 .git/hooks/'
                ].join(' && ')
            },
            karmadev: {
                options: {
                    stdout: true
                },
                command: './node_modules/karma/bin/karma start --browsers=Chrome --single-run=false'
            },
            karmacontinuous: {
                options: {
                    stdout: true,
                    failOnError: true
                },
                command: './node_modules/karma/bin/karma start --browsers=PhantomJS --reports=progress,junit --single-run=true'
            }
        }

    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-shell');

    grunt.registerTask('setup', ['shell:hooks']);
    grunt.registerTask('continuous', ['jshint', 'shell:karmacontinuous']);
    grunt.registerTask('dev', ['shell:karmadev']);
    grunt.registerTask('default', ['continuous']);
};
