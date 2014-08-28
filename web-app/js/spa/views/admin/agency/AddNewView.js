define([
    '../../../../views/BaseView',
    'jquery'
], function(BaseView, $) {
    return BaseView.extend({

        className: "agency-summary ",

        events: {
            "click" : "addNew"
        },

        template: _.template("<div class='admin-add-new'>" +
                                    "<a class='admin-add-new-link'>+</a>" +
                                    "<span class='admin-add-new-text'>Add</span>" +
                              "</div>"),


        render: function() {
            BaseView.prototype.render.apply(this, arguments);
            this.$el.append(this.template());
            return this;
        },

        addNew: function(){
        	this.trigger("agency:open-edit");
        }

    });
});
