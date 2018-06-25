/*global describe, it, expect, sinon */
define([
    'views/LoadMask',
    'backbone',
    'spin',
    'marketplace'
], function(LoadMask, Backbone, Spinner, Marketplace) {
    'use strict';

    describe('LoadMask', function() {

        it('is a view instance', function() {
            expect(LoadMask).to.be.a(Backbone.View);
        });

        it('has a loading-mask element that is a child of the body', function() {
            expect(LoadMask.$el.parent()[0]).to.be(document.body);
            expect(LoadMask.$el.hasClass('loading-mask')).to.be.ok();
        });

        it('has show and hide methods that add and remove the "active" class', function() {
            expect(LoadMask.$el.hasClass('active')).to.not.be.ok();

            LoadMask.show();
            expect(LoadMask.$el.hasClass('active')).to.be.ok();

            //ensure duplicate calls are not a problem
            LoadMask.show();
            expect(LoadMask.$el.hasClass('active')).to.be.ok();

            LoadMask.hide();
            expect(LoadMask.$el.hasClass('active')).to.not.be.ok();

            //ensure duplicate calls are not a problem
            LoadMask.hide();
            expect(LoadMask.$el.hasClass('active')).to.not.be.ok();
        });

        it('puts showLoadMask and hideLoadMask methods on the Marketplace object', function() {
            expect(LoadMask.$el.hasClass('active')).to.not.be.ok();

            Marketplace.showLoadMask();
            expect(LoadMask.$el.hasClass('active')).to.be.ok();

            //ensure duplicate calls are not a problem
            Marketplace.showLoadMask();
            expect(LoadMask.$el.hasClass('active')).to.be.ok();

            Marketplace.hideLoadMask();
            expect(LoadMask.$el.hasClass('active')).to.not.be.ok();

            //ensure duplicate calls are not a problem
            Marketplace.hideLoadMask();
            expect(LoadMask.$el.hasClass('active')).to.not.be.ok();
        });

        it('adds a Spinner to its loading-mask-spinner-container element', function() {
            LoadMask.show();

            expect(LoadMask.$('.loading-mask-spinner-container > .loading-spinner').size()).to.be(1);
        });
    });
});
