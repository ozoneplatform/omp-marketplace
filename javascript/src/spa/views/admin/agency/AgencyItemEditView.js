define([
    '../../../../views/BaseView',
    '../../../../spa/views/ErrorView',
    'jquery',
    'underscore'
], function(BaseView, ErrorView, $, _ ) {

    /*
     * This view is where the user edits the agency information.
     * If this view was created in "edit" mode then it expects an itemView to be passed in so that view can be rendered again
     * upon save.
     *
     */
    return BaseView.extend({


        errorViews:  [],

        className: "agency-row-edit ",

        template: _.template("<div class='bootstrap-active agency-edit-url-title'>" +
                                "<div class='agency-title'>Icon URL</div>" +
                                "<input placeholder='enter Icon URL' class='admin-text-input agency-url-input'  value='<%- src %>' maxlength='2000' />" +
                             "</div>" +
                             "<div class='bootstrap-active agency-edit-name-title'>" +
                                "<div class='agency-title'>Company Name</div>" +
                                "<input placeholder='enter company name' class='admin-text-input agency-name-input' value='<%- title %>' maxlength='255'  />" +
                                "<span title='Save' class='agency-action-menu agency-save sprite-ok-out'></span>" +
                                "<span title='Cancel' class='agency-action-menu agency-cancel-save sprite-cancel-out'></span>" +
                            "</div>"),

        events: {
            "click     .agency-save"        : "save",
            "mousedown .agency-save"        : "onMouseDownOk",
            "mouseover .agency-save"        : "onMouseOverOk",
            "mouseout  .agency-save"        : "onMouseOutOk",
            "click     .agency-cancel-save" : "exit",
            "mousedown .agency-cancel-save" : "onMouseDownCancel",
            "mouseover .agency-cancel-save" : "onMouseOverCancel",
            "mouseout  .agency-cancel-save" : "onMouseOutCancel",
            "focus     .admin-text-input"   : "onAgencyTextInputToggleFocus",
            "blur      .admin-text-input"   : "onAgencyTextInputToggleFocus"
        },

        onMouseDownOk: function(e){
            $(e.target).removeClass("sprite-ok-over").addClass("sprite-ok-down");
        },

        onMouseOverOk: function(e){
            $(e.target).removeClass("sprite-ok-out").addClass("sprite-ok-over");
        },

        onMouseOutOk: function(e){
            $(e.target).removeClass("sprite-ok-over").addClass("sprite-ok-out");
        },

        onMouseDownCancel: function(e){
            $(e.target).removeClass("sprite-cancel-over").addClass("sprite-cancel-down");
        },

        onMouseOverCancel: function(e){
            $(e.target).removeClass("sprite-cancel-out").addClass("sprite-cancel-over");
        },

        onMouseOutCancel: function(e){
            $(e.target).removeClass("sprite-cancel-over").addClass("sprite-cancel-out");
        },

        onAgencyTextInputToggleFocus: function(e){
            $(e.target).toggleClass("admin-text-active-input");
        },

        render: function() {
            BaseView.prototype.render.apply(this, arguments);

            var configs = {
                src : this.model.get("iconUrl"),
                title : this.model.get("title")
            }

            this.$el.append(this.template(configs));

            this.$el.find(".agency-action-menu").tooltip();

            return this;
        },

        //On save we bind the new values to the model, save, then remove this view and display
        //the item view if needed
        save: _.debounce(function(e){
            var me = this;
            var isNew = this.model.get("id") == null;
            this.model.set("title",     this.$el.find(".agency-name-input").val());
            this.model.set("iconUrl",   this.$el.find(".agency-url-input").val());

            _.invoke(this.errorViews, 'remove');

            this.model.save().then(function(model, response){
                if(isNew){
                    Backbone.trigger('agency:add-new', {model : me.model});
                }
                me.exit();
            }).fail(function(jqXHR, textStatus, errorThrown){
                var errorMessages, errorMessage;

                if(jqXHR.status === 500) {
                    errorMessage = errorThrown;
                }
                else {
                    errorMessages = jqXHR.responseJSON.resolvedMessges;
                    errorMessage = jqXHR.responseJSON.message;
                }

                if(errorMessages){
                    _.each(errorMessages, function(item){
                        var errorView = new ErrorView({model : new Backbone.Model( {errorMessage : item} ) });
                        me.$el.append(errorView.el);
                        me.errorViews.push(errorView);
                    });
                } else{
                    //Fallback to whatever the unresolved message is
                    var errorView = new ErrorView({model : new Backbone.Model( {errorMessage :  errorMessage} ) });
                    me.$el.append(errorView.el);
                    me.errorViews.push(errorView);
                }
            });

        }, 750, true),


        //If canceled this view is removed and we render the item view if needed.
        //Passing a view in is exactly what we want here because upon save we only need to render that row again, ie, we
        //dont need a global event fired off.
        exit: function(){
            var close = _.bind(function(){
                _.invoke(this.errorViews, 'remove');
                this.trigger("agency:close-edit");
                this.unbind();
                this.remove();
            },this);

            if(this.errorViews.length > 0){
                this.model.fetch().done(close);
            } else{
                close();
            }
        }

    });
});
