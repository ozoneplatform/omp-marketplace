define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'dataTables',
    '../../views/WizardBaseView',
    './SelectListingsTable'
], function($, _, Backbone, Handlebars, DataTables, BaseView, SelectListingsTable) {

    var SuperClass = BaseView;

    var selectListingsView = SuperClass.extend({

        tagName: 'div',
        className: 'wizard-section',

        events: {
            'click .export-all': 'hideTable',
            'click .export-selection': 'showTable'
        },

        // Radio button markup
        template: Handlebars.compile (

            '<p style="margin-left: -10px; margin-bottom: 20px">Note: Required listings are automatically included in the export.</p>' +
            '<label class="radio export-all">' +
                  '<input type="radio" name="selectListings" value="all" checked>Export all' +
            '</label>'
                +
            '<label class="radio export-selection">' +
                   '<input id="select-some" type="radio" name="selectListings" value="selection">Select individual listings to export' +
            '</label>'
        ),

        // collection of service items
        collection: null,

        initialize: function(options) {

            SuperClass.prototype.initialize.call(this, options);

            var me = this;
            me.listingsCollection = options.listingsCollection;
            me.selectListingsData = me.model.get('data');
            me.notificationDisplayed = false;

            // Remove the notification
            $('#wizard-wait-notification').remove();
            me.notificationDisplayed = false;

            // Put the listing data in an array
            me.listings = me._retrieveData(me.listingsCollection);
            me.listingsTable = new SelectListingsTable({
                model: new Backbone.Model({
                    listings:  me._retrieveData(me.listingsCollection)
                })
            });

            // Save all service items in model for now
            me.selectListingsData['allServiceItems'] = me.listings;
        },

        // Render function for the SelectListingsView
        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },

        // Show the listings table  (handler)
        showTable: function() {
            var me = this;
            if (me.listingsTable.isRendered()) {
                me.listingsTable.show();
                $(".selection-count").show();
            } else {
                me.$el.append(me.listingsTable.render().el);
            }

            me.listingsTable.shown();
        },

        shown: function() {
            var radioSelection = this.$el.find("input:radio[name = 'selectListings']:checked").val();
            if (radioSelection === "selection") this.listingsTable.shown();
        },

        // Hide (remove) the table
        hideTable: function() {

            $(".next-button").prop('disabled', false);
            this.listingsTable.hide();
            $(".selection-count").hide();



        },

        // This is the function called when the Next button is pressed
        beforeNextCard: function() {

            var me = this;

            $(".selection-count").hide();

            var selectedServiceItemIds = [];
            var radioSelection = me.$("input:radio[name = 'selectListings']:checked").val();

            if (radioSelection === "selection") {
                //Find selected service items and put ids in the JSON
                me.listingsTable.resetSelectAllListingsCheckbox();
                _.each(me.listingsTable.selectedListings, function (id) {
                    selectedServiceItemIds.push(id);
                });
            } else {
                //User has "export all" selected so take all service items
                _.each(me.selectListingsData['allServiceItems'], function (item) {
                    selectedServiceItemIds.push(item.id);
                });
            }

            me.selectListingsData['serviceItems'] = selectedServiceItemIds;
            me.selectListingsData['exportAll'] = (me.selectListingsData['serviceItems'].length == me.selectListingsData['allServiceItems'].length);
        },

        createNextViews: function() {
            return null;
        },

        remove: function() {
            this.listingsTable.remove();
            SuperClass.prototype.remove.call(this);

        },

        // Get data directly from the collection's models
        _retrieveData: function(data) {
            return _.map(data.models, function(item) { return item.attributes; });
        }

    });

    return selectListingsView;

});
