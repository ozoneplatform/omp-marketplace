define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
], function($, _, Backbone, Handlebars, BaseView) {
    var SuperClass = BaseView;
    var reviewView = SuperClass.extend({
        tagName: 'div',
        className: 'review-page',

        initialize: function() {
            this.template = Handlebars.compile($('#import-review').html());
            this.model.on('change', this.render, this);
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));

            return this;
        },

        shown: function() {
            this._updateReviewData();
            var message = this.selectedItems.length + " listing(s) are ready to be imported " +
                "into the Store. This includes any required listings. After reviewing the " +
                "listings, click Import to add them to the Store.";
            this.model.set({
                listings: this.selectedItems,
                message: message,
                types: this.newTypes,
                categories: this.newCategories,
                customFields: this.newCustomFields,
                agencies: this.newAgencies,
                contactTypes: this.newContactTypes
            });

            $('.review-toggle').unbind('click');

            $('.review-section').hide();
            $('.review-toggle').removeClass('review-toggle-collapse');

            $('.review-toggle').click(function() {
                $(this).next('.review-section').toggle();
                $(this).toggleClass('review-toggle-collapse');
            });

             $("[data-toggle=tooltip]").tooltip();
        },

        _updateReviewData: function() {
            var json = this.model.get('data');

            this.selectedItems = json['selectedServiceItems'];
            this.newTypes = _.filter(json['types'], function(item) {
                return item.mapsTo === "create";
            });

            this.newCategories = _.filter(json['categories'], function(item) {
                return item.mapsTo === "create";
            });

            this.newCustomFields = _.filter(json['customFieldDefs'], function(item) {
                return item.mapsTo === "create";
            });

            this.newAgencies = _.filter(json['agencies'], function(item) {
                return item.mapsTo === "create" && item.id !== 'ID_NO_AGENCY';
            });

            this.newContactTypes = _.filter(json['contactTypes'], function(item) {
                return item.mapsTo === "create";
            });
        }
    });

    return reviewView;
});
