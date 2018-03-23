/*global describe, it, beforeEach, afterEach, expect */
define([
    'timeout/TimeoutWatcher',
    'timeout/TimeoutWarningDialog',
    'marketplace',
    'sinon',
    'underscore',
    'jquery'
], function(TimeoutWatcher, TimeoutWarningDialog, Marketplace, sinon, _, $) {
    'use strict';

    describe('TimeoutWatcher', function() {
        var watcher;

        beforeEach(function() {
            _.extend(Marketplace, {
                image: { s: '/image' }
            });
        });

        afterEach(function() {
            watcher = null;

            $(document.body).empty();
        });

        it('creates a TimeoutWarningDialog when there are 120 seconds left', function(done) {
            sinon.stub(TimeoutWarningDialog.prototype, 'initialize', function(options) {
                this.deferred = $.Deferred();

                expect(options.countdownStart).to.be(120);

                TimeoutWarningDialog.prototype.initialize.restore();
                done();
            });

            //timeoutSeconds one second longer than the countdown
            watcher = new TimeoutWatcher(121, null, null);
        });

        it('creates a TimeoutWarningDialog immediately when the timeout is less than 120 ' +
                'seconds', function(done) {
            var timeout = 50;

            sinon.stub(TimeoutWarningDialog.prototype, 'initialize', function(options) {
                this.deferred = $.Deferred();

                expect(options.countdownStart).to.be(timeout);

                TimeoutWarningDialog.prototype.initialize.restore();
                done();
            });

            watcher = new TimeoutWatcher(timeout, null, null);
        });

        it("refreshes the session if the dialog's promise is rejected", function(done) {
            var deferred = $.Deferred();

            sinon.stub(TimeoutWarningDialog.prototype, 'promise', function() {
                return deferred.promise();
            });

            sinon.stub(TimeoutWarningDialog.prototype, 'initialize', function() {
                deferred.resolve();
            });

            sinon.stub($, 'get', function() {
                //wait until now to stub startTimeout so we don't catch
                //the initial call to it from the constructor
                sinon.stub(watcher, 'startTimeout', function() {
                    expect($.get.calledOnce).to.be.ok();
                    expect($.get.calledWith('/image', { cache: false })).to.be.ok();

                    //should be called due to the promise from $.get being resolved
                    expect(watcher.startTimeout.calledOnce).to.be.ok();
                    expect(watcher.logout.called).to.not.be.ok();

                    TimeoutWarningDialog.prototype.promise.restore();
                    TimeoutWarningDialog.prototype.initialize.restore();
                    $.get.restore();
                    watcher.startTimeout.restore();
                    TimeoutWatcher.prototype.logout.restore();

                    done();
                });

                return $.Deferred().resolve().promise();
            });

            sinon.stub(TimeoutWatcher.prototype, 'logout');

            watcher = new TimeoutWatcher(121, null, null);
        });

        it("logs out if the dialog's promise is rejected", function(done) {
            var deferred = $.Deferred(),
                redirectUrl = '/logout',
                errorMessage = 'Error!!!';

            sinon.stub(TimeoutWarningDialog.prototype, 'promise', function() {
                return deferred.promise();
            });

            sinon.stub(TimeoutWarningDialog.prototype, 'initialize', function() {
                deferred.reject();
            });

            sinon.stub(Marketplace, 'handleLogout', function(obj) {
                expect(obj.redirectUrl).to.be(redirectUrl);
                expect(obj.settingSessionDataErrorMsg).to.be(errorMessage);

                expect(watcher.refreshSession.called).to.not.be.ok();

                TimeoutWarningDialog.prototype.promise.restore();
                TimeoutWarningDialog.prototype.initialize.restore();
                Marketplace.handleLogout.restore();
                TimeoutWatcher.prototype.refreshSession.restore();

                done();
            });

            sinon.stub(TimeoutWatcher.prototype, 'refreshSession');

            watcher = new TimeoutWatcher(121, errorMessage, redirectUrl);
        });
    });
});
