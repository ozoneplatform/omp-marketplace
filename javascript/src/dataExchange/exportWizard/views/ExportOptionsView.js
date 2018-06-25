define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/OptionsView'
], function($, _, Backbone, Handlebars, BaseView) {

    var SuperClass = BaseView;

    var exportOptionsView = SuperClass.extend({

        optionsTpl: '#import-options-template',

        optionsDefaults: {'exportProfiles':true, 'exportRatings': true},

        initialize: function() {
            SuperClass.prototype.initialize.call(this);
            this.json = this.model.get('data');
        },

        shown: function() {
            SuperClass.prototype.shown.call(this);

            if (!this.hasRatings()) {
                this.json.options['exportRatings'] = false;
                this.$el.find('.ratings-col').html("<div class='no-option'>No reviews included</div>");
            }
        },

        hasRatings: function() {
            var hasRatings = false;
            _.each(this.json.allServiceItems, function(serviceItem) {
                if (serviceItem.totalComments > 0) {
                    hasRatings = true;
                }
            });

            return hasRatings;
        }
    });

    return exportOptionsView;
});
