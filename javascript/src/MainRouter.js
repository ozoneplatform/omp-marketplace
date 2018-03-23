define([
    './views/GlobalTagList',
    './profile/Window',
    './quickview/index',
    './affiliatedSearch/views/AffiliatedResultsGridView',
    './spa/views/admin/agency/AgencyAdminPageView',
    'views/About',
    'backbone',
    'underscore',
    'jquery'
], function(GlobalTagListView, ProfileWindow, Quickview, AffiliatedResultsGridView,
        AgencyAdminView, AboutWindow, Backbone, _, $) {
    'use strict';

    var SuperClass = Backbone.Router;

    /**
     * Takes a list of parameter names and a list of arguments
     * and matches the names to the arguments by array index, and returns
     * an object containing the resulting key-value pairs.
     */
    function makeViewParams(paramNames, args) {
        return _.object(_.map(paramNames, function(name, idx) {
            return [name, args[idx]];
        }));
    }

    function getRelevantArgs(hasSubRoutes, args) {
        return hasSubRoutes ? Array.prototype.slice.call(args, 0, args.length - 1) : args;
    }

    //Ideally this would just return window.location.hash, but for some reason firefox
    //unescapes that, leading to a problem where /'s that are supposed to be escaped as
    //part of a URI component are indistinguishable from /'s that separate URI components
    function getHash() {
        var loc = window.location.href,
            index = loc.indexOf('#');

        return index === -1 ? '' : loc.slice(index);
    }

    return SuperClass.extend({
        //similar to the routes object on normal Backbne routers,
        //but stores extra information to allow the handling of modals
        ozoneRoutes: {
            'profile/:profileId': {
                ViewClass: ProfileWindow,
                modal: true,
                paramNames: ['profileId', 'tab'],
                subRoutes: true
            },
            'quickview/:serviceItemId': {
                ViewClass: Quickview,
                modal: true,
                paramNames: ['serviceItemId', 'tab'],
                subRoutes: true
            },
            'quickview-affiliated/:context/:serviceItemId': {
                ViewClass: Quickview,
                modal: true,
                paramNames: ['context', 'serviceItemId', 'tab'],
                extraParameters: {
                    isAffiliated: true
                },
                subRoutes: true
            },
            'affiliated/:serverId(/:searchParamsJSON)': {
                ViewClass: AffiliatedResultsGridView,
                modal: false,
                paramNames: ['serverId', 'searchParamsJSON']
            },
            'agency': {
                ViewClass: AgencyAdminView,
                modal: false,
                paramNames: []
            },
            'tags': {
                ViewClass: GlobalTagListView,
                modal: false,
                paramNames: []
            },
            'about': {
                ViewClass: AboutWindow,
                modal: true,
                paraNames: []
            }
        },

        //the route currently in use. This is used to determine if the current view can
        //continue to be used
        currentRouteURL: null,
        currentRoute: null,
        currentRouteArgs: [],

        //keep track of the currently open modal window
        currentModal: null,

        //stack of routes of previously opened modal windows
        previousModalRouteURLStack: [],
        previousModalTitleStack: [],

        //the current view that is present in the main content area
        currentMainView: null,
        currentMainRouteURL: '',

        //a jquery element to contain modals
        modalContainer: null,

        //The container for non-modal views. This should be passed in when the router is created
        mainContainer: null,

        //the jquery collection representing the pre-existing children of
        //mainContainer (ie, what was rendered server-side).
        //this gets hidden whenever a mainContainer route is processed
        defaultEl: null,

        //keep track of whether or not we are currently routing to avoid reentering the router
        routing: false,

        //keep trakc of whether we are currently routing as the result of a toPreviousModal
        //operation
        goingBack: false,

        //private
        //wrapper function for route handlers than handles modal window concerns
        createModalHandler: function(info, route) {
            var me = this,
                ViewClass = info.ViewClass,
                paramNames = info.paramNames,
                extraParameters = info.extraParameters;

            return function() {
                var params = _.extend(makeViewParams(paramNames, arguments), extraParameters);

                me.routing = true;

                if (!this.currentModal || !this.isSameRoute(route, info.subRoutes, arguments)) {
                    if (me.goingBack) {
                        me.previousModalTitleStack.pop();
                        params.backTitle =
                            me.previousModalTitleStack[me.previousModalTitleStack.length - 1];
                    }
                    else if (me.currentModal) {
                        params.backTitle = me.currentModal.getTitle();
                        me.previousModalTitleStack.push(params.backTitle);
                    }
                    //else no backTitle

                    var view = new ViewClass(params);

                    me.removeModal();

                    me.currentModal = view.render();
                    me.currentModal.$el.appendTo(this.modalContainer);

                    me.currentModal.show();

                    view.onHidden = _.wrap(view.onHidden, function (onHidden) {
                        onHidden.call(view);
                        me.closeModal();
                    });

                    if (me.currentRouteURL && !me.goingBack) {
                        me.previousModalRouteURLStack.push(me.currentRouteURL);
                    }
                }
                else {
                    me.currentModal.route(params);
                }

                me.currentRoute = route;
                me.currentRouteURL = getHash();
                me.currentRouteArgs = getRelevantArgs(info.subRoutes, arguments);

                me.routing = false;
            };
        },

        //private
        //generator for handlers that work with non-modal views. Ensures that any currently
        //open modals are closed
        createNonModalHandler: function(info, route) {
            var me = this,
                ViewClass = info.ViewClass,
                paramNames = info.paramNames,
                extraParameters = info.extraParameters;

            return function() {
                var params = _.extend(makeViewParams(paramNames, arguments),
                    extraParameters);

                me.routing = true;

                //create a new view, or defer to the existing one if it is just
                //a different subroute
                if (!this.isSameRoute(route, info.subRoutes, arguments)) {
                    var view = new ViewClass(params);

                    me.removeModal();
                    me.removeMainView();

                    me.currentMainView = view.render();

                    me.defaultEl.hide();
                    me.currentMainView.$el.appendTo(me.mainContainer);
                }
                else {
                    me.currentModal.route(params);
                }

                me.currentRoute = route;
                me.currentRouteURL = me.currentMainRouteURL = window.location.hash;
                me.currentRouteArgs = getRelevantArgs(info.subRoutes, arguments);

                me.routing = false;
            };
        },

        //private
        //determine if this route is the same as the last one, for the purposes of using the
        //same view instead of destroying and re-creating it
        isSameRoute: function(route, hasSubRoutes, args) {
            //the args that will be compared.  Chop off the last arg if this route has
            //subRoutes
            var argsToCompare = getRelevantArgs(hasSubRoutes, args);

            return route === this.currentRoute &&

                //list equality of argsToCompare and currentRouteArgs
                _.all(_.zip(argsToCompare, this.currentRouteArgs), function(pair) {
                    return pair[0] === pair[1];
                });
        },

        //private
        removeModal: function() {
            if (this.currentModal && this.currentModal.isRendered()) {
                this.currentModal.remove();
            }
            this.currentModal = null;
        },

        //private
        removeMainView: function() {
            if (this.currentMainView) {
                this.currentMainView.remove();
                this.currentMainView = null;
                this.currentMainRouteURL = '';
            }
        },

        //private
        showDefault: function() {
            this.routing = true;

            this.removeModal();
            this.removeMainView();

            this.currentRoute = '';
            this.currentRouteURL = '';
            this.currentRouteArgs = [];

            this.defaultEl.show();

            this.routing = false;
        },

        //a public method to close the modal and navigate the URL back to whatever view
        //is behind it
        closeModal: function() {
            if (!this.routing) {
                this.navigate(this.currentMainRouteURL, { trigger: true });
                this.previousModalRouteURLStack = [];
                this.previousModalTitleStack = [];
                this.removeModal();
            }
        },

        toPreviousModal: function() {
            if (this.previousModalRouteURLStack.length) {
                this.goingBack = true;

                this.navigate(this.previousModalRouteURLStack.pop(), { trigger: true });

                this.goingBack = false;
            }
        },

        initialize: function(options) {
            var me = this;
            options = options || {};

            _.each(this.ozoneRoutes, function(info, route) {
                var handlerFactoryName = 'create' + (info.modal ? '' : 'Non') + 'ModalHandler',
                    handlerFactory = _.bind(me[handlerFactoryName], me),
                    handler = handlerFactory(info, route),

                    //the actual route passed to backbone
                    realRoute = info.subRoutes ? route + '(/:subroutes)' : route;

                me.route(realRoute, null, handler);
            });

            this.modalContainer =
                $('<div class="bootstrap-active modal-container">').appendTo(document.body);
            this.mainContainer = options.mainContainer || $('body');

            //add a default route that just displays whatever is in the mainContainer by default
            this.route('', 'main', _.bind(this.showDefault, this));

            this.defaultEl = this.mainContainer.children();
        }
    });
});
