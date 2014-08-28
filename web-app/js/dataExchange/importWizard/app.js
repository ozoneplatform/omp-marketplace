define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'bootstrap',
    './views/WizardView',
    './views/CardView',
    './views/SelectListingsView',
    './views/ImportOptionsView',
    './views/ReviewView',
    './models/CardModel',
    './collections/CardsCollection'

], function($, _, Backbone, Handlebars, Bootstrap, WizardView, CardView, SelectListingsView, ImportOptionsView, ReviewView, CardModel, CardsCollection) {
    // turn off caching through cache busting
    $.ajaxSettings.cache = false;

    function onModalOpen () {
        $('html').addClass('modal-open');
    }

    function onModalClose () {
        $('html').removeClass('modal-open');
    }

    var initialize = function() {

        $.extend( $.fn.dataTableExt.oStdClasses, {
            "sWrapper": "dataTables_wrapper form-inline"
        });

        var frame = frames["hidden-upload-frame"].document.documentElement;
        var response = frame.textContent || frame.innerText,
                json;

        if (response) {
            try {
                json = JSON.parse(response);
//                console.log(json);
            }
            catch(err) {
                //
            }
        }

        if (json) {
            if (json.serviceItems) {
                var cardsCollection = new CardsCollection();
                var selectListingsModel = new Backbone.Model({
                    title: 'Select the listing(s) you want to import into the Store.',
                    data: json

                });
                var reviewModel = new Backbone.Model({ data: json });

                var stepConfig = [
                    {
                        step:              1,
                        title:             "Select Listings",
                        instructions:      "Select the listing(s) you want to import into the Store.",
                        createsNewContentViews: true,
                        contentViews: [ new SelectListingsView({ model: selectListingsModel }) ]
                    },
                    {
                        step:               2,
                        title:              "Import Options",
                        instructions:       "Select the options for this import.",
                        contentViews: [ new ImportOptionsView({ model: new Backbone.Model({

                            ratingTrueTooltip: "Import the listing's star-ratings and user reviews",
                            ratingFalseTooltip: "Do not import the listing's star-ratings and user reviews",
                            ratingName: "importRatings",
                            userProfileTrueTooltip: "Import all user profiles from the selected import file",
                            userProfileFalseTooltip: "Only import the user profiles of owners, administrators and reviewers associated with the listing(s)",
                            profileName: "importAllProfiles",
                            data: json
                        })})]
                    },
                    {
                        step:               3,
                        title:                  "Resolve Differences",
                        instructions:           "",
                        contentViews: []
                    },
                    {
                        step:               4,
                        title:             "Review",
                        instructions:      "",
                        contentViews: [ new ReviewView({model: reviewModel}) ]
                    },
                    {
                        step:               5,
                        title:              "Import Complete",
                        instructions:       ""
                    }
                ];

                cardsCollection.add(new CardModel(stepConfig[0]));
                cardsCollection.add(new CardModel(stepConfig[1]));
                cardsCollection.add(new CardModel(stepConfig[2]));
                cardsCollection.add(new CardModel(stepConfig[3]));
                cardsCollection.add(new CardModel(stepConfig[4]));

                var wizardModel = new Backbone.Model({title: "Import from File", data: json });

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
            } else {
                if (json.message.indexOf("file null or empty") !== -1 ) {
                    $('.file-upload-warning').html("Choose a file to import");
                } else {
                    $('.file-upload-warning').html("Choose a valid JSON file");
                }
            }
        }
    };

    return {
        initialize: initialize
    };
});
