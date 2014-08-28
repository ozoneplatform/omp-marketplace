/**
*
* Application data
*
**/
define([
    './affiliatedSearch/collections/AffiliatedServersCollection',
    './collections/CustomFieldsCollection',
    'marketplace'
],
function(AffiliatedServersCollection, CustomFieldsCollection, Marketplace) {

    var affiliatedServers = new AffiliatedServersCollection();
    var customFields = new CustomFieldsCollection([], { context: Marketplace.context });

    return {
        AffiliatedServers: affiliatedServers,
        CustomFields: customFields
    };

});
