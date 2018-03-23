package marketplace

//import java.sql.Date
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import ozone.utils.Utils

import static marketplace.ValidationUtil.validateUrl


class ServiceItem extends AuditStamped implements Serializable, ChangeLogging, ToJSON {

    private final DateFormat RELEASE_DATE_FORMAT =
        new SimpleDateFormat(Constants.RELEASE_DATE_FORMAT_STRING)

    //these two fields are used by the RestService to determine
    //how to handle marshalling of this domain
    final static bindableProperties = [
        'types',
        'owners',
        'categories',
        'state',
        'owfProperties',
        'customFields',
        'approvalStatus',
        'releaseDate',
        'agency',
        'title',
        'description',
        'requirements',
        'dependencies',
        'contacts',
        'versionName',
        'imageLargeUrl',
        'imageSmallUrl',
        'imageMediumUrl',
        'installUrl',
        'launchUrl',
        'docUrls',
        'isOutside',
        'screenshots',
        'isEnabled',
        'techPocs',
        'organization',
        'relationships',
        'recommendedLayouts',
        'opensInNewBrowserTab',
        'satisfiedScoreCardItems',
        'uuid'
    ]

    final static modifiableReferenceProperties = [
        'owfProperties',
        'docUrls',
        'customFields',
        'screenshots',
        'relationships',
        'contacts'
    ]

    static searchable = {
        types component: true
        owners component: true
        categories component: true
        tags component: true, excludeFromAll: false
        state component: true
        owfProperties component: true
        itemComments component: true
        customFields component: true
        lastActivityDate index: 'not_analyzed', excludeFromAll: true
//        approvalDate index: 'not_analyzed', excludeFromAll: true
        approvalDate excludeFromAll: true
        // Yes we need this much precision unless you want to see rounding errors between the short and detailed view
        avgRate index: 'not_analyzed', excludeFromAll: true
        totalRate5 index: 'not_analyzed', excludeFromAll: true
        totalRate4 index: 'not_analyzed', excludeFromAll: true
        totalRate3 index: 'not_analyzed', excludeFromAll: true
        totalRate2 index: 'not_analyzed', excludeFromAll: true
        totalRate1 index: 'not_analyzed', excludeFromAll: true
        totalVotes index: 'not_analyzed', excludeFromAll: true
        approvalStatus index: 'not_analyzed', excludeFromAll: false
//        releaseDate index: 'not_analyzed', excludeFromAll: true
        releaseDate excludeFromAll: true
        agency component: true
        title boost: 2.0
        sortTitle index: 'not_analyzed'
        description boost: 1.9
        requirements boost: 1.8
        dependencies boost: 1.7
        versionName index: 'not_analyzed', excludeFromAll: true
        totalComments index: 'not_analyzed', excludeFromAll: true
        imageSmallUrl index: 'not_analyzed', excludeFromAll: true
        imageMediumUrl index: 'not_analyzed', excludeFromAll: true
        imageLargeUrl index: 'not_analyzed', excludeFromAll: true
        installUrl index: 'not_analyzed', excludeFromAll: true
        launchUrl index: 'not_analyzed', excludeFromAll: true
        docUrls component: true, excludeFromAll: true
        uuid index: 'not_analyzed', excludeFromAll: false
        screenshots component: true, excludeFromAll: true
        contacts component: true, excludeFromAll: true
        isHidden index: 'not_analyzed', excludeFromAll: false
        isOutside index: 'not_analyzed', excludeFromAll: false
        only = [
                'state', 'tags', 'categories', 'owners', 'types', 'id', 'owfProperties',
                'screenshots', 'releaseDate', 'approvalDate', 'lastActivityDate',
                'customFields', 'itemComments', 'contacts', 'totalRate1', 'totalRate2',
                'totalRate3', 'totalRate4', 'totalRate5', 'totalVotes', 'avgRate',
                'description', 'requirements', 'dependencies', 'versionName', 'sortTitle',
                'title', 'agency', 'docUrls', 'uuid', 'launchUrl', 'installUrl',
                'imageLargeUrl', 'imageMediumUrl', 'imageSmallUrl', 'approvalStatus',
                'editedDate', 'isHidden', 'isOutside'
        ]
    }

