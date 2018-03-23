define(
[
    '../../AppData',
    '../../views/tabbable/TabPaneView',
    'marketplace',
    './Field',
    './Intents',
    './Required',
    'backbone',
    'handlebars',
    'jquery',
    'moment',
    'underscore',
    'underscore.string',
    'jquery.magnific-popup'
],
function(AppData, TabPaneView, Marketplace, Field, Intents, Required, Backbone, Handlebars, $, Moment, _, _s) {

    //Default config list
    //types, categories, domain, state, releasedDate, lastActivity, owners, Government Sponsor, Sponsor Contact, organization, Alternate POC Info, Technical POC Info, Support POC Info, launchUrl, scoreCard, requirements, dependencies, resources, guid

    var SuperClass = TabPaneView;

    var customFieldsCollectionFetchPromise;

    //Object to hold the ordering information for the "Additional Specifications" section
    //In order for a new element to show on the screen it must be configured in either app config or here and also be in the Field.js file which actually adds the data based on these configs
    function AdditionalSpecificationsConfiguration (configuredFields) {
        var me = this;
        var removeExisting = function (fields) {
            return _.filter(fields, function(field) {
                return !_.contains(configuredFields, field);
            });
        };

        this.primaryCharacteristicsFields = removeExisting(['title', 'types', 'state', 'agency', 'version', 'categories', 'releasedDate', 'lastActivity']);
        this.technicalProperties = removeExisting(['installationURL', 'resources', 'screenShots']);
        this.owfPropertiesFields = removeExisting(['singleton', 'visibleInLaunch', 'mobileReady', 'background', 'recommendedLayouts', 'size', 'universalName']);
        this.appComponentFields =  removeExisting(['largeIcon', 'mediumIcon', 'smallIcon','launchUrl']);
        this.storeReferences = removeExisting(['guid', 'owners', 'organization', 'requirements', 'dependencies', 'scoreCard']);

        this.addCustomFields = function (customFields) {
            _.each(customFields, function (cf) {
                var section = cf.section,
                    field = cf.name;

                if(!section || _.contains(configuredFields, field)) {
                    return;
                }

                if(section === 'primaryCharacteristics') {
                    me.primaryCharacteristicsFields.push(field);
                } else if(section === 'technicalProperties') {
                    me.technicalProperties.push(field);
                } else if(section === 'typeProperties') {
                    me.appComponentFields.push(field);
                } else if(section === 'marketplaceReferences') {
                    me.storeReferences.push(field);
                }
            });
        };

        this.addContactTypes = function (contactTypes) {
            _.each(contactTypes, function (contactType) {
                if(_.contains(configuredFields, contactType)) {
                    return;
                }

                me.storeReferences.push(contactType);
            });
        };
    }

    return SuperClass.extend({

        // the tab's title
        title: 'Details',

        // this.el's CSS class
        className: 'quickview-details',

        sectionHeaderTemplate: Handlebars.compile(
            '<div class="row" id="{{id}}">' +
                '<div class="specifications span8 {{cls}}">'+
                    '<h4 class="specifications-title">{{title}}</h4>' +
                    '<div class="{{leftColumnClass}}"></div>' +
                '</div>' +
                '{{#if rightColumnClass}}' +
                    '<div class="{{rightColumnClass}} span4"></div>' +
                '{{/if}}' +
            '</div>'
        ),

        subSectionHeaderTpl: Handlebars.compile(
            '<h5 class="specifications-title collapsible" data-toggle="collapse" data-target="#{{target}}"><span class="arrow"></span>{{title}}</h5>'
        ),

        subSectionBodyTpl:  Handlebars.compile(
            '<div id="{{id}}" class="sub-section-body collapse in"></div>'
        ),
        // the service item
        // which this QuickView displays
        serviceItemModel: null,

        initialize: function() {
            customFieldsCollectionFetchPromise = customFieldsCollectionFetchPromise ||
                AppData.CustomFields.fetchIfEmpty();

            SuperClass.prototype.initialize.apply(this, arguments);
        },

        /**
         * Renders the tab.
         * @returns {Overview} this for chaining purposes
         */
        render: function() {
            var fields = _.findWhere(Marketplace.appconfigs, {code: 'store.quick.view.detail.fields'});

            var configurableFields = _.map(fields.value.split(','), function(item) {
                return _s.trim(item);
            });

            this.renderSpecificationsSection(configurableFields);
            this.renderRequiredSection();

            if(this.isOwnerOrAdmin === true && !this.serviceItemModel.get('isAffiliated')) {
                this.renderAdditionalSpecifications(configurableFields);
            }

            this.renderIntents();

            this.enableGallery();

            return this;
        },

        //This renders the main specifications section based on the field configuration list comming from app config
        renderSpecificationsSection: function(configurableFields) {
            var me = this;

            this.$el.append(this.sectionHeaderTemplate({title: 'Specifications', leftColumnClass: 'specifications-body', rightColumnClass: 'required-items'}));
            var section = this.getLeftColumnSection();
            _.each(configurableFields, function(field) {
                var specification = new Field({model : me.serviceItemModel, field : field});
                section.append(specification.el);
            });
            if(configurableFields.length === 1) {
                this.$el.find('.specifications-title').first().hide();
            }
        },

        //This renders the required items section next too the main specifications
        renderRequiredSection: function() {
            var requiresItemSection = this.$el.find('.required-items');
            var required = new Required({
                serviceItemModel : this.serviceItemModel,
                context: this.context
            });
            requiresItemSection.append(required.el);
        },

         //This renders the 'intents' data table.
        renderIntents: function() {
            var intentsView = new Intents({model :  this.serviceItemModel});
            if(intentsView.$el.find('.intents').length > 0) {
                this.getLeftColumnSection().append(this.sectionHeaderTemplate({title: 'Intents'})).append(intentsView.el);
            }
        },

        //Renders the "Additional Specifications" section.
        //This section is dynamic in that if the field is not displayed in the "configurable fields" (app config) then
        // it falls through to here and is rendered based on the AdditionalSpecificationsConfiguration object.
        //The AdditionalSpecificationsConfiguration object was created to encapsulate the field order for each sub section.
        renderAdditionalSpecifications: function(configurableFields) {
            var me = this;

            customFieldsCollectionFetchPromise.done(function() {
                me.getLeftColumnSection().append(me.sectionHeaderTemplate({
                    title: 'Additional Specifications',
                    id: 'additionalSpecificationsHeader',
                    cls:'specifications-additional'
                }));

                me.$('#additionalSpecificationsHeader').hide();

                var addSectionDetails = function(title, fieldList, cls) {
                    if(fieldList.length === 0) {
                        return;
                    }
                    var target = cls + '-subsection';
                    var sectionHeader = $(me.subSectionHeaderTpl({title: title, target: target}));
                    var subSectionBody = $(me.subSectionBodyTpl({id : target}));
                    _.each(fieldList, function(field) {
                        var specification = new Field({model : me.serviceItemModel, field : field});
                        specification.$el.addClass(cls);
                        subSectionBody.append(specification.$el);
                    });
                    me.getLeftColumnSection().append(sectionHeader).append(subSectionBody);
                };

                var additionalSpecificationsConfig = new AdditionalSpecificationsConfiguration(configurableFields),
                    contactTypes = _.chain(me.serviceItemModel.get('contacts'))
                                       .map(function (contact) { return contact.type.title; }).uniq().value();

                var customFieldDefinitions = AppData.CustomFields.map(function(cf) {
                    return {
                        name: cf.get('name'),
                        section: cf.get('section')
                    };
                });

                additionalSpecificationsConfig.addCustomFields(customFieldDefinitions);
                additionalSpecificationsConfig.addContactTypes(contactTypes);

                addSectionDetails('Primary Characteristics', additionalSpecificationsConfig.primaryCharacteristicsFields, 'primary-characteristics-specifications');
                addSectionDetails('Technical Properties', additionalSpecificationsConfig.technicalProperties, 'technical-properties-specifications');

                var owfProps = me.serviceItemModel.get('owfProperties');
                if(owfProps !== undefined && owfProps !== null) {
                    addSectionDetails('OWF Properties', additionalSpecificationsConfig.owfPropertiesFields, 'owf-properties-specifications');
                }

                var typeName = me.serviceItemModel.get('types').title + ' Properties';
                addSectionDetails(typeName, additionalSpecificationsConfig.appComponentFields, 'app-component-specifications');
                addSectionDetails('Store References', additionalSpecificationsConfig.storeReferences, 'store-references-specifications');


                //If any of the three sub sections exists then show the header.  This could be its own view so that it could be removed instead of toggled
                if(me.$('.primary-characteristics-specifications, .technical-properties-specifications, .owf-properties-specifications, .app-component-specifications, .store-references-specifications').length > 0) {
                    me.$('#additionalSpecificationsHeader').show();
                }
            });
        },

        /**
         * Gets the tab's href.
         * @returns {String} the tab's href
         */
        href: function () {
            return 'quickview-' + this.title.toLowerCase();
        },

        /**
         * Destroys the tab. Calls the super class.
         * @returns {Overview} this for chaining purposes
         */
        remove: function() {
            if (this.$requiredItemIcons && this.$requiredItemIcons.length > 0) {
                this.$requiredItemIcons.off('.icon');
            }

            return SuperClass.prototype.remove.call(this);
        },

        getLeftColumnSection: function() {
            if(this.leftColumn === undefined) {
                this.leftColumn = this.$el.find('.specifications-body');
            }
            return this.leftColumn;
        },

        enableGallery: function () {
            // if gallery/preview button is found
            if(this.$el.find('.icon-picture').length > 0) {
                this.$el.magnificPopup({
                    delegate: '.icon-picture', // the selector for gallery item
                    type: 'image',
                    gallery: {
                        enabled: true
                    }
                });
            }
        }

    });
});
