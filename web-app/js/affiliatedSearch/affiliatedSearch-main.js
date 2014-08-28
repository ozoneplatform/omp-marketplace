require([
    'jquery',
    'underscore',
    'backbone',
    'serviceItem/util/ToggleResultsView',
    'affiliatedSearch/views/AffiliatedServersPageView',
    'affiliatedSearch/collections/AffiliatedServersCollection',
    'affiliatedSearch/views/AffiliatedResultsGridView'
],
function($, _ , Backbone, ToggleResultsView,
    AffiliatedServersPageView, AffiliatedServersCollection, AffiliatedResultsGridView) {

    //the collection of affiliated marketplace models, and a promise for when
    //it has been fetched
    var serverCollection = new AffiliatedServersCollection(),
        serverCollectionPromise = serverCollection.fetch({
            wait : true,
            data: {
                active: true
            }
        }),
        searchParams = _.defaults({client: 'amp'}, affiliatedMarketplaceSearchParams),
        affiliatedServersView;

    function affiliatedStoreInit() {
        var configs = {
            searchParams     : searchParams,
            searchResultSize : affiliatedMarketplaceSearchSize,
            collection: serverCollection
        };

        if (affiliatedServersView) {
            affiliatedServersView.remove();
        }

        //Only searches will render AMP results
        if(affiliatedMarketplaceSearchSize > 0){
            affiliatedServersView = new AffiliatedServersPageView(configs);

            //after the data comes back, render the view
            serverCollectionPromise.done(function() {
                if (serverCollection.size() > 0) {
                    affiliatedServersView.render().$el.appendTo($('#pending_list'));
                }
            });
        }
    }

    //make the server collection globally available so that the AffiliatedResultsGridView can get to it
    window.Marketplace.affiliatedServers = {
        promise: serverCollectionPromise,
        collection: serverCollection
    };

    affiliatedStoreInit();
    ToggleResultsView.setDefaultView(gridOrListView, sortBy, sortOrder);
});
