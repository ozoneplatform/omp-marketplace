define([
    '../views/tabbable/TabsView',
    './ProfileTab',
    './PreferencesTab',
    './ServiceItemTab',
    './ItemCommentTab',
    'underscore'
], function(TabsView, ProfileTab, PreferencesTab, ServiceItemTab, ItemCommentTab, _) {
    'use strict';

    var SuperClass = TabsView,
        //a mapping from the sub-fragment identifier to the constructor for the
        //corresponding tab
        tabMapping = {
            'profile': ProfileTab,
            'preferences': PreferencesTab,
            'myListings': ServiceItemTab,
            'reviews': ItemCommentTab
        };

    return SuperClass.extend({
        className: 'store-modal-contents',

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);
            this.makeTabPanes();
        },

        getTabMappings: function() {
            return this.model.isSelf() ? tabMapping : _.omit(tabMapping, 'preferences');
        },

        //create instances of the various child views for each tab
        makeTabPanes: function() {
            var model = this.model;

            this.tabPanes = _.object(_.map(this.getTabMappings(), function(Tab, subFragment) {
                return [subFragment, new Tab({ model: model })];
            }));
        },

        remove: function() {
            _.each(this.tabPanes, function(view) {
                view.remove();
            });
        }
    });
});
