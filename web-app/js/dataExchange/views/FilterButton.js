define([
    'jquery',
    'underscore',
    'backbone',
    '../../views/BaseView'
], function($, _, Backbone, BaseView) {

    return BaseView.extend({

        tagName: 'button',
        className: 'btn dropdown-toggle filter-btn',
        tpl: '<i class="icon-filter"></i> Filter <span class="caret"></span>',
        events: {},

        render: function() {
            this.$el.html(this.tpl);

            return this;
        }
    });
});