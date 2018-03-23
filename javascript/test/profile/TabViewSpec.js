/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/TabView',
    'profile/ProfileTab',
    'profile/PreferencesTab',
    'profile/ServiceItemTab',
    'profile/ItemCommentTab',
    'models/Profile',
    'underscore'
], function(TabView, ProfileTab, PreferencesTab, ServiceItemTab, ItemCommentTab, ProfileModel, _) {
    'use strict';

    describe('Profile TabView', function() {
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
            isSelf = false,
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [ProfileTab, PreferencesTab,  ServiceItemTab, ItemCommentTab];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    sinon.stub(Tab.prototype, method);
                });
            });

            model = new ProfileModel(attrs);
            model.url = '/asdf';
            sinon.stub(model, 'isSelf', function() {
                return isSelf;
            });

            view = new TabView({ model: model });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            model.isSelf.restore();
        });

        it('creates its tabPanes object', function() {
            expect(view.tabPanes).to.be.an(Object);
            expect(view.tabPanes.profile).to.be.a(ProfileTab);
            expect(view.tabPanes.myListings).to.be.a(ServiceItemTab);
            expect(view.tabPanes.reviews).to.be.a(ItemCommentTab);
        });

        it('passes its model to the panes', function() {
            expect(view.tabPanes.profile.initialize.calledWith({
                model: view.model
            })).to.be.ok();
            expect(view.tabPanes.myListings.initialize.calledWith({
                model: view.model
            })).to.be.ok();
            expect(view.tabPanes.reviews.initialize.calledWith({
                model: view.model
            })).to.be.ok();
        });

        it('removes the panes on remove', function() {
            view.remove();

            _.each(view.tabPanes, function(pane) {
                expect(pane.remove.calledOnce).to.be.ok();
            });
        });

        it('only creates the preferences pane is the user is self', function() {
            var preferencesTab;

            //currently not self, no preferences tab
            expect(view.tabPanes.preferences).to.be(undefined);

            isSelf = true;
            view.remove();
            view = new TabView({ model: model });

            preferencesTab = view.tabPanes.preferences;
            expect(preferencesTab).to.be.a(PreferencesTab);

            view.remove();

            expect(preferencesTab.remove.calledOnce).to.be.ok();
        });
    });
});
