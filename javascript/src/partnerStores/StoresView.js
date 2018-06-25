define([
    '../views/BaseView',
    '../views/ListView',
    './StoreView',
    'underscore',
    'jquery',
    'marketplace'
],
function(BaseView, ListView, StoreView, _, $, Marketplace) {
    'use strict';

    return BaseView.extend({

        tagName: 'table',

        className: 'table',

        template:
            '<thead>' +
                '<tr>' +
                    '<th><h5>Name</h5></th>' +
                    '<th><h5>URL</h5></th>' +
                    '<th><h5>Timeout (ms)</h5></th>' +
                    '<th><h5>Active</h5></th>' +
                    '<th></th>' +
                '</tr>' +
            '</thead>' +
            '<tbody></tbody>',

        render: function () {
            this.$el.html(this.template);
            this.renderListView();

            return this;
        },

        renderListView: function () {
            this.listView = new ListView({
                collection: this.collection,
                ItemView: StoreView,
                el: this.$el.children('tbody')
            });
            this.listView.render();
            return this;
        },

        remove: function () {
            this.listView.remove();
            BaseView.prototype.remove.call(this);
        }

    });
});
