define(
[
    '../BaseView',
    'backbone',
    'jquery'
],
function(BaseView, Backbone, $) {
    'use strict';

    var SuperClass = BaseView;

    return SuperClass.extend({

        // *required
        // tab href, css selector, used for switching tabs
        href: null,

        // *required
        // tab title
        title: null,

        // boolean flag to keep track of whether tab has been activated yet or not
        _activatedOnce: false,

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);

            if(!this.title) {
                throw 'Missing option: title is required.';
            }
        },

        // executed when the tab is activated first time
        onActivateOnce: $.noop,

        // executed when the tab is activated
        onActivate: function () {
            if(!this._activatedOnce) {
                this.onActivateOnce();
                this._activatedOnce = true;
            }
        }

    });
});
