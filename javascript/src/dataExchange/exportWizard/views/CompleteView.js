define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView'

], function($, _, Backbone, Handlebars, BaseView) {

    var SuperClass = BaseView;

    var completeView = SuperClass.extend({

        initialize: function() {
            this.template = Handlebars.compile(
                '<p class="review-message">{{message}}</p>'
            );
            this.listenTo(this.model, 'change', _.bind(this.render, this));
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },

        shown: function() {
            this.model.set('message', "The listing(s) were exported from the Store.");
        }


    });

    return completeView;
});