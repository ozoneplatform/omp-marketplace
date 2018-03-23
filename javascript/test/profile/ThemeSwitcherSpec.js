/*global describe, beforeEach, afterEach, it, expect */
define([
    'profile/ThemeSwitcher',
    'profile/ThemeView',
    'views/ListView',
    'collections/ThemeCollection',
    'backbone',
    'underscore',
    'sinon'
], function(ThemeSwitcher, ThemeView, ListView, ThemeCollection, Backbone, _, sinon) {
    'use strict';

    describe('ThemeSwitcher', function() {
        var view,
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [ThemeView];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    sinon.stub(Tab.prototype, method);
                });
            });

            sinon.stub(ThemeCollection.prototype, 'fetch');
            view = new ThemeSwitcher();
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            ThemeCollection.prototype.fetch.restore();

            view.remove();
        });

        it('contains a "Theme" header', function() {
            view.render();

            expect(view.$('h4').text()).to.be('Theme');
        });

        it('uses a ThemeCollection as its collection', function() {
            expect(view.collection).to.be.a(ThemeCollection);
            expect(view.collection.fetch.calledOnce).to.be.ok();
        });

        it('is a subclass of ListView', function() {
            expect(view).to.be.a(ListView);
        });

        it('has a ul element for its $body', function() {
            view.render();

            expect(view.$body.prop('tagName')).to.be('UL');
        });

        it('uses ThemeView as its ItemView', function() {
            expect(view.ItemView).to.be(ThemeView);
        });

        it('has a css class of "themes"', function() {
            view.render();

            expect(view.$el.hasClass('themes')).to.be.ok();
        });
    });
});
