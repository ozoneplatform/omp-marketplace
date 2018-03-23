/*global describe, beforeEach, afterEach, it, expect, sinon */
define([
    'profile/Header',
    'backbone'
], function (Header, Backbone) {
    'use strict';

    describe('Profile Window Header', function() {
        var view,
            myId = 10,
            attrs = {
                displayName: 'testUser',
                id: myId
            },
            model,
            oldMarketplace;

        beforeEach(function() {
            model = new Backbone.Model(attrs);
            view = new Header({ model: model });

            oldMarketplace = window.Marketplace;
            window.Marketplace = {
                mainRouter: { closeModal: undefined },
                user: { id: undefined }
            };
        });

        afterEach(function() {
            window.Marketplace = oldMarketplace;
        });

        it('has a header and a close button', function() {
            view.render();

            expect(view.$('h3')[0]).to.be.ok();
            expect(view.$('h3 > .header-text')[0]).to.be.ok();
            expect(view.$('h3 > .close')[0]).to.be.ok();
        });

        it('displays the display name for other users', function() {
            window.Marketplace.user.id =  myId + 1;

            view.render();
            expect(view.$('.header-text').text()).to.be(attrs.displayName);
        });

        it('displays "My Account" for your own profile', function() {
            window.Marketplace.user.id = myId;

            view.render();
            expect(view.$('.header-text').text()).to.be('My Account');
        });

        it('calls closeModal on the router when its close button is clicked', function() {
            var spy = sinon.spy();
            window.Marketplace.user.id = myId;
            window.Marketplace.mainRouter.closeModal = spy;

            view.render().$el.appendTo(document.body);
            view.$('.close').click();

            expect(spy.calledOnce).to.be.ok();

            view.remove();
        });
    });
});
