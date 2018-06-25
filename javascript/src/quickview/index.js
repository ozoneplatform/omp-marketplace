define([
    '../views/ModalView',
    '../events/EventBus',
    './main',
    'jquery',
    'underscore'
],
function(ModalView, Eventbus, QuickViewMainView, $, _) {

    var SuperClass = ModalView;

    $.magnificPopup && ($.magnificPopup.defaults.gallery.arrowMarkup = '<a title="%title%" type="button" class="mfp-icon-chevron-%dir% icon-chevron-%dir%" href="javascript:void(0)"></a>');

    return SuperClass.extend({

        className: SuperClass.prototype.className + ' quickview',

        footer: false,

        animate: false,

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);

            options = _.extend({ context: window.Marketplace.context }, options);

            this.mainView = new QuickViewMainView(options);
            _.bindAll(this, 'edit', 'copy');
            Eventbus.on('edit:listing', this.edit);
            Eventbus.on('copy:listing', this.copy);
        },

        render: function () {
            SuperClass.prototype.render.call(this);
            this.mainView.render().$el.appendTo(this.$body);
            return this;
        },

        remove: function () {
            this.mainView.remove();
            return SuperClass.prototype.remove.call(this);
        },

        edit: function (model) {
            this.hide()
                .done(function () {
                    require(['createEditListing/index'], function (CreateEditListing) {
                        CreateEditListing.edit(model);
                    });
                });
        },

        copy: function (model) {
            this.hide()
                .done(function () {
                    require(['createEditListing/index'], function (CreateEditListing) {
                        CreateEditListing.copy(model);
                    });
                });
        },

        onHidden: function () {
            Eventbus.off('edit:listing', this.edit);
            Eventbus.off('copy:listing', this.copy);
        },

        route: function() {
            this.mainView.route.apply(this.mainView, arguments);
        },

        getTitle: function() {
            return this.mainView.getTitle();
        }
    });
});
