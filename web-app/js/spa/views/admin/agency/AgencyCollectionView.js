define([
    '../../../../views/CollectionView',
    '../../../../spa/collections/AgencyCollection',
    '../agency/AgencyItemView',
    'jquery'
], function(CollectionView, AgencyCollection, AgencyItemView, $) {

    /*
     * This view is what fetches the agency collection then iterates through it creating a
     * AgencyItemView for each record.
     */
    return CollectionView.extend({

        className:  "agency-summary",

        CollectionItemView: AgencyItemView,

        initialize: function(){
            CollectionView.prototype.initialize.apply(this, arguments);
            this.listenTo(Backbone, 'agency:add-new', this.addNew, this);
            //Initialize the agency collection
            this.collection = new AgencyCollection();

            var me = this;

            //Fetch the results then when its complete call render
            //TODO add error handling
            this.collection.fetch().done(function(){
                me.render();
            });
        },

        addNew: function(event){
            this.addOne(event.model);
        }
    });
});
