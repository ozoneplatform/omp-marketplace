define(
[
    'backbone',
    'underscore'
],
function (Backbone, _) {

    return Backbone.Model.extend({
        sync: function (method, model, options) {
            var action = {
                'create': '/saveItemComment',
                'update': '/edit',
                'delete': '/deleteItemComment'
            }[method];

            if(!action) {
                return Backbone.sync.apply(this, arguments);
            }

            var data = {
                itemCommentId: model.get('id'),
                newUserRating: model.get('userRate'),
                commentTextInput: model.get('text')
            };

            _.extend(options, {
                type: 'POST',
                data: _.extend(data, model.toJSON()),
                processData: true,
                url: Marketplace.context + '/itemComment' + action
            });

            return Backbone.sync.apply(this, arguments);
        },

        parse: function (resp, options) {
            return (resp.data || resp);
        }
    });
});
