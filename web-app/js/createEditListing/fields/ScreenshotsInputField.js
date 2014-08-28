define([
    '../jsonForm',
    'jquery',
    'underscore',
    'handlebars'
], function (JsonForm, $, _, Handlebars) {

    var FormFieldTypes = JsonForm.FormFieldTypes,
        ArrayInputField = FormFieldTypes.get('array'),
        ImageInputField = FormFieldTypes.get('image');

    var ResourcesField = function (options) {
        this.init('screenshots', options);
    };

    _.extend(ResourcesField.prototype, ArrayInputField.prototype, {

        constructor: ResourcesField,

        template: Handlebars.compile(
            '<div class="array-items screenshots">' +
                '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>' +
            '</div>'
        ),

        headerTemplate: '<div class="header row">' +
                            '<div class="span small-image-field">Small Image URL<span class="required-star">*</span></div>'+
                            '<div class="span large-image-field">Large Image URL</div>'+
                        '</div>',

        itemTemplate: Handlebars.compile(
            '<div class="array-item">' +
                '<div class="row">' +
                    '<div class="span small-image-field"></div>'+
                    '<div class="span large-image-field"></div>'+
                '</div>' +
                '<div class="btn-field-remove"><a href="#" class="btn btn-small">-</a></div>' +
            '</div>'
        ),

        index: 0,
        smallImageFields: [],
        largeImageFields: [],

        init: function (type, options) {
            ArrayInputField.prototype.init.call(this, type, options);
            this.smallImageFields = [];
            this.largeImageFields = [];
        },

        addItem: function (value) {
            var $item = $(this.itemTemplate());

            var smallImageField = new ImageInputField({
                attrs: {
                    name: 'screenshots[' + this.index + '][smallImageUrl]',
                    required: true,
                    maxlength: 2083,
                    placeholder: '600px x 375px',
                    value: (value && value.smallImageUrl) || ''
                }
            }).render();

            var largeImageField = new ImageInputField({
                attrs: {
                    name: 'screenshots[' + this.index + '][largeImageUrl]',
                    maxlength: 2083,
                    placeholder: '960px x 600px',
                    value: (value && value.largeImageUrl) || ''
                }
            }).render();

            $item.find('.small-image-field').append(smallImageField.$el);
            $item.find('.large-image-field').append(largeImageField.$el);

            this.$el.find('.btn-field-add').before($item);

            this.index++;
            this.smallImageFields.push(smallImageField);
            this.largeImageFields.push(largeImageField);

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
            _.invoke(this.smallImageFields, 'remove');
            _.invoke(this.largeImageFields, 'remove');
            return ArrayInputField.prototype.remove.call(this);
        }

    });

    FormFieldTypes.register('screenshots', ResourcesField);

    return ResourcesField;

});