    // Specifies that changes to serviceItems will be written to the database as ChangeDetail
    // records and which fields to ignore.
    static auditable = [ignore:[
        'version',
        'lastUpdated',
        'editedBy',
        'editedDate',
        'totalVotes',
        'avgRate',
        'totalRate5',
        'totalRate4',
        'totalRate3',
        'totalRate2',
        'totalRate1',
        'itemComments',
        'totalComments',
        'lastActivity',
        'isHidden',
        'rejectionListings',
        'serviceItemActivities',

        //these fields are technically auditable, but are associated with a separate activity
        'relationships',
        'isEnabled',
        'approvalStatus',
        'isOutside',

        //the following fields are audited as a special case in ServiceItemRestService
        'customFields',
        'owfProperties'
    ]]

    Date releaseDate
    Date approvalDate
    String title
    String description
    String launchUrl
    List<Profile> owners
    String installUrl
    String versionName
    // AML-1128 isOutside null initially, rather than false
    Boolean isOutside


    /** Hidden: administrator can unhide, no one else can see **/
    //TODO why is this an Integer?
    Integer isHidden = 0
    Boolean isEnabled = true
    String requirements
    String dependencies
    String organization

    Agency agency

    Float avgRate = 0F
    Integer totalVotes = 0
    Integer totalRate5 = 0
    Integer totalRate4 = 0
    Integer totalRate3 = 0
    Integer totalRate2 = 0
    Integer totalRate1 = 0
    String uuid = Utils.generateUUID()
    String imageSmallUrl
    String imageMediumUrl
    String imageLargeUrl
    Boolean opensInNewBrowserTab = false
    String approvalStatus = Constants.APPROVAL_STATUSES["IN_PROGRESS"]

    String toString() {
        return "${id}:${title}:${uuid}:${releaseDate}:${approvalStatus}"
    }

    String prettyPrint() {
        return "${id}:${title}:${uuid}:${releaseDate}:${approvalStatus}:${types}:${categories}:${customFields}"
    }

    State state
    Types types
    OwfProperties owfProperties
    List<CustomField> customFields
    Set<ItemComment> itemComments = new LinkedHashSet<ItemComment>()
    Integer totalComments = 0
    List<Category> categories
    List<Screenshot> screenshots = []
    SortedSet<RejectionListing> rejectionListings
    List<ServiceItemActivity> serviceItemActivities
    ServiceItemActivity lastActivity


    static transients = ['sortTitle', 'lastActivityDate', 'isEnabled']

    static hasMany = [categories: Category,
        owners: Profile,
        recommendedLayouts: RecommendedLayout,
        itemComments: ItemComment,
        customFields: CustomField,
        rejectionListings: RejectionListing,
        serviceItemActivities: ServiceItemActivity,
        docUrls: ServiceItemDocumentationUrl,
        screenshots: Screenshot,
        techPocs: String,
        relationships: Relationship,
        contacts: Contact,
        tags: ServiceItemTag,
        satisfiedScoreCardItems: ScoreCardItem]

    //so that GORM knows which property of the relationship is the backref
    static mappedBy = [relationships: 'owningEntity']

