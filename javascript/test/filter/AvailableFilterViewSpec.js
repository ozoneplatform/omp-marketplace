/*global describe, beforeEach, afterEach, it, expect */
define([
    'views/filter/AvailableFilterView',
    'views/filter/FilterView',
    'backbone',
    'marketplace',
    'underscore',
    'jquery',
    'sinon'
], function(AvailableFilterView, FilterView, Backbone, Marketplace, _, $, sinon) {
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
            baseUrl = '/test-server/search/',
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

            view = new AvailableFilterView({
                model: new Backbone.Model(attrs),
                filterKind: filterKind,
                filterRemoveField: filterRemoveField
            });

            sinon.stub(view, 'getFilterBaseUrl', function() {
                return baseUrl;
            });
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            view.getFilterBaseUrl.restore();

            view.remove();
        });

        it('is a subclass of FilterView', function() {
            expect(view).to.be.a(FilterView);
        });

        it('has an a tag with the correct href', function() {
            view.render();

            expect(view.$('a').attr('href')).to.be('/test-server/search/Type/2');
        });

        it('has a filter-value element with the title', function() {
            view.render();

            expect(view.$('.filter-value').text()).to.be(attrs.title);
        });

        it('has a filter-count element with the count in parens', function() {
            view.render();

            expect(view.$('.filter-count').text()).to.be('(68)');
        });
    });
});
