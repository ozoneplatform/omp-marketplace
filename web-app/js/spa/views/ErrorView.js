define([
    '../../views/BaseView'
], function(BaseView) {

    return BaseView.extend({

        template: _.template("<div class='admin_field_error'>  <%- msg %> </div>"),

    	initialize: function(){
    		this.render();
    	},

        render: function(){
            this.$el.append(this.template({
                msg: this.model.get("errorMessage")
            }));
            return this;
        }

    });

});
