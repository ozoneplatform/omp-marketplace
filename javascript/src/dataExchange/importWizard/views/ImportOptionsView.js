define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/OptionsView'
], function($, _, Backbone, Handlebars, BaseView) {

    var SuperClass = BaseView;

    var importOptionsView = SuperClass.extend({

        optionsTpl: '#import-options-template',

        optionsDefaults: {'importAllProfiles':true, 'importRatings': true, 'importScorecards': true},

        initialize: function() {
            SuperClass.prototype.initialize.call(this);
            this.json = this.model.get('data');
        },

        shown: function() {
            SuperClass.prototype.shown.call(this);

            if (!this.hasRatings()) {
                this.json.options['importRatings'] = false;
                this.$el.find('.ratings-col').html("<div class='no-option'>No reviews included</div>");
            }
        },

        hasRatings: function() {
            var hasRatings = false;
            _.each(this.json.selectedServiceItems, function(serviceItem) {
                if (serviceItem.itemComments && serviceItem.itemComments.length > 0) {
                    hasRatings = true;
                }
            });

            return hasRatings;
        }
    });

    return importOptionsView;
});
