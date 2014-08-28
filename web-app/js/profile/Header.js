define([
    '../views/BaseView',
    'handlebars'
], function(BaseView, Handlebars) {
    'use strict';

    var SuperClass = BaseView,
        ownProfileName = 'My Account',
        template = Handlebars.compile('<h3><span class="header-text">{{profileName}}</span>' +
            '<button class="close">×</button></h3>');

    return SuperClass.extend({
        className: 'store-modal-header',

        events: {
            'click .close': 'closeWindow'
        },

        render: function() {
            //determine the name to display.  Use "My Account" if it is your own profile
            var profileName = this.model.id === window.Marketplace.user.id ?
                ownProfileName :
                this.model.get('displayName');

            this.$el.append(template({ profileName: profileName }));

            return this;
        },

        closeWindow: function() {
            window.Marketplace.mainRouter.closeModal();
        }
    });
});
