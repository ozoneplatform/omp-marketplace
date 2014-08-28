define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    'dataTables',
    '../../views/WizardBaseView',
    './SelectListingsTable',
    './ResolveDifferencesView',
    './NoResolvingView',
    '../../collections/Categories',
    '../../collections/Types',
    '../../collections/Agencies',
    '../../collections/ContactTypes'
], function($, _, Backbone, Handlebars, DataTables, BaseView, SelectListingsTable,
        ResolveDifferencesView, NoResolvingView, Categories, Types, Agencies, ContactTypes) {

    var SuperClass = BaseView;
    var selectListingsView = SuperClass.extend({

        tagName: 'div',

        className: 'wizard-section',

        events: {
            'click .import-all': 'hideTable',
            'click .import-selection': 'showTable'
        },

        initialize: function() {
            var me = this;
            this.template = Handlebars.compile($('#select-listings-template').html());

            this.selectListingsData = this.model.get('data');
            var listingsModel = new Backbone.Model({
                listings: this.selectListingsData.serviceItems,
                agencies: this.selectListingsData.agencies,
                contactTypes: this.selectListingsData.contactTypes
            });
            this.listingsTable = new SelectListingsTable({ model: listingsModel });


            this.categories = new Categories();
            this.types = new Types();
            this.agencies = new Agencies();
            this.contactTypes = new ContactTypes();

            this.categories.fetch().always(function () {
                me.categories = me._retrieveData(me.categories);
            });

            this.types.fetch().always(function() {
                me.types = me._retrieveData(me.types);
            });

            this.agencies.fetch().always(function() {
                me.agencies = me._retrieveData(me.agencies);
            });

            this.contactTypes.fetch().always(function() {
                me.contactTypes = me._retrieveData(me.contactTypes);
            });
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },

        showTable: function() {
            if (this.listingsTable.isRendered()) {
                this.listingsTable.show();
                $(".selection-count").show();
            } else {
                this.$el.append(this.listingsTable.render().el);
            }

            this.listingsTable.shown();
        },

        shown: function() {
            var radioSelection = $("input:radio[name = 'selectListings']:checked").val();
            if (radioSelection === "selection") this.listingsTable.shown();
        },

        hideTable: function() {
            $(".next-button").prop('disabled', false);
            this.listingsTable.hide();
            $(".selection-count").hide();
        },

        beforeNextCard: function() {
            var me = this;

            me.selectListingsData['selectedServiceItems'] = [];
            var selectedServiceItems = [];
            var radioSelection = $("input:radio[name = 'selectListings']:checked").val();
            var index;

            if (radioSelection === "selection") {
                this.listingsTable.resetSelectAllListingsCheckbox();
                //Find selected service items and put them in the selected items list
                _.each(this.listingsTable.selectedListings, function(id) {
                    _.each(me.selectListingsData['serviceItems'], function(serviceItem) {
                        if (serviceItem.id === id) {
                            index = selectedServiceItems.indexOf(serviceItem);
                            if (index === -1) {
                                selectedServiceItems.push(serviceItem);
                            } else {
                                //This listing is not just required anymore but selected by the
                                //user so set the required flag to false
                                selectedServiceItems[index].isRequiredListing = false;
                            }

                            //Find the required items of the selected items and also put
                            //them in the selected items list
                            var requiredListings = me.getRequiredListings(serviceItem);
                            _.each(requiredListings, function(requiredItem) {
                                index = selectedServiceItems.indexOf(requiredItem);
                                if (index === -1) {
                                    requiredItem.isRequiredListing = true;
                                    selectedServiceItems.push(requiredItem);
                                }
                            });
                        }
                    });
                });

                this.selectListingsData['selectedServiceItems'] = selectedServiceItems;
            } else {
                //User has "import all" selected so take all service items
                this.selectListingsData['selectedServiceItems'] =
                    this.selectListingsData['serviceItems'];
            }
        },

        getRequiredListings: function(serviceItem, requiredListings) {
            requiredListings = requiredListings || [];

            var me = this;
            _.each(serviceItem.requires, function(itemIndex) {
                var requiredItem = me.selectListingsData['serviceItems'][itemIndex];
                if (requiredListings.indexOf(requiredItem) === -1) {
                    requiredListings.push(requiredItem);
                    me.getRequiredListings(requiredItem, requiredListings);
                }
            });

            return requiredListings;
        },

        createNewViews: function() {
            //Create resolve views if there are things to resolve
            var newViews = [];
            var defaultChoices;

            var typesToResolve = _.where(this.selectListingsData['types'],
                    {needsResolving: true });

            if (typesToResolve.length > 0) {
                defaultChoices =
                    [{choiceValue: "create", choiceMessage: "Add this type to the store"}];

                var typesModel = new Backbone.Model({
                    instructions: 'You are attempting to import an invalid type ' +
                        'of listing. Please choose an action for each type.',
                    differenceType: 'type',
                    sourceItems: typesToResolve,
                    targetItems: this.types,
                    defaultChoices: defaultChoices
                });

                newViews.push(new ResolveDifferencesView({model: typesModel}));
            }

            var categoriesToResolve =
                _.where(this.selectListingsData['categories'], {needsResolving: true });

            if (categoriesToResolve.length > 0) {
                defaultChoices = [{
                    choiceValue: "create",
                    choiceMessage: "Add this category to the Store"
                }, {
                    choiceValue: "remove",
                    choiceMessage: "Remove this category"
                }];

                var categoriesModel = new Backbone.Model({
                    instructions: 'You are attempting to import a listing(s) with an ' +
                        'invalid category. Please choose an action for each category.',
                    differenceType: 'category',
                    sourceItems: categoriesToResolve,
                    targetItems: this.categories,
                    defaultChoices: defaultChoices
                });

                newViews.push(new ResolveDifferencesView({model: categoriesModel}));
            }

            var agenciesToResolve =
                _.where(this.selectListingsData['agencies'], {needsResolving: true });

            if (agenciesToResolve.length > 0) {
                defaultChoices = [{choiceValue: "remove", choiceMessage: "Keep Company field"}];

                var agencyModel = new Backbone.Model({
                    instructions: 'You are attempting to import listing(s) from a new agency or vendor. ' +
                        'Please choose an action for each field.',
                    differenceType: 'agency',
                    sourceItems: agenciesToResolve,
                    targetItems: this.agencies,
                    defaultChoices: defaultChoices
                });

                newViews.push(new ResolveDifferencesView({model: agencyModel}));
            }

            var customFieldsToResolve =
                _.where(this.selectListingsData['customFieldDefs'], {needsResolving: true });

            if (customFieldsToResolve.length > 0) {
                defaultChoices = [{
                    choiceValue: "create",
                    choiceMessage: "Add this custom field to the store"
                }, {
                    choiceValue: "remove",
                    choiceMessage: "Remove this custom field"
                }];

                var customFieldDefsModel = new Backbone.Model({
                    instructions: 'You are attempting to import an invalid custom field. ' +
                        'Please choose an action for each field.',
                    differenceType: 'custom field',
                    sourceItems: customFieldsToResolve,
                    targetItems: [],
                    defaultChoices: defaultChoices
                });

                newViews.push(new ResolveDifferencesView({model: customFieldDefsModel}));
            }

            var contactTypesToResolve =
                _.where(this.selectListingsData['contactTypes'], {needsResolving: true});

            if (contactTypesToResolve.length > 0) {
                defaultChoices = [{
                    choiceValue: "create",
                    choiceMessage: "Add this contact type to the store"
                }, {
                    choiceValue: "remove",
                    choiceMessage: "Remove this contact type"
                }];

                var contactTypeDefsModel = new Backbone.Model({
                    instructions: 'You are attempting to import an invalid contact type. ' +
                        'Please choose an action for each field.',
                    differenceType: 'contact type',
                    sourceItems: contactTypesToResolve,
                    targetItems: this.contactTypes,
                    defaultChoices: defaultChoices
                });

                newViews.push(new ResolveDifferencesView({model: contactTypeDefsModel}));
            }

            //No items to resolve so create a simple view to tell the user
            if (newViews.length < 1) {
                var noResolveView = new NoResolvingView({
                    model: new Backbone.Model({
                        message: "All of your listings were accepted. You do not have any " +
                            "differences to resolve."
                    })
                });

                newViews.push(noResolveView);
            }

            return newViews;
        },

        _retrieveData: function(data) {
            return _.map(data.models, function(model) { return model.attributes; });
        }
    });

    return selectListingsView;
});
