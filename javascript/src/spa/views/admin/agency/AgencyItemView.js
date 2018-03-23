define([
    '../../../../views/BaseView',
    '../agency/AgencyItemEditView',
    '../agency/AgencyItemReadOnlyView',
    'jquery',
    'underscore'
], function( BaseView, AgencyItemEditView, AgencyItemReadOnlyView, $, _ ) {

    /*
     * This view initially shows in 'read only' mode.  If edit is clicked then it
     * will remove the read only view and create an editable view.
     */
    return BaseView.extend({

        className: "agency-row",


        render: function() {
            BaseView.prototype.render.apply(this, arguments);

            //By default render the read only view.
            this.renderReadyOnlyView();

            return this;
        },

        //Method to create the read only view
        renderReadyOnlyView: function(){

        	this.agencyItemReadOnlyView = new AgencyItemReadOnlyView({
                model: this.model
            });
            this.agencyItemReadOnlyView.render();
            this.$el.append(this.agencyItemReadOnlyView.el);

            this.listenTo(this.agencyItemReadOnlyView, "agency:open-edit", this.renderEditView, this);
        },

        //Method to create the edit view.
        renderEditView: function(){

            this.agencyItemEditView = new AgencyItemEditView({
                model: this.model
            });
            this.agencyItemEditView.render();
            this.$el.append(this.agencyItemEditView.el);

            this.listenTo(this.agencyItemEditView, "agency:close-edit", this.renderReadyOnlyView, this);
        }

    });
});
