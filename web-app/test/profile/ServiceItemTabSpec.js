/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/ServiceItemTab',
    'profile/MyListingsView',
    'profile/MyChangelogView',
    'models/Profile',
    'underscore'
], function(ServiceItemTab, MyListingsView, MyChangelogView, ProfileModel, _) {
    'use strict';

    describe('ServiceItemTab', function() {
        var view,
            profileAttrs = {
                id: 1
            },
            model,
            isAdmin = true,
            isSelf = true,
            methodsToStub = ['initialize', 'render', 'remove'];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each([MyListingsView, MyChangelogView], function(View) {
                    sinon.stub(View.prototype, method, function() { return this; });
                });
            });

            model = new ProfileModel(profileAttrs);
            model.isSelf = sinon.spy(function() {
                return isSelf;
            });
            model.isAdmin = sinon.spy(function() {
                return isAdmin;
            });

            view = new ServiceItemTab({ model: model });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each([MyListingsView, MyChangelogView], function(View) {
                    View.prototype[method].restore();
                });
            });
        });

        it('has the title "Listings"', function() {
            var expectedTitle = 'Listings';

            expect(view.title).to.be(expectedTitle);
        });

        it('has the css class service-item-tab', function() {
            expect(view.$el.hasClass('service-item-tab')).to.be.ok();
        });

        it('has a MyListingsView and a MyChangelogView', function() {
            expect(view.myListingsView).to.be.a(MyListingsView);
            expect(view.recentActivityView).to.be.a(MyChangelogView);

            expect(view.myListingsView.initialize.calledWith({
                model: view.model
            })).to.be.ok();
            expect(view.recentActivityView.initialize.calledWith({
                model: view.model
            })).to.be.ok();
        });

        it('renders it children views into itself', function() {
            view.render();

            expect(view.myListingsView.$el.parent()[0]).to.be(view.el);
            expect(view.recentActivityView.$el.parent()[0]).to.be(view.el);
        });

        it('removes its children on remove', function() {
            view.render();
            view.remove();

            expect(view.myListingsView.remove.calledOnce).to.be.ok();
            expect(view.recentActivityView.remove.calledOnce).to.be.ok();
        });

        it('does not have the Changelog when the user is neither an admin nor self', function(){
            var spy = MyChangelogView.prototype.initialize;

            spy.reset();
            isSelf = false;

            view = new ServiceItemTab({model: model});

            //still admin, changelog should be created
            expect(spy.calledOnce).to.be.ok();


            spy.reset();
            isAdmin = false;
            view = new ServiceItemTab({model: model});

            //neither self nor admin; should not be created
            expect(spy.called).to.be(false);

            spy.reset();
            isSelf = true;
            view = new ServiceItemTab({model: model});

            //non-admin viewing own profile, changelog should be created
            expect(spy.calledOnce).to.be.ok();
        });
    });
});
