package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

class RejectionActivity extends ServiceItemActivity {

    public RejectionActivity(){
        action = Constants.Action.REJECTED
    }

    RejectionListing rejectionListing

    static mapping = {
        //TODO: this doesn't seem right - rejectionListings do not belong to rejectionActivities
        rejectionListing cascade:"delete"
    }

    JSONObject asJSON() {
        def json = super.asJSON()
        json.put("rejectionListing", rejectionListing.asJSON())
        return json
    }
}
