define([
  'jquery',
  'underscore',
  'backbone'
], function ($, _, Backbone) {
	
	return Backbone.View.extend({

		tagName: "div",
		
		className: "franchise_discover_see_more_text",
		
		template: _.template("<h1 class='search_results_title'><%= serverName %><a href='<%= url %>'><span class='see-all-text'>See All</span></a></h1>"),
		
		initialize: function (options) {
			this.searchAllUrl = options.searchAllUrl;
			this.serverName   = options.serverName;
		},
		
		render: function () {
			var templateParams = {
				url: this.searchAllUrl,
				serverName : this.serverName
			}

			//$(this.el).append("<h1 class='search_results_title'>Our Partners</h1>");
			this.$el.append(this.template(templateParams));
			return this;
		}
		
	});
});
