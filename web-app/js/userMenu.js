define([
    'views/BaseView',
    'backbone',
    'marketplace',
    'handlebars',
    'bootstrap-select'
], function (BaseView, Backbone, Marketplace, Handlebars, $) {

    var MENU_ITEM_CLICK_CHANNEL = "_MARKETPLACE_MENU_ITEM_CLICK";
    var MENU_ADMIN_TOGGLE_CHANNEL = "_MARKETPLACE_MENU_ADMIN_TOGGLE";

    OWF.ready(function () {
        var container = Ozone.eventing.Widget.getInstance();
        container.send('..', MENU_ADMIN_TOGGLE_CHANNEL, null, JSON.stringify({
            isAdmin: Marketplace.user.isAdmin
        }));
        container.registerHandler(MENU_ITEM_CLICK_CHANNEL, handleOWFMenuClick);

        function handleOWFMenuClick (message) {
            message = JSON.parse(message);
            switch (message.itemId) {
                case "marketplaceUserProfile":
                    Backbone.history.navigate('profile/self', {trigger: true});
                    break;
                case "marketplaceThemes":
                    Backbone.history.navigate('profile/self/preferences', {trigger: true});
                    break;
                case "marketplaceMyListings":
                    Backbone.history.navigate('profile/self/myListings', {trigger: true});
                    break;
                case "marketplaceCreateListing":
                    require(['createEditListing/index'], function (CreateEditListing) {
                        CreateEditListing.create();
                    });
                    break;
                case "marketplaceConfigurationPages":
                    window.location = Marketplace.context + '/admin';
                    break;
                case "marketplaceFranchiseAdministration":
                    window.location = Marketplace.context + '/admin/partner-stores';
                    break;
                default:
                    break;
            }
        }
    });

    var UserMenuView = BaseView.extend({

        className: 'dropdown user-dropdown',

        template: Handlebars.compile(
            '<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">User Options</a>' +
            '<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">' +
                '<li><a tabindex="-1" href="#profile/self">User Profile</a></li>' +
                '<li><a tabindex="-1" class="create-listing" href="#">Create Listing</a></li>' +
                '{{#if isAdmin}}<li><a tabindex="-1" href="' + Marketplace.context + '/admin">Configuration Pages</a></li>{{/if}}' +
                '<li class="divider"></li>' +
                '<li><a tabindex="-1" href="../logout.jsp" class="logout">Logout</a></li>' +
            '</ul>'
        ),

        render: function () {
            this.$el.append(this.template(Marketplace.user));
            return this;
        }

    });

    var menu = new UserMenuView();
    var $userMenu = $('#userMenu').addClass('bootstrap-active');
    $userMenu.html(menu.render().$el);

    if(Ozone.util.isInContainer() && Marketplace.widget.isContainerVersionGreaterThan(7,3)) {
        $userMenu.css('visibility', 'hidden');
    }

    $(document).on('click', '[href="#create-listing"]', function (e) {
        e.preventDefault();

        require(['createEditListing/index'], function (CreateEditListing) {
            CreateEditListing.create();
        });
    });

});
