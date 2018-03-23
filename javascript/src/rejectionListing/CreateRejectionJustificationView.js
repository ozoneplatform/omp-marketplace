define([
    '../views/BaseView',
    '../collections/RejectionJustificationsCollection',
    'handlebars',
    'underscore',
    'marketplace',
    'bootstrap-select',
    'jquery.serialize-object'
],
function(BaseView, RejectionJustificationsCollection, Handlebars, _, Marketplace, $) {

    var _collection;

    return BaseView.extend({

        // ServiceItem model
        model: null,

        template: Handlebars.compile(
            '<p>Please provide a justification for rejecting this listing.</p>' +
            '<h5 class="free-text-warning">{{freeTextWarning}}</h5>' +
            '<form class="form-horizontal">' +
                '<div class="control-group">' +
                    '<label class="control-label" for="reason">Reason</label>' +
                    '<div class="controls">' +
                        '<select class="selectpicker" name="reason" title="Select a rejection justification">' +
                            '{{#each reasons}}' +
                                '<option title="{{description}}">{{title}}</option>' +
                            '{{/each}}' +
                        '</select>' +
                    '</div>' +
                '</div>' +
                '<div class="control-group">' +
                    '<label class="control-label" for="description">Detail</label>' +
                    '<div class="controls">' +
                        '<textarea name="description" placeholder="Provide justification for rejecting this listing"></textarea>' +
                    '</div>' +
                '</div>' +
                '<p class="remaining"><span class="remaining-count">{{remaining}}</span> characters remaining</p>' +
            '</form>'
        ),

        events: {
            'keyup textarea': 'updateRemainingCount'
        },

        maxCharacters: 2000,

        initialize: function (options) {
            this.collection = _collection = (_collection || new RejectionJustificationsCollection());
            BaseView.prototype.initialize.call(this, options);
        },

        data: function () {
            var freeTextWarning = _.findWhere(Marketplace.appconfigs, {code: 'free.warning.content'}).value;
            var reasons = this.collection.toJSON();
            return {
                freeTextWarning: freeTextWarning,
                remaining: this.maxCharacters,
                reasons: reasons
            };
        },

        render: function () {
            var me = this;

            function doRender () {
                me.$el.html(me.template(me.data()));
                me.$('.selectpicker').selectpicker();
                me.__isRendered__ = true;
                me.__renderDeferred__ && me.__renderDeferred__.resolve();
            }

            this.collection.length ? doRender() : this.collection.fetch().done(doRender);

            return this;
        },

        submit: function () {
            var $form = this.$('form'),
                data = $form.serializeObject();

            data.id = this.collection.findWhere({title: data.reason}).get('id');
            return this.model.reject(data);
        },

        updateRemainingCount: function (evt) {
            var $textarea = $(evt.target),
                value = $textarea.val();

            if(value.length > this.maxCharacters) {
                value = value.substr(0, this.maxCharacters);
                $textarea.val(value);
            }

            this.$('.remaining-count').text(this.maxCharacters - value.length);
        }

    });

});