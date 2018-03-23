define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace'
], function($, _, Backbone, Marketplace){

	var affiliatedMarketplaceSearchResourceBundle = affiliatedMarketplaceSearchResourceBundle || null;

	if(!affiliatedMarketplaceSearchResourceBundle) {
		affiliatedMarketplaceSearchResourceBundle = {
			activeStar: Marketplace.context + '/static/themes/common/images/activeStar.png',
			activeHalfStar: Marketplace.context + '/static/themes/common/images/activeHalfStar.png',
			inActiveStar: Marketplace.context + '/static/themes/common/images/inActiveStar.png'
		};
	}

	return Backbone.View.extend({

		tagName: "tr",

		template:  			 _.template("<img id='rating_star_<%= id %>' src='<%= src %>' /img>&nbsp"),

		totalRatingTemplate: _.template("<span class='rating_total_votes rating_total_votes_count' id='rating_total_id<%= id %>'> (<%= totalVotes %>)</span></td></span>"),

		render: function(){

            var html = "";
			var starRateVal = this.model.get("avgRate");

			if(starRateVal !== undefined){
				for(idx = 1 ; idx <= 5; idx ++){
					var halfStarLow = (idx - 1)  + 0.25;
					var halfStarHigh = (idx - 1) + 0.99;

					if(starRateVal > halfStarHigh){
						html = html + this.template({id : this.model.get("id"), src : affiliatedMarketplaceSearchResourceBundle.activeStar});
					} else{
						if((halfStarLow <= starRateVal) && (starRateVal <= halfStarHigh)){
							html = html + this.template({id : this.model.get("id"), src : affiliatedMarketplaceSearchResourceBundle.activeHalfStar});
						} else{
							html = html + this.template({id : this.model.get("id"), src : affiliatedMarketplaceSearchResourceBundle.inActiveStar});
						}
					}
				}

				html = html + this.totalRatingTemplate({id : this.model.get("id"), totalVotes : this.model.get("totalVotes")});
				$(this.el).append("<td class='discover rating'><span class='rating_total_votes rating_star_group' title='Average Rating: " + starRateVal + "'>" + html + "</span></td>");
			}

		}

	});
});
