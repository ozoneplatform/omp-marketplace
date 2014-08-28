/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/Contents',
    'profile/Header',
    'profile/TabView',
    'modal/Contents',
    'models/Profile',
    'underscore'
], function(Contents, Header, TabView, ModalContents, Profile, _) {
    'use strict';

    describe('Profile Window Contents', function() {
        var view,
            constructorArgs = { profileId: 1 },
            superclassInitStub,
            renderStub;

        beforeEach(function() {
            superclassInitStub = sinon.stub(ModalContents.prototype, 'initialize',
                function() {
                    this.bodyView = {};
                });

            renderStub = sinon.stub(Contents.prototype, 'render');

            view = new Contents(constructorArgs);
        });

        afterEach(function() {
            renderStub.restore();
            superclassInitStub.restore();
        });

        it('use Header as its header class', function() {
            expect(view.HeaderViewClass).to.be(Header);
        });

        it('use TabView as its body class', function() {
            expect(view.BodyViewClass).to.be(TabView);
        });

        it('makes its href from the profileId', function() {
            expect(view.href).to.equal('profile/' + constructorArgs.profileId);
        });

        it('sets the active tab to "profile" by default', function() {
            expect(view.bodyView.activeTab).to.be('profile');
        });

        it('sets the active tab to its tab option', function() {
            view = new Contents(_.extend({
                tab: 'myListings'
            }, constructorArgs));

            expect(view.bodyView.activeTab).to.be('myListings');
        });

        it('sets the active tab on route', function() {
            var spy = view.bodyView.setActiveTab = sinon.spy(),
                tab = 'myListings',
                args = { tab: tab};

            view.render();

            view.route(args);
            expect(spy.calledOnce).to.be.ok();
            expect(spy.calledWith(tab)).to.be.ok();
        });

        it('creates a Profile model', function() {
            expect(view.model).to.be.a(Profile);
        });
    });
});
