/*global describe, beforeEach, afterEach, it, expect */
define([
    'profile/PreferencesTab',
    'profile/ThemeSwitcher',
    'backbone',
    'sinon',
    'underscore'
], function(PreferencesTab, ThemeSwitcher, Backbone, sinon, _) {
    'use strict';

    describe('ProfileTab', function() {
        var view,
            myId = 10,
            attrs = {
                displayName: 'Test User',
                username: 'testUser',
                email: 'testUser@nowhere.com',
                bio: '',
                createdDate: '2014-02-25T17:05:13Z',
                id: myId,
                animationsEnabled: false
            },
            model,
            Marketplace,
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [ThemeSwitcher];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    sinon.stub(Tab.prototype, method, function() {
                        return this;
                    });
                });
            });

            Marketplace = {
                mainRouter: { closeModal: undefined },
                user: { id: myId },
                context: window.Marketplace.context
            };

            model = new Backbone.Model(attrs);
            model.isSelf = sinon.spy(function() {
                return Marketplace.user.id === myId;
            });
            model.isAdmin = sinon.spy(function() {
                return Marketplace.user.isAdmin;
            });

            view = new PreferencesTab({ model: model });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            view.remove();
        });

        it('has a title of "Preferences"', function() {
            expect(view.title).to.be('Preferences');

            view.render();
            expect(view.$('.preferences h4').text()).to.be('Preferences');
        });

        it('has preferences for yourself', function() {
            Marketplace.user.id = myId;

            view.render();

            expect(view.$('.preferences')[0]).to.be.ok();
        });

        it('activates the svitch plugin on the preferences checkbox', function() {
            Marketplace.user.id = myId;

            view.render();

            expect(view.$('.preferences input[type=checkbox]').data('svitch')).to.be.ok();
        });

        it('saves the animations preference when the checkbox value is changed', function() {
            var xhr,
                xhrRequests = [];

            Marketplace.user.id = myId;
            xhr = sinon.useFakeXMLHttpRequest();
            xhr.onCreate = function(request) {
                xhrRequests.push(request);
            };

            view.render().$el.appendTo(document.body);

            view.$('.preferences .animations').click();

            expect(xhrRequests.length).to.be(1);
            expect(xhrRequests[0].url).to.be(Marketplace.context +
                '/preferences/enableAnimations');

            expect(xhrRequests[0].requestBody).to.be('enableAnimations=true');

            xhr.restore();
        });

        it('removes any popovers on remove', function() {
            view.render();
            view.preferencesErrorPopover();

            var animationsPopoverSpy =
                        sinon.spy(view.$('.animations-preference').data('bs.popover'), 'destroy');

            view.remove();

            expect(animationsPopoverSpy.calledOnce).to.be.ok();
        });

        it('displays an error popover if the animations preference cannot be saved',
                function() {
            var xhr, request;

            Marketplace.user.id = myId;
            xhr = sinon.useFakeXMLHttpRequest();
            xhr.onCreate = function(req) {
                request = req;
            };

            view.render().$el.appendTo(document.body);
            view.$('.preferences .animations').click();

            request.respond(500, { "Content-Type": 'application/json' },
                '{ "error": true, "message": "Error!" }');

            expect(view.$('.animations-preference + .popover')[0]).to.be.ok();

            xhr.restore();
        });

        it('closes the animations popover when the ok button on it is clicked', function(done) {
            view.render().$el.appendTo(document.body);

            view.preferencesErrorPopover();

            expect(view.$('.preferences .popover')).to.be.ok();
            view.$('.preferences .popover button').click();

            //popover removal is apparently asynchronous
            setTimeout(function() {
                expect(view.$('.preferences .popover')[0]).to.not.be.ok();

                view.remove();
                done();
            }, 500);
        });

        it('has a themes switcher', function() {
            view.render();

            expect(view.themeSwitcher).to.be.a(ThemeSwitcher);
            expect(view.themeSwitcher.$el.parent()[0]).to.be(view.el);

            expect(view.themeSwitcher.render.calledOnce).to.be.ok();

            view.remove();
            expect(view.themeSwitcher.remove.calledOnce).to.be.ok();
        });
    });
});
