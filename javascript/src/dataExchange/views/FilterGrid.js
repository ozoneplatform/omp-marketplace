define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/BaseView',
    './FilterGroupView'
], function($, _, Backbone, Handlebars, BaseView, FilterGroupView) {

    return BaseView.extend({

        tagName: 'div',
        className: 'filter-grid',

        numCols: 2,

        config: [{
            description: "Available",
            options: [{
                name: "isInside",
                displayText: "Inside"
            }, {
                name: "isOutside",
                displayText: "Outside"
            }]
        }],

        template:   '<div class="row-fluid">' + 
                        '<a href="#" class="filter-toggle-btn" id="select-all-btn">Select All</a>' + 
                        '<a href="#" class="filter-toggle-btn" id="deselect-all-btn">Deselect All</a>' + 
                    '</div>' +
                    '<div class="row-fluid" id="filter-group-cols">' +
                        '<div class="span6" id="filter-group-col-0"></div>' +
                        '<div class="span6" id="filter-group-col-1"></div>' +
                    '</div>' +
                    '<div class="row-fluid">' + 
                        '<div class="pull-right">' + 
                            '<button class="btn btn-primary" type="button" id="apply-filters-btn">Filter</button>' + 
                            '<button class="btn" type="button" id="cancel-filters-btn">Cancel</button>' + 
                        '</div>' + 
                    '</div>',

        initialize: function() {
            // Add agency options
            if (this.options.agencies) {
                if (!_.find(this.config, function(val) {return val.description === "Company"})) {
                    this.config.push(
                        {
                            description: "Company",
                            options: _.map(this.options.agencies, function(agency) {return {name: agency, displayText: agency} })
                        }
                    );
                }
            }
        },

        render: function() {
            this.$el.html(this.template);

            var colIndex = 0;

            _(this.config).each(function(val, key, coll) {
                var view = new FilterGroupView({
                    config: val
                });

                this.$el.find('#filter-group-col-' + (colIndex % this.numCols)).append(view.render().el);

                colIndex++;
            }, this);

            return this;
        }
    });
});