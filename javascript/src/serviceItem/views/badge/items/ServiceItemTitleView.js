define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace',
], function($, _, Backbone, Marketplace){


	return Backbone.View.extend({

		tagName: "tr",

        //the sub-route on the quickview that should be opened when
        //the listing is clicked
        quickviewTab: null,

		template:  _.template("<td class='discover' > " +
									"<div class='discover discover_item_title'>" +
										"<a class='auto' href='<%- quickviewUrl %>'>" +
											"<div class='auto_title_text ellipsis' title='<%- title %>'>" +
												"<%- title %> " +
											"</div>" +
										"</a>" +
									"</div>" +
							  "</td>"),


        initialize: function(options) {
            this.context = options.context;
            this.quickviewTab = options.quickviewTab;

            Backbone.View.prototype.initialize.apply(this, arguments);
        },

		render: function(){
			var templateParams = {
				id: this.model.get("id"),
				title: this.model.get("title"),
                quickviewUrl: this.makeQuickviewUrl()
			}
			$(this.el).append(this.template(templateParams));
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
        }
	});
});
