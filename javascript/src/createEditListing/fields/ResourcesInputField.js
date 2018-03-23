define([
    '../jsonForm',
    'jquery',
    'underscore',
    'handlebars'
], function (JsonForm, $, _, Handlebars) {

    var FormFieldTypes = JsonForm.FormFieldTypes,
        ArrayInputField = FormFieldTypes.get('array'),
        TextInputField = FormFieldTypes.get('text'),
        UrlInputField = FormFieldTypes.get('url');

    var ResourcesField = function (options) {
        this.init('resources', options);
    };

    _.extend(ResourcesField.prototype, ArrayInputField.prototype, {

        constructor: ResourcesField,

        template: Handlebars.compile(
            '<div class="array-items docurls">' +
                '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>' +
            '</div>'
        ),

        itemTemplate: Handlebars.compile(
            '<div class="array-item">' +
                '<div class="row">' +
                    '<div class="span label-field"></div>'+
                    '<div class="span url-field"></div>'+
                '</div>' +
                '<div class="btn-field-remove"><a href="#" class="btn btn-small">-</a></div>' +
            '</div>'
        ),

        headerTemplate: '<div class="header row">' +
                            '<div class="span label-field">Label<span class="required-star">*</span></div>'+
                            '<div class="span url-field">URL<span class="required-star">*</span></div>'+
                        '</div>',

        labels: [],
        urls: [],

        init: function (type, options) {
            ArrayInputField.prototype.init.call(this, type, options);
            this.labels = [];
            this.urls = [];
        },

        addItem: function (value) {
            var $item = $(this.itemTemplate());

            var label = new TextInputField({
                attrs: {
                    name: 'docUrls[][name]',
                    required: true,
                    maxlength: 255,
                    value: (value && value.name) || ''
                }
            }).render();

            var url = new UrlInputField({
                attrs: {
                    name: 'docUrls[][url]',
                    required: true,
                    maxlength: 2083,
                    value: (value && value.url) || ''
                }
            }).render();

            $item.find('.label-field').append(label.$el);
            $item.find('.url-field').append(url.$el);

            this.$el.find('.btn-field-add').before($item);

            this.labels.push(label);
            this.urls.push(url);

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
            _.invoke(this.labels, 'remove');
            _.invoke(this.urls, 'remove');
            return ArrayInputField.prototype.remove.call(this);
        }

    });

    FormFieldTypes.register('resources', ResourcesField);

    return ResourcesField;

});
