define(
[
    '../BaseView',
    'backbone',
    'jquery',
    'underscore',
    'handlebars',
    'bootstrap'
],
function(BaseView, Backbone, $, _, Handlebars) {
    'use strict';

    // <div class="tabbable">
    //   <ul class="nav nav-tabs">
    //     <li class="active"><a href="#tab1" data-toggle="tab">Section 1</a></li>
    //     <li><a href="#tab2" data-toggle="tab">Section 2</a></li>
    //   </ul>
    //   <div class="tab-content">
    //     <div class="tab-pane active" id="tab1">
    //       <p>I'm in Section 1.</p>
    //     </div>
    //     <div class="tab-pane" id="tab2">
    //       <p>Howdy, I'm in Section 2.</p>
    //     </div>
    //   </div>
    // </div>

    var SuperClass = BaseView;

    return SuperClass.extend({

        className: 'tabbable',

        markup: '<ul class="nav nav-tabs"></ul>'+
                '<div class="tab-content"></div>',

        tabTpl: Handlebars.compile('<li><a href="#{{href}}">{{title}}</a></li>'),

        // identifier of the currently active tab
        activeTab: null,

        // map of tabs to render. keys are ids (not actual dom ids)
        tabPanes: null,
        tabMap: null,

        initialize: function() {
            SuperClass.prototype.initialize.apply(this, arguments);

            this.tabMap = {};
        },

        render: function() {
            var me = this;

            me.$el.append(me.markup);

            me.$tabs        = me.$el.children('.nav-tabs');
            me.$tabContent  = me.$el.children('.tab-content');

            var tabsFrag = document.createDocumentFragment();
            var tabContentFrag = document.createDocumentFragment();

            _.each(this.tabPanes, function (tabPane, id) {
                var $tab = $(me.tabTpl({
                    href: me.href + '/' + id,
                    title: tabPane.title
                }));

                if(me.activeTab === id) {
                    $tab.addClass('active');
                    tabPane.$el.addClass('active');
                }

                tabsFrag.appendChild( $tab[0] );
                tabContentFrag.appendChild(tabPane.render().$el.addClass('tab-pane')[0]);

                me.tabMap[id] = $tab;
            });

            me.$tabs.append(tabsFrag);
            me.$tabContent.append(tabContentFrag);

            return me;
        },

        remove: function () {
            _.invoke(this.tabPanes, 'remove');
            return SuperClass.prototype.remove.call(this);
        },

        /**
         * Called as soon as the view has been loaded into the DOM. You can safely do DOM-specific stuff here.
         * @returns {Tabs} this for chaining purposes
         */
        shown: function() {
            this.tabPanes[this.activeTab].onActivate();
            _.invoke(this.tabPanes, 'shown');
            return this;
        },

        /**
         * @param tab The id string of the tab to activate
         */
        setActiveTab: function(tab) {
            if (!tab) {
                return;
            }

            var view = this.tabPanes[tab],
                $tab = this.tabMap[tab];

            $tab.addClass('active');
            view.$el.show();
            view.onActivate();

            _.each(this.tabPanes, function(tab) {
                if (tab !== view) {
                    tab.$el.hide();
                }
            });

            _.each(this.tabMap, function($t) {
                if ($t !== $tab) {
                    $t.removeClass('active');
                }
            });
        }
    });
});
