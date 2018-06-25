package marketplace

import org.grails.web.json.JSONObject

import marketplace.Constants.Action

//Domain object that holds an audit trail of a listing.
class ServiceItemActivity extends AuditStamped implements Serializable, ToJSON {

    static constraints = {
        action maxSize: 128
    }

    Action action
    Date activityTimestamp = new Date()

    static belongsTo = [serviceItem: ServiceItem, author: Profile]

    static hasMany = [changeDetails: ChangeDetail]

    static mapping = {
        author fetch: 'join'
        cache true
        tablePerHierarchy false
        serviceItem index: 'svc_item_act_svc_item_id_idx'
        activityTimestamp type: Date
        changeDetails batchSize: 25
    }

    String toString(){
        return AdminObjectFormatter.fullDateDisplay(activityTimestamp)
    }

    String prettyPrint() {
        toString()
    }

//    void setActivityTimestamp(timestamp) {
//        switch(timestamp.getClass()) {
//            case String:
//                this.setActivityTimestampString((String) timestamp)
//                break
//            case Date:
//                this.setActivityTimestampDate((Date) timestamp)
//                break
//            default:
//                this.setActivityTimestampString((String) timestamp)
//        }
//    }
//
//    public void setActivityTimestampString(String dateString) throws ParseException {
//        DateFormat dateFormat = new SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT)
//        this.activityTimestamp = dateFormat.parse(dateString)
//    }
//
//    public void setActivityTimestampDate(Date activityTimestamp) {
//        this.activityTimestamp = activityTimestamp
//    }

    @Override
    JSONObject asJSON() {
        new JSONObject([author           : author.asJSONRef(),
                        action           : action.asJSON(),
                        activityTimestamp: activityTimestamp,
                        serviceItem      : serviceItem.asJSONMinimum(),
                        changeDetails    : changeDetails.collect { it.asJSON() }])
    }

    void bindFromJSON(JSONObject obj) {
        println(obj)
        this.with {
            action = obj.action
            activityTimestamp = obj.activityTimestamp
        }
    }
}
