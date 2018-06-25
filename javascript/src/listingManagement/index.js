define([
    '../serviceItem/ListingsWithCountView',
    '../recentActivity/index',
    'views/LoadMask',
    '../collections/ServiceItemsCollection',
    '../collections/GlobalServiceItemActivityCollection',
    '../events/EventBus',
    'underscore',
    'jquery',
    'marketplace'
],
function(ListingsWithCountView, RecentActivityView, LoadMask, ServiceItemsCollection, GlobalServiceItemActivityCollection, EventBus, _, $, Marketplace) {

    var refreshAll = _.debounce(function () {
        recentActivityCollection.refresh();
        pendingListingsCollection.refresh();
    }, 1000);

    // show load mask initially
    LoadMask.show();

    /*========================================
    =            Pending Listings            =
    ========================================*/

    var pendingListingsCollection = new ServiceItemsCollection([], {
        url: Marketplace.context + '/api/search?statuses=Pending&sort=editedDate&order=desc'
    });

    var pendingListingsView = new ListingsWithCountView({
        title: 'Pending Listings',
        collection: pendingListingsCollection,
        quickviewTab: 'admin'
    });

    var pendingListingsPromise = pendingListingsCollection.fetch().always(function () {
        pendingListingsView.render();
        pendingListingsView.$el.appendTo('#pendingListings');
    });


    /*=======================================
    =            Recent Activity            =
    =======================================*/

    var recentActivityCollection = new GlobalServiceItemActivityCollection([], {
        context: Marketplace.context
    });

    var recentActivityView = new RecentActivityView({
        title: 'Recent Activity',
        collection: recentActivityCollection,
        quickviewTab: 'admin'
    });

    var recentActivityPromise = recentActivityCollection.fetch().always(function () {
        recentActivityView.render();
        recentActivityView.$el.appendTo('#recentActivity');
    });

    EventBus.on('quickview:shown', function (model, view) {

        model.on('sync', refreshAll);

        EventBus.once('quickview:remove', function (model, view) {
            model.off('sync', refreshAll);
        });

    });

    // hide promise after initial fetch completes
    $.when(pendingListingsPromise, recentActivityPromise).always(function () {
        LoadMask.hide();
    });
});