    static mapping = {
        cache true
        tablePerHierarchy false
        recommendedLayouts joinTable: 'si_recommended_layouts'
        recommendedLayouts batchSize: 50
        categories batchSize: 50
        customFields batchSize: 50
        serviceItemActivities batchSize: 50
        itemComments batchSize: 50
        itemComments cascade: "all-delete-orphan"
        rejectionListings batchSize: 50
        categories index: 'svc_item_cat_id_idx'
        customFields index:'svc_item_cst_fld_id_idx', cascade: 'all-delete-orphan'
        techPocs joinTable: [
            table: 'service_item_tech_pocs',
            column: 'tech_poc'
        ]
        screenshots indexColumn: [name: "ordinal", type: Integer], cascade: 'all-delete-orphan'
        contacts cascade: 'all-delete-orphan'
        relationships cascade: 'all-delete-orphan'
        docUrls cascade: 'all-delete-orphan'
        tags cascade: 'all-delete-orphan'
        satisfiedScoreCardItems joinTable: [name: 'service_item_score_card_item',
                                            column: 'score_card_item_id',
                                            key: 'service_item_id']
    }

    static constraints = {
        isOutside(nullable: true, auditable: false)
        title(blank: false, maxSize: 256)
        description(maxSize: 4000, nullable: true)
        versionName(maxSize: 256, nullable: true)
        types(blank: false)
        requirements(nullable: true, maxSize: 1000)
        dependencies(nullable: true, maxSize: 1000)
        organization(nullable: true, maxSize: 256)
        agency(nullable: true)
        totalVotes(bindable: true)
        avgRate(bindable: true)
        totalComments(bindable: false)
        totalRate1(nullable: true, bindable: false)
        totalRate2(nullable: true, bindable: false)
        totalRate3(nullable: true, bindable: false)
        totalRate4(nullable: true, bindable: false)
        totalRate5(nullable: true, bindable: false)
        tags(bindable: false)
        launchUrl(nullable: true, maxSize: Constants.MAX_URL_SIZE, validator: { val, obj ->
            if (obj.types?.hasLaunchUrl && (!val || 0 == val.trim().size())) {
                return [
                    'serviceItem.launchUrl.required'
                ]
            }
            if (obj.types?.hasLaunchUrl && !val && !validateUrl(val)) {
                return [
                    'serviceItem.launchUrl.url.invalid'
                ]
            }
            if (val != null && val.trim().size() > 0 && !validateUrl(val)) {
                return [
                    'serviceItem.launchUrl.url.invalid'
                ]
            }
        }
        )
        installUrl(nullable: true, maxSize: Constants.MAX_URL_SIZE, validator: { val, obj ->
            if (val != null && val.trim().size() > 0 && !validateUrl(val)) {
                return [
                    'serviceItem.installUrl.url.invalid'
                ]
            }
        }
        )
        categories(nullable: true)
        state(nullable: true)
        releaseDate(nullable: true)
        owfProperties(nullable: true, modifiable: true, auditable: false)
        uuid(nullable:false, matches: /^[A-Fa-f\d]{8}-[A-Fa-f\d]{4}-[A-Fa-f\d]{4}-[A-Fa-f\d]{4}-[A-Fa-f\d]{12}$/)
        imageSmallUrl(nullable:true, maxSize:Constants.MAX_URL_SIZE, validator:{ val, obj ->
            if(val?.trim()?.size() > 0 && !validateUrl(val)) {
                return [
                    'serviceItem.imageSmallUrl.url.invalid'
                ]
            }
        })
        imageMediumUrl(nullable:true, maxSize:Constants.MAX_URL_SIZE, validator:{ val, obj ->
            if(val?.trim()?.size() > 0 && !validateUrl(val)) {
                return [
                    'serviceItem.imageLargeUrl.url.invalid'
                ]
            }
        })
        imageLargeUrl(nullable:true, maxSize:Constants.MAX_URL_SIZE, validator:{ val, obj ->
            if(val?.trim()?.size() > 0 && !validateUrl(val)) {
                return [
                    'serviceItem.imageLargeUrl.url.invalid'
                ]
            }
        })
        approvalStatus(auditable: false, inList:Constants.APPROVAL_STATUSES.values().toList())
        lastActivity(nullable:true, bindable: false)
        approvalDate(nullable:true, bindable: false)
        recommendedLayouts(nullable:true)
        // Need to validate each custom field and propagate any errors up to serviceItem
        customFields(modifiable: true, auditable: false,
                validator: { cf, si, errs ->
            def valid = true
            cf?.every {
                if (!it?.validate()){
                    valid = false
                    it?.errors?.allErrors.each{error->
                        si.errors.rejectValue('customFields',error.code + ".CustomField",error.arguments,error.defaultMessage)
                    }
                }
            }
            return valid
        }
        )
        owners(validator: { val, si  ->
            if (val == null || val.isEmpty()) {
                return 'empty'
            }
        })
        isEnabled(bindable: true, auditable: false)
        isHidden(bindable: false, auditable: false)
        serviceItemActivities(bindable: false)
        itemComments(bindable: false)
        rejectionListings(bindable: false)
        docUrls(modifiable: true)
        relationships(modifiable: true, auditable: false)
        contacts(modifiable: true)
        screenshots(modifiable: true)
    }

//    void cleanBeforeSave() {
//        if (log.isDebugEnabled()) {
//            log.debug 'cleanBeforeSave: this.types = ' + this.types
//            customFields?.each { log.debug "${it} - ${it.customFieldDefinition?.types}" }
//        }
//
//        if (this.customFields?.retainAll({ it.customFieldDefinition.belongsToType(this.types) })) {
//            log.debug 'something was removed by service item type test!'
//            customFields?.each { log.debug it }
//        }
//
//        this.scrubCR()
//    }

