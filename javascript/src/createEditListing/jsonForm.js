define([
    'handlebars',
    'jquery',
    'underscore',
    'select2',
    'bootstrap-datepicker',
    'jquery.magnific-popup',
    'jquery.validate'
], function (Handlebars, $, _) {

    var oldSerializeArray = $.fn.serializeArray;

    /* jshint ignore:start */

    var r20 = /%20/g,
        rbracket = /\[\]$/,
        rCRLF = /\r?\n/g,
        rsubmitterTypes = /^(?:submit|button|image|reset|file)$/i,
        rsubmittable = /^(?:input|select|textarea|keygen)/i,
        manipulation_rcheckableType = /^(?:checkbox|radio)$/i;

    $.fn.serializeArray = function () {
        var values = this.map(function(){
            // Can add propHook for "elements" to filter or add form elements
            var elements = $.prop( this, "elements" );
            return elements ? $.makeArray( elements ) : this;
        })
        .filter(function(){
            var type = this.type;
            // Use .is(":disabled") so that fieldset[disabled] works
            return this.name && // !jQuery( this ).is( ":disabled" ) &&
                rsubmittable.test( this.nodeName ) && !rsubmitterTypes.test( type );
                // commented out to include unchecked checkboxes while maintaining order
                // && ( this.checked || !manipulation_rcheckableType.test( type ) );
        }).map(function( i, elem ){
            var $el = jQuery( this ),
                val = $el.val();

            var map = val == null ?
                        null :
                        $.isArray( val ) ?
                            $.map( val, function( val ){
                                return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
                            }) :
                            { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };

            if(manipulation_rcheckableType.test(this.type)) {
                map.value = this.checked;
            }
            else if($el.is('input.selectpicker[data-multiple="true"]')) {
                if(map.value === '') {
                    map.value = [];
                }
                else {
                    map.value = map.value.split(',');
                }
            }

            return map;
        }).get();

        // add buttons-checkbox values as array using data-id as values
        var $buttonsCheckbox = this.find('[data-toggle="buttons-checkbox"]');
        _.forEach($buttonsCheckbox, function (buttonCheckbox) {
            var $el = $(buttonCheckbox),
                name = $el.attr('name'),
                $actives = $el.find('.active');

            values.push({
                name: name,
                value: _.map($actives, function (active) {
                    return $(active).attr('data-id');
                })
            });
        });

        // add buttons-radio value
        var $buttonsRadio = this.find('[data-toggle="buttons-radio"]');
        _.forEach($buttonsRadio, function (buttonCheckbox) {
            var $el = $(buttonCheckbox),
                name = $el.attr('name');

            values.push({
                name: name,
                value: $el.find('.active').attr('data-id')
            });
        });

        return values;
    };
    /* jshint ignore:end */

    /*========================================================
    =            jQuery validation customizations            =
    ========================================================*/
    var onkeyup = $.validator.defaults.onkeyup,
        DEBOUNCE_DELAY = $.browser.msie && $.browser.versionNumber < 9 ? 1000 : 400;

    $.extend(true, $.validator, {
        defaults: {
            onkeyup: _.debounce(onkeyup, DEBOUNCE_DELAY),

            errorPlacement: function (error, element) {
                element.parent().append(error);
            },

            highlight: function( element, errorClass, validClass ) {
                if ( element.type === 'radio' ) {
                    this.findByName(element.name).closest('.control-group').addClass(errorClass).removeClass(validClass);
                } else {
                    $(element).closest('.control-group').addClass(errorClass).removeClass(validClass);
                }
            },

            unhighlight: function( element, errorClass, validClass ) {
                var $element = $(element),
                    $controlGroup, $visibleErrors;

                if ( element.type === 'radio' ) {
                    $controlGroup = this.findByName(element.name).closest('.control-group');
                } else {
                    $controlGroup = $(element).closest('.control-group');
                }

                // unlight only if there are no errors visible
                // timeout is required as errors are removed after unlight is called
                setTimeout(function () {
                    $visibleErrors = $controlGroup.find('label.error').filter(':visible');
                    if($visibleErrors.length === 0) {
                        $controlGroup.removeClass(errorClass).addClass(validClass);
                    }
                }, 17);
            }
        },
        prototype: {

            optional: function( element, param ) {
                var value = this.elementValue(element),
                    $element = $(element);

                // check if dependency is met
                if ( !this.depend(param, element) ) {
                    return 'dependency-mismatch';
                }
                if ( element.nodeName.toLowerCase() === 'select' ) {
                    // could be an array for select-multiple or a string, both are fine this way
                    var val = $(element).val();
                    return val && val.length > 0;
                }
                if ( this.checkable(element) ) {
                    return this.getLength(value, element) > 0;
                }
                // enforce validation if attribute required is not found on element
                if($element.attr('required')) {
                    return false;
                }
                else if($element.val().length > 0) {
                    return false;
                }
                return true;
            },

            focusInvalid: function() {
                if ( this.settings.focusInvalid ) {
                    try {
                        var $invalidEl = $(this.findLastActive() || this.errorList.length && this.errorList[0].element || []);
                        // manually focus select2 element
                        if($invalidEl.hasClass('select2-offscreen')) {
                            $invalidEl.select2('focus');
                        }
                        else {
                            $invalidEl
                                .filter(':visible')
                                .focus()
                                // manually trigger focusin event; without it, focusin handler isn't called, findLastActive won't have anything to find
                                .trigger('focusin');
                        }
                    } catch(e) {
                        // ignore IE throwing errors when focusing hidden elements
                    }
                }
            }
        }
    });

    /*==========  Custom Validations  ==========*/

    $.validator.addMethod('url', function(value, element, param) {
        return this.optional(element) || /^(((https|http|ftp|sftp|file):\/)|(\/)){1}(.*)+$/.test(value);
    }, $.validator.messages.url);


    $.validator.addMethod('tel', function(value, element, param) {
        return this.optional(element) || /(^\+\d((([\s.-])?\d+)?)+$)|(^(\(\d{3}\)\s?|^\d{3}[\s.-]?)?\d{3}[\s.-]?\d{4}$)/.test(value);
    }, 'Must be a valid phone number (e.g. 555-5555 or 555-555-5555. For international numbers, start with a +)');







    /*===============================================================================
    =            Service to register and get diffrent form element types            =
    ===============================================================================*/

    var FormFieldTypes =  {
        _types: {},

        register: function (type, config) {
            if(!type) {
                throw 'type property is missing.';
            }
            this._types[type] = config;
        },
        get: function (type) {
            return this._types[type];
        }
    };

    var FieldContainer = function (options) {
        this.init(options);
    };

    _.extend(FieldContainer.prototype, {

        template: Handlebars.compile(
            '<div class="control-group">' +
                '<label class="control-label" for="{{attrs.name}}">{{label}} ' +
                    '{{#if tooltip}}' +
                        '<i class="icon-info" data-title="{{tooltip}}" data-toggle="tooltip" data-placement="right"></i>' +
                    '{{/if}}' +
                '</label>' +
                '<div class="controls"></div>' +
            '</div>'
        ),

        init: function (options) {
            this.options = options;
        },

        render: function () {
            var type, Type;

            Type = FormFieldTypes.get(this.options.type);
            type = new Type(this.options);

            if(this.options.type === 'fieldset') {
                this.$el = type.render().$el;
            } else {
                this.$el = $(this.template(this.options));
                this.$el.find('.controls').append(type.render().$el);
            }

            if(this.options.type === 'hidden') {
                this.$el.hide();
            }

            this.addRequiredStar();

            return this;
        },

        addRequiredStar: function () {
            if (this.options.attrs.required) {
                this.$el.find('.control-label').append('<span class="required-star">*</span>');
            }
        },

        remove: function () {
            this.$el.remove();
            return this;
        }

    });



    /*===============================================
    =            Base ElementType Class             =
    ===============================================*/

    var ElementType = function (options) {
        this.init('text', options);
    };

    _.extend(ElementType.prototype, {

        template: null,

        defaults: null,

        init: function (type, options) {
            this.type = type;
            this.defaults = this.defaults || {};

            this.options = options;
            this.options.attrs = _.extend({}, this.defaults.attrs, options.attrs);

            // if(!this.options.attrs.name) {
            //     console.warn('name attribute missing for ', options);
            // }
        },

        render: $.noop,

        $attrElement: $.noop,

        applyAttrs: $.noop,

        remove: function () {
            this.$el.remove();
            return this;
        }

    });




    /*==================================
    =            Text Input            =
    ==================================*/

    var TextInputField = function (options) {
        this.init('text', options);
    };
    _.extend(TextInputField.prototype, ElementType.prototype, {

        constructor: TextInputField,

        template: Handlebars.compile(
            '<input type="{{type}}" value="{{value}}" />'
        ),

        render: function () {
            var data = _.extend({}, this.options, {
                type: this.type
            });

            this.$el = $(this.template(data));
            this.applyAttrs();

            return this;
        },

        $attrElement: function () {
            return this.$el;
        },

        applyAttrs: function () {
            var $el = this.$attrElement();

            $el.attr(_.omit(this.options.attrs, 'class'));
            $el.addClass(this.options.attrs['class']);
        }

    });
    FormFieldTypes.register('text', TextInputField);




    /*====================================
    =            Hidden Input            =
    ====================================*/

    var HiddenInputField = function (options) {
        this.init('hidden', options);
    };
    _.extend(HiddenInputField.prototype, TextInputField.prototype, {

        constructor: HiddenInputField

    });
    FormFieldTypes.register('hidden', HiddenInputField);




    /*=================================
    =            URL Input            =
    =================================*/

    var UrlInputField = function (options) {
        this.init('url', options);
    };
    _.extend(UrlInputField.prototype, TextInputField.prototype, {

        constructor: UrlInputField,

        template: Handlebars.compile(
            '<input type="{{type}}" value="{{value}}" class="url"/>'
        )

    });
    FormFieldTypes.register('url', UrlInputField);




    /*=======================================
    =            Telephone Input            =
    =======================================*/

    var TelephoneInputField = function (options) {
        this.init('tel', options);
    };
    _.extend(TelephoneInputField.prototype, TextInputField.prototype, {

        constructor: TelephoneInputField

    });
    FormFieldTypes.register('tel', TelephoneInputField);




    /*===================================
    =            Email Input            =
    ===================================*/

    var EmailInputField = function (options) {
        this.init('email', options);
    };
    _.extend(EmailInputField.prototype, TextInputField.prototype, {

        constructor: EmailInputField

    });
    FormFieldTypes.register('email', EmailInputField);




    /*===================================
    =            Image Input            =
    ===================================*/

    var ImageInputField = function (options) {
        this.init('image', options);
    };
    _.extend(ImageInputField.prototype, TextInputField.prototype, {

        constructor: ImageInputField,

        template: Handlebars.compile(
            '<div class="input-append">' +
                '<input type="url" class="url"></input>' +
                '<a class="add-on btn image-preview" href="{{attrs.value}}"><i class="icon-picture"></i></a>' +
            '</div>'
        ),

        render: function () {
            TextInputField.prototype.render.call(this);

            var $input = this.$el.find('input'),
                $imagePreview = this.$el.find('a');

            $imagePreview.attr('href', this.options.attrs.value || '#');

            $input.on(
                'keyup.update-image-preview',
                _.debounce(
                    function () {
                        $imagePreview.attr('href', $input.val());
                    },
                    DEBOUNCE_DELAY
                )
            );

            return this;
        },

        $attrElement: function () {
            return this.$el.find('input');
        },

        remove: function () {
            this.$el.find('input').off('.update-image-preview');
            return TextInputField.prototype.remove.call(this);
        }

    });
    FormFieldTypes.register('image', ImageInputField);




    /*==================================
    =            File Input            =
    ===================================*/

    var FileInputField = function (options) {
        this.init('file', options);
    };
    _.extend(FileInputField.prototype, TextInputField.prototype, {

        constructor: FileInputField,

        template: Handlebars.compile(
            '<div class="fileupload fileupload-new" data-provides="fileupload">' +
                '<div class="uneditable-input">' +
                    '<span class="fileupload-preview">' +
                        '{{#if attrs.value}}' +
                            '{{attrs.value}}' +
                        '{{else}}' +
                            '{{attrs.placeholder}}' +
                        '{{/if}}' +
                    '</span>' +
                '</div>' +
                '<span class="btn btn-file" {{#if attrs.disabled}}disabled{{/if}}>' +
                    '<span class="fileupload-new">Browse</span>' +
                    '<input type="file" name="{{name}}" {{#if attrs.disabled}}disabled{{/if}} />' +
                '</span>' +
            '</div>'
        ),

        $attrElement: function () {
            return this.$el.find('input');
        }

    });
    FormFieldTypes.register('file', FileInputField);





    /*=========================================
    =            Date Picker Input            =
    =========================================*/

    var DatePickerInputField = function (options) {
        this.init('text', options);
    };
    _.extend(DatePickerInputField.prototype, TextInputField.prototype, {

        constructor: DatePickerInputField,

        template: Handlebars.compile(
            '<div class="input-append">' +
                '<input type="text"></input>' +
                '<a class="add-on btn"><i class="icon-calendar"></i></a>' +
            '</div>'
        ),

        render: function () {
            TextInputField.prototype.render.call(this);

            setTimeout(_.bind(function () {
                this.$el.addClass('date datepicker').datepicker({
                    format: 'mm/dd/yyyy'
                });
            }, this), 17);

            return this;
        },

        $attrElement: function () {
            return this.$el.find('input');
        },

        remove: function () {
            this.$el.datepicker('remove');
            return TextInputField.prototype.remove.call(this);
        }

    });
    FormFieldTypes.register('datepicker', DatePickerInputField);





    /*======================================
    =            Checkbox Input            =
    ======================================*/

    var CheckboxInputField = function (options) {
        this.init('checkbox', options);
    };
    _.extend(CheckboxInputField.prototype, TextInputField.prototype, {

        constructor: CheckboxInputField,

        init: function (type, options) {
            TextInputField.prototype.init.call(this, type, options);

            var checked = this.options.attrs.value || false;

            if(/true/i.test(checked.toString())) {
                this.options.attrs.checked = true;
            }
            else {
                this.options.attrs.checked = false;
            }
        },

        render: function () {
            TextInputField.prototype.render.call(this);

            setTimeout(_.bind(function () {
                this.$el.addClass('ios switch').svitch();
            }, this), 17);
            return this;
        }

    });
    FormFieldTypes.register('checkbox', CheckboxInputField);





    /*============================================
    =           Button Checkbox Input            =
    ============================================*/

    var ButtonCheckboxInputField = function (options) {
        this.init('buttons-checkbox', options);
    };
    _.extend(ButtonCheckboxInputField.prototype, TextInputField.prototype, {

        constructor: ButtonCheckboxInputField,

        template: Handlebars.compile(
            '<div class="btn-group" data-toggle="buttons-checkbox"></div>'
        ),

        isActive: function (button) {
            return _.contains(this.options.attrs.value, button.id);
        },

        render: function () {
            var me = this;

            TextInputField.prototype.render.call(me);

            _.forEach(this.options.buttons, function (button) {
                var isActive = me.isActive(button);
                me.$el.append('<button data-id="' + button.id  + '"' + (isActive ? ' class="btn active" ' : ' class="btn" ') + '>' + button.value + '</button>');
            });

            this.$el.on('click.preventDefault', function (e) {
                e.preventDefault();
            });
            return me;
        },

        remove: function () {
            this.$el.off('click.preventDefault');
            return TextInputField.prototype.remove.call(this);
        }

    });
    FormFieldTypes.register('buttons-checkbox', ButtonCheckboxInputField);





    /*===========================================
    =            Buttons Radio Input            =
    ===========================================*/

    var ButtonRadioInputField = function (options) {
        this.init('buttons-radio', options);
    };
    _.extend(ButtonRadioInputField.prototype, ButtonCheckboxInputField.prototype, {

        constructor: ButtonRadioInputField,

        template: Handlebars.compile(
            '<div class="btn-group" data-toggle="buttons-radio"></div>'
        ),

        isActive: function (button) {
            return this.options.attrs.value === button.id;
        }

    });
    FormFieldTypes.register('buttons-radio', ButtonRadioInputField);





    /*====================================
    =            Number Input            =
    ====================================*/

    var NumberInputField = function (options) {
        this.init('number', options);
    };
    _.extend(NumberInputField.prototype, TextInputField.prototype, {

        constructor: NumberInputField

    });
    FormFieldTypes.register('number', NumberInputField);





    /*======================================
    =            Textarea Input            =
    ======================================*/

    var TextareaInputField = function (options) {
        this.init('textarea', options);
    };
    _.extend(TextareaInputField.prototype, TextInputField.prototype, {

        constructor: TextareaInputField,

        template: Handlebars.compile(
            '<textarea name="{{attrs.name}}">{{attrs.value}}</textarea>'
        )

    });
    FormFieldTypes.register('textarea', TextareaInputField);




    /*=====================================
    =            Select2 Input            =
    =====================================*/

    var Select2InputField = function (options) {
        this.init('text', options);
    };
    _.extend(Select2InputField.prototype, TextInputField.prototype, {

        constructor: Select2InputField,

        defaults: {
            attrs: {
                'data-width': '100%',
                'data-search': false,
                'data-multiple': false,
                'data-ajax': ''
            }
        },

        findMultiSelections: function (ids, response) {
            var data = response.data || response.rows || response.records || response;
            var matches = [];

            _.forEach(data, function (obj) {
                if(obj.id && _.contains(ids, '' + obj.id)) {
                    matches.push(obj);
                }
            });

            return matches;
        },

        multiInitSelection: function (element, callback) {
            var me = this,
                ajaxUrl = this.options.attrs['data-ajax'],
                ids = $(element).val().split(',');

            me.options.data ?
                callback(me.findMultiSelections(ids, me.options.data)) :
                $.ajax({
                    url: ajaxUrl,
                    dataType: 'json'
                }).done(function (response) {
                    callback(me.findMultiSelections(ids, response));
                });
        },

        findSingleSelection: function (val, response) {
            var data = response.data || response.rows || response.records || response;
            var match = _.where(data, function (a) {
                return a.id.toString() === val.toString();
            });

            return match[0];
        },

        singleInitSelection: function (element, callback) {
            var me = this,
                ajaxUrl = this.options.attrs['data-ajax'],
                val = $(element).val();

            me.options.data ?
                callback(me.findSingleSelection(val, me.options.data)) :
                $.ajax({
                    url: ajaxUrl,
                    dataType: 'json'
                }).done(function (response) {
                    callback(me.findSingleSelection(val, response));
                });
        },

        format: function (obj) {
            return _.escape(obj.title || obj.display || obj.displayName || obj.name || obj);
        },

        getRenderOptions: function () {
            var data = this.$el.data(),
                ajaxUrl = data.ajax,
                options = {
                    placeholder: this.options.attrs.placeholder || 'Select a value',
                    width: '100%',
                    closeOnSelect: true
                };

            data.multiple && (options.multiple = data.multiple);

            if(data.search === false) {
                options.minimumResultsForSearch = -1;
            }
            if(ajaxUrl) {
                options.minimumInputLength = 0;
                options.ajax = {
                    url: ajaxUrl,
                    dataType: 'json',
                    data: function (term, page) {
                        if(data.search !== false) {
                            return {
                                query: term,
                                offset: 0,
                                max: 50
                            };
                        }
                    },
                    results: function (response) {
                        return {results: response.data || response.rows || response.records || response};
                    }
                };

            }
            else {
                options.data = {
                    results: this.options.data,
                    text: function (obj) {
                        return obj.title || obj.display || obj.displayName || obj.name || obj;
                    }
                };
            }

            options.formatSelection = this.format;
            options.formatResult = function (result, label, query, escapeMarkup) {
                var tooltip = result.description || result.tooltip;
                if(tooltip) {
                    label.attr('title', tooltip);
                    label.attr('data-toggle', 'tooltip');
                    label.attr('data-placement', 'left');
                }
                return options.formatSelection(result, label, query, escapeMarkup);
            };

            options.initSelection = data.multiple ? _.bind(this.multiInitSelection, this) : _.bind(this.singleInitSelection, this);
            options.closeOnSelect = data.multiple ? false : true;
            options.allowClear = data.allowclear || false;

            // TODO: remove when we fully switch to bootstrap
            options.dropdownCssClass = 'bootstrap-active';

            return options;
        },

        render: function () {
            TextInputField.prototype.render.call(this);
            this.$el.addClass('selectpicker');
            this.renderSelect2(this.getRenderOptions());
            return this;
        },

        renderSelect2: function (options) {
            this.select2Timeout = setTimeout(_.bind(function () {
                var $el = this.$el,
                    $tooltipEls;

                $el
                    .select2(options)
                    // validate on blur as jQuery validation doesn't listen to select2 blur/focusout events
                    .on('select2-blur', function () {
                        $(this).valid();
                    })
                    .on('select2-loaded', function () {
                        var $dropdown = $el.data('select2').dropdown;

                        $tooltipEls = $dropdown.find('[data-toggle="tooltip"]');

                        if($tooltipEls.length) {
                            $tooltipEls.tooltip({
                                container: $dropdown
                            });
                        }
                    })
                    .on('select2-closing', function () {
                        if($tooltipEls && $tooltipEls.length) {
                            $tooltipEls.tooltip('destroy');
                        }
                        $tooltipEls = null;
                    });
            }, this), DEBOUNCE_DELAY);
        },

        remove: function () {
            clearTimeout(this.select2Timeout);
            this.$el
                    .off('select2-blur, select2-loaded, select2-closing')
                    .select2('close')
                    .select2('destroy');

            return TextInputField.prototype.remove.call(this);
        }

    });
    FormFieldTypes.register('select2', Select2InputField);




    /*======================================
    =            Fieldset Input            =
    ======================================*/

    var FieldSet = function (options) {
        this.init('fieldset', options);
    };
    _.extend(FieldSet.prototype, TextInputField.prototype, {

        constructor: FieldSet,

        template: Handlebars.compile('<fieldset><h4>{{label}}</h4></fieldset>'),

        render: function () {
            var data = _.extend({}, this.defaults, this.options, {
                type: this.type
            });

            this.$el = $(this.template(data));
            this.$el.attr(data.attrs);

            renderFields.call(this, this.$el, data.fields);
            return this;
        },

        remove: function () {
            _.invoke(this.fields, 'remove');
            return TextInputField.prototype.remove.call(this);
        }

    });
    FormFieldTypes.register('fieldset', FieldSet);





    /*===================================
    =            Array Input            =
    ===================================*/

    var ArrayInputField = function (options) {
        this.init('array', options);
    };
    _.extend(ArrayInputField.prototype, TextInputField.prototype, {

        constructor: ArrayInputField,

        template: Handlebars.compile(
            '<div class="array-items">' +
                '<div class="btn-field-add"><a href="#" class="btn btn-small">+</a><span>Add</span></div>' +
            '</div>'
        ),

        itemTemplate: Handlebars.compile(
            '<div class="array-item">' +
                '<input type="text" name="{{name}}" value="{{value}}" />' +
                '<div class="btn-field-remove"><a href="#" class="btn btn-small">-</a></div>' +
            '</div>'
        ),

        addItem: function (value) {
            var templateOptions, $item;

            templateOptions = _.extend({}, this.options.attrs, {
                value: value || ''
            });
            $item = $(this.itemTemplate(templateOptions));

            $item.find('input').attr(_.omit(templateOptions, 'name', 'value'));
            this.$el.find('.btn-field-add').before($item);
        },

        addItems: function () {
            var me = this,
                value = this.options.attrs.value;

            value && (value = [].concat(value));

            _.forEach(value, function (item) {
                me.addItem(item);
            });
        },

        onAddClick: function () {
            this.addItem();
        },

        removeItem: function (evt) {
            evt.preventDefault();
            $(evt.currentTarget).closest('.array-item').remove();
        },

        render: function () {
            var me = this;

            TextInputField.prototype.render.call(this);

            this.$el.on('click.add', '.btn-field-add', function (evt) {
                evt.preventDefault();
                me.onAddClick();
            });

            this.addItems();

            this.$el.on('click.removeItem', '.array-item > .btn-field-remove', _.bind(this.removeItem, this));

            return this;
        },

        // attrs are applied to children, .array-item
        applyAttrs: $.noop,

        remove: function () {
            this.$el.off('.add, .removeItem');
            return TextInputField.prototype.remove.call(this);
        }

    });

    FormFieldTypes.register('array', ArrayInputField);



    /*===============================
    =            Helpers            =
    ===============================*/

    function renderFields ($el, fields) {
        var me = this;
        me.fields = me.fields || [];

        _.each(fields, function (options) {
            var field = renderField(options);
            me.fields.push(field);
            $el.append(field.$el);
        });
    }

    function renderField (field) {
        return (new FieldContainer(field)).render();
    }



    /*===============================
    =            exports            =
    ===============================*/

    function applyValues (src, values) {
        _.forEach(src, function (value, key) {
            var attrs;
            if(key === 'attrs') {
                attrs = value;
                if(attrs.name && values[attrs.name]) {
                    attrs.value = values[attrs.name];
                }
            }
            else if(key === 'fields') {
                _.forEach(value, function (subfield) {
                    applyValues(subfield, values);
                });
            }
        });
    }

    function JsonForm ($el, options) {
        this.fields = [];

        this.options = options;

        this.$el = $el;
    }

    _.extend(JsonForm.prototype, {

        render: function () {
            renderFields.call(this, this.$el, this.options);

            this.$el.magnificPopup({
                delegate: '.image-preview',
                type: 'image',
                gallery: {
                    enabled: true
                }
            });

            return this;
        },

        add: function (options, index) {
            var field = renderField(options);

            if(index === 0) {
                this.$el.prepend(field.$el);
                this.fields.splice(0, 0, field);
            }
            else if(index > 0) {
                this.$el.children(':nth-child(' + index + ')').after(field.$el);
                this.fields.splice(index, 0, field);
            }
            else {
                this.$el.append(field.$el);
                this.fields.push(field);
            }

            return field;
        },

        update: function (field, options) {
            // calling serializeArray on entire form here, calling it on field param doesn't work in IE due to http://bugs.jquery.com/ticket/2114
            var fieldValues = _.reduce(this.$el.serializeArray(), function (memo, field) {
                memo[field.name] = field.value;
                return memo;
            }, {});

            applyValues(options, fieldValues);

            var index = _.indexOf(this.fields, field);

            this.remove(field);

            return this.add(options, index);
        },

        remove: function (field) {
            if(!field) { return; }
            field.remove();
            this.fields = _.without(this.fields, field);
        }

    });

    return {
        FormFieldTypes: FormFieldTypes,
        Form: JsonForm
    };

});
