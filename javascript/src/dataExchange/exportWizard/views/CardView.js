define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView'
], function($, _, Backbone, Handlebars, BaseView){

    var SuperClass = BaseView;

    var cardView = SuperClass.extend({

        className: "wizard-card",

        initialize: function() {

            this.template = Handlebars.compile($('#card-template-export').html());
            this.contentViews = this.model.get('contentViews');
            this.createsNewContentViews = this.model.get('createsNewContentViews');
            this.currentStep = 0;

            // If the user switches to this card, the step (i.e. contentView or subview number)
            // is reset so we always start on the first subview
            this.model.bind('change:switchToMe', function(e) {
                if(e.get('switchToMe')){
                    me.resetSteps();
                }
            });
        },

        render: function() {
            // Render this card
            this.$el.html(this.template(this.model.toJSON()));
            // If this card has contentViews, render the current one
            if (this.contentViews) {
                this.renderCurrentStep();
            }
            return this;
        },


        renderCurrentStep: function() {

            this.currentView = this.contentViews[this.currentStep];
            if (this.lastShownView) {
                this.lastShownView.hide();
            }

            if (this.currentView.isRendered()) {
                this.currentView.show();
            } else {
                this.$el.append(this.currentView.render().el);
            }

            this.shown();

        },

        shown: function() {

            $("[data-toggle=tooltip]").tooltip({
                placement: 'bottom'
            });

            if (this.currentView) {
                this.currentView.shown();
            }
            return this;
        },

        nextStep: function() {
            this.lastShownView = this.currentView;
            this.currentStep += 1;
            this.renderCurrentStep();
        },

        prevStep: function() {
            this.lastShownView = this.currentView;
            this.currentStep -= 1;
            this.renderCurrentStep();
        },

        isFirstStep: function() {
            return (this.currentStep === 0);
        },

        isLastStep: function() {
            //If we have multiple content views check if last step
            if (this.contentViews) {
                return (this.currentStep === this.contentViews.length - 1);
                //If no content views then return true since it's the only step (thus the last step)
            } else {
                return true;
            }
        },

        beforeNextCard: function() {
            return this.currentView.beforeNextCard();
        },

        createNextViews: function() {
            return this.currentView.creatNextViews();
        },

        resetSteps: function() {
            this.currentStep = 0;
        },

        remove: function () {

            if (this.currentView) {
                this.currentView.remove();
            }

            if (this.lastShownView) {
                this.lastShownView.remove();
            }

            return SuperClass.prototype.remove.call(this);
        }
});

return cardView;
});