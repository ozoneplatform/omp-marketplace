define([
    '../views/BaseView',
    'handlebars',
    'marketplace',
    'bootstrap-select',
    'underscore',
    'underscore.string',
    'jquery.serialize-object'
],
function(BaseView, Handlebars, Marketplace, $, _, _s) {

    var _collection;

    return BaseView.extend({

        // ServiceItem model
        model: null,

        template: Handlebars.compile(
            '<form class="form-horizontal">' +
                '<div class="control-group">' +
                    '<label class="control-label" for="reason">Reason:</label>' +
                    '<div class="controls">' +
                        '{{title}}' +
                    '</div>' +
                '</div>' +
                '<div class="control-group">' +
                    '<label class="control-label" for="description">Detail:</label>' +
                    '<div class="controls">{{description}}</div>' +
                '</div>' +
            '</form>'
        ),

        data: function (rejectionJustification) {
            return {
                title: rejectionJustification.justification.title || 'None',
                description: _s.trim(rejectionJustification.description) || 'None'
            }
        },

        render: function (rejectionJustification) {
            var me = this;

            function doRender (rejectionJustification) {
                me.$el.html(me.template(me.data(rejectionJustification)));
                me.__isRendered__ = true;
                me.__renderDeferred__ && me.__renderDeferred__.resolve();
            }

            this.model.getRejectionJustification().done(doRender);

            return this;
        }

    });

});