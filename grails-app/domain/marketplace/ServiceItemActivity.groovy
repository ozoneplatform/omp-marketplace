package marketplace

import java.text.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat


import gorm.AuditStamp
import org.codehaus.groovy.grails.web.json.JSONObject
import marketplace.Constants.Action

//Domain object that holds an audit trail of a listing.
@AuditStamp
class ServiceItemActivity implements Serializable {

    static constraints = {
        action maxSize: 128
    }

    Action action
    Date activityDate = new Date()

    static belongsTo = [serviceItem: ServiceItem, author: Profile]

    static hasMany = [changeDetails: ChangeDetail]

    static mapping = {
        author fetch: 'join'
        cache true
        tablePerHierarchy false
        serviceItem index: 'svc_item_act_svc_item_id_idx'
        changeDetails batchSize: 25
    }

    String toString(){
        return AdminObjectFormatter.fullDateDisplay(activityDate)
    }

    String prettyPrint() {
        toString()
    }

    public void setActivityDate(String dateString) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT)
        activityDate = dateFormat.parse(dateString)
    }
    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate
    }

    JSONObject asJSON() {
        new JSONObject(
            author: author.asJSONRef(),
            action: action.asJSON(),
            activityDate: activityDate,
            serviceItem: serviceItem.asJSONMinimum(),
            changeDetails: changeDetails.collect { it.asJSON() }
        )
    }
}
