define([
    'jquery',
    'underscore',
    'backbone'
], function($, _, Backbone){

    return Backbone.View.extend({

        tagName: "tr",

        template:  _.template('<td class="discover">' +
                                    '<div id="agency_<%- id %>" class="agency" title="<%- title %>" >' +
                                        '<img src="<%- agencyIcon %>" id="agency_icon_<%- id %>" class="agency-image" >' +
                                        '<span class ="agency_text"> <%- agency %> </span>' +
                                    '</div>' +
                              '</td>'),

        render: function(){
        
            if (this.model.get("agency")) {
                var agency = this.model.get("agency");
                //Have to be backward compatible here so we need to check for the agency object attributes then
                //fallback to agency and agencyIcon in the model
                var templateParams = {
                    id: agency.id ? agency.id : "service_item_" + this.model.get("id"),
                    title: agency.title ? agency.title : this.model.get("agency"),
                    agency: Marketplace.getTruncatedValue(agency.title ? agency.title : this.model.get("agency"), 12),
                    agencyIcon: agency.iconUrl ? agency.iconUrl : this.model.get("agencyIcon")
                };
                var html = this.template(templateParams);

                $(this.el).append(html);
            }
        }
    });
});
