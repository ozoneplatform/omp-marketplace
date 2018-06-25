define([
    '../../../../views/BaseView',
    'jquery'
], function(BaseView, AgencyItemEditView, $) {

    /*
     * This is the read only view that shows the agency and icon.  It expects a model to be padded in to it.
     */
    return BaseView.extend({

        className: "agency-read-only",

        events: {
            "click" : "edit"
        },

        template: _.template("<div  class='agency-element agency-element-img'>" +
                                "<img src='<%- src %>' />" +
                             "</div>" +
                             "<div  class='agency-element agency-element-name'> <%- title %> </div>"),


        render: function() {
            BaseView.prototype.render.apply(this, arguments);
            //Literal for template
            var configs = {
                src : this.model.get("iconUrl"),
                title : this.model.get("title")
            }

            this.$el.append(this.template(configs));
            return this;
        },

        edit: function(){
            this.trigger("agency:open-edit");
            this.unbind();
            this.remove();
        }

    });
});