    String cats2String() {
        def ret = ""
        this.categories?.each { c ->
            ret += c.title + ", "
        }

        if (ret.length() > 0) {
            ret = ret.substring(0, ret.length() - 2)
        }
        return ret
    }

    String lastActivityString() {
        def ret = ""
        if (lastActivity?.getAction()?.description()) {
            ret = lastActivity?.getAction()?.description()
            if (lastActivity?.getActivityTimestamp()?.getDateTimeString()) {
                def dateStr = AdminObjectFormatter.standardShortDateDisplay(lastActivity.activityTimestamp)
                ret += " on " + dateStr
            }
        }
        return ret
    }

    /**
     * Service Item agencies are now stored internally as separate objects.
     * However, for compatibility, the import/export format still needs to have
     * agency and agencyIcon as separate strings
     */
    private static void transformAgencyToLegacy(JSONObject json) {
        if (json.agency instanceof JSONObject) {
            json.agencyIcon = json.agency.iconUrl
            json.agency = json.agency.title
        }
    }

    /**
     * Legacy compat method
     */
    public void setAgencyIcon(String agencyIcon) {
        if (agencyIcon) {
            if (!agency) {
                agency = new Agency()
            }

            agency.iconUrl = agencyIcon
        }
    }

    /**
     * Legacy compat method
     */
    public void setAgency(String agencyName) {
        if (agencyName) {
            if (!agency) {
                agency = new Agency()
            }

            agency.title = agencyName
        }
    }
    public void setAgency(Agency agency) {
        this.agency = agency
    }

    /**
     * Screenshot legacy screenshots format
     */
    public void setScreenshot1Url(String url) {
        if (!screenshots) {
            screenshots = []
        }

        screenshots[0] = new Screenshot(smallImageUrl: url)
    }
    public void setScreenshot2Url(String url) {
        if (!screenshots) {
            screenshots = []
        }

        screenshots[1] = new Screenshot(smallImageUrl: url)
    }

    /**
     * Service Item screenshots are now stored internally as separate objects.
     * However, for compatibility, the import/export format still needs to have
     * the old format
     */
    private static void transformScreenshotsToLegacy(JSONObject json) {
        if (json.screenshots?.size() > 0) {
            json.screenshot1Url = json.screenshots[0]?.smallImageUrl
        }
        if (json.screenshots?.size() > 1) {
            json.screenshot2Url = json.screenshots[1]?.smallImageUrl
        }
    }

