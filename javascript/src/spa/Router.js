define([
    'jquery',
    'underscore',
    'backbone',
    'spa/views/admin/agency/AgencyAdminPageView'
], function($, _, Backbone, AgencyAdminView) {

    var adminHome = $('#admin_home'),
        pageParent = $('#marketContent');

    //private method to navigate to a given admin page
    //'this' should be a router instance
    function navigateToPage(pageName, ViewClass) {
        var view = this.pages[pageName];

        if (!view) {
            view = this.pages[pageName] = new ViewClass();
            view.render().$el.appendTo(pageParent);
        }

        adminHome.hide();
        view.show();

        this.currentPage = view;
    }

    return Backbone.Router.extend({

        //map from page name to the view for that page
        pages: {},

        //the admin page currently in view (null if we are at the menu)
        currentPage: null,

        initialize: function() {
            Backbone.history.start({pushState: false});

            Backbone.Router.prototype.initialize.apply(this, arguments);
        },

        routes: {
            '': 'adminMenu',
            'agency': 'navigateToAgencyPage'
        },

        adminMenu: function() {
            if (this.currentPage) {
                this.currentPage.hide();
            }

            adminHome.show();
        },

        navigateToAgencyPage: _.partial(navigateToPage, 'agency', AgencyAdminView)
    });
});
