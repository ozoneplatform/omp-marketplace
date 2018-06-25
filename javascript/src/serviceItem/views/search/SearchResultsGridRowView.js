define([
    'jquery',
    'underscore',
    'backbone',
    '../badge/ItemBadgeBlockView'
], function ($, _, Backbone, ItemBadgeBlockView) {

    /*
     * This is the entry point for the views.  This class will fetch the data, iterate through it
     * and call the ItemView for each record.
     */
    return Backbone.View.extend({

        tagName: "tr",

        className: "grid_result_row",

        template:  _.template("<td>" +
            "<div id='miniBadgeId<%- id %>' " +
                    "class='mini_badge_extended_div ozone_aware_service_item' " +
                    "data-affiliated='<%- affiliated %>' data-context='<%- context %>' " +
                    "data-id='<%- id%>'>" +
                "<table class='discover_market_items_table'> <%= badgeHtml %> </table>" +
            "</div>" +
        "</td>"),

        initialize: function (options) {
            this.items          = options.items;
            this.showScoreCard  = options.showScoreCard;
        },

        render: function () {
            var me = this,
                showScoreCard = this.showScoreCard;

            this.items.each(function (model) {
                var blockView = new ItemBadgeBlockView({
                    model: model,
                    showScoreCard: showScoreCard
                });
                blockView.render();
                
                var dlu = model.get('detailListingUrl'),
                    context = dlu.slice(0, dlu.indexOf('/serviceItem/show/'));

                //This is kind of a hack.  $el.html() is only giving the innerhtml and we need the <table> declaration
                me.$el.append(me.template({
                    id: model.get("id"),
                    badgeHtml: blockView.$el.html(),
                    affiliated: model.get('isAffiliated'),
                    context: context
                }));
            });
            return this;
        }
    });
});
