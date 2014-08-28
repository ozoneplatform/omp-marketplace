package marketplace

import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Created by IntelliJ IDEA.
 * Date: Jul 21, 2010
 * Time: 4:33:22 PM
 */

@gorm.AuditStamp
public class RejectionListing implements Comparable, Serializable {

    static bindableProperties = [
        'justification',
        'serviceItem',
        'description'
    ]

    static modifiableReferenceProperties = []

    String description

    static belongsTo = [
            serviceItem: ServiceItem,
            author: Profile,
            justification: RejectionJustification
    ]

    static constraints = {
        justification(blank: true, nullable: true)
        description(blank: true, nullable: true, maxSize: 2000, validator: {
            if (it && it.size() > 2000) {
                return ["rejectionListing.description.maxsize", 2000]
            } else
                return true
        })
    }

    static mapping = {
        sort 'description'
        cache true
        author fetch: 'join'
        author index: 'rej_lst_author_id_idx'
        serviceItem index:'rej_lst_svc_item_id_idx'
    }

    int compareTo(obj) {
        if (createdDate == null) {
            return 1
        }
        else {
            int result = createdDate.compareTo(obj.createdDate)

            if (result > 0) {
                return -1
            } else if (result < 0) {
                return 1
            } else {
                if (id && obj.id) {
                    return obj.id.compareTo(id)
                } else if (description && obj.description) {
                    return description.compareTo(obj.description)
                } else {
                    return 0
                }
            }
        }
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    def asJSON() {
        new JSONObject(
            id: id,
                description: description,
                author: author?.asJSONRef(),
                justification: justification?.asJSON(),
                createdDate: createdDate
        )
    }

    //ensure that carriage returns are always removed
    def beforeValidate() {
        this.scrubCR()
    }
}
