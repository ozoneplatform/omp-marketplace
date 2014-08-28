define(
[
    '../../views/BaseView',
    '../../views/CategorizedServiceItemList',
    'backbone',
    'handlebars',
    'jquery',
    'underscore'
],
function(BaseView, CategorizedServiceItemList, Backbone, Handlebars, $, _) {
    'use strict';

    var SuperClass = BaseView;

    /**
     * A generic function for rendering each of the collections. This function is meant to
     * be curried and bound for each collection
     */
    function renderCollection(title, collection, requiresItemSection) {
        /*jshint validthis:true*/
        if(collection.length) {
            $('<h5>').text(title).appendTo(requiresItemSection);

            var listView = new CategorizedServiceItemList({
                collection: collection,
                isAffiliated: this.serviceItemModel.get('isAffiliated'),
                affiliatedUrl: this.context
            });

            requiresItemSection.append(listView.render().$el);

            return listView;
        }
    }

    return SuperClass.extend({
        //child views to clean up on remove
        subViews: null,

        initialize: function(options){
            this.serviceItemModel = options.serviceItemModel;
            this.requiredItemsCollection = options.serviceItemModel.includedItems();
            this.requiredByItemsCollection = options.serviceItemModel.requiredByItems();

            this.subViews = [];

            SuperClass.prototype.initialize.apply(this, arguments);

            this.render();
        },

        renderRequiredSection: _.partial(renderCollection, 'Requires'),
        renderRequiredBySection: _.partial(renderCollection, 'Required By'),

        render: function(){
            var requiredContainer = $('<div class="required-container">'),
                requiredByContainer = $('<div class="required-container">'),
                me = this;

            this.$el.append(requiredContainer).append(requiredByContainer);

            //render each collection
            _.each([
                [
                    this.requiredItemsCollection,
                    _.bind(this.renderRequiredSection, this, this.requiredItemsCollection,
                        requiredContainer)
                ],
                [
                    this.requiredByItemsCollection,
                    _.bind(this.renderRequiredBySection, this, this.requiredByItemsCollection,
                        requiredByContainer)
                ]
            ], function(tuple) {
                var collection = tuple[0], renderFn = tuple[1];
                collection.fetch({data: {accessAlertShown: true}}).always(function() {
                    me.subViews.push(renderFn());
                });
            });

            return this;
        },

        remove: function() {
            _.each(this.subViews, function(view) {
                view.remove();
            });

            SuperClass.prototype.remove.apply(this, arguments);
        }
    });
});