    /**
     * @return a JSONObject representing this service item in the format
     * required for backwards compatibility for export and extserviceitems
     */
    JSONObject asLegacyJSON() {

        JSONObject json = this.asJSON()

        transformAgencyToLegacy(json)
        transformScreenshotsToLegacy(json)

        json.author = this.owners[0]?.username

        if (this.releaseDate) {
            DateFormat legacyDateFormat = new SimpleDateFormat(Constants.EXTERNAL_DATE_FORMAT)
            legacyDateFormat.setTimeZone(TimeZone.getTimeZone('UTC'))
            json.releasedDate = legacyDateFormat.format(this.releaseDate)
        }

        json
    }

    // TODO: it would be nice if we just used the JSON marshaller registered in BootStrap.groovy
    @Override
    JSONObject asJSON() {
        return asJSON(null)
    }

    // The parameter requires allows the caller to pass in a list of required listings which will be
    // add to the JSON structure being returned.
    JSONObject asJSON(def requires) {
        def currJSON = marshall([id                     : id,
                                 title                  : title,
                                 versionName            : versionName,
                                 releaseDate            : releaseDate ? RELEASE_DATE_FORMAT.format(releaseDate)
                                                                      : null,
                                 approvalDate           : approvalDate,
                                 lastActivity           : [activityTimestamp: lastActivity?.activityTimestamp],
                                 approvalStatus         : approvalStatus,
                                 isOutside              : isOutside,
                                 isHidden               : isHidden,
                                 avgRate                : avgRate,
                                 agency                 : agency?.asJSON(),
                                 totalVotes             : totalVotes,
                                 totalRate5             : totalRate5,
                                 totalRate4             : totalRate4,
                                 totalRate3             : totalRate3,
                                 totalRate2             : totalRate2,
                                 totalRate1             : totalRate1,
                                 totalComments          : totalComments,
                                 categories             : categories?.collect { it.asJSONRef() } ?: [],
                                 customFields           : customFields?.collect { it.asJSON() } ?: [],
                                 owners                 : owners?.collect { it.username } ?: [],
                                 dependencies           : dependencies,
                                 description            : description,
                                 docUrls                : docUrls?.collect { it.asJSON() } ?: [],
                                 imageSmallUrl          : imageSmallUrl,
                                 imageMediumUrl         : imageMediumUrl,
                                 imageLargeUrl          : imageLargeUrl,
                                 installUrl             : installUrl,
                                 isPublished            : state?.isIsPublished(),
                                 launchUrl              : launchUrl,
                                 validLaunchUrl         : validateUrl(launchUrl),
                                 organization           : organization,
                                 satisfiedScoreCardItems: satisfiedScoreCardItems?.collect { it.asJSON() } ?: [],
                                 recommendedLayouts     : recommendedLayouts?.collect { it.name() } ?: [],
                                 requirements           : requirements,
                                 screenshots            : screenshots.collect { it?.asJSON() }.findAll { it != null },
                                 state                  : state?.asJSONRef(),
                                 techPocs               : techPocs ?: [],
                                 types                  : types?.asJSON(),
                                 class                  : getClass().toString(),
                                 uuid                   : uuid,
                                 ozoneAware             : types?.ozoneAware,
                                 isEnabled              : isEnabled,
                                 intents                : [],
                                 contacts               : contacts.collect { it.asJSON() },
                                 opensInNewBrowserTab   : opensInNewBrowserTab,
                                 relationships          : relationships.collect { it.asJSON() },
                                 tags                   : tags?.collect { it.asJSONwithCreatedBy() } ?: []])

        if (owners != null) {
            currJSON.put('owners', new JSONArray(owners?.collect {
                new JSONObject(id: it.id, name: it.displayName, username: it.username)
            }))
        }

        if (owfProperties != null) {
            currJSON.put("owfProperties", owfProperties.asJSON())
            //TODO remove this once the non-REST API is gone
            currJSON.put("intents", ((owfProperties.intents == null) ? new JSONArray() :
                new JSONArray(owfProperties.intents?.collect { it.asJSON()})))
        }
        if (requires != null) {
            currJSON.put("requires", new JSONArray(requires?.collect {
                new JSONObject(id: it.id, title: it.title, uuid: it.uuid)
            }))
        }

        return currJSON
    }

