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
            '<table>' +
            '<tr><td style="padding-right: 1em">Version:</td><td>{{version}}</td></tr>' +
            '<tr><td style="padding-right: 1em">Build Date:</td><td>{{buildDate}}</td></tr>' +
            '<tr><td style="padding-right: 1em">Build Number:</td><td>{{buildNumber}}</td></tr>' +
            '</table>' +
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
                    imagePath: Marketplace.context + getConfigByCode('about.box.image'),
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
