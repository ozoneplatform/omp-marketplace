(function ($, user) {
    var $bootstrapActive, isUserAdmin = user.isAdmin, username = user.username,
        selector = ['.mini_badge_extended_div', '.widget_list_item', '.listings-grid-view .discover_market_items_table',
                    '.affiliated-store-search-results .discover_market_items_table', '.mini_badge_extended_div', '.widget_list_item',
                    '.read_more_link', '.quickview-link'].join(',');

    var Quickview = {
        open: function (id, isAffiliated, context, activeTabIndex) {
            require(['./quickview/index'], function (QuickView) {
                var quickview = new QuickView({
                    isAffiliated: isAffiliated || false,
                    context: context || Marketplace.context,
                    serviceItemId: id,
                    isUserAdmin: isUserAdmin,
                    username: username,
                    activeTabIndex: activeTabIndex
                });

                $bootstrapActive = $bootstrapActive || $('<div class="bootstrap-active modal-container"></div>').appendTo(document.body);
                $bootstrapActive.append(quickview.render().el);
                quickview.show();
            });
        }
    };

    // expose as service
    window.Quickview = Quickview;

})(jQuery, Marketplace.user || {});
