define(
[
    'backbone'
],
function(Backbone) {

    var SuperClass = Backbone.Model;

    return SuperClass.extend({

        // {
        //     id: 1,
        //     userId: 1,
        //     username: "testAdmin1",
        //     displayName: "Test Administrator 1",
        //     text: "review....",
        //     date: "08/23/2013 01:17 PM EDT",
        //     userRate: 4,
        //     serviceItemRateStats: {
        //         avgRate: 4,
        //         totalRate2: 0,
        //         totalRate3: 0,
        //         totalRate1: 0,
        //         totalRate5: 0,
        //         totalRate4: 2
        //     }
        // }


        // newUserRating:4
        // currUserRating:4
        // commentTextInput:asasa
        // serviceItemId:1
        // id:1

        getDefaultIconUrl: function() {
            var typeId = this.get('types').id;

            return window.Marketplace.context + '/images/types/' + typeId;
        }
    });

});
