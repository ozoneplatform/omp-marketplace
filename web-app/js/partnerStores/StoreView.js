define([
    '../views/BaseView',
    'handlebars',
    'bootstrap'
],
function(BaseView, Handlebars, $) {

    return BaseView.extend({

        tagName: 'tr',

        template: Handlebars.compile(
            '<td>' +
                '<div class="name">' +
                    '<img src="{{icon.url}}">' +
                    '<span>{{name}}</span>' +
            '</td>' +
            '<td>' +
                '<div class="url">' +
                    '{{serverUrl}}</td>' +
                '</div>' +
            '<td>{{timeout}}</td>' +
            '<td><input type="checkbox" class="ios switch brand-success" {{#if active}}checked{{/if}} disabled/></td>' +
            '<td>' +
                '<a href="#" class="edit btn btn-small" title="Edit"><i class="icon-pencil"></i></a>' +
                '<a href="#" class="delete btn btn-small" title="Delete"><i class="icon-minus"></i></a>' +
            '</td>'
        ),

        modelEvents: {
            'change': 'render'
        },

        render: function () {
            this.$el.html(this.template(this.model.attributes));
            this.$el.find('input[type="checkbox"]').svitch();
            this.$el.find('.switch').attr('title', 'Click Edit to enable or disable this partnership.');
            return this;
        }

    });
});
