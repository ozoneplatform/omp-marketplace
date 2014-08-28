define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
], function($, _, Backbone, Handlebars, BaseView) {
    var SuperClass = BaseView;

    var resolveDifferencesItemView = SuperClass.extend({
        tagName: 'tr',

        initialize: function() {
            this.template = Handlebars.compile($('#resolve-differences-row').html());
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });

    return resolveDifferencesItemView;
});