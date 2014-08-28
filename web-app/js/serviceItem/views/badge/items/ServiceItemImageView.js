define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace'
], function($, _, Backbone, Marketplace){


	return Backbone.View.extend({

		tagName: "tr",

        //the sub-route on the quickview that should be opened when
        //the listing is clicked
        quickviewTab: null,


		template:  _.template("<td class='discover discover_icon_image' id='icon_image_id_<%= id %>'>" +
                                '<div style="position:relative;">' +
                                    "<a class='auto' href='<%= quickviewUrl %>'>" +
                                        '<img class="listing_profile_table_thumbnail" ' +
                                            "src='<%= src %>' " +
                                            "alt='<%= alt %>' />" +
                                        '<div class="discover_favorite_icon_image"></div>' +
                                    "</a>" +
                                '</div>' +
                               "</td>"),


        initialize: function(options) {
            this.context = options.context;
            this.quickviewTab = options.quickviewTab;

            Backbone.View.prototype.initialize.apply(this, arguments);
        },

        render: function(){
            var me = this,
                iconDefault = this.getDefaultTypeIcon(),
                itemIcon =  this.model.get("imageMediumUrl") || this.model.get("imageLargeUrl"),
                templateParams = {
                    id : this.model.get("id"),
                    alt: this.model.get("title"),
                    quickviewUrl: this.makeQuickviewUrl()
                };

            templateParams.src = itemIcon && itemIcon.trim().length > 0 ? itemIcon : iconDefault;

            this.$el.append(this.template(templateParams));


            var data = this.model.toJSON();
            var isAddedGuid = data.uuid;

            if (data.owfProperties && data.owfProperties.stackContext) {
                isAddedGuid = data.owfProperties.stackContext;
            }

            Marketplace.widget.isAlreadyInOwf(isAddedGuid, function(isInOwf) {
                if (isInOwf) {
                    me.$el.find('.discover_favorite_icon_image').show();
                }
            });
            return this;
        },

        makeQuickviewUrl: function() {
            var url;
            if (this.model.get('isAffiliated')) {
                url = '#quickview-affiliated/' + encodeURIComponent(this.context) + '/' +
                    this.model.get('id');
            }
            else {
                url = '#quickview/' + this.model.get('id');
            }

            return this.quickviewTab ? url + '/' + this.quickviewTab : url;
        },

        getDefaultTypeIcon: function () {
            var typeId = this.model.get('types').id;
            return encodeURI(Marketplace.context + '/images/types/' + typeId);
        }

	});
});
