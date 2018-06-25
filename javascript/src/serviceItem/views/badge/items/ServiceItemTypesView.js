define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace',
], function($, _, Backbone, Marketplace){
	

	return Backbone.View.extend({
		
		tagName: "tr",
		
		//TODO toggle this based on a passed in flag
		className: "hidden_row x-hide-display",
		
		template:  _.template("<td class='discover discover_item_types'> " +
									"<%= types %>" +
							  "</td>"),
		
				  
		render: function(){
			
			//Get the string by first trying the type list then falling back to the types object
			var typesString = "";
			if(this.model.get("typesString") == undefined){
				//This is only used in older version of MP that do not have the trimmed list
				_.each(this.model.get("types"), function(key, value){
					if(value == "title"){
						typesString = typesString + key + ", ";
					}
				});				
				typesString = typesString.substr(0, typesString.length - 2);
			} else{
				typesString = this.model.get("typesString");
			}
			
			var templateParams = {
					id: this.model.get("id"), 
					types: Marketplace.getTruncatedValue(typesString, 90)
			}
			
			$(this.el).append(this.template(templateParams));
		}
		
	});
});
