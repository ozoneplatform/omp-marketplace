define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView'
], function($, _, Backbone, Handlebars, BaseView) {
    var SuperClass = BaseView;
    var completeView = SuperClass.extend({
        tagName: 'div',
        className: 'wizard-card-body',

        initialize: function() {
            this.template = Handlebars.compile($('#import-complete').html());
        },

        render: function() {
            var val = this.model.get('response');
            if (val) this._getCompletedData(val);
            this.$el.html(this.template(this.model.toJSON()));

            return this;

        },

        shown: function() {
            $('.complete-toggle').unbind('click');

            $('.complete-section').hide();
            $('.complete-toggle').removeClass('complete-toggle-collapse');

            $('.complete-toggle').click(function() {
                $(this).next('.complete-section').toggle();
                $(this).toggleClass('complete-toggle-collapse');
            });
        },

        _getCompletedData: function(val) {
            var serviceItems = val.serviceItems,
                categories = val.categories,
                profiles = val.profiles,
                types = val.types,
                customFields = val.customFieldDefs,
                agencies = val.agencies,
                contactTypes = val.contactTypes;

            var complete = [{
              name: 'Listings',
              total: serviceItems.created + serviceItems.failed + serviceItems.updated + serviceItems.notUpdated,
              created: serviceItems.created,
              failed: serviceItems.failed,
              updated: serviceItems.updated,
              notUpdated: serviceItems.notUpdated
            }, {
              name: 'Categories',
              total: categories.created + categories.failed + categories.updated + categories.notUpdated,
              created: categories.created,
              failed: categories.failed,
              updated: categories.updated,
              notUpdated: categories.notUpdated
            }, {
              name: 'Profiles',
              total: profiles.created + profiles.failed + profiles.updated + profiles.notUpdated,
              created: profiles.created,
              failed: profiles.failed,
              updated: profiles.updated,
              notUpdated: profiles.notUpdated
            }, {
              name: 'Types',
              total: types.created + types.failed + types.updated + types.notUpdated,
              created: types.created,
              failed: types.failed,
              updated: types.updated,
              notUpdated: types.notUpdated
            }, {
              name: 'Custom Fields',
              total: customFields.created + customFields.failed + customFields.updated + customFields.notUpdated,
              created: customFields.created,
              failed: customFields.failed,
              updated: customFields.updated,
              notUpdated: customFields.notUpdated
            }, {
              name: 'Companies',
              total: agencies.created + agencies.failed + agencies.updated + agencies.notUpdated,
              created: agencies.created,
              failed: agencies.failed,
              updated: agencies.updated,
              notUpdated: agencies.notUpdated
            }, {
              name: 'Contact Types',
              total: contactTypes.created + contactTypes.failed + contactTypes.updated + contactTypes.notUpdated,
              created: contactTypes.created,
              failed: contactTypes.failed,
              updated: contactTypes.updated,
              notUpdated: contactTypes.notUpdated
            }];

            this.model.set('completeData', complete);
        }
    });

    return completeView;
});
