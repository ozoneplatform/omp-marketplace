define(
[
    '../views/BaseView',
    './header',
    './body',
    '../tag/views/TagPanelView',
    '../models/ServiceItem',
    '../events/EventBus',
    'backbone',
    'handlebars',
    'jquery',
    'underscore'
],
function(BaseView, HeaderView, BodyView, TagPanelView, ServiceItemModel, EventBus, Backbone, Handlebars, $, _) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        // the Ext modal
        modal: null,

        // whether the service item is from an affiliated marketplace
        isAffiliated: false,

        // the context of the service item's marketplace
        context: null,

        // the ID string of the service item
        // which this QuickView modal displays
        serviceItemId: null,

        // whether the user is an admin
        isUserAdmin: false,

        // the user's username
        username: null,

        // the service item
        // which this QuickView modal displays
        serviceItemModel: null,

        // the header view
        headerView: null,

        // the body view
        bodyView: null,

        footerView: null,

        // listing not found Handlebars template
        listingNotFoundTpl: Handlebars.compile(
            '<div class="quickview-listing-not-found">' +
                '<h1 class="listing-not-found-header">Listing Not Found</h1>' +
                '<p class="listing-not-found-error-msg">{{errorMsg}}</p>' +
            '</div>'
        ),

        /**
         * Initializes the view. Calls the super class.
         * @param {Object} options the options
         */
        initialize: function(options) {
            SuperClass.prototype.initialize.call(this, options);

            if (this.context.charAt(this.context.length - 1) === '/') {
                this.context = this.context.slice(0, -1);
            }

            if (this.serviceItemId) {
                this.serviceItemModel = new ServiceItemModel({
                    isAffiliated: this.isAffiliated,
                    context: this.context,
                    id: this.serviceItemId
                });
            }

            this.username = options.username;
        },

        /**
         * Renders the view.
         * @returns {MainView} this for chaining purposes
         */
        render: function() {
            var me = this;

            // service item?
            if (me.serviceItemModel) {

                me.serviceItemModel.fetch()
                    .done(function() {
                        me.headerView = new HeaderView({
                            serviceItemModel: me.serviceItemModel
                        });
                        me.bodyView = new BodyView({
                            serviceItemModel: me.serviceItemModel,
                            isAffiliated: me.isAffiliated,
                            isUserAdmin: me.isUserAdmin,
                            context: me.context,
                            username: me.username
                        });

                        if (me.tab) {
                            me.bodyView.activeTab = me.tab;
                        }

                        me.$el.append(
                            me.headerView.render().el,
                            me.bodyView.render().el
                        );

                        if(!me.isAffiliated){
                            me.footerView = new TagPanelView({
                                serviceItemModel: me.serviceItemModel
                            });
                            me.$el.append(me.footerView.el);
                        }

                    })
                    .fail(function() {
                        me.$el.append(
                            me.renderListingNotFound()
                        );
                    })
                    .always(function () {
                        me.shown();
                    });
            }
            // no service item?
            else {
                me.$el.append(
                    me.renderListingNotFound()
                );
            }

            return me;
        },

        /**
         * Renders the listing not found page.
         * @returns {jQuery} a jQuery representation of the listing not found page
         */
        renderListingNotFound: function() {
            var errorMsg = 'Could not find listing #' + this.serviceItemId + '.';

            return $(this.listingNotFoundTpl({
                errorMsg: errorMsg
            }));
        },

        /**
         * Destroys the view. Calls the super class. Automatically destroys the HeaderView and BodyView objects.
         * @returns {MainView} this for chaining purposes
         */
        remove: function () {
            EventBus.trigger('quickview:remove', this.serviceItemModel, this);

            this.headerView && this.headerView.remove();
            this.bodyView && this.bodyView.remove();
            this.footerView && this.footerView.remove();

            return SuperClass.prototype.remove.call(this);
        },

        /**
         * Called as soon as the view has been loaded into the DOM. You can safely do DOM-specific stuff here.
         * @returns {MainView} this for chaining purposes
         */
        shown: function() {
            var me = this;

            this.headerView && this.headerView.shown();
            this.bodyView && this.bodyView.shown();

            EventBus.trigger('quickview:shown', this.serviceItemModel, this);

            return this;
        },

        route: function() {
            this.bodyView.route.apply(this.bodyView, arguments);
        },

        getTitle: function() {
            return this.serviceItemModel.get('title');
        }
    });
});
