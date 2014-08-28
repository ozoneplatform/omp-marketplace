define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'bootstrap',
    './views/WizardView',
    './views/CardView',
    './views/SelectListingsView',
    './views/ExportOptionsView',
    './views/ReviewView',
    './views/CompleteView',
    'views/LoadMask',
    './models/CardModel',
    './collections/CardsCollection',
    '../collections/ServiceItems'

], function($, _, Backbone, Handlebars, Bootstrap, WizardView, CardView, SelectListingsView, ExportOptionsView, ReviewView, CompleteView, LoadMask, CardModel, CardsCollection, ServiceItems) {
    // turn off caching through cache busting
    $.ajaxSettings.cache = false;

    function onModalOpen () {
        $('html').addClass('modal-open');
    }

    function onModalClose () {
        $('html').removeClass('modal-open');
    }

    var initialize = function() {
        LoadMask.show();

        $.extend( $.fn.dataTableExt.oStdClasses, {
            "sWrapper": "dataTables_wrapper form-inline"
        });
        var cardsCollection = new CardsCollection();
        var serviceItems = new ServiceItems();

        serviceItems.fetch().always(_.bind(LoadMask.hide, LoadMask)).done(function() {

            // Initialize json to be sent to server. Passes as reference, so all card models
            // can use it
            // Example json
            // {"serviceItems":[{ "id" : 2}, {"id":5}], "options":[{"exportAllProfiles":true}, {"exportRatings":true}]}
            var json = {};
            var selectListingsModel = new Backbone.Model({
                title: 'BSelect the listing(s) you want to export from the Store.',
                data: json
            });
            var reviewModel = new Backbone.Model({data: json});

            var stepConfig = [{
                step: 1,
                title: "Select Listings",
                instructions: 'Select the listing(s) you want to export from the Store.',
                // True if this view will create new content views (subViews) for the *next* card
                createsNewContentViews: false,
                // Content views (subViews) for this card
                contentViews: [ new SelectListingsView({
                    model: selectListingsModel,
                    listingsCollection: serviceItems
                }) ] ,
                enabled: true
            },
            {
                step: 2,
                title: "Export Options",
                instructions: "Select the options for this export",
                contentViews: [ new ExportOptionsView({
                    model: new Backbone.Model({
                        ratingTrueTooltip: "Export listings' star-ratings and user reviews",
                        ratingFalseTooltip: "Do not export listings' star-ratings and " +
                            "user reviews",
                        ratingName: "exportRatings",
                        userProfileTrueTooltip: "Export the profiles for every user in the Store",
                        userProfileFalseTooltip: "Only export the user profiles of owners, " +
                            "administrators and reviewers associated with the listing(s)",
                        profileName: "exportProfiles",
                        data: json
                    })
                })],
                enabled: true
            },
            {
                step: 3,
                title: "Review",
                instructions: "",
                contentViews: [ new ReviewView({model: reviewModel}) ],
                enabled: true
            },
            {
                step: 4,
                title: "Export Complete",
                instructions: "",
                contentViews: [ new CompleteView({model: new Backbone.Model()}) ],
                enabled: true
            }];

            cardsCollection.add(new CardModel(stepConfig[0]));
            cardsCollection.add(new CardModel(stepConfig[1]));
            cardsCollection.add(new CardModel(stepConfig[2]));
            cardsCollection.add(new CardModel(stepConfig[3]));

            var wizardModel = new Backbone.Model({
                title: "Export Listings",
                data: json,
                step: 0
            });

            var view = new WizardView({
                model: wizardModel,
                cards: cardsCollection
            });

            view.$el.one({
                show: onModalOpen
            });

            view.$el.on('hide', function (evt) {
                if(evt.target === view.el) {
                    onModalClose();
                }
            });

            view.show();
        });

    };

    return {
        initialize: initialize
    };
});
