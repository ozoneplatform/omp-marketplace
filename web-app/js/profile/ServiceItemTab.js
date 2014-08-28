define([
    '../views/tabbable/TabPaneView',
    './MyListingsView',
    './MyChangelogView'
], function(TabPaneView, MyListingsView, MyChangelogView) {
    'use strict';

    var SuperClass = TabPaneView;

    return SuperClass.extend({
        className: 'service-item-tab',

        title: 'Listings',

        myListingsView: null,
        recentActivityView: null,

        initialize: function(options) {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.myListingsView = new MyListingsView(options);

            if (this.model.isSelf() || this.model.isAdmin()) {
                this.recentActivityView = new MyChangelogView(options);
            }
        },

        render: function() {
            this.$el
                .append(this.myListingsView.render().$el);

            if (this.recentActivityView) {
                this.$el.append(this.recentActivityView.render().$el);
            }

            return this;
        },

        remove: function() {
            this.myListingsView.remove();

            if (this.recentActivityView) {
                this.recentActivityView.remove();
            }

            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
