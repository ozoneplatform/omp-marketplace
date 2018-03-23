define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/BaseView'
], function($, _, Backbone, Handlebars, BaseView) {

    return BaseView.extend({

        tagName: 'dl',
        className: 'filter-group-view',

        tpl:    Handlebars.compile(
                '<dt>{{description}}</dt>' +
                '{{#each options}}' +
                    '<dd>' +
                        '<label class="checkbox">' +
                            '<input type="checkbox" name="{{name}}" id="{{name}}-checkbox">{{displayText}}</input>' +
                        '</label>' +
                    '</dd>' +
                '{{/each}}'),

        render: function() {
            this.$el.html(this.tpl(this.options.config));

            return this;
        }
    });
});