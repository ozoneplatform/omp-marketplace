define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView'
//    './ReviewTable'
], function($, _, Backbone, Handlebars, BaseView) {

    var SuperClass = BaseView;

    var reviewView = SuperClass.extend({

        tagName: 'div',

        className: 'wizard-card-body',

        initialize: function() {
            this.template = Handlebars.compile($('#export-review').html());
            this.listenTo(this.model, 'change', _.bind(this.render, this));
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },

        shown: function() {
            this._updateReviewData();
            this.model.set('listings', this.selectedItems);
            this.model.set('message', this.selectedItems.length + " listing(s) are ready to be exported from the Store." +
                "  Depending on your selection, this may include required listings that are not shown in the export list. After reviewing the listings, click Export.");
        },

        _updateReviewData: function() {
            this.json = this.model.get('data');
            this.selectedItems = [];

            var me = this;
            _.each(me.json['serviceItems'], function(itemIndex) {
                var serviceItem = _.find(me.json['allServiceItems'], {'id': itemIndex});
                me.selectedItems.push(serviceItem);
            });
        }
    });

    return reviewView;
});
