define(
[
    '../views/BaseView',
    '../views/tabbable/TabsView',
    './Overview',
    './Reviews',
    './details/Details',
    './Scorecard',
    './admin/AdminTabView',
    'backbone',
    'jquery',
    'underscore',
    'handlebars'
],
function(BaseView, TabsView, Overview, Reviews, Details, Scorecard, AdminTabView, Backbone, $, _, Handlebars) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'quickview-body' + (Modernizr.csstransforms3d ? ' csstransforms3d' : ''),

        // ServiceItem model
        serviceItemModel: null,

        // the tabs view
        tabs: null,

        // the tab panes
        tabPanes: [],

        // tab to activate
        activeTab: 'overview',

        /**
         * Renders the view.
         * @returns {BodyView} this for chaining purposes
         */
        render: function() {
            var isOwnerOrAdmin = Marketplace.user.isAdmin || this.serviceItemModel.isOwnedBy(Marketplace.user.username);

            var overview = new Overview({serviceItemModel: this.serviceItemModel}),
                details = new Details({
                    serviceItemModel: this.serviceItemModel,
                    isOwnerOrAdmin : isOwnerOrAdmin,
                    context: this.context
                }),
                reviews = new Reviews({serviceItemModel: this.serviceItemModel}),
                scorecardConfig = _.findWhere(Marketplace.appconfigs, {code: 'store.enable.scoreCard'});


            this.tabPanes = { overview: overview, reviews: reviews, details: details };

            if(isOwnerOrAdmin && !this.serviceItemModel.get('isAffiliated')) {
                this.tabPanes.admin = new AdminTabView({
                    model: this.serviceItemModel
                });
            }

            if(scorecardConfig.value === 'true' && isOwnerOrAdmin && !this.serviceItemModel.get('isAffiliated')) {
                this.tabPanes.scorecard = new Scorecard({
                    model: this.serviceItemModel
                });
            }

            this.tabs = new TabsView({
                tabPanes: this.tabPanes,
                activeTab: this.activeTab,
                href: this.href()
            });

            this.$el.append(this.tabs.render().el);

            return this;
        },

        href: function () {
            var id = this.serviceItemModel.get('id');
            if(this.isAffiliated) {
                return 'quickview-affiliated/' + encodeURIComponent(this.context) + '/' + id;
            }

            return 'quickview/' + id;
        },

        /**
         * Destroys the view. Calls the super class.
         * @returns {BodyView} this for chaining purposes
         */
        remove: function () {
            if (this.tabs) {
                this.tabs.remove();
            }

            return SuperClass.prototype.remove.call(this);
        },

        /**
         * Called as soon as the view has been loaded into the DOM. You can safely do DOM-specific stuff here.
         * @returns {BodyView} this for chaining purposes
         */
        shown: function() {
            this.tabs.shown();

            return this;
        },

        route: function(params) {
            this.tabs.setActiveTab(params.tab);
        }

    });
});
