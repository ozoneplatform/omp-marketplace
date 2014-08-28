define([
    './BaseModel',
    '../collections/Reviews',
    '../collections/RequiredItems',
    '../collections/RequiredByItems',
    '../collections/ScorecardQuestionCollection',
    '../collections/ChangelogCollection',
    './RequiredItem',
    '../collections/ServiceItemTagsCollection',
    'backbone',
    'jquery',
    'underscore',
    'marketplace'
],
function(BaseModel, ReviewsCollection, RequiredItemsCollection, RequiredByItemsCollection, ScorecardQuestionCollection, ChangelogCollection, RequiredItem, ServiceItemTagsCollection, Backbone, $, _, Marketplace) {

    var SuperClass = BaseModel;

    function Screenshot (url) {
        this.smallImageUrl = url;
        this.largeImageUrl = url;
    }

    var resolvedPromise = function () {
        return $.Deferred().resolve().promise();
    };

    var refreshChangelogs = function () {
        if (!this.isDestroyed) {
            this._changelogs && this._changelogs.refresh();
        }
    };

    return SuperClass.extend({

        // whether the service item is from an affiliated marketplace
        isAffiliated: false,

        // the context of the service item's marketplace
        context: null,

        api: function () {
            return {
                'create': this.context + '/api/serviceItem/',
                'update': this.context + '/api/serviceItem/' + this.id,
                'delete': this.context + '/api/serviceItem/' + this.id
            };
        },

        defaults: function () {
            var user = Marketplace.user,
                defaultState = Marketplace.defaultState;

            return {
                state: _.clone(defaultState),
                owners: [_.clone(user)],
                techPocs: [user.displayName],
                organization: user.organization,
                opensInNewBrowserTab: false,
                docUrls: [],
                screenshots: [],
                contacts: []
            };
        },

        initialize: function(options) {
            SuperClass.prototype.initialize.call(this, options);

            this.context = (options && options.context) || Marketplace.context;
            this.isAffiliated = !!(options && options.isAffiliated);
            this.jsonp = this.isAffiliated;
        },

        url: function() {
            return (this.context + '/public/serviceItem/' + (this.id || ''));
        },

        set: function(key, val, options) {
            var attrs;

            // Handle both `"key", value` and `{key: value}` -style arguments.
            if (typeof key === 'object') {
                attrs = key;
                options = val;
            } else {
                (attrs = {})[key] = val;
            }

            if(attrs.avgRate) {
                // round to integer if possible (For example, 4.0, 5.0, etc.)
                attrs.avgRate = parseFloat(parseFloat(attrs.avgRate, 10).toFixed(1), 10);
            }

            // compact arrays by only removing null and undefined values
            _.forEach(attrs, function (value, key, attrs) {
                if(_.isArray(value)) {
                    attrs[key] = _.filter(value, function (v) {
                        return v !== null && v !== undefined;
                    });
                }
            });

            return SuperClass.prototype.set.call(this, attrs, options);
        },

        parse: function (resp) {
            var data = resp.data || resp;

            data.owners = data.owners || [data.owner];

            // handle partial responses from server by merging client and server attrs
            data = _.extend({}, this.attributes, data);

            data.techPocs = data.techPocs || [data.techPoc];

            // for backwards compatibility
            data.screenshots = data.screenshots || [];
            data.screenshot1Url && data.screenshots.push(new Screenshot(data.screenshot1Url));
            data.screenshot2Url && data.screenshots.push(new Screenshot(data.screenshot2Url));

            delete data.techPoc;
            delete data.owner;
            return data;
        },

        toJSON: function () {
            var json = SuperClass.prototype.toJSON.call(this);

            // TODO: remove after CF json format is fixed
            _.each(json.customFields, function (customField) {
                if(customField['class'] === 'marketplace.DropDownCustomField') {
                    if(_.isArray(customField.value)) {
                        customField.fieldValueList = customField.value;
                    }
                    else if(_.isArray(customField.fieldValue)) {
                        customField.fieldValueList = _.pluck(customField.fieldValue, 'value');
                    }

                    if (customField.fieldValueList) {
                        delete customField.value;
                    }
                }
            });

            return json;
        },

        clone: function () {
            var attributes = _.omit(
                _.cloneDeep(this.attributes),
                'id', 'approvalStatus', 'isOutside', 'owners', 'techPocs'
            );

            if(attributes.types && attributes.types.title === 'OZONE App') {
                delete attributes.types;
            }

            if(attributes.owfProperties) {
                delete attributes.owfProperties.universalName;
            }

            attributes.isEnabled = true;

            return new this.constructor(attributes, {
                silent: true
            });
        },

        reviews: function () {
            if(this._reviews) {
                return this._reviews;
            }

            var me = this;

            this._reviews = new ReviewsCollection([], {
                id: this.id,
                jsonp: this.jsonp,
                context: this.context
            });

            this.listenTo(this._reviews, 'change:serviceItemRateStats', function (model, serviceItemRateStats, options) {
                this.set(serviceItemRateStats);
            });

            //TODO: This is view specific - not ideal to have this logic on the model
            // we need to refresh changelogs when the reviews are synced, but not until they are fetched the first time
            // to avoid multiple requests for the changelogs as the quickview is being initially rendered
            this._reviews.once('sync', function () {
                me.listenTo(me._reviews, 'sync', _.bind(refreshChangelogs, me));
            });

            this.listenTo(this._reviews, 'destroy', function (model, collection, options) {
                var serviceItemRateStats,
                    xhr = options.xhr,
                    me = this;

                if(xhr) {
                    xhr.done(function (data) {
                        serviceItemRateStats = data.data.serviceItemRateStats;
                        if(serviceItemRateStats) {
                            me.set(serviceItemRateStats);
                        }
                        me.set('totalComments', me._reviews.length);
                        refreshChangelogs.apply(me);
                    });
                }
            });

            this.listenTo(this._reviews, 'add', function (model, collection, options) {
                this.set(model.get('serviceItemRateStats'));
                this.set('totalComments', this._reviews.length);
            });

            return this._reviews;
        },

        includedItems: function () {
            this._includedItemsCollection = this._includedItemsCollection || new RequiredItemsCollection([], {
                id: this.id,
                jsonp: this.jsonp,
                context: this.context,
                isAffiliated: this.isAffiliated
            });
            return this._includedItemsCollection;
        },

        requiredByItems: function() {
            this._requiredByItemsCollection = this._requiredByItemsCollection ||
                new RequiredByItemsCollection([], {
                    id: this.id,
                    jsonp: this.jsonp,
                    context: this.context,
                    isAffiliated: this.isAffiliated
                });

            return this._requiredByItemsCollection;
        },

        scorecards: function () {
            if(this.get('satisfiedScoreCardItems')){
                return this.get('satisfiedScoreCardItems');
            }

            this.satisfiedScoreCardItems = [];
            return this.satisfiedScoreCardItems;
        },

        changelogs: function () {
            if(this._changelogs) {
                return this._changelogs;
            }

            this._changelogs = new ChangelogCollection([], {
                id: this.id,
                jsonp: this.jsonp,
                context: this.context
            });

            this.listenTo(this, 'sync', refreshChangelogs);

            return this._changelogs;
        },


        tags: function () {
            if(this._tags) {
                return this._tags;
            }

            var me = this;

            this._tags = new ServiceItemTagsCollection([], {
                id: this.id,
                jsonp: this.jsonp,
                context: this.context
            });

            //TODO: This is view specific - not ideal to have this logic on the model
            // we need to refresh changelogs when the tags are synced, but not until they are fetched the first time
            // to avoid multiple requests for the changelogs as the quickview is being initially rendered
            this._tags.once('sync', function () {
                me.listenTo(me._tags, 'sync', _.bind(refreshChangelogs, me));
            });

            this.listenTo(this._tags, 'destroy', function (model, collection, options) {
                var xhr = options.xhr,
                    refreshChangelogsThis = _.bind(refreshChangelogs, this);

                if(xhr) {
                    xhr.done(function () {
                        refreshChangelogsThis();
                    });
                }
            });

            return this._tags;
        },


        approve: function () {
            if(this.isApproved()) { return resolvedPromise(); }
            this.save('approvalStatus', 'Approved');
        },

        reject: function (data) {
            if(this.isRejected()) { return resolvedPromise(); }
            var rejectionListing = {
                description: data.description,
                justification: data
            };
            this.set('approvalStatus', 'Rejected');

            return $.ajax({
                url: this.context + '/api/serviceItem/' + this.id + '/rejectionListing',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(rejectionListing),
                dataType: 'json'
            });
        },

        saveTags: function(tags){
            return $.ajax({
                url: this.context + '/api/serviceItem/' + this.id + '/tag',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(tags),
                dataType: 'json'
            });
        },

        getRejectionJustification: function () {
            return $.ajax({
                url: this.context + '/api/serviceItem/' + this.id + '/rejectionListing',
                dataType: 'json'
            });
        },

        submit: function () {
            if(this.isSubmitted()) { return resolvedPromise(); }
            return this.save('approvalStatus', 'Pending');
        },

        isOwnedBy: function (username) {
            return _.some(this.get('owners'), function(owner) {
                return (username === owner.username);
            });
        },

        isOzoneAware: function  () {
            var types = this.get('types');
            return types && types.ozoneAware;
        },

        isStack: function () {
            var owfProperties = this.get('owfProperties'),
                hasStackDescriptor = !!owfProperties && !!owfProperties.stackDescriptor;

            return hasStackDescriptor;
        },

        isEnabled: function () {
            return this.get('isEnabled') === true;
        },

        isPublished: function () {
            return this.get('isPublished') === true;
        },

        isSubmitted: function () {
            return !this.isInProgress() && !this.isRejected();
        },

        isPending: function () {
            return this.get('approvalStatus') === 'Pending';
        },

        isApproved: function () {
            return this.get('approvalStatus') === 'Approved';
        },

        isRejected: function () {
            return this.get('approvalStatus') === 'Rejected';
        },

        isInProgress: function () {
            return this.get('approvalStatus') === 'In Progress';
        },

        hasValidLaunchUrl: function () {
            return (this.get('types').hasLaunchUrl && this.get('validLaunchUrl') !== false);
        },

        isAddable: function () {
            return this.isEnabled() && this.isApproved() && this.isPublished() &&
                    ((this.isStack() && Marketplace.widget.isStackAddable()) ||
                     this.hasValidLaunchUrl());
        },

        getDefaultIconUrl: RequiredItem.prototype.getDefaultIconUrl,

        destroy: function() {
            this.isDestroyed = true;

            return SuperClass.prototype.destroy.apply(this, arguments);
        }
    });
});
