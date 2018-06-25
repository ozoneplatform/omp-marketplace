define(
[
    'jquery',
    'underscore',
    'backbone',
    'views/BaseView'
],
function($, _, Backbone, BaseView) {
    var SuperClass = BaseView;
    
    var View = SuperClass.extend({
        beforeNextCard: function() {
            return this;
        }
    });

    return View;

});