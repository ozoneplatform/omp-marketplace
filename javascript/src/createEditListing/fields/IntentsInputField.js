define([
    '../jsonForm',
    'jquery',
    'underscore',
    'handlebars',
    'marketplace'
], function (JsonForm, $, _, Handlebars, Marketplace) {

    var FormFieldTypes = JsonForm.FormFieldTypes,
        ArrayInputField = FormFieldTypes.get('array'),
        Select2InputField = FormFieldTypes.get('select2'),
        CheckboxInputField = FormFieldTypes.get('checkbox'),
        HiddenInputField = FormFieldTypes.get('hidden'),
        intentActions, intentDataTypes, promise;



    promise = $.when(
        $.get(Marketplace.context + '/public/intentAction'),
        $.get(Marketplace.context + '/public/intentDataType')
    ).done(function (ia, idt) {
        intentActions = ia[0].data;
        intentDataTypes = idt[0].data;
    });



    var IntentsInputField = function (options) {
        this.init('intents', options);
    };

    _.extend(IntentsInputField.prototype, ArrayInputField.prototype, {

        constructor: IntentsInputField,

        template: Handlebars.compile(
            '<div class="array-items intents">' +
                '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>' +
            '</div>'
        ),

        headerTemplate: '<div class="header row">' +
                            '<div class="span action-field">Action<span class="required-star">*</span></div>'+
                            '<div class="span data-type-field">Data Type<span class="required-star">*</span></div>'+
                            '<div class="span send-field">Send</div>'+
                            '<div class="span receive-field">Receive</div>'+
                        '</div>',

        itemTemplate: Handlebars.compile(
            '<div class="array-item">' +
                '<div class="row">' +
                    '<div class="span action-field"></div>'+
                    '<div class="span data-type-field"></div>'+
                    '<div class="span send-field"></div>'+
                    '<div class="span receive-field"></div>'+
                '</div>' +
                '<div class="btn-field-remove"><a href="#" class="btn btn-small">-</a></div>' +
            '</div>'
        ),

        index: 0,

        actionFields: null,
        dataTypeFields: null,
        sendFields: null,
        receiveFields: null,
        idFields: null,

        init: function (type, options) {
            ArrayInputField.prototype.init.call(this, type, options);
            this.actionFields = [];
            this.dataTypeFields = [];
            this.sendFields = [];
            this.receiveFields = [];
            this.idFields = [];
        },

        renderActionAndDataType: function ($item, value, index) {
            var actionField = new Select2InputField({
                data: intentActions,
                attrs: {
                    'name': 'owfProperties[intents][' + index + '][action][id]',
                    'required': true,
                    'value': value ? value.action.id: ''
                }
            }).render();

            var dataTypeField = new Select2InputField({
                data: intentDataTypes,
                attrs: {
                    'name': 'owfProperties[intents][' + index + '][dataType][id]',
                    'required': true,
                    'value': value ? value.dataType.id: ''
                }
            }).render();

            $item.find('.action-field').append(actionField.$el);
            $item.find('.data-type-field').append(dataTypeField.$el);

            this.actionFields.push(actionField);
            this.dataTypeFields.push(dataTypeField);
        },

        addItem: function (value) {
            var me = this,
                $item = $(this.itemTemplate()),
                index = this.index,
                sendField, receiveField, idField;

            sendField = new CheckboxInputField({
                attrs: {
                    name: 'owfProperties[intents][' + index + '][send]',
                    value: value ? value.send : true,
                    'class': 'brand-success'
                }
            }).render();

            receiveField = new CheckboxInputField({
                attrs: {
                    name: 'owfProperties[intents][' + index + '][receive]',
                    value: value ? value.receive : false,
                    'class': 'brand-success'
                }
            }).render();

            if(value && value.id) {
                idField = new HiddenInputField({
                    attrs: {
                        name: 'owfProperties[intents][' + index + '][id]',
                        value: value.id
                    }
                }).render();
            }

            promise.done(_.bind(this.renderActionAndDataType, this, $item, value, index));

            $item.find('.send-field').append(sendField.$el);
            $item.find('.receive-field').append(receiveField.$el);
            idField && $item.append(idField.$el);

            this.$el.find('.btn-field-add').before($item);

            this.index++;
            this.sendFields.push(sendField);
            this.receiveFields.push(receiveField);
            idField && this.idFields.push(idField);

            if(this.$el.find('.header.row').length === 0) {
                this.$el.prepend(this.headerTemplate);
            }
        },

        removeItem: function (evt) {
            ArrayInputField.prototype.removeItem.call(this, evt);

            if(this.$el.find('.array-item').length === 0) {
                this.$el.find('.header.row').remove();
                this.$el.closest('.control-group').removeClass('error');
            }
        },

        remove: function () {
            _.invoke(this.idFields, 'remove');
            _.invoke(this.sendFields, 'remove');
            _.invoke(this.receiveFields, 'remove');
            return ArrayInputField.prototype.remove.call(this);
        }

    });

    FormFieldTypes.register('intents', IntentsInputField);

    return IntentsInputField;

});