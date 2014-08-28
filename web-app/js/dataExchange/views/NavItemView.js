define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    './WizardBaseView',
    '../renderFunctions'
], function($, _, Backbone, Handlebars, BaseView){

    var SuperClass = BaseView;

    var navItemView = SuperClass.extend({


        navItemTpl: Handlebars.compile(

            '<li class="wizard-nav-item {{setVisited visited}} {{activate active}} {{enable active visited}}">' +
                '<a class="wizard-nav-link"><i class="icon-chevron-right"></i>' +
                '<span class= "wizard-step-num">{{step}}</span> {{title}}</a>'+
                '</li>'),

        events: {
            // Disabled for now
//            "click .wizard-nav-item" : "switchCard"
        },

        initialize: function() {
            var me = this;
            me.model.on('change',me.render,me);

        },

        render: function() {
            this.$el.html(this.navItemTpl(this.model.toJSON()));
            return this;
        },

        switchCard: function(e) {
            // Will trigger a change event
            // Disabled for now until we get figured out how side navigation should work in light of the fact that there
            // can be sub-steps for each card that aren't reflected in the nav panel
//            if (this.model.get('visited') && !this.model.get('active'))  {
//                // This event is intended to trigger a switch to the selected card
//                this.model.set('switchToMe', true);
//            }
        }
    });

    return navItemView;
});