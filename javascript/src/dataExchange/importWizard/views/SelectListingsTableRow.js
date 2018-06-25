define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
    '../../renderFunctions'
], function($, _, Backbone, Handlebars, BaseView) {

    var SuperClass = BaseView;
    
    var selectListingsTableRow = SuperClass.extend({
        tagName: 'tr',

        initialize: function() {
            this.template = Handlebars.compile($('#select-listings-table-row').html());
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });

    return selectListingsTableRow;
});