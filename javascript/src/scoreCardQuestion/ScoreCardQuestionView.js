define([
    '../views/BaseView',
    'handlebars'
],
function(BaseView, Handlebars) {

    return BaseView.extend({

        tagName: 'tr',

        template: Handlebars.compile(
            '<td><div class="image">{{#if image}}<img src="{{image}}">{{/if}}</div></td>' +
            '<td><div class="question">{{question}}</div></td>' +
            '<td><div class="description">{{description}}</div></td>' +
            '<td><input type="checkbox" class="ios switch brand-success" {{#if showOnListing}}checked{{/if}} disabled /></td>' +
            '<td class="button-container">' +
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
            this.$el.find('.switch').attr('title', 'Click Edit to enable or disable this scorecard question.');
            return this;
        }

    })
});
