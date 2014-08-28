/**
 * A global loading spinner that appears as a modal in front of everything else.
 * This module returns the singleton instance of the mask, not the
 * constructor
 */
define([
    'views/BaseView',
    'spin',
    'marketplace',
    'underscore'
], function(BaseView, Spinner, Marketplace, _) {
    'use strict';

    var SuperClass = BaseView,
        html =
            '<div class="loading-mask-bg"></div>' +
            '<div class="loading-mask-content">' +
                '<div class="loading-mask-spinner-container"></div>' +
                '<div class="loading-mask-message">Loading...</div>' +
            '</div>',
        loadMaskOptions = {
            color: '#FFF',
            width: 2,
            length: 12,
            radius: 15,
            className: 'loading-spinner'
        },
        activeClass = 'active';

    var LoadMask = SuperClass.extend({
        className: 'loading-mask',

        render: function() {
            this.$el.html(html);

            return this;
        },

        show: function() {
            this.$el.addClass(activeClass);

            if (!this.spinner) {
                this.spinner = new Spinner(loadMaskOptions);
                this.spinner.spin(this.$('.loading-mask-spinner-container')[0]);
            }
        },

        hide: function() {
            this.$el.removeClass(activeClass);
        }
    });

    var loadMask = new LoadMask();
    loadMask.render().$el.appendTo(document.body);

    Marketplace.showLoadMask = _.bind(loadMask.show, loadMask);
    Marketplace.hideLoadMask = _.bind(loadMask.hide, loadMask);

    return loadMask;
});
