define(
[
    '../../views/BaseView',
    'backbone',
    'handlebars',
    'jquery',
    'underscore'
],
function(BaseView, Backbone, Handlebars, $, _) {

    var SuperClass = BaseView;

    return SuperClass.extend({

        tpl: Handlebars.compile(
            '<div class="intents row">' +
                '<div class="span8">'+
                    '<table id="intents" class="table"> ' +
                        '<tr>' +
                            '<thead>' +
                                '<th>Action</th>' +
                                '<th>Data Type</th>' +
                                '<th>Send</th>' +
                                '<th>Receive</th>' +
                            '</thead>' +
                            '<tbody>' +
                                '{{#each intents}}' +
                                    '<tr>' +
                                        '<td>{{action}}</td>' +
                                        '<td>{{dataType}}</td>' +
                                        '{{#if send}}' +
                                                '<td class="send checkmark"></td>' +
                                            '{{else}}' +
                                                '<td></td>' +
                                        '{{/if}}' +
                                        '{{#if receive}}' +
                                                '<td class="receive checkmark"></td>' +
                                            '{{else}}' +
                                                '<td></td>' +
                                        '{{/if}}' +
                                    '</tr>' +
                                '{{/each}}' +
                            '</tbody>' +
                        '</tr>' +
                    '</table>' +
                '</div>' +
            '</div>'
        ),

        initialize: function(options){
            this.serviceItemModel = options.model;
            this.render();
        },

        render: function() {
            var configs = this.getIntents();
            if(configs.length > 0) {
                this.$el.append(this.tpl({
                    intents : configs
                }));
            }

            return this;
        },

        getIntents: function(){
            return _.map(this.serviceItemModel.get('intents'), function(intent){
                return {
                    action : intent.action.title,
                    dataType: intent.dataType.title,
                    send : intent.send,
                    receive: intent.receive
                };
            });
        }

    });
});

