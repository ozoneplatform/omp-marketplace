define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
    '../../views/FilterView',
    './SelectListingsTableRow'
], function($, _, Backbone, Handlebars, BaseView, FilterView, SelectListingsTableRow) {

    var SuperClass = BaseView;

    var selectListingsTable = SuperClass.extend({

        className: "wizard-table-section",

        events: {
            'click input.listing-checkbox': 'onRowSelected',
            'click #apply-filters-btn': 'applyFilters',
            'click #cancel-filters-btn': 'cancelFilters',
            'click #clear-filters': 'clearFilters',
            'click #select-all-listings': 'onSelectAllListingsClicked',
            'click .clear-search': 'onClearSearchClick'
        },

        initialize: function() {
            this.template = Handlebars.compile($('#select-listings-table').html());
            this.listings = this.model.get('listings');
            this.exportAgencies = _.map(this.listings, function(val) {
            	if(val.agency == null){
            		return '';
            	}
                return val.agency.title ? val.agency.title.trim() : '';
            });
            this.exportAgencies = _.without(_.uniq(this.exportAgencies), '');
        },


        render: function() {

            var me = this;
            me.$el.html(me.template(me.model.toJSON()));

            // Initialize the selected listings array
            me.selectedListings = [];

            me._views = [];

            // For each listing, render a row
            _.each(me.listings, _.bind(me.setRow, me));

            // make the table of listings a dataTable
            me.dataTable = me.$el.find('#select-listings').dataTable({
                "sDom": "<'row-fluid'<'span2'i><'span3 clear-filters-container'><'span2 filter-view-container'><'span5'f>r>t<'row-fluid'<'span6'l><'span6'p>>",
                "bFilter": true,
                "bLengthChange": false,
                "bInfo": true,
                "bPaginate": false,
                "bAutoWidth": false,
                "aoColumnDefs": [{
                    "bSortable": false,
                    "aTargets": [0, 2]
                }, {
                    "bSearchable": true,
                    /* Hide IsOutside column */
                    "bVisible": false,
                    "aTargets": [7]
                }],
                "oLanguage": {
                    "sSearch": "",
                    "sInfo": "_TOTAL_ listings",
                    "sInfoFiltered": "",
                    "sInfoEmpty": "_TOTAL_ listings"
                }
            });

            /*  OP-3357 Add callback after this.dataTable is assigned
                because unselectAllListings() calls this.dataTable.fnGetNodes,
                and the callback added here is called immediately 
                as the table is being drawn */
            this.dataTable.fnSettings().aoDrawCallback.push({
                "fn": _.bind(function(tableSettings) {
                    this.unselectAllListings();
                    this.resetSelectAllListingsCheckbox()
                }, me),
                "sName": "user"
            });

            //make sure these haven't been rendered just by checking for one of the elements
            if ($('i.icon-search').size() < 1) {
                me.renderTableSearchBox();
                me.renderClearFiltersBtn();
                me.renderFilterView();
            }

            var selectionCount = $(".selection-count");

            selectionCount.text("0 listings selected");
            selectionCount.show();

            $(".next-button").prop('disabled', true);
            me.setClearFiltersVisibility(false);

            return me;
        },

        setRow: function(listing, key, list) {

            var categories = [],
                rowView;

            _.each(listing.categories, function(cat) {
                categories.push(cat.title);
            });


            rowView = new SelectListingsTableRow({

                model: new Backbone.Model({
                    listingId: listing.id,
                    listingName: listing.title,
                    listingType: listing.types.title,
                    listingState: (listing.state) ? listing.state.title : '',
                    listingCategories: categories,
                    listingAgency: (listing.agency) ? listing.agency.title : '',
                    duplicate: listing.hasDuplicate,
                    isOutside: listing.isOutside ? 'true' : 'false'
                })
            });

            rowView.render();
            this.$el.find('tbody').append(rowView.el);
            this._views.push(rowView);

        },

        shown: function() {
            $(".selection-count").show();
            this.setNextButton();

        },

        setNextButton: function() {
            if (this.selectedListings.length === 0) {
                $(".next-button").prop('disabled', true);
            } else {
                $(".next-button").prop('disabled', false);
            }
        },

        renderTableSearchBox: function() {
            this.$searchBox = this.$el.find('input[type=text]');
            this.$searchBox.attr('placeholder', "Search");
            this.$searchBox.addClass('table-search-box');
            
            this.$el.find('div.dataTables_filter label').addClass('input-prepend input-append');

            $('<span class="add-on search-icon-container"><i class="icon-search"></i></span>').insertBefore(this.$searchBox);
            $('<span class="add-on clear-search" title="Clear"><i class="icon-remove"></i></span>').insertAfter(this.$searchBox);
        },

        renderClearFiltersBtn: function() {
            this.$el.find('.clear-filters-container').html('<a href="#" id="clear-filters">clear filters</a>');
        },

        renderFilterView: function() {
            this.filterView = new FilterView({
                agencies: this.exportAgencies
            });
            this.$el.find('.filter-view-container').html(this.filterView.render().el);
        },

        applyFilters: function(e) {
            this.filterInsideOutside(this.filterView.filters);
            this.filterAgencies(this.filterView.filters);
            this.setClearFiltersVisibility(this.filterView.hasSelections);
            this.unselectAllListings();
            this.resetSelectAllListingsCheckbox();

            this.filterView.hideGrid();
        },

        cancelFilters: function(e) {
            this.filterView.cancelFilters();
        },

        setClearFiltersVisibility: function(visible) {
            if (visible)
                this.$el.find('#clear-filters').show();
            else
                this.$el.find('#clear-filters').hide();
        },

        clearFilters: function(e) {
            e.preventDefault();
            var me = this;

            _.each(_.range(me.dataTable.fnSettings().aoColumns.length), function(i) {
                me.dataTable.fnFilter('', i);
            });

            this.filterView.clearFilters();

            this.$searchBox.val('');
            this.dataTable.fnFilter('');

            this.setClearFiltersVisibility(false);
            this.resetSelectAllListingsCheckbox();
            this.unselectAllListings();
        },

        onSelectAllListingsClicked: function(e) {
            var checked = $(e.currentTarget).prop('checked');

            this._toggleAllListings(checked);
        },

        selectAllListings: function() {
            this._toggleAllListings(true);
        },

        unselectAllListings: function() {
            this._toggleAllListings(false);
        },

        _toggleAllListings: function(isSelected) {
            var me = this;

            me.selectedListings = [];

            _.forEach(
                _.filter(me.dataTable.fnGetNodes(), function(row) {
                    return $(row).is(':visible');
                }), function(row) {
                    var $checkbox = $(row).find('input[type=checkbox]');

                    $checkbox.prop('checked', isSelected);

                    if (isSelected) {
                        me.selectedListings.push(parseInt($checkbox.prop('value'), 10));
                    }
                });

            me.setNextButton();
            me.updateSelectionCount();
        },

        filterInsideOutside: function(filters) {
            var filter = filters['Available'];
            if (filter['isInside'] === filter['isOutside']) {
                // Reset filter
                this.dataTable.fnFilter('', 8);
            } else {
                // Apply filter
                this.dataTable.fnFilter(filter['isOutside'] ? 'true' : 'false', 8);
            } 
        },

        filterAgencies: function(filters) {
            var regex = _.transform(filters['Company'], function(result, isSelected, agencyName) {
                if (isSelected) result.push(agencyName);
            }, []).join('|');
            this.dataTable.fnFilter(regex, 7, true, false);
        },

        onRowSelected: function(e) {
            var listingId = e.currentTarget.value;
            if (e.currentTarget.checked === true) {
                this.selectedListings.push(parseInt(listingId, 10));
            } else {
                var index = this.selectedListings.indexOf(listingId);
                this.selectedListings.splice(index, 1);
            }

            this.setNextButton();
            this.updateSelectionCount();
            this.resetSelectAllListingsCheckbox();
        },

        remove: function() {
            if (this.filterView) this.filterView.remove();
            _.each(this._views, function(view) {
                view.remove();
            })
            return SuperClass.prototype.remove.call(this);
        },

        updateSelectionCount: function() {
            $(".selection-count").text(this.selectedListings.length + (this.selectedListings.length === 1 ? " listing selected" : " listings selected"));
        },

        resetSelectAllListingsCheckbox: function() {
            this.$el.find('#select-all-listings').prop('checked', false);
        },

        onClearSearchClick: function(e) {
            this.$searchBox.val('');
            this.dataTable.fnFilter('');
        }
    });

    return selectListingsTable;
});
