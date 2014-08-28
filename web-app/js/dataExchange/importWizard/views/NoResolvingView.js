define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
], function($, _, Backbone, Handlebars, BaseView) {
    var SuperClass = BaseView;
    var noResolvingView = SuperClass.extend({
        tagName: 'div',
        className: 'wizard-section',
    
        initialize: function() {
            this.template = Handlebars.compile($('#no-resolving-template').html());
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });

    return noResolvingView;
});