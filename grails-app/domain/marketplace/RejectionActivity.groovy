package marketplace

import org.grails.web.json.JSONObject

class RejectionActivity extends ServiceItemActivity implements ToJSON {

    public RejectionActivity(){
        action = Constants.Action.REJECTED
    }

    RejectionListing rejectionListing

    static mapping = {
        //TODO: this doesn't seem right - rejectionListings do not belong to rejectionActivities
        rejectionListing cascade:"delete"
    }

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()
        json.put("rejectionListing", rejectionListing.asJSON())
        json
    }

}
