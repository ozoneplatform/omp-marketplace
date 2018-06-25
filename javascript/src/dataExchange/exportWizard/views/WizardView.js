
define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
    'views/LoadMask',
    './CardView',
    '../../views/NavView'

], function($, _, Backbone, Handlebars, BaseView, LoadMask, CardView, NavView) {

    var SuperClass = BaseView;

    var WizardView = SuperClass.extend({

        events: {
            'click .close': 'close',
            'click .cancel-button': 'close',
            'click .next-button': 'nextStep',
            'click .prev-button': 'prevStep'
        },


        initialize: function() {

            var me = this;
            me.wizardTemplate = Handlebars.compile($('#wizard-template').html());
            me.currentStep = 0;

            // Get view for each cardModel
            // Array of views
            var cardViews = [];

            _.each(this.options.cards, function(value, key, list) {

                cardViews.push(new CardView({
                    model: list.at(key)
                }));

            });

            this.cardViews = cardViews;

            this.navView =  new NavView({
                model: new Backbone.Model({
                    title: "Navigation",
                    cards: this.options.cards

                })
            }) ;

            this.currentProgressBarStep = 0;
            this.totalProgressBarSteps = this.options.cards.length -1;

            var me = this;

            this.listenTo(this.options.cards, 'change:switchToMe', function(e) {
                    if(e.get('switchToMe')){
                        me.switchCards(e);
                    }
            });

//            this.options.cards.bind('change:switchToMe', function(e) {
//                if(e.get('switchToMe')){
//                    me.switchCards(e);
//                }
//            });
        },

        switchCards: function(e) {
            e.set('switchToMe', false);
            // Get the current step from the model
            var newCardStep = e.get('step') - 1;
            this.currentStep = newCardStep;
            // Render the step
            this.renderCurrentStep();

        },

        render: function() {

            this.$el.html(this.wizardTemplate(this.model.toJSON()));
            this.$currentViewContainer = this.$el.find(".wizard-card-container");
            this.$navContainer = this.$el.find(".wizard-nav-container") ;

            this.$nextStepButton = this.$el.find(".next-button");
            this.$prevStepButton = this.$el.find(".prev-button");
            this.$cancelButton = this.$el.find(".cancel-button");
            this.$progressBar = this.$el.find(".bar");

            this.$navContainer.html(this.navView.el);

            this.navView.render();
            this.updateProgressBar();
            this.renderCurrentStep();

            return this;
        },

        // Get index of next valid card view
        getNextStepIndex: function() {

            var next = this.currentStep + 1 ;
            while (next < (this.options.cards.length - 1) && !this.cardViews[next].model.get('enabled')) {
                next +=1 ;
            }

            // -1 means there is no next view
            return (next < this.options.cards.length) ? next : -1;
        },

        // Get index of previous valid card view
        getPrevStepIndex: function() {

            var prev = this.currentStep - 1;
            if (!this.isFirstStep()) {
                while (prev > 0 && !this.cardViews[prev].model.get('enabled')) {
                    prev -= 1 ;
                }
            }

            // -1 means there is no prev view
            return (prev >= 0)  ? prev : -1;

        },

        // Get a view given a step index. A value of -1 indicates the view does not exist
        // so return null
        getView: function(step) {

            if (step >= 0) {

                return this.cardViews[step] ;
            } else {
                return null;
            }
        },

        // Update the active and visited states of the views
        updateViewStates: function()  {

            this.currentView.model.set({active: true, visited: true});
            if (this.nextView) {
                this.nextView.model.set({active: false});
            }
            if (this.prevView) {
                this.prevView.model.set({active: false});
            }

        },

        // Update button states
        // Button text, hide or show
        renderButtons: function() {

            if (!this.isFirstStep()) {
                this.$prevStepButton.html("Back").show();
            } else {
                this.$prevStepButton.hide();
            }

            if (this.nextView) {
                // Next to last screen button is "Import"
                if (this.isSubmitStep()) {
                    this.$nextStepButton.html("Export");
                } else {
                    this.$nextStepButton.html("Next");
                }
            } else {
                this.$nextStepButton.html("Finish");
                // hide the Back button
                this.$prevStepButton.hide();
                // hide the Cancel button
                this.$cancelButton.hide();
            }
        },


        // Render the current Card
        renderCurrentStep: function() {

            this.currentView = this.getView(this.currentStep);
            this.prevView = this.getView(this.getPrevStepIndex());
            this.nextView = this.getView(this.getNextStepIndex());

            this.updateViewStates();

            if (this.isFirstStep()) {
                this.totalProgressBarSteps = this.options.cards.length - 1;
                this.updateProgressBar();
            }

            if (this.lastShownView) {
                this.lastShownView.hide();
            }

            if (this.currentView.isRendered()) {
                this.currentView.show().$el.scrollTop(0);
            } else {
                this.$currentViewContainer.append(this.currentView.render().el);
            }

            this.renderButtons();

            this.shown();
            return this;

        },

        updateProgressBar: function() {
            this.$progressBar.css("width", ((this.currentProgressBarStep)/this.totalProgressBarSteps * 100) + '%');
        },

        shown: function() {
            this.currentView.shown();
            return this;
        },


        // Handler for click on Next Button
        nextStep: function() {
            var me = this;
            this.currentProgressBarStep += 1;
            this.updateProgressBar();

            if (!this.currentView.isLastStep()) {
                this.currentView.nextStep();
            }
            else if (!this.isLastStep()) {
//                var nextView = this.cardViews[this.currentStep + 1];

                // Do any processing necessary before the next card
                this.currentView.beforeNextCard();
                //Check to see if current card is capable of creating new contentViews for the next card
                if (this.currentView.createsNewContentViews) {

                    // get the new views
                    var newContentViews = this.currentView.createNextViews();
                    this.nextView.contentViews = newContentViews;

                    // The total progress bar steps can change based on the number of resolve differences views that were created
                    if (newContentViews.length === 2) {
                        this.totalProgressBarSteps -= 1;
                    } else if (newContentViews.length === 1) {
                        this.totalProgressBarSteps -= 2;
                    }
                    this.updateProgressBar();
                }
                this.lastShownView = this.currentView;
                this.currentStep = this.getNextStepIndex();

                if (this.isLastStep()) {
                    this.submitExportRequest();  // Form so we can download the data
                }

                this.renderCurrentStep();
            }
            else {
                this.close();
            }
        },

        // Use form so data will download
        submitExportRequest: function() {
            var me = this;
            var data = this.model.get('data');
            delete data.allServiceItems;
            // Transform options values for exportProfiles
            data.options.exportProfiles = (data.options.exportProfiles) ? 'all' : 'associated';
            // encode it
            var json = (JSON.stringify(data)).replace(/"/g, "&#34");
            // create the form
            var form = $('<form method="POST" action="exportListings" name="exportForm">' +
                '<input type="hidden" name="json" value="'+ json + ' ">' +
                '</form>');
            $('body').append(form);
            form.submit();

            LoadMask.hide();
        },


        updateCompleteCard: function(val) {
            if (val.status === 200) {

                this.currentView.model.set('title', 'Export Complete');
                this.currentView.model.set('instructions', val.responseJSON.serviceItems.length + ' listings exported. ');

            }
            else {
                this.currentView.model.set('title', 'Export Failed');
                this.currentView.model.set('instructions', 'There was an error Exporting listings. Please check the system logs for more details.');
            }
            this.renderCurrentStep();
        },

        prevStep: function() {

            this.currentProgressBarStep -= 1;
            this.updateProgressBar();

            if (!this.currentView.isFirstStep()) {
                this.currentView.prevStep();
            }
            else if (!this.isFirstStep()) {
                this.lastShownView = this.currentView;
                this.currentStep = this.getPrevStepIndex();
                this.renderCurrentStep();
            }
        },

        isFirstStep: function() {
            return (this.currentStep === 0);
        },

        isLastStep: function() {
            return (this.currentStep === this.options.cards.length - 1);
        },

        isSubmitStep: function() {
            return (this.currentStep === this.options.cards.length - 2);
        },

        show: function() {
            $(document.body).append(this.render().el);
            this.$el.trigger('show');
        },

        close: function() {
            this.hide().remove();
        },

        remove: function () {

            this.navView.remove();
            _.each(this.cardViews, function(view) {
                view.remove();
            });
            if (this.currentView) {
                this.currentView.remove();
            }

            if (this.lastShownView) {
                this.lastShownView.remove();
            }

            return SuperClass.prototype.remove.call(this);
        }
    });

    return WizardView;
});
