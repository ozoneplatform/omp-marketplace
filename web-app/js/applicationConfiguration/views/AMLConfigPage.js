define([
    './Page',
    'marketplace'
], function(Page, Marketplace){

    /*
    * This is the entry point for the views.  This class will fetch the data, iterate through it
    * and call the ItemView for each record.
    */
    return Page.extend({

        needToRender: function(model) {
            var modelCode = model.get("code");

            //check for special cases where certain settings shouldn't rendered
            if (this.appConfigs["store.is.franchise"][0].get("value") === "false") {
                switch (modelCode) {
                    case "store.valid.domains": return false;
                    case "store.domains": return false;
                    default: return true;
                }
            }

            return true;
        }
    });
});
