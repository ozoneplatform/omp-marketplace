define([
    '../jsonForm',
    'jquery',
    'underscore',
    'handlebars',
    'marketplace'
], function (JsonForm, $, _, Handlebars, Marketplace) {

    var FormFieldTypes = JsonForm.FormFieldTypes,
        ArrayInputField = FormFieldTypes.get('array'),
        TextInputField = FormFieldTypes.get('text'),
        EmailInputField = FormFieldTypes.get('email'),
        TelephoneInputField = FormFieldTypes.get('tel'),
        Select2InputField = FormFieldTypes.get('select2'),
        HiddenInputField = FormFieldTypes.get('hidden'),
        contactTypes = Marketplace.contactTypes;


    var ContactsInputField = function (options) {
        this.init('intents', options);
    };

    _.extend(ContactsInputField.prototype, ArrayInputField.prototype, {

        constructor: ContactsInputField,

        template: Handlebars.compile(
            '<div class="array-items contacts">' +
                '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>' +
            '</div>'
        ),

        itemTemplate: Handlebars.compile(
            '<div class="array-item">' +
                '<div class="row">' +
                    '<div class="span type-field"><label>Type<span class="required-star">*</span></label></div>' +
                    '<div class="span name-field"><label>Name<span class="required-star">*</span></label></div>' +
                    '<div class="span organization-field"><label>Organization</label></div>' +
                '</div>' +
                '<div class="row">' +
                    '<div class="span email-field"><label>Email<span class="required-star">*</span></label></div>' +
                    '<div class="span secure-phone-field"><label>Secure Phone<span class="required-star">*</span></label></div>' +
                    '<div class="span unsecure-phone-field"><label>Unsecure Phone<span class="required-star">*</span></label></div>' +
                '</div>' +
                '{{#unless disabled}}' +
                    '<div class="btn-field-remove"><a href="#" class="btn btn-small">-</a></div>' +
                '{{/unless}}' +
            '</div>'
        ),

        index: 0,

        idFields: null,
        typeFields: null,
        nameFields: null,
        organizationFields: null,
        emailFields: null,
        securePhoneFields: null,
        unsecurePhoneFields: null,

        init: function (type, options) {
            ArrayInputField.prototype.init.call(this, type, options);
            this.idFields = [];
            this.typeFields = [];
            this.nameFields = [];
            this.organizationFields = [];
            this.emailFields = [];
            this.securePhoneFields = [];
            this.unsecurePhoneFields = [];
        },

        addItem: function (contact, contacts, disabled) {
            var me = this,
                $item = $(this.itemTemplate(_.extend({
                    disabled: disabled
                }, contact))),
                index = this.index,
                typeField, nameField, organizationField, emailField, securePhoneField, unsecurePhoneField, idField;

            nameField = new TextInputField({
                attrs: {
                    name: 'contacts[' + index + '][name]',
                    required: true,
                    maxlength: 100,
                    value: contact.name || ''
                }
            }).render();

            organizationField = new TextInputField({
                attrs: {
                    name: 'contacts[' + index + '][organization]',
                    maxlength: 100,
                    value: contact.organization || ''
                }
            }).render();

            emailField = new EmailInputField({
                attrs: {
                    name: 'contacts[' + index + '][email]',
                    required: true,
                    maxlength: 100,
                    value: contact.email || ''
                }
            }).render();

            securePhoneField = new TelephoneInputField({
                attrs: {
                    name: 'contacts[' + index + '][securePhone]',
                    required: contact.unsecurePhone ? false : true,
                    maxlength: 50,
                    value: contact.securePhone || ''
                }
            }).render();

            unsecurePhoneField = new TelephoneInputField({
                attrs: {
                    name: 'contacts[' + index + '][unsecurePhone]',
                    required: contact.securePhone ? false : true,
                    maxlength: 50,
                    value: contact.unsecurePhone || ''
                }
            }).render();

            typeField = new Select2InputField({
                data: contactTypes,
                attrs: {
                    name: 'contacts[' + index + '][type][id]',
                    required: true,
                    value: contact.type ? contact.type.id : '',
                    disabled: disabled
                }
            }).render();


            if(contact && contact.id) {
                idField = new HiddenInputField({
                    attrs: {
                        name: 'contacts[' + index + '][id]',
                        value: contact && contact.attrs || ''
                    }
                }).render();
            }

            $item.find('.type-field').append(typeField.$el);
            $item.find('.name-field').append(nameField.$el);
            $item.find('.organization-field').append(organizationField.$el);
            $item.find('.email-field').append(emailField.$el);
            $item.find('.secure-phone-field').append(securePhoneField.$el);
            $item.find('.unsecure-phone-field').append(unsecurePhoneField.$el);
            idField && $item.append(idField.$el);

            this.$el.find('.btn-field-add').before($item);

            this.index++;
            this.typeFields.push(typeField);
            this.nameFields.push(nameField);
            this.organizationFields.push(organizationField);
            this.emailFields.push(emailField);
            this.securePhoneFields.push(securePhoneField);
            this.unsecurePhoneFields.push(unsecurePhoneField);
            idField && this.idFields.push(idField);
        },

        addItems: function () {
            var me = this,
                contacts = this.options.attrs.value || [],
                requiredContactTypes = _.where(contactTypes, {required: true}),
                typesRendered = {};

            if(contacts.length === 0) {
                _.forEach(requiredContactTypes, function (ct) {
                    // no values exist, disable all required types
                    me.addItem({
                        type: {
                            id: ct.id,
                            title: ct.title,
                            required: ct.required
                        }
                    }, contacts, true);
                });
            }
            else {
                _.forEach(contacts, function (contact, i) {
                    var type = contact.type;
                    // disable this contact's type from changing if contact type is required and
                    // at least one of contact of that type is not yet renderd
                    me.addItem(contact, contacts, type.required && !typesRendered[type.id]);

                    // update the flag to keep track of which types have rendered
                    typesRendered[type.id] = true;
                });
            }
        },

        onAddClick: function () {
            this.addItem({});
        },

        remove: function () {
            _.invoke(this.idFields, 'remove');
            _.invoke(this.nameFields, 'remove');
            _.invoke(this.organizationFields, 'remove');
            _.invoke(this.emailFields, 'remove');
            _.invoke(this.securePhoneFields, 'remove');
            _.invoke(this.unsecurePhoneFields, 'remove');
            return ArrayInputField.prototype.remove.call(this);
        }

    });

    $(document).on('keyup', '.array-items.contacts input[name*="securePhone"]', _.debounce(function (evt) {
        var input = this,
            $input = $(input),
            value = $input.val(),
            isUnsecurePhone = _.contains($input.attr('name'), 'unsecurePhone'),
            $sibling = $input.parent().siblings(isUnsecurePhone ? '.secure-phone-field' : '.unsecure-phone-field').find('input'),
            siblingValue = $sibling.val();

        if(value || siblingValue) {
            $sibling.removeAttr('required').valid();
        }
        else {
            $input.attr('required', true).valid();
            $sibling.attr('required', true).valid();
        }
    }, 200));

    FormFieldTypes.register('contacts', ContactsInputField);

    return ContactsInputField;

});