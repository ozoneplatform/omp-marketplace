define([
    'views/ConfirmationDialog',
    'handlebars',
    'underscore'
], function(ConfirmationDialog, Handlebars, _) {
    'use strict';

    var SuperClass = ConfirmationDialog,
        paragraphTemplate = Handlebars.compile(
            'Your session will timeout in ' +
            '<span class="countdown"></span> seconds. ' +
            'Would you like to continue your session?'
        );

    return SuperClass.extend({
        //the number of seconds at which to start the countdown
        countdownStart: null,

        title: 'Session Timeout',

        render: function() {
            SuperClass.prototype.render.apply(this, arguments);

            this.$('p').html(paragraphTemplate());
            this.$('.ok').text('Yes');
            this.$('.dialog-footer > .close-dialog').text('No');

            this.countdownEl = this.$('.countdown');

            this.startCountdown();

            return this;
        },

        startCountdown: function() {
            this.secondsLeft = this.countdownStart;

            this.updateCountdown();
            this.interval = setInterval(_.bind(this.updateCountdown, this), 1000);
        },

        updateCountdown: function() {
            if (this.secondsLeft === 0) {
                this.cancel();
            }
            else {
                this.countdownEl.text(this.secondsLeft);
            }

            this.secondsLeft--;
        },

        remove: function() {
            clearInterval(this.interval);
            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});
