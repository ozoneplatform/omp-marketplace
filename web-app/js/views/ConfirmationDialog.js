define([
    './Dialog',
    'marketplace',
    'handlebars',
    'jquery'
], function(Dialog, Marketplace, Handlebars, $) {
    'use strict';

    var SuperClass = Dialog,
        buttonsTemplate = Handlebars.compile(
            '<button class="ok btn btn-primary btn-small">OK</button>' +
            '<button class="close-dialog btn btn-small">Cancel</button>');

    var ConfirmationDialog = Dialog.extend({
        className: 'confirmation-dialog ' + SuperClass.prototype.className,

        events: {
            'click .close-dialog': 'cancel',
            'click .ok': 'ok'
        },

        deferred: null,

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);
            this.deferred = $.Deferred();
        },

        //in order to perform functionality when this confirmation is dismissed, calling
        //code should use this function to get a promise object and attach callbacks to it
        promise: function() {
            return this.deferred.promise();
        },

        render: function() {
            SuperClass.prototype.render.apply(this, arguments);

            this.$('.dialog-footer').html(buttonsTemplate());

            return this;
        },

        ok: function() {
            this.remove();
            this.deferred.resolve();
        },

        cancel: function() {
            this.remove();
            this.deferred.reject();
        }
    });

    Marketplace.showConfirmationDialog = ConfirmationDialog.show = function(title, text) {
        var dialog = new ConfirmationDialog({title: title, text: text});
        dialog.show();

        return dialog;
    };

    return ConfirmationDialog;
});
