define([
    'jquery',
    'underscore',
    'backbone',
    'handlebars',
    '../../views/WizardBaseView',
    './ResolveDifferencesItemView',
    'bootstrap-select',
    '../../renderFunctions'
], function($, _, Backbone, Handlebars, BaseView, ResolveDifferencesItemView) {

    var SuperClass = BaseView;

    var resolveDifferencesView = SuperClass.extend({
        className: "resolve-card",

        events: {
            "change .resolve-select" : "mapSelection"
        },
        
        initialize: function() {
            this.template = Handlebars.compile($('#resolve-differences-template').html());
            this.sourceItems = this.model.get('sourceItems');
            this.targetItems = this.model.get('targetItems');
            this.differenceType = this.model.get('differenceType');
            this.defaultChoices = this.model.get('defaultChoices');

            _.each(this.sourceItems, function(item) {
                item['mapsTo'] = "create";
            });
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            this.renderDifferenceRows();
            return this;
        },

        renderDifferenceRows: function() {
            var me = this;
            var toResolve = _.where(this.sourceItems, {needsResolving: true });
            _.each(toResolve, function(item) {
                var differencesModel = new Backbone.Model({ sourceItemId: item.id,
                                                            sourceItemTitle: item.title ? item.title : item.name,
                                                            targetItems: me.targetItems,
                                                            defaultChoices:  me.defaultChoices});
                var itemView = new ResolveDifferencesItemView({ model: differencesModel });
                me.$el.find('tbody').append(itemView.render().el);
            });
        },

        shown: function() {
            this.$el.find('.selectpicker').selectpicker({
                size: 8,
                dropupAuto: false
            });

            return this;
        },

        mapSelection: function(e) {
            var sourceId = e.currentTarget.name;
            var selectedId = e.currentTarget.value;

            var selectedItem = _.find(this.sourceItems, function(item) {
                return item.id === sourceId || item.id === parseInt(sourceId, 10);
            });

            selectedItem['mapsTo'] = selectedId;
        }
    });

    return resolveDifferencesView;
});