    void bindFromJSON(JSONObject obj) {
        this.with {
             releaseDate = obj.releasedDate
             approvalDate = obj.approvalDate
             title = obj.title
             description = obj.description
             launchUrl = obj.launchUrl
             owners = obj.owners
             installUrl = obj.installUrl
             versionName = obj.versionName
             isOutside = obj.isOutside

             isHidden = obj.isHidden

             requirements = obj.requirements
             dependencies = obj.dependencies
             organization = obj.organization

             agency = obj.agency

             avgRate = 0F
             totalVotes = 0
             totalRate5 = 0
             totalRate4 = 0
             totalRate3 = 0
             totalRate2 = 0
             totalRate1 = 0
             uuid = Utils.generateUUID()
             imageSmallUrl = obj.imageSmallUrl
             imageMediumUrl = obj.imageMediumUrl
             imageLargeUrl = obj.imageLargeUrl
             opensInNewBrowserTab = false
             approvalStatus = Constants.APPROVAL_STATUSES["IN_PROGRESS"]

             state = obj.state
             types = obj.types
             owfProperties = obj.owfProperties
            customFields = obj.customFields
             itemComments = obj.itemComments
             totalComments = 0
             categories = obj.categories
             screenshots = []
             rejectionListings = obj.rejectionListings
             serviceItemActivities = obj.serviceItemActivities
             lastActivity = obj.lastActivity
        }
    }

    def asJSONMinimum () {
        return new JSONObject(
            id: id,
            title: title,
            imageSmallUrl: imageSmallUrl
        )
    }

    //Used for affliated searches or to minimum data needed for the grid / list and hover badges
    def asJSONRef() {

        def json = new JSONObject(
                id: id,
                uuid: uuid,
                title: title,
                description: description,
                imageSmallUrl: imageSmallUrl,
                imageMediumUrl: imageMediumUrl,
                imageLargeUrl: imageLargeUrl,
                totalVotes: totalVotes,
                avgRate: avgRate,
                categoriesString: categories?.collect { it.title?.padLeft(it.title.size() + 1, " ") },
                typesString: types?.collect { it.title?.padLeft(it.title.size() + 1, " ") },
                agency:agency?.asJSON(),

                //These are not used in 7.0 and greater releases but are left here for backwards compatibility
                owners: owners ? new JSONArray(owners?.collect { new JSONObject(id: it.id, name: it.displayName, username: it.username) }) : null,
                versionName: versionName,
                releasedDate: releaseDate,
                lastActivity: lastActivity,
                state: state,
                types: types
        )

        if (owfProperties != null) {
            json.put("owfProperties", owfProperties.asJSON())
        }

        return json
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
        if (this.requirements) {
            this.requirements = this.requirements.replaceAll("\r", "")
        }
        if (this.dependencies) {
            this.dependencies = this.dependencies.replaceAll("\r", "")
        }
    }

    boolean hasAccess(String username) {
        if (this.owners?.find { it.username == username }) {
            return true
        } else {
            return !this.isHidden && this.statApproved()
        }
    }

    boolean statApproved() {
        return this.approvalStatus == Constants.APPROVAL_STATUSES["APPROVED"]
    }

    boolean statPending() {
        return this.approvalStatus == Constants.APPROVAL_STATUSES["PENDING"]
    }

    boolean statInProgress() {
        return this.approvalStatus == Constants.APPROVAL_STATUSES["IN_PROGRESS"]
    }

    boolean statRejected() {
        return this.approvalStatus == Constants.APPROVAL_STATUSES["REJECTED"]
    }

    boolean submittable() {
        return !(this.statApproved() || this.statPending())
    }

    Boolean isOWFCompatible() {
        return (statApproved() && types.ozoneAware && state?.isPublished)
    }

