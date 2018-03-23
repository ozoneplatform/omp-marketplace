define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace'
], function($, _, Backbone, Marketplace){
	

	return Backbone.View.extend({

		tagName: "tr",
		
		template:	_.template("<td class='discover score'>" + 
									"<div class='score_card_wheel <%=imgCls%>' title='Score: <%= value %>%' >" + 
											"<span class='score_text'>Scorecard: </span>" +
										    "<span class='score_label'> <%= value %>% </span>" +
									 "</div>" +
							    "</td>"),									   
		
		render: function(){
			
			var score = this.model.get("scoreCard.score");
			//Score might not be supported by the store
			if(score !== undefined){
			
				var templateParams = {
					value:  Math.round(score),
					src:    this.getScoreCardWheelImg(score),
					imgCls: this.getScoreCardWheelClass(score)
				}
				$(this.el).append(this.template(templateParams));				
			}
			
		},
		
		//Taken from grails-app\views\serviceItem\_compliance_wheel.gsp
		getScoreCardWheelImg : function(score){
			var scorePercent = Math.round(score / 10);
			return Marketplace.context + "/themes/common/images/scorecard/scorecard_" + scorePercent + "0.png";
		},
		
		getScoreCardWheelClass : function(score){
			var scorePercent = Math.round(score / 10);
			return "scorecard_wheel_" + scorePercent + "0_pct";
		}
		
	});
});
