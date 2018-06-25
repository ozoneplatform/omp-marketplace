define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/BaseView',
    './FilterButton',
    './FilterGrid'
], function($, _, Backbone, Handlebars, BaseView, FilterButton, FilterGrid) {

    var SuperClass = BaseView;

    return BaseView.extend({

        tagName: 'div',
        className: 'filter-view',
        filters: {},
        agencies: null,

        events: {
            'click #apply-filters-btn': 'filterClicked',
            'click a#select-all-btn': 'selectAll',
            'click a#deselect-all-btn': 'deselectAll'
        },

        render: function() {
            this.filterBtn = new FilterButton({});
            this.filterGrid = new FilterGrid({agencies: this.agencies});

            this.$el.append(this.filterBtn.render().el);


            this.filterBtn.$el.popover({
                html: true,
                placement: 'bottom',
                content: (this.filterGrid.render().$el)
            });


            return this;
        },

        hideGrid: function() {
            this.filterBtn.$el.popover('hide');
        },

        cancelFilters: function() {
            this.hideGrid();
            // Reapply last selection in case the user changed it before cancelling
            this.reapplyFilterSelection();
        },

        clearFilters: function() {
            this.filters = {};
            this.deselectAll();
        },

        reapplyFilterSelection: function() {
            var me = this;
            if (me.filters && !_.isEmpty(me.filters)) {
                // Synchronize each checkbox with the last confirmed user selection
                _.forEach(this.$el.find('input[type=checkbox]'), function(checkbox) {
                    var checked;
                    var $checkbox = $(checkbox);

                    // Find the group to which the checkbox belongs
                    var group = $checkbox.closest('dl').find('dt').text();

                    if (me.filters[group]) {
                        checked = me.filters[group][$checkbox.prop('name')]
                    }

                    $checkbox.prop("checked", checked);
                });
            } else {
                this.deselectAll();
            }

        },

        filterClicked: function(e) {
            this.filters = _.reduce(this.$el.find('input[type=checkbox]'), function(filters, filter) {
                var group = $(filter).closest('dl').find('dt').text();
                filters[group] = filters[group] || {};
                filters[group][$(filter).attr('name')] = $(filter).is(':checked');
                return filters;
            }, {});
            this.hasSelections = this.$el.find('input[type=checkbox]:checked').size() > 0;
        },

        selectAll: function(e) {
            e && e.preventDefault();
            this._toggleAll(true);
        },

        deselectAll: function(e) {
            e && e.preventDefault();
            this._toggleAll(false);
        },

        _toggleAll: function(checked) {
            this.filterGrid.$el.find('input[type=checkbox]').each( function(i, el) {
                $(el).prop('checked', checked);
            });
        },

        remove: function() {
            this.filterBtn.remove();
            this.filterGrid.remove();
            return SuperClass.prototype.remove.call(this);
        }
    });
});