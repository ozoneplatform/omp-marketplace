define([
    '../views/BaseView',
    '../views/ModalView',
    '../views/Dialog',
    '../createEditListing/jsonForm',
    '../models/ScorecardQuestion',
    'marketplace',
    'jquery',
    'underscore',
    'handlebars'
], function(BaseView, ModalView, Dialog, JsonForm, ScorecardQuestion, Marketplace, $, _, Handlebars) {

    'use strict';

    var $bootstrapActive = $('<div class="bootstrap-active modal-container"></div>');

    var CreateEditScoreCardQuestion = ModalView.extend({

        id: 'create-edit-scorecard-question',

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
                '<h4 class="modal-title">Add Scorecard Question</h4>' +
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
            this.jsonForm = new JsonForm.Form(this.$form, [{
                'label': 'Question',
                'type': 'textarea',
                'attrs': {
                    'value': this.model.get('question'),
                    'name': 'question',
                    'required': true,
                    'maxlength': 250
                }
            }, {
                'label': 'Description',
                'type': 'textarea',
                'attrs': {
                    'value': this.model.get('description'),
                    'name': 'description',
                    'maxlength': 500,
                    'required': true
                }
            },{ 
                'label': 'Image URL',
                'type': 'url',
                'attrs': {
                    'value': this.model.get('image'),
                    'name': 'image'
                }
            },{
                'label': 'Visible',
                'type': 'checkbox',
                'attrs': {
                    'value': this.model.get('showOnListing'),
                    'checked': this.model.get('showOnListing'),
                    'class': 'brand-success',
                    'name': 'showOnListing'
                }
            }]).render();

            this.doLayout();
            this.$form.appendTo(this.$body);
            
            this.$el.find('input[type="checkbox"]').svitch();
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

        ok: function () {
            console.log('Saving question...');
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

        save: function (json) {
            if(!this.$form.valid()) {
                return false;
            }

            var me = this,
                $ok = this.$el.find('.btn.ok'),
                isNew = this.model.isNew();

            $ok.button('loading');

            this.model
                    .set(json)
                    .save()
                    .then(function () {
                        return me.hide();
                    })
                    .done(function () {
                        
                        //isNew && AppData.ScoreCardQuestionCollection.add(me.model);
                        window.location.reload();
                    })
                    .fail(function (jqXHR, textStatus, errorThrown) {
                        $ok.button('reset');
                        var resolvedMessges = jqXHR.responseJSON.resolvedMessges;
                        Dialog.show('Server Error!', resolvedMessges ? resolvedMessges.join('<br><br>') : errorThrown);
                    });
        }

    });


    CreateEditScoreCardQuestion.create = function () {
        var modal = new CreateEditScoreCardQuestion({
            model: new ScorecardQuestion()
        });
        $bootstrapActive.appendTo(document.body).append(modal.render().el);
        modal.show();
    };

    CreateEditScoreCardQuestion.edit = function (model) {
        var modal = new CreateEditScoreCardQuestion({
            model: model
        });
        $bootstrapActive.appendTo(document.body).append(modal.render().el);
        modal.show();
    };

    return CreateEditScoreCardQuestion;

});