    Boolean isOWFAddable() {
        return (isOWFCompatible() && (owfProperties?.isStack() || (types.hasLaunchUrl && validateUrl(launchUrl))))
    }

    Boolean isHidden() {
        return this.isHidden
    }

    /**
     * Determines whether a service item can be launched based upon it's approval, published state
     * and launch URL.
     * @returns Boolean True, if the item can be launched; false, otherwise
     */
    Boolean isLaunchable() {
        return (this.statApproved() && state?.isPublished &&
            (types.hasLaunchUrl && validateUrl(launchUrl)))
    }

    @Override
    int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder()
        builder.append(id)
            .append(version)
            .append(uuid)
        def code = builder.toHashCode()
        return code;
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof ServiceItem) {
            ServiceItem other = (ServiceItem) obj
            EqualsBuilder builder = new EqualsBuilder()
            builder.append(id, other.id)
                .append(uuid, other.uuid)
                .append(version, other.version)
            return builder.isEquals();
        }
        return false;
    }

    String getSortTitle() {
        title?.toLowerCase()
    }

    Date getLastActivityDate() {
        lastActivity?.activityTimestamp
    }

    /**
     * Set the value of a custom field
     * @param name
     * @param value
     * @return
     */
    void propertyMissing(String name, value) {
        setCustomFieldValue(name, value)
    }

    public void setCustomFieldValue(String name, value) {
        // Verify the existence of a custom field with the given name
        CustomFieldDefinition customFieldDefinition = CustomFieldDefinition.findByName(name)
        if (customFieldDefinition) {
            CustomField customField = customFieldDefinition.styleType.newField([:])
            customField.customFieldDefinition = customFieldDefinition
            customField.setValue(value)

            if (!this.customFields) {
                this.customFields = new ArrayList<CustomField>()
            } else {
                // Search for an existing value and remove it
                CustomField existingField = this.customFields.find { it.customFieldName == name }
                if (existingField) this.customFields.remove(existingField)
            }
            this.customFields.add customField
        } else {
            throw new MissingPropertyException("Could not find custom field named " + name)
        }
    }

    /**
     * the flag internally known as 'hidden' is externally known as 'enabled'
     */
    public void setIsEnabled(boolean enabled) {
        this.isHidden = enabled ? 0 : 1
        this.isEnabled = enabled
    }
    public boolean getIsEnabled() {
        !this.isHidden
    }

//    void setReleasedDate(date) {
//        switch(date.getClass()) {
//            case String:
//                this.setReleasedDateStr(date)
//                break;
//            case Date:
//                this.setReleasedDateObj(date)
//                break;
//            default:
//                this.setReleasedDateStr(date)
//        }
//    }

    public void setReleasedDateFromStr(String dateString) throws ParseException {
        if (dateString && dateString != '') {
            try {
                releaseDate = RELEASE_DATE_FORMAT.parse(dateString)
            }
            catch (ParseException e) {
                try {
                    DateFormat dateFormat =
                        new SimpleDateFormat(Constants.EXTERNAL_DATE_PARSE_FORMAT)
                    releaseDate = dateFormat.parse(dateString)
                }
                catch (ParseException e2) {
                    //throw the original exception
                    throw e
                }
            }
        }

    }
