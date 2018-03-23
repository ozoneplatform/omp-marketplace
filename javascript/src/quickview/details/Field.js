define(
[
    '../../views/BaseView',
    '../../AppData',
    'marketplace',
    'backbone',
    'handlebars',
    'jquery',
    'underscore',
    'moment'
],
function(BaseView, AppData, Marketplace, Backbone, Handlebars, $, _, moment) {

    var SuperClass = BaseView;

    // NOTE: This function intentionally returns false for falsy booleans
    var isUndefinedOrEmptyString = function(value) {
        return _.isNull(value) || _.isUndefined(value) || value === '';
    };

    var customFieldsCollectionFetchPromise;

    return SuperClass.extend({

        escapeHtml:  Handlebars.Utils.escapeExpression,

        className: 'specifications-content row-fluid',

        tpl: Handlebars.compile(
            '<div class="specifications-key specifications-label span4">' +
                '<p>{{label}}</p>' +
            '</div>' +
            '<div class="specifications-value span8">' +
                '{{#unless isEmail}}' +
                    '<p>{{{value}}}</p>' +
                '{{/unless}}' +
                '{{#if isEmail}}' +
                    '<a href="mailto:{{{value}}}">{{{value}}}</a>' +
                '{{/if}}' +
            '</div>'
        ),

        imageTpl: Handlebars.compile(
            '<div class="listing-icon">' +
                '<div class="image-icon-url" style="display:inline">{{url}}</div>' +
                '<a href="{{url}}" class="icon-picture"></a>' +
            '</div>'
        ),

        screenshotTpl: Handlebars.compile(
            '<p class="screenshot">' +
                '<span>Small</span>' +
                '{{smallImageUrl}}' +
                '<a href="{{smallImageUrl}}" class="icon-picture"></a>' +
            '</p>' +
            '<p class="screenshot">' +
                '<span>Large</span>' +
                '<span>{{largeImageUrl}}</span>' +
                '<a href="{{largeImageUrl}}" class="icon-picture"></a>' +
            '</p><br>'
        ),

        contactTpl: Handlebars.compile(
            '<p class="contact">{{name}}{{#if organization}}, {{organization}}{{/if}}' +
                '<a href="mailto:{{email}}">, {{email}}</a>' +
                '{{#if unsecurePhone}}, {{unsecurePhone}} (unsecure){{/if}}' +
                '{{#if securePhone}}, {{securePhone}} (secure){{/if}}</p>'
        ),

        scoreCardTpl: Handlebars.compile(
            '{{#if showOnListing}}{{#if image}}<img class="scorecard-icon-details-small" src="{{image}}" title="{{question}}" />{{/if}}{{/if}}'
        ),

        customFieldDefinitions: null,

        initialize: function(options) {
            //fetch custom fields if they haven't already been fetched
            customFieldsCollectionFetchPromise = customFieldsCollectionFetchPromise ||
                AppData.CustomFields.fetchIfEmpty();

            BaseView.prototype.initialize.call(this, options);

            this.scoreCardEnabled = _.findWhere(Marketplace.appconfigs, {code: 'store.enable.scoreCard'});
            this.render();
        },

        render: function() {
            var me = this;

            customFieldsCollectionFetchPromise.done(function() {
                me.customFieldDefinitions = AppData.CustomFields.map(function(cf) {
                    return {
                        name: cf.get('name'),
                        label: cf.get('label'),
                        fieldType: cf.get('fieldType')
                    };
                });

                var configs = me.getFieldConfig();

                if(configs) {
                    me.$el.append(me.tpl(configs));
                }

            });

            return me;
        },


        /**
         * Get the data needed to render the details Handlebars template.
         * @returns {Object} the data needed to render the details Handlebars template
         */
        getFieldConfig: function() {
            var me = this,
                model = this.model,
                context = model.context,
                data = model.toJSON(),
                escapeHtml = this.escapeHtml,
                name, html, url,
                contactsByType = _.filter(data.contacts, function (contact) {
                    return contact.type && contact.type.title === me.field;
                });

            function makeOwnerLink(owner) {
                var name = me.escapeHtml(owner.name);
                var url = me.escapeHtml('#profile/' + owner.id);
                var html = '<a href="' + url + '">' + name + '</a>';
                return html;
            }

            function makeOwnerNoLink(owner) {
                var name = me.escapeHtml(owner.name);
                return name;
            }

            //Custom fields are looked up in the system wide list of custom fields
            var customFieldDefinition = _.findWhere(this.customFieldDefinitions, { name: this.field });
            if(customFieldDefinition) {
                return this.addCustomField(customFieldDefinition);
            }

            if(contactsByType.length > 0) {
                return { label: this.field, value: _.map(contactsByType, function (contact) {
                    return me.contactTpl(contact);
                }).join('')};
            }

            //Primary Fields
            if(this.field === 'title') {
                return this.addTextField('Name', data.title);
            }

            if(this.field === 'types') {
                var typeTitle = data.types.title;
                if(!model.isOzoneAware() && data.opensInNewBrowserTab) {
                    typeTitle += ' (opens in a new browser tab)';
                }
                return this.addTextField('Type', typeTitle);
            }

            if(this.field === 'categories') {
                return this.addTextField('Categories', _.pluck(data.categories, 'title').join(', '));
            }

            if(this.field === 'state') {
                return this.addTextField('State', data.approvalStatus + '/' + data.state.title);
            }

            if(this.field === 'agency') {
                return this.addTextField('Company', data.agency ? data.agency.title : '');
            }

            if(this.field === 'version') {
                return this.addTextField('Version', data.versionName);
            }

            if(this.field === 'description') {
                return this.addTextField('Description', data.description);
            }

            if(this.field === 'releasedDate') {
                return this.addDateField('Released', data.releasedDate);
            }

            if(this.field === 'lastActivity') {
                return this.addDateField('Updated', data.lastActivity.activityTimestamp);
            }

            if(this.field === 'guid') {
                return this.addTextField('GUID', data.uuid);
            }

            //Tech Properties
            if(this.field === 'installationURL') {
                return this.addURLField('Installation URL', data.installUrl);
            }

            if (this.field === 'resources') {
                return this.addHtmlField(
                    'Resources',
                    data.docUrls && data.docUrls.length === 0 ?
                        null :
                        _.map(data.docUrls, function(docUrl) {
                            name = escapeHtml(docUrl.name);
                            url = escapeHtml(docUrl.url);
                            return '<a href="' + url + '">' + name + '</a>';
                        }).join('<br>')
                );
            }

            if(this.field === 'screenShots') {
                return this.addHtmlField('Screenshots', _.map(data.screenshots, function (screenshot) {
                    return me.screenshotTpl(screenshot);
                }).join(''));
            }


            //Owf Properties
            if(this.field === 'singleton' && data.owfProperties) {
                return this.addBooleanField('Singleton', data.owfProperties.singleton);
            }

            if(this.field === 'visibleInLaunch' && data.owfProperties) {
                return this.addBooleanField('Visible', data.owfProperties.visibleInLaunch);
            }

            if(this.field === 'background' && data.owfProperties) {
                return this.addBooleanField('Run in Background', data.owfProperties.background);
            }

            if(this.field === 'mobileReady' && data.owfProperties) {
                return this.addBooleanField('Mobile Ready', data.owfProperties.mobileReady);
            }

            if(this.field === 'recommendedLayouts') {
                return this.addTextField('Recommended Layouts', data.recommendedLayouts.length > 0 ? data.recommendedLayouts.join(', ') : null);
            }

            if(this.field === 'size' && data.owfProperties) {
                return this.addTextField('Approximate Size', data.owfProperties.height + ' x ' + data.owfProperties.width);
            }

            if(this.field === 'universalName'  && data.owfProperties) {
                return this.addTextField('Universal Name', data.owfProperties.universalName);
            }

            //App Components
            if(this.field === 'largeIcon') {
                return this.addImageField('Large Icon',  data.imageLargeUrl);
            }

            if(this.field === 'mediumIcon') {
                return this.addImageField('Medium Icon',  data.imageMediumUrl);
            }

            if(this.field === 'smallIcon') {
                return this.addImageField('Small Icon',  data.imageSmallUrl);
            }

            if(this.field === 'launchUrl' && !this.model.isStack()) {
                return this.addURLField('Launch URL', data.launchUrl);
            }

            //Store References
            if(this.field === 'owners') {
                return this.addHtmlField('Owners',
                    _.map(data.owners, this.model.get('isAffiliated') ?
                        makeOwnerNoLink : makeOwnerLink).join(', '));
            }

            if(this.field === 'techPocs') {
                return this.addTextField('Technical POC',  data.techPocs);
            }

            if (this.field === 'requirements') {
                return this.addTextAreaField('Requirements', data.requirements);
            }

            if (this.field === 'dependencies') {
                return this.addTextAreaField('Dependencies',  data.dependencies);
            }

            //Other
            if(this.field === 'organization') {
                return this.addTextField('Organization', data.organization);
            }

            if(this.field === 'scoreCard' && this.scoreCardEnabled.value === 'true') {
                html = '';
                _.each(this.model.scorecards(), function(scorecard) {
                    html += (me.scoreCardTpl(scorecard));
                });
                return this.addHtmlField('Compliance', html);


                //return this.addHtmlField('Compliance', this.scoreCardTpl(this.model.scorecards()));
            }

            // TODO: fix this by creating a service to avoid creating views if not required.
            // hide empty view to remove gaps.
            this.$el.hide();

            return null;
        },

        addContactField: function(contacts) {
            var me = this;

            return _.map(contacts, function(contact) {
                return me.contactTpl(contact);
            }).join();
        },

        addTextField: function(label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }
            var isEmail = _.isString(value) ? value.indexOf('@') !== -1 : false;
            return {label: label, value: this.escapeHtml(value), isEmail : isEmail };
        },

        addDefaultField: function (label) {
            var value = '<div class="specifications-label specifications-key"><p><i>None</i></p></div>';
            return {label: label, value: value, isEmail : false };
        },

        addCustomField: function(customFieldDefinition) {
            var customFields = this.model.get('customFields');
            var customField = _.findWhere(customFields, { name: customFieldDefinition.name });
            if(!customField || customField.value === null || customField.value === '') {
                return this.addDefaultField(customFieldDefinition.label);
            }
            if(customField.fieldType === 'IMAGE_URL') {
                return this.addImageField(customField.label, customField.value);
            }
            if(customField.fieldType === 'TEXT_AREA') {
                return this.addTextAreaField(customField.label, customField.value);
            }
            if(_.isBoolean(customField.value)) {
                return this.addBooleanField(customField.label, customField.value);
            }
            if(customField.fieldType === 'DROP_DOWN') {
                var fieldValue = [].concat(customField.fieldValue);
                return this.addTextField(customField.label, fieldValue.length ? _.pluck(fieldValue, 'value').join(', ') : null);
            }
            return this.addTextField(customField.label, customField.value);
        },

        addHtmlField: function(label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }
            return {label: label, value: value, isEmail : false };
        },

        addTextAreaField: function (label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }
            value = this.escapeHtml(value);
            value = value !== null ? value.replace(/\n/g, '<br />') : '';
            return {label: label, value: value, isEmail : false };
        },

        addURLField: function(label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }
            var html = '<a target="_blank" href="' + this.escapeHtml(value) + '">' + this.escapeHtml(value) + '</a>';
            return {label: label, value: html, isEmail : false };
        },

        addImageField: function(label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }

            var html = this.imageTpl({url: value});
            return {label: label, value: html, isEmail : false };
        },

        addDateField: function(label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }
            var valueAsDate = value ? moment(value).format('MM/DD/YYYY') : null;
            return {label: label, value: valueAsDate, isEmail : false };
        },

        addBooleanField: function(label, value) {
            if (isUndefinedOrEmptyString(value)) {
                return this.addDefaultField(label);
            }
            var valueAsBoolean = (value === false ? 'No' : 'Yes');
            return {label: label, value: valueAsBoolean, isEmail : false };
        },

        getScorecardsData: function () {
            var satisfiedScoreCardItems = this.model.get('satisfiedScoreCardItems');
            var data = {};



            _.each(satisfiedScoreCardItems, function(scoreCardItem) {
                if(scoreCardItem.showOnListing === false) { return; }

                if (scoreCardItem.image === '') {
                    data.ems = 'icon-headset';
                }
                else if (scoreCardItem.image === 'CloudHost') {
                    data.cloudHost = 'icon-gear-cloud';
                }
                else if (scoreCardItem.image === 'SecurityService') {
                    data.securityService = 'icon-shield';
                }
                else if (scoreCardItem.image === 'Scale') {
                    data.scale = 'icon-coins';
                }
                else if (scoreCardItem.image === 'LicenseFree') {
                    data.licenseFree = 'icon-cert';
                }
                else if (scoreCardItem.image === 'CloudStorage') {
                    data.cloudStorage = 'icon-data-cloud';
                }
                else if (scoreCardItem.image === 'Browser') {
                    data.browser = 'icon-application';
                }
            });

            return data;
        }

    });
});
