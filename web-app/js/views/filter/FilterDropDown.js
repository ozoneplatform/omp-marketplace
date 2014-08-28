define([
    'views/BaseView',
    './AvailableFilterView',
    './SelectedFilterView',
    'handlebars',
    'backbone',
    'underscore',
    'jquery'
], function(BaseView, AvailableFilterView, SelectedFilterView, Handlebars, Backbone, _, $) {
    'use strict';

    var SuperClass = BaseView,
        template = Handlebars.compile(
            '<h4>{{title}}<span class="arrow"></span></h4>' +
            '<div class="hover-container">' +
                '<ol class="available-filters"></ol>' +
            '</div>' +
            '<ol class="selected-filters"></ol>'
        ),
        ieFixHtml = '<div class="ie-fix"></div>';

    return SuperClass.extend({
        className: 'filter-drop-down',

        title: null,

        availableFilterViews: [],
        selectedFilterViews: [],

        initialize: function(options) {
            var filters = options.facets;

            var groupedFilters = _.groupBy(filters, function(filter) {
                return filter.selected ? 'selected' : 'available';
            });

            //populated availableFilters and selectedFilters instance variables
            this.doOnAvailableAndSelected(function(kind) {
                this[kind + 'FilterViews'] = _.map(groupedFilters[kind], function(filter) {
                    var Class = kind === 'available' ? AvailableFilterView : SelectedFilterView;

                    return new Class({
                        model: new Backbone.Model(filter),
                        filterKind: options.filterKind,
                        filterRemoveField: options.filterRemoveField
                    });
                });
            });

            this.title = options.title;

            return SuperClass.prototype.initialize.apply(this, arguments);
        },

        render: function() {
            var contents = $(template(_.pick(this, 'title')));

            contents.filter('.selected-filters').append(
                _.map(this.selectedFilterViews, function(filter) {
                    return filter.render().$el;
                })
            );

            contents.children('.available-filters').append(
                _.map(this.availableFilterViews, function(filter) {
                    return filter.render().$el;
                })
            );

            this.$el.append(contents);


            this.fixIE();

            return this;
        },

        remove: function() {
            this.doOnAvailableAndSelected(function(kind) {
                _.each(this[kind + 'FilterViews'], function(view) {
                    view.remove();
                });
            });

            return SuperClass.prototype.remove.apply(this, arguments);
        },

        /**
         * Utility function to run the same processing on both the available and selected
         * filters
         */
        doOnAvailableAndSelected: function(fn) {
            var me = this;
            _.each(['available', 'selected'], function(kind) {
                fn.call(me, kind);
            });
        },

        /**
         * hack to fix z-index :hover issue in IE7 and 8.  In IE9 this issue
         * is fixed in pure css by applying a background color of rgba(0,0,0,0)
         */
        fixIE: function() {
            var htmlEl = $('html');

            if (htmlEl.hasClass('ie7') || htmlEl.hasClass('ie8')) {
                this.$('.hover-container').append(ieFixHtml);
            }
        }
    });
});
