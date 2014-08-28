define([
    '../views/ModalView',
    'views/Dialog',
    '../rejectionListing/ReadRejectionJustificationView',
    '../models/ServiceItem',
    '../collections/CustomFieldsCollection',
    './jsonForm',
    './PrimarySection',
    './TechnicalSection',
    './OWFSection',
    './ListingTypeSection',
    './FranchiseStoreSection',
    'marketplace',
    'jquery',
    'underscore',
    'handlebars',
    'backbone',
    './fields/RequiredItemsInputField',
    './fields/ResourcesInputField',
    './fields/ScreenshotsInputField',
    './fields/IntentsInputField',
    './fields/ContactsInputField',
    './fields/TagsInputField'
],
function(
    ModalView, Dialog, ReadRejectionJustificationView, ServiceItem, CustomFieldsCollection,
    JsonForm, PrimarySection, TechnicalSection, OWFSection, ListingTypeSection, FranchiseStoreSection,
    Marketplace, $, _, Handlebars, Backbone
) {

    var SuperClass = ModalView,
        $bootstrapActive = $('<div class="bootstrap-active modal-container"></div>'),
        APP_COMPONENT = 'App Component',
        APP = 'OZONE App',
        customFieldsCollection = new CustomFieldsCollection([], {
            context: Marketplace.context
        }),
        customFieldsCollectionFetchPromise = customFieldsCollection.fetch();

    var CreateEditModal = SuperClass.extend({

        id: 'create-edit-listing',

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
            '{{#if title}}' +
                '<div class="modal-header">' +
                    '{{#if closable}}' +
                        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true" tabindex="-1">&times;</button>' +
                    '{{/if}}' +
                    '<h4 class="modal-title">{{title}}' +
                        '{{#if isRejected}}' +
                            '<a href="#" class="rejection-justification">(why?)</a>' +
                        '{{/if}}' +
                    '</h4>' +
                '</div>' +
            '{{/if}}' +
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

        events: _.extend({}, SuperClass.prototype.events, {
            'change [name="types[id]"]': 'updateFields',
            'click .rejection-justification': 'showRejectionJustification'
        }),

        freeTextWarning: _.findWhere(Marketplace.appconfigs, { code: 'free.warning.content' }).value,

        initialize: function (options) {
            SuperClass.prototype.initialize.call(this, options);
            this.type = this.model.get('types');

            this.$form = $('<form class="form-horizontal"></form>');
        },

        _doRender: function () {
            SuperClass.prototype.render.call(this);

            if(this.title) {
                this.$el.find('.modal-header').addClass(this.title.toLowerCase().replace(' ', '-'));
            }

            this.$body.append('<p class="free-text-warning">' + _.escape(this.freeTextWarning) + '</p>');

            this.jsonForm = new JsonForm.Form(this.$form, [
                this.buildPrimarySection(),
                this.buildTechnicalSection(),
                this.buildFranchiseStoreSection()
            ]).render();

            this.primarySection = this.jsonForm.fields[0];
            this.technicalSection = this.jsonForm.fields[1];
            this.franchiseStoreSection = this.jsonForm.fields[2];

            this.updateFields();

            this.doLayout();
            this.$form.appendTo(this.$body);

            this.initTooltips();
        },

        render: function () {
            var me = this,
                dfd = $.Deferred();

            customFieldsCollectionFetchPromise
                .done(function () {
                    me._doRender();
                    dfd.resolve();
                })
                .fail(function () {
                    dfd.reject();
                });

            return dfd.promise();
        },

        isRejected: function () {
            return this.model.isRejected();
        },

        initTooltips: function () {
            var $tooltips = this.$form.find('[data-toggle="tooltip"]');
            if($tooltips.length) {
                this.$tooltips = $tooltips.tooltip({
                    container: this.$el
                });
            }
        },

        destroyTooltips: function () {
            if(this.$tooltips) {
                this.$tooltips = this.$tooltips.tooltip('destroy');
            }
        },

        showRejectionJustification: function (evt) {
            if(!this.model.isRejected()) { return; }

            var $body = this.$el,
                readRejectionJustificationView = new ReadRejectionJustificationView({
                    model: this.model
                });

            readRejectionJustificationView.render().promise().then(function () {
                this.$rejectionJustificationPopover = $(evt.target).popover({
                    trigger: 'manual',
                    placement: 'bottom',
                    cls: 'rejection-justification-popover',
                    container: $body,
                    title: 'Rejection Justification',
                    html: true,
                    content: readRejectionJustificationView.el
                }).popover('toggle');
            });
        },

        updateFields: function (evt) {
            var type = evt ? evt.added : this.type,
                listingTypeSectionIndex;

            if(!type) {
                return;
            }

            this.$form.detach();
            this.destroyTooltips();

            this.type = type;

            console.log('Type selected:', type.title);

            if(type.title === APP || !type.ozoneAware) {
                this.jsonForm.remove(this.owfSection);
                this.owfSection = null;
            }

            if(type.title === APP) {
                this.listingTypeSection = this.listingTypeSection ?
                                            this.jsonForm.update(this.listingTypeSection, this.buildListingTypeSection()) :
                                            this.jsonForm.add(this.buildListingTypeSection(), 2);
            }
            else {
                if(type.ozoneAware) {
                    if(!this.owfSection) {
                        this.owfSection = this.jsonForm.add(this.buildOWFSection(), 2);
                    }
                    listingTypeSectionIndex = 3;
                }
                else {
                    listingTypeSectionIndex = 2;
                }
                this.listingTypeSection = this.listingTypeSection ?
                                            this.jsonForm.update(this.listingTypeSection, this.buildListingTypeSection()) :
                                            this.jsonForm.add(this.buildListingTypeSection(), listingTypeSectionIndex);
            }

            // these sections only need to be updated if this method is invoked because of select-change event
            if(evt && evt.added) {
                this.primarySection = this.jsonForm.update(this.primarySection, this.buildPrimarySection());
                this.technicalSection = this.jsonForm.update(this.technicalSection, this.buildTechnicalSection());
                this.franchiseStoreSection = this.jsonForm.update(this.franchiseStoreSection, this.buildFranchiseStoreSection());
            }

            this.$form.appendTo(this.$body);
            this.initTooltips();
            this.doLayout();
        },

        /**
        * Forces layout in IE7.
        * For some reasoe IE7, doesn't layout fields that are not in view correctly.
        **/
        doLayout: function () {
            if($.browser.msie && $.browser.versionNumber === 7) {
                this.$form.find('*').each(function (i, elem) {
                    var $elem = $(elem);
                    if($elem.css('position') !== 'absolute') {
                        $elem.css('position', 'relative');
                    }
                });
            }
        },

        buildPrimarySection: function () {
            return PrimarySection.build(this.model, this.type, customFieldsCollection);
        },

        buildTechnicalSection: function () {
            return TechnicalSection.build(this.model, this.type, customFieldsCollection);
        },

        buildOWFSection: function () {
            return OWFSection.build(this.model);
        },

        buildListingTypeSection: function () {
            return ListingTypeSection.build(this.model, this.type, customFieldsCollection);
        },

        buildFranchiseStoreSection: function () {
            return FranchiseStoreSection.build(this.model, this.type, customFieldsCollection);
        },

        ok: function () {
            console.log('Saving listing...');
            var me = this,
                formJson = me.$form.serializeObject();

            me.$form
                .one('submit', function () {
                    me.save(formJson);
                    return false;
                }).submit();
        },

        cancel: function (evt) {
            console.log('Cancelled ' + this.title);

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
            SuperClass.prototype.onShown.apply(this, arguments);

            this.$form.validate();
        },

        onHidden: function () {
            this.jsonForm.remove();
        },

        remove: function () {
            if(this.$rejectionJustificationPopover) {
                this.$rejectionJustificationPopover.popover('destroy');
            }
            SuperClass.prototype.remove.call(this);
            $bootstrapActive.remove();
        },

        save: function (json) {
            if(!this.$form.valid()) {
                return false;
            }

            var me = this,
                $ok = this.$el.find('.btn.ok');

            _.forEach(['agency', 'state', 'types'], function (prop) {
                var id = json[prop].id;

                if(id === '') {
                    delete json[prop];
                }
                else {
                    json[prop].id = parseInt(id, 10);
                }

            });

            _.each(['categories', 'relatedItems', 'owners'], function (key, i) {
                var values = json[key];
                if(values && values.length) {
                    values = _.map(values, function (id) {
                        return { id: parseInt(id, 10) };
                    });
                    json[key] = values;
                }
            });

            json.customFields = _.compact(json.customFields);

            json.relationships = [{
                relationshipType: 'REQUIRE',
                relatedItems: json.relatedItems || []
            }];
            delete json.relatedItems;

            !json.contacts && (json.contacts = []);
            !json.docUrls && (json.docUrls = []);
            !json.screenshots && (json.screenshots = []);

            var ozoneAware = _.findWhere(Marketplace.types, { id : json.types.id }).ozoneAware;
            !ozoneAware && (delete json.owfProperties);
            json.owfProperties && !json.owfProperties.intents && (json.owfProperties.intents = []);

            console.log('Saving...' + json.title);
            console.log(json);

            $ok.button('loading');

            this.model
                    .set(json)
                    .save()
                    .then(function(response) {
                        var dfd = $.Deferred(),
                            tags = me.model.tags();

                        tags = tags.filter(function (model) {
                            return !model.get('tag');
                        });

                        if(tags.length === 0) {
                            dfd.resolve();
                        }
                        else {

                            console.log(tags);

                            _.each(tags, function(tag){
                                tag.set('serviceItemId', response.id);
                            });

                            return me.model.saveTags(tags);
                        }

                        return dfd.promise();
                    })
                    .then(function () {
                        return me.hide();
                    })
                    .done(function () {
                        Backbone.history.stop();
                        window.location = window.location.href.replace(/#.*/, '') + '#quickview/' + me.model.get('id') + '/admin';
                        window.location.reload();
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        $ok.button('reset');
                        var resolvedMessges = jqXHR.responseJSON.resolvedMessges;
                        Dialog.show('Server Error!', resolvedMessges ? resolvedMessges.join('<br><br>') : errorThrown);
                    });
        }

    });

    function renderAndShowModal (modal) {
        modal.render().then(function () {
            $bootstrapActive.appendTo(document.body).append(modal.el);
            modal.show();
        });
    }

    return {
        create: function () {
            var modal = new CreateEditModal({
                title: 'Draft',
                model: new ServiceItem()
            });

            renderAndShowModal(modal);
        },
        edit: function (model) {
            var modal = new CreateEditModal({
                title: model.get('approvalStatus'),
                model: model
            });

            renderAndShowModal(modal);
        },
        copy: function (model) {
            var newModel = model.clone();
            newModel.set('uuid', null);

            var modal = new CreateEditModal({
                title: 'Draft',
                model: newModel
            });

            renderAndShowModal(modal);
        }
    };

});
