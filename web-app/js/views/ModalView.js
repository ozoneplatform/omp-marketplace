define([
    './BaseView',
    '../events/EventBus',
    'jquery',
    'underscore',
    'handlebars',
    'marketplace',
    'bootstrap'
],

function(View, EventBus, $, _, Handlebars, Marketplace) {
    'use strict';

    var killEvent = function (e) { return false; },
        template = Handlebars.compile(
            '{{#if title}}' +
                '<div class="modal-header">' +
                    '{{#if closable}}' +
                        '<button type="button" class="close" data-dismiss="modal" ' +
                                'aria-hidden="true" tabindex="-1">' +
                            '&times;' +
                        '</button>' +
                    '{{/if}}' +
                    '<h4 class="modal-title">{{title}}</h4>' +
                '</div>' +
            '{{/if}}' +

            '<div class="modal-body store-modal-body">{{content}}</div>' +

            '{{#if footer}}' +
                '<div class="modal-footer">' +
                    '<button class="btn {{okClassName}} ok">{{okText}}</button>' +
                    '{{#if cancelText}}' +
                        '<button data-dismiss="modal" class="btn {{cancelClassName}} cancel">' +
                            '{{cancelText}}' +
                        '</button>' +
                    '{{/if}}' +
                '</div>' +
            '{{/if}}'
        ),

        //template for the back button that can appear to allow navigation to previous modals
        backButtonTemplate = Handlebars.compile(
            '<a class="modal-back">' +
                '<span class="title">' +
                    '{{#if backTitle}}' +
                        'Back to {{backTitle}}' +
                    '{{else}}' +
                        'Back' +
                    '{{/if}}' +
                '</span>' +
            '</a>'
        );

    /**
     * Bootstrap Modal wrapper for use with Backbone
     *
     * Events:
     * shown: Fired when the modal has finished animating in
     * hidden: Fired when the modal has finished animating out
     */
    var ModalView = View.extend(_.extend({}, {

        className: 'bootstrap-active modal hide store-modal',

        // let scripts enforce focus on the modal element so it may receive
        // key events i.e. 'esc' to close
        attributes: {
            'tabindex' : '-1'
        },

        events:  {
            'click .cancel': 'cancel',
            'click .ok': 'ok',
            'click .modal-back': 'goToPreviousModal'
        },

        template: template,

        defaults: {
            useShims: false,
            title: false,
            closable: true,
            escape: true,
            backdrop: true,
            removeOnClose: true,
            footer: true,
            okText: 'OK',
            okClassName: 'btn-primary',
            cancelText: 'Cancel',
            cancelClassName: '',
            animate: true,
            onShow: $.noop,
            onShown: $.noop,
            onHide: $.noop,
            onHidden: $.noop,
            ok: killEvent,
            cancel: killEvent
        },

        /**
         * Creates an instance of a Bootstrap Modal
         *
         * @see http://twitter.github.com/bootstrap/javascript.html#modals
         *
         * @param {Object} options
         * @param {String} [options.title]              Title. Default: none
         * @param {String|Method} [options.content]     Modal content. Default: none
         * @param {String|Method} [options.closable]    'X' button in the header. Default: true

         * @param {Boolean} [options.escape]            Closes the modal when escape key is pressed. Default: true
         * @param {String} [options.backdrop]           Includes a modal-backdrop element. Alternatively, specify static for a backdrop which doesn't close the modal on click. Default: true
         * @param {String} [options.removeOnClose]      Remove view from DOM on close. Default: false

         * @param {String} [options.footer]             Whether to show footer. Default: true
         * @param {String} [options.okText]             Text for the OK button. Default: 'OK'
         * @param {String} [options.cancelText]         Text for the cancel button. Default: 'Cancel'. If passed a falsey value, the button will not be shown.

         * @param {Boolean} [options.animate]           Animates modal. Default: true
         * @param {Boolean} [options.onShow]            Callback to execute when show method is called
         * @param {Boolean} [options.onShown]           Callback to execute when modal is shown
         * @param {Boolean} [options.onHide]            Callback to execute when hide method is called
         * @param {Boolean} [options.onHidden]          Callback to execute when modal is hidden
         * @param {Boolean} [options.ok]                Callback to execute when ok is pressed
         * @param {Boolean} [options.cancel]            Callback to execute when cancel is pressed
         * @param {Boolean} [options.backTitle]         Title to display on the back tab.
         *                                              If undefined, the back tab will not be
         *                                              displayed.  If null, it will be displayed
         *                                              but without a title, If a string, the
         *                                              back tab will include that string
         */
        initialize: function () {
            View.prototype.initialize.apply(this, arguments);

            var me = this,
                options = _.extend({}, ModalView.prototype.defaults, this.options);

            // fill in falsy values in `this` with options
            _.defaults(this, options);

            this.animate && this.$el.addClass('fade');

            this.$el
                .on('show.bs.modal', function(e) {
                    e.namespace === 'bs.modal' && me.onShow.apply(me, arguments);
                })
                .on('shown.bs.modal', function(e) {
                    e.namespace === 'bs.modal' && me.onShown.apply(me, arguments);
                })
                .on('hide.bs.modal', function(e) {
                    e.namespace === 'bs.modal' && me.onHide.apply(me, arguments);
                })
                .on('hidden.bs.modal', function(e) {
                    if(e.namespace === 'bs.modal') {
                        me.onHidden.apply(me, arguments);
                        me.removeOnClose && me.isRendered() && me.remove();
                    }
                });
        },

        render: function () {
            this.$el.html( this.template(this) );
            this.maybeRenderBackButton();
            this.$body = this.$el.children( '.modal-body' );

            return this;
        },

        maybeRenderBackButton: function() {
            if (this.backTitle !== undefined) {
                this.$el.append(backButtonTemplate({ backTitle: this.backTitle }));
            }
        },

        isVisible: function() {
            return this.$el.is(':visible');
        },

        toggleVisible: function() {
            if(this.isVisible()) {
                return this.hide();
            } else {
                return this.show();
            }
        },

        center: function () {
            this.$el.css('margin-left', (this.$el.outerWidth() / 2) * -1);
        },

        /*
         * Show window. If render is not called before, view is added to body.
         */
        show: function (options) {
            var me = this,
                dfd = $.Deferred(),
                $modal = $('.modal:visible');

            // Check if another modal is active
            if ($modal.length && $modal[0].id !== this.id) {
                // Cannot show modal because another is active. Reject deferred and return
                return dfd.reject().promise();
            }

            if(!this.isRendered()) {
                $(document.body).append(this.render().el);
            }

            this.$el.one('shown.bs.modal', function () {
                me._shim();
                dfd.resolve();
            }).modal({
                show: true,
                keyboard: this.escape,
                backdrop: this.backdrop
            });

            return dfd.promise();
        },

        /*
         * Hide window.
         */
        hide: function () {
            var dfd = $.Deferred();

            if(this.$el.is(':visible')) {
                this.$el.one('hidden.bs.modal', function () {
                    dfd.resolve();
                }).modal('hide');
            }
            else {
                return dfd.resolve().promise();
            }

            return dfd.promise();
        },

        remove: function (animate) {
            this.hide();

            // remove hidden and show events
            this.$el.off('.bs.modal');
            //this.tearDownCircularFocus();
            View.prototype.remove.call(this);
        },

        _shim: function () {
            // if(this.useShims) {
            //     this.$el.bgiframe();
            // }
        },

        onShown: function() {
            $(document.body).children('.modal-backdrop').appendTo(this.$el.parent());
        },

        //subclasses should override this method to allow the MainRouter to know the title
        //of the modal
        getTitle: function() {
            return null;
        },

        goToPreviousModal: function() {
            Marketplace.mainRouter.toPreviousModal();
        },

        closeModal: function() {
            Marketplace.mainRouter.closeModal();
        }
    }));

    return ModalView;
});
