define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace',
], function($, _, Backbone, Marketplace){
	

	return Backbone.View.extend({
		
		maxLength: 90,
		
		tagName: "tr",
		
		//TODO toggle this based on a passed in flag
		className: "hidden_row x-hide-display",
		
		template:  _.template("<td class='discover discover_item_categories' >" +
									"<%= categories %>" +
									"<% if(showReadMore == true) { %>" +
										"<div><a style='margin-left:0px' class='read_more_link' href='<%= detailListingUrl %>'>more</a></div>" +
						             "<% } %>" +	
							  "</td>"),
		
				  
		render: function(){
			var categories = "";			
			if(this.model.get("categoriesString") != undefined){
				categories = this.model.get("categoriesString").join();
			}
			
			var templateParams = {
				id: this.model.get("id"), 
				categories:  Marketplace.getTruncatedValue(categories, this.maxLength),
				detailListingUrl : this.model.get("detailListingUrl"),
				showReadMore: (categories.length > this.maxLength)					
			}
			$(this.el).append(this.template(templateParams));
		}
		
	});
});
