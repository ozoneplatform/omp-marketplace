package marketplace

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import com.google.common.base.MoreObjects

import ozone.marketplace.enums.OzoneSize

/*
 Properties associated with ServiceItems that represent OWF Widgets, describing how the widget
 should be presented in OWF.
*/
class OwfProperties extends AuditStamped implements Serializable, ToJSON {

    static final String DEFAULT_WIDGET_TYPE = 'standard'

    static bindableProperties = [
        'singleton', 'visibleInLaunch',
        'background', 'height',
        'stackDescriptor', 'width',
        'descriptorUrl', 'owfWidgetType',
        'stackContext', 'universalName',
        'intents', 'mobileReady'
    ]

    static modifiableReferenceProperties = ['intents']

    //list of properties that when updated, should trigger a changelog entry.  This is used
    //by the new REST API and not the old code
    static changeLogProperties = [
        'singleton', 'visibleInLaunch',
        'background', 'intents',
        'height', 'width',
        'owfWidgetType', 'universalName',
        'stackContext', 'stackDescriptor',
        'mobileReady'
    ]

    static long minDimension = 200L
    static long maxDimension = 2000L

    static searchable = {
        //root false
        owfWidgetType index: 'not_analyzed', excludeFromAll: false
        singleton index: 'not_analyzed', excludeFromAll: true
        visibleInLaunch index: 'not_analyzed', excludeFromAll: true
        background index: 'not_analyzed', excludeFromAll: true
        mobileReady index: 'not_analyzed', excludeFromAll: true
        height index: 'not_analyzed', excludeFromAll: true
        stackDescriptor index: 'analyzed', excludeFromAll: false
        width index: 'not_analyzed', excludeFromAll: true
        descriptorUrl index: 'not_analyzed', excludeFromAll: false
        intents component: true
        except = ['createdBy', 'createdDate', 'editedBy', 'editedDate', 'size', 'version', 'serviceItem']
    }

    static transients = ['size']

    // back-reference used for universalName validation.
    static belongsTo = [serviceItem: ServiceItem]

    boolean singleton = false
    boolean visibleInLaunch = true
    boolean background = false
    boolean mobileReady = false
    Long height
    Long width
    String owfWidgetType = DEFAULT_WIDGET_TYPE
    String stackContext
    String stackDescriptor
    String universalName
    String descriptorUrl
    Set intents = new HashSet()

    static constraints = {
        owfWidgetType(nullable: false, blank: false)
        singleton(nullable:false)
        visibleInLaunch(nullable:false)
        intents(nullable:true, modifiable: true)
        background(nullable:false)
        mobileReady(nullable:false)
        height min: this.minDimension, max: this.maxDimension, nullable: true
        width min: this.minDimension, max: this.maxDimension, nullable: true
        stackContext nullable: true, maxSize: 200
        stackDescriptor nullable: true
        universalName maxSize: 255, nullable: true, validator: { val, obj ->      //SQL Server treats nulls as comparable values,
            if(val) {                                                             //so a regular unique constraint won't work here
                def owfProps = OwfProperties.findByUniversalName(val)

                //for some strange reason, just using
                //OwfProperties.serviceItem does not retrieve
                //the correct object here
                def serviceItem = ServiceItem.findByOwfProperties(owfProps)
                if (owfProps && owfProps.id != obj.id &&
                        //handle the case where one owfProperties is being swapped out for another
                        serviceItem?.id &&
                        obj.serviceItem?.id != serviceItem?.id) {
                    return "unique.nameExists"
                }
            }
        }
        descriptorUrl(nullable: true, blank: true, maxSize: Constants.MAX_URL_SIZE, auditable: false)
        serviceItem(nullable: true, bindable: false)
    }

    static hasMany = [ intents : Intent ]


    static mapping = {
        cache true
        stackDescriptor type: 'text'
        intents index:'owfProps_intent_id_idx'
        intents joinTable: [
            name: 'owf_properties_intent',
            key: 'owf_properties_intents_id'
        ]
    }

    def beforeValidate() {
        //TODO BVEST added if statement
        intents.each {
            if (it?.metaClass?.respondsTo(it, 'beforeValidate')) {
                it.beforeValidate()
            }
        }
    }

    @Override
    JSONObject asJSON() {
        new JSONObject([id             : id,
                        singleton      : singleton,
                        visibleInLaunch: visibleInLaunch,
                        background     : background,
                        mobileReady    : mobileReady,
                        height         : height,
                        width          : width,
                        owfWidgetType  : owfWidgetType,
                        stackContext   : stackContext,
                        stackDescriptor: stackDescriptor,
                        universalName  : universalName,
                        descriptorUrl  : descriptorUrl,
                        intents        : new JSONArray(intents?.collect { it.asJSON() })])
    }

    public Long getHeight() {
        height ?: OzoneSize.MEDIUM.height
    }

    public Long getWidth() {
        width ?: OzoneSize.MEDIUM.width
    }

    /**
     * A setter that allows the height and width to be set using pre-set values associated with
     * the OzoneSize enum.
     *
     * @param size A string matching one of the enum constants from the OzoneSize enum
     */
    public void setSize(String size) {
        OzoneSize ozoneSize = OzoneSize.valueOf(size)
        this.height = ozoneSize.height
        this.width = ozoneSize.width
    }

    def isStack() {
        return (stackDescriptor && !stackDescriptor.isEmpty())
    }

    def convertToSize() {
        def size = "MEDIUM"
        OzoneSize.values().each {
            if(height == it.height && width == it.width) {
                size = it.name
            }
        }
        return size
    }

    void bindFromJSON(JSONObject obj) {
        this.with {
            singleton = obj.singleton
            visibleInLaunch = obj.visibleInLaunch
            background = obj.background
            height = obj.height
            stackDescriptor = obj.stackDescriptor
            width = obj.width
            descriptorUrl = obj.descriptorUrl
            owfWidgetType = obj.owfWidgetType
            stackContext = obj.stackContext
            universalName = obj.universalName
            mobileReady = obj.mobileReady
            intents = obj.intents
        }
    }

    void setUniversalName(String name) {
        universalName = null
        if(name) {
            universalName = name.isAllWhitespace() || name.size() == 0 ? null : name.trim()
        }
    }

    @Override
    String toString() {
        MoreObjects.toStringHelper(this).add("id", id).toString()
    }

}
