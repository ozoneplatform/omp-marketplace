define([
    './TimeoutWarningDialog',
    'marketplace',
    'underscore',
    'jquery'
], function(TimeoutWarningDialog, Marketplace, _, $) {
    'use strict';

    var countdownSeconds = 120,

        //the constructor exported by this module
        TimeoutWatcher = function(timeoutSeconds, errorMessage, redirectUrl) {
            this.timeoutSeconds = timeoutSeconds;
            this.errorMessage = errorMessage;
            this.redirectUrl = redirectUrl;
            this.countdownSeconds = timeoutSeconds < countdownSeconds ?
                timeoutSeconds : countdownSeconds;

            this.startTimeout();
        };

    TimeoutWatcher.prototype = {
        startTimeout: function() {
            setTimeout(_.bind(this.createDialog, this),
                (this.timeoutSeconds - this.countdownSeconds) * 1000
            );
        },

        createDialog: function() {
            var dialog = new TimeoutWarningDialog({ countdownStart: this.countdownSeconds });
            dialog.show();
            dialog.promise()
                .done(_.bind(this.refreshSession, this))
                .fail(_.bind(this.logout, this));
        },

        logout: function() {
            Marketplace.handleLogout({
                redirectUrl: this.redirectUrl,
                isSessionTimeout: true,
                settingSessionDataErrorMsg: this.errorMessage
            });
        },

        refreshSession: function() {
            $.get(Marketplace.image.s, {
                cache: false
            }).always(_.bind(this.startTimeout, this));
        }
    };

    return TimeoutWatcher;
});
