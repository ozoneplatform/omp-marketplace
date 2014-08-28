/*global describe, beforeEach, it, expect */
define([
    'timeout/TimeoutWarningDialog',
    'views/ConfirmationDialog',
    'sinon'
], function(TimeoutWarningDialog, ConfirmationDialog, sinon) {
    'use strict';

    describe('TimeoutWarningDialog', function() {
        var view,
            countdownStart = 3;

        beforeEach(function() {
            view = new TimeoutWarningDialog({ countdownStart: countdownStart });
        });

        it('is a ConfirmationDialog', function() {
            expect(view).to.be.a(ConfirmationDialog);
        });

        it('has yes and no buttons', function() {
            view.render();

            expect(view.$('.ok').text()).to.be('Yes');
            expect(view.$('button.close-dialog').text()).to.be('No');
        });

        it('rejects its promise and removes itself if the countdown reaches zero',
                function(done) {
            sinon.stub(view, 'remove');

            view.render().promise().fail(function() {
                expect(view.secondsLeft).to.be(0);
                expect(view.remove.calledOnce).to.be.ok();

                view.remove.restore();
                done();
            });
        });

        it('displays a message including a countdown which updates each second', function() {
            var clock = sinon.useFakeTimers();

            view.render();

            expect(view.$('.countdown').text()).to.be('3');

            clock.tick(1001);
            expect(view.$('.countdown').text()).to.be('2');

            clock.tick(1001);
            expect(view.$('.countdown').text()).to.be('1');

            clock.restore();
        });
    });
});
