define([
    '../../../views/BaseView',
    'jquery'
], function(BaseView, $) {

    return BaseView.extend({

        template: _.template("<div class='agency-menu'>" +
                                    "<h2> <%= title  %> </h2>" +
                                     "<p class='subheader-description'> <%- subTitle  %> </p>" +
                                "</div>"),


        render: function(){
            var configs = {
                title    : this.title,
                subTitle : this.subTitle
            }
            this.$el.append(this.template(configs));
            return this;
        }

    });

});
