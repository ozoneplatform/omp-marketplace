/*global describe, beforeEach, afterEach, it, expect */
define([
    'views/filter/FilterDropDown',
    'views/filter/AvailableFilterView',
    'views/filter/SelectedFilterView',
    'underscore',
    'jquery',
    'sinon'
], function(FilterDropDown, AvailableFilterView, SelectedFilterView, _, $, sinon) {
    'use strict';

    describe('FilterDropDown', function() {
        var view,
            filterConfig = {
                "facets": [{
                    "count": 68,
                    "description": "app component",
                    "id": 2,
                    "title": "App Component"
                }, {
                    "count": 4,
                    "description": "desktop apps",
                    "id": 4,
                    "selected": true,
                    "title": "Desktop Apps"
                }, {
                    "count": 15,
                    "description": "OZONE app",
                    "id": 3,
                    "selected": true,
                    "title": "OZONE App"
                }, {
                    "count": 3,
                    "description": "plugins",
                    "id": 7,
                    "title": "Plugin"
                }, {
                    "count": 8,
                    "description": "web app",
                    "id": 1,
                    "title": "Web App"
                }],
                "filterKind": "filterType",
                "filterRemoveField": "typeFilters",
                "id": "Type",
                "title": "Type"
            },
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [AvailableFilterView, SelectedFilterView];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    sinon.stub(Tab.prototype, method, function() {
                        return this;
                    });
                });
            });

            view = new FilterDropDown(filterConfig);
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            view.remove();
        });

        it('sets its Title from the constructor options', function() {
            expect(view.title).to.be(filterConfig.title);
            view.render();

            expect(view.$('h4').text()).to.be(filterConfig.title);
        });

        it('creates AvailableFilterViews for non-selected filters', function() {
            var nonSelectedFilterIds = [1,2,7],
                modelIds;

            expect(view.availableFilterViews.length).to.be(3);

            expect(AvailableFilterView.prototype.initialize.calledThrice).to.be.ok();
            expect(AvailableFilterView.prototype.initialize.alwaysCalledWithMatch({
                filterKind: filterConfig.filterKind,
                filterRemoveField: filterConfig.filterRemoveField
            })).to.be.ok();

            modelIds = _.map(AvailableFilterView.prototype.initialize.args, function(argArray) {
                return argArray[0].model.id;
            });

            _.each(modelIds, function(id) {
                expect(nonSelectedFilterIds).to.contain(id);
            });
        });

        it('creates SelectedFilterViews for non-selected filters', function() {
            var selectedFilterIds = [3,4],
                modelIds;

            expect(view.selectedFilterViews.length).to.be(2);

            expect(SelectedFilterView.prototype.initialize.calledTwice).to.be.ok();
            expect(SelectedFilterView.prototype.initialize.alwaysCalledWithMatch({
                filterKind: filterConfig.filterKind,
                filterRemoveField: filterConfig.filterRemoveField
            })).to.be.ok();

            modelIds = _.map(SelectedFilterView.prototype.initialize.args, function(argArray) {
                return argArray[0].model.id;
            });

            _.each(modelIds, function(id) {
                expect(selectedFilterIds).to.contain(id);
            });
        });

        it('appends available filter views to the available-filters element', function() {
            view.render();

            _.each(view.availableFilterViews, function(v) {
                expect(v.$el.parent()[0]).to.be(view.$('.available-filters')[0]);
            });
        });

        it('appends selected filter views to the selected-filters element', function() {
            view.render();

            _.each(view.selectedFilterViews, function(v) {
                expect(v.$el.parent()[0]).to.be(view.$('.selected-filters')[0]);
            });
        });

        it('adds an ie-fix element to the hover-container for IE7 and IE8', function() {
            var htmlEl = $('html'),
                alreadyHasClass = htmlEl.hasClass('ie7');

            htmlEl.addClass('ie7');

            view.render();

            expect(view.$('.hover-container > .ie-fix')[0]).to.be.ok();

            if (!alreadyHasClass) {
                htmlEl.removeClass('ie7');
            }
        });

        it('removes the FilterViews when it is removed', function() {
            view.remove();

            _.each(view.availableFilterViews, function(v) {
                expect(AvailableFilterView.prototype.remove.calledOn(v)).to.be.ok();
            });

            _.each(view.selectedFilterViews, function(v) {
                expect(SelectedFilterView.prototype.remove.calledOn(v)).to.be.ok();
            });
        });
    });
});
