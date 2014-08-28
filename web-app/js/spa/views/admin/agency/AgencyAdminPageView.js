define([
    '../../../../views/BaseView',
    '../../../../spa/views/admin/AdminHomeLinkView',
    '../../../../spa/views/admin/AdminSubMenuView',
    '../../../../spa/views/admin/agency/AgencyCollectionView',
    '../../../../spa/views/admin/agency/AddNewView',
    '../../../../spa/views/admin/agency/AgencyItemEditView',
    '../../../../spa/models/AgencyModel',
    'jquery'
], function( BaseView, AdminHomeLinkView ,AdminSubMenuView, AgencyCollectionView, AddNewView, AgencyItemEditView, AgencyModel, $) {


    /*
     * Template for the top part of the view.  This will assemble any headers and then agency content.
     */
    return BaseView.extend({

        className: "agency-admin-page",

        render: function(){
            this.renderMenuViews();
            this.renderCollectionView();
            this.renderAddNewView();
            return this;
        },

        //Renders the header and sub headers
        renderMenuViews: function(){
            var adminHomeLinkView = new AdminHomeLinkView();
            adminHomeLinkView.render();
            this.$el.append(adminHomeLinkView.el);

            var adminSubMenuView = new AdminSubMenuView({title: "Store Companies", subTitle: "Manage your store's companies"});
            adminSubMenuView.render();
            this.$el.append(adminSubMenuView.el);
        },

        //Renders the collection view that in turn renders the detail rows
        renderCollectionView: function(){
            this.agencyCollectionView = new AgencyCollectionView();
            this.$el.append(this.agencyCollectionView.el);
        },

        //Generates the 'add new' link
        renderAddNewView: function(){
            this.addNewView = new AddNewView();
            this.addNewView.render();
            this.$el.append(this.addNewView.el);
            this.listenTo(this.addNewView, "agency:open-edit", this.renderEditView, this);
        },

        //Method to create the edit view in case add new is clicked
        renderEditView: function(){

            this.agencyItemEditView = new AgencyItemEditView({
            	model : new AgencyModel()
            });
            this.agencyItemEditView.render();
            this.agencyCollectionView.$el.append(this.agencyItemEditView.el);

            this.listenTo(this.agencyItemEditView, "agency:close-edit", this.closeEditView, this);
            this.addNewView.hide();
        },

        closeEditView: function(event){
        	this.addNewView.show();
        }

    });
});
