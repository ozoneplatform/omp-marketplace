define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
    './CardView',
    '../../views/NavView',
    'views/LoadMask',
    './CompleteView'

], function($, _, Backbone, Handlebars, BaseView, CardView, NavView, LoadMask, CompleteView) {

    var SuperClass = BaseView;

    var WizardView = SuperClass.extend({

        events: {
            'click .close': 'close',
            'click .cancel-button': 'close',
            'click .next-button': 'nextStep',
            'click .prev-button': 'prevStep'
        },

        initialize: function() {
            this.wizardTemplate = Handlebars.compile($('#wizard-template').html());
            this.currentStep = 0;
            this.totalSteps = this.options.cards.length;

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
            this.totalProgressBarSteps = 7;


            var me = this;
            this.options.cards.bind('change:switchToMe', function(e) {
                if(e.get('switchToMe')){
                    console.log('wizardView');
                    console.log(e);
                    me.switchCards(e);
                }
            });
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

            this.$currentViewContainer = this.$(".wizard-card-container");
            this.$navContainer = this.$(".wizard-nav-container") ;

            this.$nextStepButton = this.$(".next-button");
            this.$prevStepButton = this.$(".prev-button");
            this.$cancelButton = this.$(".cancel-button");
            this.$progressBar = this.$(".bar");

            this.$navContainer.html(this.navView.el);

            this.navView.render();
            this.updateProgressBar();
            this.renderCurrentStep();

            return this;
        },

        renderCurrentStep: function() {
            this.currentView = this.cardViews[this.currentStep];
            this.nextView = this.cardViews[this.currentStep + 1];

            if (!this.isFirstStep()) {
                this.prevView = this.cardViews[this.currentStep - 1];
                this.prevView.model.set({active: false});
            }

            this.currentView.model.set({active: true, visited: true});

            if (this.lastShownView) this.lastShownView.hide();

            if (this.currentView.isRendered()) {
                this.currentView.show().$el.scrollTop(0);
            } else {
                this.$currentViewContainer.append(this.currentView.render().el);
            }

            this.renderButtons();

            this.shown();

            return this;
        },

        renderButtons: function() {
            if (!this.isFirstStep()) {
                this.$prevStepButton.html("Back").show();
            } else {
                this.$prevStepButton.hide();
            }
            if (this.nextView) {
                // Next to last screen button is "Import"
                if (this.isSubmitStep()) {
                    this.$nextStepButton.html("Import");
                } else {
                    this.$nextStepButton.html("Next");
                }

                this.nextView.model.set({active: false});

            } else {
                this.$nextStepButton.html("Finish");
                // hide the Back button
                this.$prevStepButton.hide();
                // hide the Cancel button
                this.$cancelButton.hide();
            }
        },

        updateProgressBar: function() {
            this.$progressBar.css("width", ((this.currentProgressBarStep)/this.totalProgressBarSteps * 100) + '%');
        },

        shown: function() {
            this.currentView.shown();

            return this;
        },

        nextStep: function() {
            this.currentProgressBarStep += 1;
            this.updateProgressBar();

            if (!this.currentView.isLastStep()) {
                this.currentView.nextStep();
            }
            else if (this.isSubmitStep()) {
                this.lastShownView = this.currentView;
                this.currentStep += 1;
                this.doImport();
            }
            else if (this.isLastStep()) {
                this.close();
            }
            else {
                this.currentView.beforeNextCard();
                //Check to see if current card is capable of creating new contentViews for the next card
                if (this.currentView.createsNewContentViews) {
                    this.addContentViews();
                    this.currentView.createsNewContentViews = false;
                }

                this.lastShownView = this.currentView;
                this.currentStep += 1;
                this.renderCurrentStep();
            }
        },

        prevStep: function() {
            this.currentProgressBarStep -= 1;
            this.updateProgressBar();

            if (!this.currentView.isFirstStep()) {
                this.currentView.prevStep();
            }
            else if (!this.isFirstStep()) {
                this.lastShownView = this.currentView;

                this.currentStep -= 1;
                this.renderCurrentStep();
            }
        },

        addContentViews: function() {
            var newContentViews = this.currentView.currentView.createNewViews();
            var resolveView = this.cardViews[this.currentStep + 2];
            resolveView.contentViews = newContentViews;

            // The total progress bar steps can change based on the number of resolve differences views that were created
            if (newContentViews.length === 3) {
                this.totalProgressBarSteps -= 1;
            }
            if (newContentViews.length === 2) {
                this.totalProgressBarSteps -= 2;
            } else if (newContentViews.length === 1) {
                this.totalProgressBarSteps -= 3;
            }
            this.updateProgressBar();
        },

        doImport: function() {
            var me = this;
            var data = this.model.get('data');

            this.getAgencyInfo();

            data['serviceItems'] = data['selectedServiceItems'];
            delete data.selectedServiceItems;

            LoadMask.show();

            $.ajax({
                dataType: "json",
                url: Marketplace.context + "/dataExchange/importSave",
                type: 'POST',
                data: {json: JSON.stringify(data)},
                success: function(val) {

                    // Update the DataExchange log display
                    addImportLogRow(val.date, val.summary);

                    // Render the current step
                    me.renderCurrentStep();

                    // Hide the load mask
                    LoadMask.hide();

                    // Update the Complete view with response values
                    me.updateCompleteCard(val);

                },
                error: function(val) {

                    // Construct the value to use for the Complete card
                    var errorResponse = {};

                    // Check to see if json was returned. If so then get the response, which will contain
                    // the error summary
                    if (val.responseJSON) {

                        errorResponse = val.responseJSON;
                        errorResponse["summary"]  = val.responseJSON.summary;
                        // Update the DataExchange log display - this type of error often will be reflected in the log
                        addImportLogRow(errorResponse.date, errorResponse.summary);

                    }  else {
                        // Construct the summary with the statusText returned from the server
                        errorResponse['summary'] =
                            val.statusText  + '. Check server logs for error details.';
                    }
                    // Save the status code in case we want to report it
                    errorResponse['status'] = val.status;
                    // Set success to false since this is used by CompleteCard
                    errorResponse['success'] = false;

                    // Render the current step
                    me.renderCurrentStep();

                    // Hide the load mask
                    LoadMask.hide();

                    // Update the Complete view with response values
                    me.updateCompleteCard(errorResponse);

                }

            });
        },

        updateCompleteCard: function(val) {
            var completeModel;
            if (val.success) {

                completeModel = new Backbone.Model({ response: val.message });
            }

            else {
                completeModel = new Backbone.Model({ message: 'There was an error importing this file. Error summary: ' + (val.summary) });
                this.currentView.$el.find('#wizard-card-header').html("Import Failed");
            }

            var completeView = new CompleteView({ model: completeModel });
            this.currentView.$el.append(completeView.render().el);
            completeView.shown();
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

        getAgencyInfo: function() {
            var me = this;
            var data = this.model.get('data');

            _.each(data['agencies'], function(agency){
                if(agency.id != 'ID_NO_AGENCY'){
                    agency.iconUrl = _.findWhere(me.model.get('data')['serviceItems'], {agency: agency.name}).agencyIcon;
                }
            });

        }
    });

    return WizardView;
});
