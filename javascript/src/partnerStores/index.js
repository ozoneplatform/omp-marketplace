define([
    '../views/BaseView',
    './StoresView',
    'views/LoadMask',
    './CreateEditPartnerStoreView',
    '../affiliatedSearch/models/AffiliatedServersModel',
    '../AppData',
    '../events/EventBus',
    'underscore',
    'jquery',
    'marketplace'
],
function (BaseView, StoresView, LoadMask, CreateEditPartnerStoreView, AffiliatedStoreModel, AppData, EventBus, _, $, Marketplace) {

    return BaseView.extend({

        template:
            '<h1>Partner Stores</h1>' +
            '<p>Add, remove or modify information about partner stores. Search results from these partner stores will be displayed below the current store\'s search results.</p>' +
            '<table class="affiliated-stores"></table>' +
            '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>',

        events: {
            'click .btn-field-add': 'addStore',
            'click .edit': 'editStore',
            'click .delete': 'deleteStore'
        },

        initialize: function (options) {
            this.collection = AppData.AffiliatedServers;
            BaseView.prototype.initialize.call(this, options);
        },

        render: function () {
            this.$el.append(this.template);

            LoadMask.show();
            this.collection.fetchIfEmpty().then(_.bind(this.renderStoresView, this));

            return this;
        },

        renderStoresView: function () {
            LoadMask.hide();

            if(this.collection.length === 0) {
                this.listenToOnce(this.collection, 'add', this.renderStoresView);
                return;
            }

            this.storesView = new StoresView({
                collection: this.collection,
                el: this.$el.children('.affiliated-stores')
            });
            this.storesView.render();
        },

        remove: function () {
            this.storesView && this.storesView.remove();
            BaseView.prototype.remove.call(this);
        },

        addStore: function () {
            CreateEditPartnerStoreView.create();
        },

        editStore: function (evt) {
            evt.preventDefault();
            var model = $(evt.target).parents('tr').data('view').model;
            CreateEditPartnerStoreView.edit(model);
        },

        deleteStore: function (evt) {
            evt.preventDefault();
            var $btn = $(evt.target),
                model = $btn.parents('tr').data('view').model;

            $btn.confirm({
                trigger: 'manual',
                placement: 'bottom',
                title: 'Delete Store',
                content: 'Are you sure you want to delete this Partner store?',
                ok: function() {
                    model.destroy({
                        wait: true
                    });
                }
            }).confirm('show');
        }

    });
});