//
//    /*def <T extends java.util.Date> */void setApprovalDate(Date date) {
//        approvalDate = date
//    }
//
//    def <T extends java.util.Date> void setReleasedDateObj(T date) {
//        releaseDate = date
//    }

    /**
     * Get the value of a custom field
     * @param name
     * @return
     */
    String propertyMissing(String name) {
        getCustomFieldValue(name)
    }

    public boolean hasCustomField(String name) {
        return (this.customFields.find { it?.customFieldDefinition?.name == name }) as boolean
    }

    private String getCustomFieldValue(String name) {
        CustomField customField = this.customFields.find { it?.customFieldDefinition?.name == name }
        if (customField) {
            customField.fieldValueText
        } else {
            throw new MissingPropertyException("Could not find custom field named " + name)
        }
    }

    static boolean findDuplicates(def obj) {
        !!(findByUuid(obj?.uuid) ?:
            OwfProperties.findByUniversalName(obj?.owfProperties?.universalName))
    }

    boolean isAuthor(Profile user) {
        isAuthor(user.username)
    }

    boolean isAuthor(String username) {
        this.owners?.find { it.username == username }
    }

    /**
     * Legacy support for JSON from single-owner stores
     */
    public void setOwner(Profile owner) {
        this.owners = [owner]
    }

    void addScreenshot(Screenshot screenshot) {
        this.addToScreenshots(screenshot)
    }

    static List<ServiceItem> findAllByAuthor(Profile user) {
        ServiceItem.where { owners { id == user.id } }.list()
    }

    def beforeValidate() {
        //ensure that carriage returns are always removed
        this.scrubCR()

        //make audit trail plugin work on child items
        (modifiableReferenceProperties + ['serviceItemActivities', 'lastActivity']).each { prop ->
            Utils.singleOrCollectionDo(this[prop]) {
                //call beforeValidate if it exists
                if (it?.metaClass?.respondsTo(it, 'beforeValidate')) {
                    it.beforeValidate()
                }
            }
        }
    }

    /**
     * Validates that the custom field values specified in the JSON
     * are for custom fields that this service item should actually
     * have. Any custom fields that shouldn't be there are silently deleted, since its possible
     * that they used to be valid for this type and were removed
     *
     * Also, make sure that all custom fields have been fully marshalled
     *
     * @param dto The ServiceItem being validated
     */
    public void processCustomFields() {
        Types type = this.types

        this.customFields?.retainAll { it.customFieldDefinition?.belongsToType(type) }

        //make sure that all FieldValues are transformed from DTOs into
        //proper FieldValues
        this.customFields?.grep {it instanceof DropDownCustomField}?.each {
            it.marshallAllFieldValues()
        }
    }



    public void checkOwfProperties() {
        boolean ozoneAware = this.types?.ozoneAware ?: false

        //if ozoneAware we should always have an OwfProperties
        if (ozoneAware && !this.owfProperties) {
            this.owfProperties = new OwfProperties()
        }
        //of not ozoneAware we should never have an OwfProperties
        else if (!ozoneAware && this.owfProperties) {
            //since this method is called on the dto, the owfProperties is still
            //transient and all we have to do is null it out
            this.owfProperties = null
        }
    }

    /**
     * Potentially override the inside/outside setting of the listing based on the
     */
    public void updateInsideOutsideFlag(String globalInsideOutside) {
        switch (globalInsideOutside) {
            case Constants.INSIDE_OUTSIDE_ALL_OUTSIDE:
                this.isOutside = true
                break
            case Constants.INSIDE_OUTSIDE_ALL_INSIDE:
                this.isOutside = false
                break
            default:
                //leave as-is
                break
        }
    }

    /**
     * Update the rating statistics fields to be up to date with the
     * current list of ItemComments.  The following fields are updated:
     * totalComments, totalRate*, totalVotes, and avgRate
     */
    public void updateRatingStats() {
        if (this.itemComments == null) this.itemComments = new HashSet()

        this.totalComments = this.itemComments.size()

        //all of the non-null rating values
        Collection<Float> ratings = this.itemComments.grep { it.rate != null }.collect { it.rate }

        //the rating values grouped
        Map<Integer, Collection<Float>> groupedRatings = ratings.groupBy { Math.round(it) }

        //update each of the totalRating1 ... totalRating5 counts
        (1..5).each { rating ->
            this."totalRate$rating" = groupedRatings[rating]?.size() ?: 0
        }

        this.totalVotes = ratings.size()
        this.avgRate = this.totalVotes ? (ratings.sum() ?: 0) / this.totalVotes : 0F
    }
}
