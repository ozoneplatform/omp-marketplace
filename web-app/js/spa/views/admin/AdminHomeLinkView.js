define([
    '../../../views/BaseView',
    'jquery',
    'handlebars'
], function(BaseView, $, Handlebars) {

    return BaseView.extend({

        template: Handlebars.compile(
            '<h5 class="admin-home-title inline" ><i class="icon-home"></i><a href="{{context}}/admin">Admin Home</a></h5>'
        ),

        render: function(){
            this.$el.append(this.template({
                context: Marketplace.context
            }));
            return this;
        }

    });

});
