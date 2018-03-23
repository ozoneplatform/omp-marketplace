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
            var me = this;
            this.template = Handlebars.compile($('#import-card-template').html());
            this.contentViews = this.model.get('contentViews');
            this.createsNewContentViews = this.model.get('createsNewContentViews');
            this.cardTitle = this.model.get('title');
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
            this.$el.html(this.template(this.model.toJSON()));
            if (this.contentViews) this.renderCurrentStep();

            $("[data-toggle=tooltip]").tooltip({
                placement: 'bottom'
            });

            return this;
        },

        renderCurrentStep: function() {
            var currentStep = this.contentViews[this.currentStep];
            this.currentView = currentStep;

            if (this.lastShownView) this.lastShownView.hide();

            if (this.currentView.isRendered()) {
                this.currentView.show();
            } else {
                this.$el.append(this.currentView.render().el);
            }

            this.shown();
        },

        shown: function() {
            if (this.cardTitle === "Resolve Differences") this.updateTitle();
            if (this.currentView) this.currentView.shown();

            return this;
        },

        updateTitle: function() {
            var difference = this.currentView.model.get('differenceType');
            var totalSteps = this.contentViews.length;
            var message;
            if (difference === 'type') {
                message = " - Type (" + (this.currentStep + 1) + " of " + totalSteps + ")";
            } else if (difference === 'category') {
                message = " - Categories (" + (this.currentStep + 1) + " of " + totalSteps + ")";
            } else if (difference === 'custom field') {
                message = " - Custom Fields (" + (this.currentStep + 1) + " of " + totalSteps + ")";
            } else if (difference === 'agency') {
                message = " - Companies (" + (this.currentStep + 1) + " of " + totalSteps + ")";
            } else if (difference === 'contact type') {
                message = " - Contact Types (" + (this.currentStep + 1) + " of " + totalSteps + ")";
            }

            if (message) {
                this.$el.find('#wizard-card-header').html("Resolve Differences" + message);
            }
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

        resetSteps: function() {
            this.currentStep = 0;
        }

    });

    return cardView;
});
