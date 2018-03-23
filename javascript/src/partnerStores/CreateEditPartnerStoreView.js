define([
    '../views/BaseView',
    '../views/ModalView',
    '../views/Dialog',
    '../createEditListing/jsonForm',
    '../AppData',
    '../affiliatedSearch/models/AffiliatedServersModel',
    'marketplace',
    'jquery',
    'underscore',
    'handlebars'
], function(BaseView, ModalView, Dialog, JsonForm, AppData, AffiliatedServersModel, Marketplace, $, _, Handlebars) {

    'use strict';

    var $bootstrapActive = $('<div class="bootstrap-active modal-container"></div>');

    var CreateEditPartnerStore = ModalView.extend({

        id: 'create-edit-partner-store',

        className: 'bootstrap-active modal hide',

        footer: true,

        animate: false,

        okText: 'Save',
        cancelText: 'Close',

        title: null,

        backdrop: 'static',

        escape: false,

        // serviceItem model when editing or copying
        model: null,

        // serviceItem type object
        type: null,

        template: Handlebars.compile(
            '<div class="modal-header">' +
                '{{#if closable}}' +
                    '<button type="button" class="close" data-dismiss="modal" aria-hidden="true" tabindex="-1">&times;</button>' +
                '{{/if}}' +
                '<h4 class="modal-title">{{title}}</h4>' +
            '</div>' +
            '<div class="modal-body">{{content}}</div>' +
            '{{#if footer}}' +
                '<div class="modal-footer">' +
                    '<button class="btn {{okClassName}} ok" data-loading-text="Saving..." data-complete-text="Saved">{{okText}}</button>' +
                    '{{#if cancelText}}' +
                        '<button class="btn {{cancelClassName}} cancel">{{cancelText}}</button>' +
                    '{{/if}}' +
                '</div>' +
            '{{/if}}'
        ),

        freeTextWarning: _.findWhere(Marketplace.appconfigs, { code: 'free.warning.content' }).value,

        disableFileUpload: _.findWhere(Marketplace.appconfigs, { code: 'store.image.allow.upload' }).value === 'false',

        initialize: function (options) {
            ModalView.prototype.initialize.call(this, options);

            this.$form = $('<form class="form-horizontal"></form>');
        },

        render: function () {
            ModalView.prototype.render.call(this);

            this.$body.append('<p class="free-text-warning">' + _.escape(this.freeTextWarning) + '</p>');

            this.renderForm();

            return this;
        },

        renderForm: function () {
            var fields = [{
                'label': 'Name',
                'type': 'text',
                'attrs': {
                    'value': this.model.get('name'),
                    'name': 'name',
                    'required': true,
                    'maxlength': 50
                }
            }, {
                'label': 'URL',
                'type': 'url',
                'attrs': {
                    'name': 'serverUrl',
                    'maxlength': 2083,
                    'value': this.model.get('serverUrl'),
                    'required': true
                }
            }, {
                'label': 'Timeout (ms)',
                'type': 'number',
                'attrs': {
                    'name': 'timeout',
                    'value': this.model.get('timeout'),
                    'required': true,
                    'min': 1
                }
            }, {
                'label': 'Active',
                'type': 'checkbox',
                'attrs': {
                    'name': 'active',
                    'value': !!this.model.get('active'),
                    'class': 'brand-success'
                }
            }, {
                'label': 'Icon',
                'type': 'file',
                'attrs': {
                    'name': 'iconFile',
                    'value': this.model.get('icon').url,
                    'placeholder': this.disableFileUpload ? 'Image upload is disabled.' : '24 x 24 icon representing the store',
                    'disabled': this.disableFileUpload
                }
            }];

            if(!this.model.isNew()) {
                fields.push({
                    'label': 'ID',
                    'type': 'hidden',
                    'attrs': {
                        'value': this.model.get('id'),
                        'name': 'id'
                    }
                });
            }
            this.jsonForm = new JsonForm.Form(this.$form, fields).render();
            this.$form.appendTo(this.$body);
        },

        ok: function () {
            var me = this;

            me.$form
                .one('submit', function () {
                    me.save();
                    return false;
                }).submit();
        },

        cancel: function (evt) {
            var me = this,
                $btn = $(evt.target);

            $btn.confirm({
                trigger: 'manual',
                placement: 'top',
                container: this.$el,
                title: 'Discard changes',
                content: 'If you click OK, changes will not be saved.',
                ok: function() {
                    $btn.confirm('destroy');
                    me.hide();
                }
            }).confirm('toggle');
        },

        onShown: function () {
            ModalView.prototype.onShown.apply(this, arguments);

            this.$form.validate();
        },

        onHidden: function () {
            this.jsonForm.remove();
        },

        remove: function () {
            ModalView.prototype.remove.call(this);
            $bootstrapActive.remove();
        },

        _submit: function () {
            var me = this,
                frameName = 'create_affiliated_store',
                $uploadFrame = $('<iframe name="' + frameName + '" style="display:none"/>'),
                deferred = $.Deferred();

            this.$form.attr('method', 'POST')
                .attr('enctype', 'multipart/form-data')
                .attr('action', this.model.api().create);

            $('body').append($uploadFrame);

            $uploadFrame.load(function(evt) {

                var $frameBody = $($uploadFrame[0].contentWindow.document.body),
                    $pre = $frameBody.children('pre'),
                    response = $pre.length === 1 ? $pre.text() : $frameBody.text(),
                    json;

                // validate JSON
                try {
                    json = JSON.parse(response);
                    json.success ?
                        deferred.resolve(json):
                        deferred.reject(json.msg);
                }
                catch (err) {
                    deferred.reject(response);
                }
                $uploadFrame.remove();
            });
            this.$form.attr('target', frameName).submit();

            return deferred.promise();
        },

        save: function () {
            if(!this.$form.valid()) {
                return false;
            }

            var me = this,
                $ok = this.$el.find('.btn.ok'),
                isNew = this.model.isNew();

            $ok.button('loading');

            this._submit()
                    .then(function (json) {
                        me.model.set(json.data ? json.data[0] : json);
                        isNew && AppData.AffiliatedServers.add(me.model);
                        return me.hide();
                    })
                    .fail(function (response) {
                        Dialog.show('Server Error!', response);
                    })
                    .always(function () {
                        $ok.button('reset');
                    });
        }

    });


    CreateEditPartnerStore.create = function () {
        var modal = new CreateEditPartnerStore({
            title: 'Create Partner Store',
            model: new AffiliatedServersModel()
        });
        $bootstrapActive.appendTo(document.body).append(modal.render().el);
        modal.show();
        return modal;
    };

    CreateEditPartnerStore.edit = function (model) {
        var modal = new CreateEditPartnerStore({
            title: 'Edit Partner Store',
            model: model
        });
        $bootstrapActive.appendTo(document.body).append(modal.render().el);
        modal.show();
        return modal;
    };

    return CreateEditPartnerStore;

});
