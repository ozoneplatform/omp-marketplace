define([
    'models/BaseModel',
    'marketplace'
], function (BaseModel, Marketplace) {
    var defaultTimeout = _.findWhere(Marketplace.appconfigs, { code: 'store.amp.search.default.timeout'}).value;

    return BaseModel.extend({

        defaults: {
            serverUrl: undefined,
            timeout: parseInt(defaultTimeout, 10),
            active: true,
            icon: {
                url: undefined
            }
        },

        api: function () {
            return {
                'create': Marketplace.context + '/affiliatedMarketplace/save',
                'update': Marketplace.context + '/affiliatedMarketplace/update/' + this.id,
                'delete': Marketplace.context + '/affiliatedMarketplace/delete/' + this.id
            };
        },

        parse: function (response) {
            if (response.serverUrl.charAt(response.serverUrl.length - 1) === '/') {
                response.serverUrl = response.serverUrl.slice(0, -1);
            }
            response.active = !!response.active;
            return response;
        },

        isActive: function () {
            return this.get('active') === 1;
        }

    });

});
