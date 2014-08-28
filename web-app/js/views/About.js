define([
    'views/ModalView',
    'models/AboutModel',
    'marketplace',
    'handlebars',
    'moment',
    'underscore'
], function(ModalView, AboutModel, Marketplace, Handlebars, moment, _) {
    'use strict';

    var SuperClass = ModalView,
        template = Handlebars.compile(
            '<button class="close close-modal">×</button>' +
            '<img src="{{imagePath}}" />' +
            '<p>{{message}}</p>' +
            '<span class="version">Version: {{version}}</span>' +
            '<span class="build-date">Build Date: {{buildDate}}</span>' +
            '<span class="build-number">Build Number: {{buildNumber}}</span>' +
            '<div class="about-footer">' +
                '<button class="close-modal btn btn-primary btn-small">Close</button>' +
            '</div>'
        );

    function getConfigByCode(code) {
        return _.find(Marketplace.appconfigs, function(config) {
            return config.code === code;
        }).value;
    }

    return SuperClass.extend({
        //disabled default ModalView buttons
        footer: false,

        animate: false,

        className: 'about-window ' + (SuperClass.prototype.className || ''),

        events: {
            'click .close-modal': 'closeModal'
        },

        initialize: function() {
            this.model = new AboutModel();

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        render: function() {
            var me = this;

            SuperClass.prototype.render.apply(this, arguments);

            this.model.fetch().done(function() {
                var attrs = _.extend({}, me.model.attributes, {
                    imagePath: getConfigByCode('about.box.image'),
                    message: getConfigByCode('about.box.content'),
                    buildDate: moment(me.model.get('buildDate')).format('MMMM DD, YYYY')
                });

                me.$body.html(template(attrs));
            });

            return this;
        },

        getTitle: function() {
            return 'About Apps Mall';
        }
    });
});
