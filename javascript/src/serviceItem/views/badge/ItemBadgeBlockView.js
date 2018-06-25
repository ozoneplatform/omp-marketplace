define([
    'jquery',
    'underscore',
    '../../../views/BaseView',
    '../badge/items/ServiceItemImageView',
    '../badge/items/ServiceItemTitleView',
    '../badge/items/ServiceItemAgencyView',
    '../badge/items/ServiceItemRatingView',
    '../badge/items/ServiceItemScoreCardView',
    '../badge/items/ServiceItemDescriptionView',
    "../badge/items/ServiceItemCategoryView",
    "../badge/items/ServiceItemTypesView"
], function ($, _, BaseView,
        ServiceItemImageView, ServiceItemTitleView,
        ServiceItemAgencyView, ServiceItemRatingView,
        ServiceItemScoreCardView,ServiceItemDescriptionView,
        ServiceItemCategoryView, ServiceItemTypesView) {

    return BaseView.extend({

        tagName: "table",

        className: "discover_market_items_table quickview-link",

        showScoreCard : true,

        //The url-context of this listing.  Useful when the listing is
        //from an affiliated store
        context: null,

        //the sub-route on the quickview that should be opened when
        //the listing is clicked
        quickviewTab: null,

        initialize: function(options){
            this.showScoreCard = options.showScoreCard;
            BaseView.prototype.initialize.apply(this, arguments);
        },

        //This will build each peace of the badge
        render: function () {
            this.renderImageView();
            this.renderTitleView();
            this.renderAgencyView();
            this.renderRatingView();
            this.renderDescriptionView();
            this.renderCategoriesView();
            this.renderTypesView();
            this.renderAttributes();

			return this;
        },


        //This renders the items image element
        renderImageView: function () {
            var item = new ServiceItemImageView({
                model : this.model,
                context: this.context,
                quickviewTab: this.quickviewTab
            });
            item.render();
            this.$el.append(item.el);
        },

        //This renders the items title element
        renderTitleView: function () {
            var item = new ServiceItemTitleView({
                model : this.model,
                context: this.context,
                quickviewTab: this.quickviewTab

            });
            item.render();
            this.$el.append(item.el);
        },

        //This renders the items agency element
        renderAgencyView: function () {
            var item = new ServiceItemAgencyView({model : this.model});
            item.render();
            this.$el.append(item.el);
        },

        //This renders the items rating element
        renderRatingView: function () {
            var item = new ServiceItemRatingView({model : this.model});
            item.render();
            this.$el.append(item.el);
        },

        //This renders the items score card element
        renderScoreCardView: function () {
            if(this.showScoreCard == "true") {
                var item = new ServiceItemScoreCardView({model : this.model});
                item.render();
                this.$el.append(item.el);
            }
        },

        //This renders the items description element
        renderDescriptionView: function () {
            var item = new ServiceItemDescriptionView({model : this.model});
            item.render();
            this.$el.append(item.el);
        },

        //This renders the items categories element
        renderCategoriesView: function () {
            if(this.model.get("categoriesString") != undefined) {
                var item = new ServiceItemCategoryView({model : this.model});
                item.render();
                this.$el.append(item.el);
            }
        },

        //This renders the items types element
        renderTypesView: function () {
            var item = new ServiceItemTypesView({model : this.model});
            item.render();
            this.$el.append(item.el);
        },

        renderAttributes: function() {
            var model = this.model;

            this.$el.attr({
                'data-affiliated': model.get('isAffiliated'),
                'data-id': model.get('id')
            });

            if (this.context) {
                this.$el.attr('data-context', this.context);
            }
        }
    });
});
