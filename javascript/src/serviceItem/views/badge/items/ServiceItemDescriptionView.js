define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace',
], function($, _, Backbone, Marketplace){
	

	return Backbone.View.extend({
		
		maxLength:  300,
		
		maxLineCount: 6,
		
		tagName: "tr",
		
		//TODO toggle this based on a passed in flag
		className: "hidden_row x-hide-display",
		
		template:  _.template("<td class='discover discover_item_description' id='description_<%= id %>' > " +
									"<p id='serviceItemBadgeDescription<%= id %>'><%= description %></p>" +
					                "<% if(showReadMore == true) { %>" +
										"<a style='margin-left:0px' class='read_more_link' href='<%= detailListingUrl %>'>more</a>" +
					                "<% } %>" +									
							  "</td>"),
		
				  
		render: function(){
			
			var description = Marketplace.getTruncatedValue(this.model.get("description"), this.maxLength, this.maxLineCount);
			var isTruncated = description != this.model.get("description");
	
			//Replace line breaks with html breaks
			description = description.replace(/\n/g, '<br />');
			
			var templateParams = {
				id: this.model.get("id"), 
				description: description,
				detailListingUrl : this.model.get("detailListingUrl"),
				showReadMore: isTruncated
			}
			
			$(this.el).append(this.template(templateParams));
		}
		
	});
});
