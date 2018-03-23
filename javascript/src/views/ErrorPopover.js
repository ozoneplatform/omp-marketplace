define([
    'views/BaseView',
    'handlebars'
], function(BaseView, Handlebars) {
    'use strict';

    var SuperClass = BaseView,
        template = Handlebars.compile(
            '<p>{{message}}</p>' +
            '<button class="btn btn-primary btn-small">OK</button>'
        );

    return SuperClass.extend({
        //should be set during construction
        message: null,
        title: null,
        placement: null,

        /**
         * To use this class, set the message, title, placement, and
         * el properties on initialization
         */

        render: function() {
            this.$el.popover({
                title: this.title,
                content: template({ message: this.message }),
                html: true,
                placement: this.placement
            }).popover('show');

            return this;
        },

        remove: function() {
            this.$el.popover('destroy');
        }
    });
});
