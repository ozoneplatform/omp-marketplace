define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    './WizardBaseView'

], function($, _, Backbone, Handlebars, BaseView) {

    var SuperClass = BaseView;

    var optionsView = SuperClass.extend({

        tagName: 'div',
        className: 'wizard-card-body',

        events: {
            'click input[type=radio]': 'toggleOption'
        },

        optionsTpl: null,

        optionsDefaults: null,

        model: null,

        initialize: function() {
            this.template = Handlebars.compile($(this.optionsTpl).html());

            this.json = this.model.get('data');

            this.json['options'] = this.optionsDefaults;
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));

            return this;
        },

        shown: function() {
            this.render();

            $("[data-toggle=tooltip]").tooltip({
                placement: 'bottom'
            });

            $(".selection-count").hide();
        },

        toggleOption: function(e) {
            var optionName = e.currentTarget.name;
            var optionValue = JSON.parse(e.currentTarget.value);

            this.json['options'][optionName] = optionValue;
        }
    });

    return optionsView;
});
