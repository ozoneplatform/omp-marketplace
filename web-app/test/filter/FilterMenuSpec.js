/*global describe, beforeEach, afterEach, it, expect */
define([
    'views/filter/FilterMenu',
    'views/filter/FilterDropDown',
    'underscore',
    'jquery',
    'sinon'
], function(FilterMenu, FilterDropDown, _, $, sinon) {
    'use strict';

    describe('FilterMenu', function() {
        var view,
            filterConfig = [{
                "facets": [{
                    "count": 68,
                    "description": "app component",
                    "id": 2,
                    "title": "App Component"
                }, {
                    "count": 4,
                    "description": "desktop apps",
                    "id": 4,
                    "title": "Desktop Apps"
                }, {
                    "count": 15,
                    "description": "OZONE app",
                    "id": 3,
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
            }, {
                "facets": [{
                    "count": 19,
                    "description": "Services, tools and solutions to analyze data",
                    "id": 8,
                    "title": "Analytics and Analysis"
                }, {
                    "count": 2,
                    "description": "Services, tools and solutions for general business needs, including HR, Travel, Accounting",
                    "id": 10,
                    "title": "Business Applications"
                }, {
                    "count": 7,
                    "description": "Services, tools and solutions for calendar and schedule-related activities",
                    "id": 11,
                    "title": "Calendar and Scheduling"
                }, {
                    "count": 3,
                    "description": "Example Category A",
                    "id": 1,
                    "title": "Category A"
                }, {
                    "count": 2,
                    "description": "Example Category B",
                    "id": 2,
                    "title": "Category B"
                }, {
                    "count": 1,
                    "description": "Example Category C",
                    "id": 3,
                    "title": "Category C"
                }],
                "filterKind": "filterCategory",
                "filterRemoveField": "categoryFilters",
                "id": "Category",
                "title": "Category"
            }],
            methodsToStub = ['initialize', 'render', 'remove'],
            classesToStub = [FilterDropDown];

        beforeEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    sinon.stub(Tab.prototype, method, function() {
                        return this;
                    });
                });
            });

            view = new FilterMenu({filterConfig: filterConfig});
        });

        afterEach(function() {
            _.each(methodsToStub, function(method) {
                _.each(classesToStub, function(Tab) {
                    Tab.prototype[method].restore();
                });
            });

            view.remove();
        });


        it('creates a FilterDropDown for each filterConfig entry', function() {
            expect(view.dropDowns.length).to.be(2);
            expect(view.dropDowns[0]).to.be.a(FilterDropDown);
            expect(view.dropDowns[0].initialize.calledWith(filterConfig[0])).to.be.ok();
            expect(view.dropDowns[1]).to.be.a(FilterDropDown);
            expect(view.dropDowns[1].initialize.calledWith(filterConfig[1])).to.be.ok();

            view.render();

            expect(FilterDropDown.prototype.render.calledTwice).to.be.ok();
            expect(FilterDropDown.prototype.render.calledOn(view.dropDowns[0])).to.be.ok();
            expect(FilterDropDown.prototype.render.calledOn(view.dropDowns[1])).to.be.ok();
        });

        it('has filter-menu and bootstrap-acitve classes', function() {
            expect(view.$el.hasClass('filter-menu')).to.be.ok();
            expect(view.$el.hasClass('bootstrap-active')).to.be.ok();
        });

        it('puts decreasing z-indexes on the drop downs in IE7', function() {
            var htmlEl = $('html'),
                alreadyHasClass = htmlEl.hasClass('ie7');

            htmlEl.addClass('ie7');

            view.render();

            var firstZIndex = parseInt(view.dropDowns[0].$el.css('z-index'), 10),
                secondZIndex = parseInt(view.dropDowns[1].$el.css('z-index'), 10);

            expect(typeof firstZIndex).to.be('number');
            expect(typeof secondZIndex).to.be('number');
            expect(firstZIndex).to.be.greaterThan(secondZIndex);

            if (!alreadyHasClass) {
                htmlEl.removeClass('ie7');
            }
        });

        it('calls remove on its drop downs when remove is called', function() {
            view.remove();

            expect(FilterDropDown.prototype.remove.calledTwice).to.be.ok();
            expect(FilterDropDown.prototype.remove.calledOn(view.dropDowns[0])).to.be.ok();
            expect(FilterDropDown.prototype.remove.calledOn(view.dropDowns[1])).to.be.ok();
        });
    });
});
