/*global describe, beforeEach, afterEach, it, expect */
define([
    'views/filter/FilterView',
    'backbone',
    'marketplace',
    'underscore',
    'jquery',
    'sinon'
], function(FilterView, Backbone, Marketplace, _, $, sinon) {
    'use strict';

    describe('FilterView', function() {
        var view,
            attrs = {
                "count": 68,
                "description": "app component",
                "id": 2,
                "title": "App Component"
            },
            filterKind = 'Type',
            filterRemoveField = 'removeType',
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    sinon.stub(Tab.prototype, method, function() {
                        return this;
                    });
                });
            });

            view = new FilterView({
                model: new Backbone.Model(attrs),
                filterKind: filterKind,
                filterRemoveField: filterRemoveField
            });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            view.remove();
        });

        it('creates an li tag', function() {
            expect(view.$el.prop('tagName')).to.be('LI');
        });

        it('adds a bootstrap tooltip with the description', function() {
            view.render().$el.appendTo(document.body);
            view.$el.tooltip('show');

            expect($('.tooltip-inner').text()).to.be(attrs.description);
        });

        it('destroys the tooltip on remove', function() {
            var spy = sinon.spy(view.$el, 'tooltip');

            view.remove();

            expect(view.$el.tooltip.calledOnce).to.be.ok();
            expect(view.$el.tooltip.calledWith('destroy')).to.be.ok();

            spy.restore();
        });

        it('returns the search URL from getFilterBaseUrl', function() {
            var oldContext = Marketplace.context,
                context = '/test-context';

            Marketplace.context = context;

            expect(view.getFilterBaseUrl()).to.be('/test-context/search/');

            Marketplace.context = oldContext;
        });
    });
});
