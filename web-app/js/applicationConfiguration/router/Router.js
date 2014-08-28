define([
    'jquery',
    'underscore',
    'backbone',
    '../collections/Collection',
    '../views/AMLConfigPage'
], function($, _, Backbone, ApplicationConfigurationCollection, AMLConfigPage){

    return Backbone.Router.extend({

        view: null,   //the current view
        appConfigs: {},    //a map of the app config models associated by groupName
        viewModels: {},

        container: $('#app-config-page'),

        routes: {
            "config/:id": "showGroup"
        },

        initialize: function() {
            _.bindAll(this, 'sortAndIndexModels');
            this.collection = new ApplicationConfigurationCollection();
            var me = this;

            //TODO: load with initial models already bootstrapped?
            this.collection.fetch().complete(function() {
                me.sortAndIndexModels();
                me.showGroup('BRANDING');
            });
        },

        cleanup: function () {
            this.view && this.view.remove();
            delete this.view;
        },

        showGroup: function (name) {
            this.cleanup();
            this.collection.fetch();

            this.view = new AMLConfigPage({ collection: new ApplicationConfigurationCollection() });
            this.view.appConfigs = this.appConfigs;
            this.view.collection.reset(this.viewModels[name]);

            this.container.append(this.view.el);
        },

        //set appConfigs to model references indexed by code
        //set viewModels to lists of model references indexed by groupName
        sortAndIndexModels: function() {
            this.appConfigs = _.groupBy(this.collection.models, function(m){ return m.get("code"); });
            this.viewModels = _.chain(this.collection.models)
                .sortBy(function(m){ return m.get("id"); })
                .groupBy(function(m){ return m.get("groupName"); })
                .value();
        }
    });
});