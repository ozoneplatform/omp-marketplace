define([
    'views/BaseView',
    'marketplace'
], function(BaseView, Marketplace) {
    'use strict';

    var SuperClass = BaseView;

    return SuperClass.extend({
        tagName: 'li',

        render: function() {
            this.$el.tooltip({
                title: this.model.get('description'),
                placement: 'right',
                delay: 675
            });

            return this;
        },

        remove: function() {
            this.$el.tooltip('destroy');

            return SuperClass.prototype.remove.apply(this, arguments);
        },

        getFilterBaseUrl: function() {
            return Marketplace.context + '/search/';
        }
    });
});
