package marketplace

import java.util.Map
import java.util.regex.Pattern

import org.codehaus.groovy.grails.web.json.JSONObject

import org.apache.commons.lang.StringUtils

class Constants {

    // TODO: See ticket MARKETPLACE-268: May want to use these constants from UserViews, instead
    public static final String VIEW_ADMIN = "Administrator"
    public static final String VIEW_USER = "User"
    public static final String VIEW_EXTERNAL = "External"

    public static final String PENDING_APPROVAL = "Pending Approval"
    public static final String HIDE = "hide"
    public static final String UNKNOWN = "unknown"
    public static final String ADMIN = "ROLE_ADMIN"
    public static final String USER = "ROLE_USER"
    public static final String EXTERNADMIN = "ROLE_EXTERN_ADMIN"

    public static final String TEXT_NAME_MP_DESCRIPTION = "mpDescription"
    public static final String TEXT_NAME_ABOUT = "About"
    public static final String TEXT_NAME_MP_ADVANCED_SEARCH_DESCRIPTION = "mpAdvancedSearchDescription"
    public static final String TEXT_NAME_ANALYST = "Analyst"
    public static final String TEXT_NAME_DEVELOPER = "Developer"
    public static final String TEXT_NAME_ADMINISTRATOR = "Administrator"
    public static final String TEXT_NAME_VERSION = "version"

    public static final String DB_HSQL = "HSQL"
    public static final String DB_ORACLE = "ORACLE"
    public static final String DB_MYSQL = "MYSQL"

    public static final String OPT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    public static final String EXTERNAL_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    public static final String EXTERNAL_DATE_PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssz"

    public static final def APPROVAL_STATUSES = ["IN_PROGRESS": "In Progress", "PENDING": "Pending", "APPROVED": "Approved", "REJECTED": "Rejected"]

    public static final String OMP_IMPORT_EXECUTOR = "Marketplace"
    public static final String FILE_BASED_IMPORT_TASK = "File Import"
    public static final String FILE_BASED_IMPORT_EXECUTOR = "File"
    public static final String OMP_IMPORT_DELTA_DATE_FIELD = "editedSinceDate"

    public static final String IMPORT_TYPE_FULL = "Entire"
    public static final String IMPORT_TYPE_DELTA = "Partial"

    public static final String FIELD_CREATEDDATE = 'createdDate'
    public static final String FIELD_EDITEDDATE = 'editedDate'
    public static final String FIELD_ACTIVITYDATE = 'activityDate'
    public static final String FIELD_IMAGELARGE = 'imageLargeUrl'
    public static final String FIELD_IMAGESMALL = 'imageSmallUrl'
    public static final String FIELD_LAUNCHURL = 'launchUrl'
    public static final String FIELD_RELEASEDATE = 'releaseDate'

    public static final String FIELD_SYSTEMURI = 'systemUri'
    public static final String FIELD_EXTERNALID = 'externalId'
    public static final String FIELD_UUID = 'uuid'
    public static final String FIELD_TITLE = 'title'
    public static final String FIELD_VERSION = 'versionName'
    public static final String FIELD_ORGANIZATION = 'organization'
    public static final String FIELD_DESCRIPTION = 'description'

    public static final String NORTH_BANNER = 'northBanner'
    public static final String SOUTH_BANNER = 'southBanner'

    public static final String INSIDE_OUTSIDE_ADMIN_SELECTED = 'ADMIN_SELECTED'
    public static final String INSIDE_OUTSIDE_ALL_OUTSIDE = 'ALL_OUTSIDE'
    public static final String INSIDE_OUTSIDE_ALL_INSIDE = 'ALL_INSIDE'

    public static final String JOB_ID_KEY = 'importTaskId'
    public static final String JOB_SERVICE_KEY = 'importService'

    /** Defines relative location of an image served by OMP ImageService; append image ID */
    public static final String OMP_IMAGE_URL_PREFIX = "/images/get/"

    public static final String DEFAULT_IMG_FILE_PREFIX = "/themes/common/images"
    public static final Map<String, String> DEFAULT_TYPE_IMAGE_FILES = ["Service:REST": "$DEFAULT_IMG_FILE_PREFIX/default_types_service_rest_icon.png",
        "Web App": "$DEFAULT_IMG_FILE_PREFIX/default_types_web_apps_icon.png",
        "Desktop Apps": "$DEFAULT_IMG_FILE_PREFIX/default_types_desktop_apps_icon.png",
        "Service:SOAP": "$DEFAULT_IMG_FILE_PREFIX/default_types_service_soap_icon.png",
        "Microformats": "$DEFAULT_IMG_FILE_PREFIX/default_types_microformats_icon.png",
        "Plugin": "$DEFAULT_IMG_FILE_PREFIX/default_types_plugin_icon.png",
        "OZONE App": "$DEFAULT_IMG_FILE_PREFIX/default_types_stack_icon.png",
        "App Component": "$DEFAULT_IMG_FILE_PREFIX/default_types_widget_icon.png",
        "default": "$DEFAULT_IMG_FILE_PREFIX/default_serviceitem_icon.png"]

    public static final String DEFAULT_SERVICE_ITEM_STATE = 'Active'

    public static final Pattern URL_PATTERN = ~/^(((https|http|ftp|sftp|file):\/)|(\/)){1}(.*)+$/
    // From RFC 3986 Appendix B (http://www.ietf.org/rfc/rfc3986.txt)
    public static final Pattern URL_PARSER = ~/^(([^#:\/?]+):)?(\/\/([^#\/?]*))?([^#?]*)(\\?([^#]*))?(#(.*))?/

    public static final int MAX_URL_SIZE = 2083  // see http://support.microsoft.com/kb/208427
    // and http://www.boutell.com/newfaq/misc/urllength.html

    private static final Map<String, String> SERVICE_ITEM_DB_NAME_TO_LISTING_NAME_MAP = createSiDbNameToListingNameMap()

    public static final int OLD_MARKETPLACE_ICON_SIZE = 9958

    private static Map<String, String> createSiDbNameToListingNameMap(){
		Map<String, String> siDbNameToListingNameMap = new HashMap<String, String>()
		//Primary Characteristics
		siDbNameToListingNameMap.put("title", "Name")
		siDbNameToListingNameMap.put("types", "Type")
		siDbNameToListingNameMap.put("state", "State")
		siDbNameToListingNameMap.put("versionName", "Version")
		siDbNameToListingNameMap.put("releaseDate", "Release Date")
		siDbNameToListingNameMap.put("description", "Description")

		//Type Properties
		siDbNameToListingNameMap.put("intents", "Intents")
		siDbNameToListingNameMap.put("imageSmallUrl", "Small Icon URL")
		siDbNameToListingNameMap.put("imageLargeUrl", "Large Icon URL")
		siDbNameToListingNameMap.put("launchUrl", "Launch URL")
		siDbNameToListingNameMap.put("opensInNewBrowserTab", "Opens in a new browser tab")
		siDbNameToListingNameMap.put("recommendedLayouts", "Recommended Layouts")
		siDbNameToListingNameMap.put("customFields", "Custom Fields")

		//Categories Widget
		siDbNameToListingNameMap.put("categories", "Categories")

		//Technical Properties
		siDbNameToListingNameMap.put("installUrl", "Installation URL")
		siDbNameToListingNameMap.put("docUrls", "Resources")

		//Marketplace References
		siDbNameToListingNameMap.put("author", "Owner")
		siDbNameToListingNameMap.put("techPocs", "Technical POCs")
		siDbNameToListingNameMap.put("organization", "Organization")
		siDbNameToListingNameMap.put("requirements", "Requirements")
		siDbNameToListingNameMap.put("dependencies", "Dependencies")
		siDbNameToListingNameMap.put("visibleInLaunch", "Visible")
		return Collections.unmodifiableMap(siDbNameToListingNameMap)
	}

	public static String getSiListingName(String fieldName){
		String siListingName = SERVICE_ITEM_DB_NAME_TO_LISTING_NAME_MAP.get(fieldName);
		if(StringUtils.isEmpty(siListingName)){
			return fieldName
		}
		return siListingName
	}

	public static final String translateApprovalStatus(String approvalStatusToTranslate){
		String translatedString = approvalStatusToTranslate
		if(StringUtils.isNotEmpty(approvalStatusToTranslate)){
			String foundKey = APPROVAL_STATUSES[approvalStatusToTranslate.toUpperCase()]
			if(StringUtils.isNotEmpty(foundKey)){
				translatedString = foundKey
			}
		}
		return translatedString
	}

	public static final enum Action{
            //TODO This really should go in the resource bundle
            CREATED("Created"),
            MODIFIED("Modified"),
            SUBMITTED("Submitted"),
            APPROVED("Approved"),
            REJECTED("Rejected"),
            ENABLED("Enabled"),
            DISABLED("Disabled"),
            ADDRELATEDTOITEM("Added as a requirement"),
            REMOVERELATEDTOITEM("Removed as a requirement"),
            ADDRELATEDITEMS("New requirements added"),
            REMOVERELATEDITEMS("Requirements removed"),
            REVIEW_EDITED("Review Edited"),
            REVIEW_DELETED("Review Deleted"),

            // AML-924 Inside/Outside
            INSIDE("Inside"),
            OUTSIDE("Outside"),

            // scorecard actions
            EMS_INCLUDED("EMS Included"),
            EMS_NOT_INCLUDED("EMS Not Included"),
            CLOUD_HOST_INCLUDED("Cloud Host Included"),
            CLOUD_HOST_NOT_INCLUDED("Cloud Host Not Included"),
            SECURITY_SERVICES_INCLUDED("Security Services Included"),
            SECURITY_SERVICES_NOT_INCLUDED("Security Services Not Included"),
            SCALE_INCLUDED("Scale Included"),
            SCALE_NOT_INCLUDED("Scale Not Included"),
            LICENSE_FREE_INCLUDED("License Free Included"),
            LICENSE_FREE_NOT_INCLUDED("License Free Not Included"),
            CLOUD_STORAGE_INCLUDED("Cloud Storage Included"),
            CLOUD_STORAGE_NOT_INCLUDED("Cloud Storage Not Included"),
            BROWSER_INCLUDED("Browser Included"),
            BROWSER_NOT_INCLUDED("Browser Not Included"),
            LOCAL_SCORECARD_QUESTION_UPDATED("Scorecard Updated"),
            TAG_CREATED("Tag Created"),
            TAG_DELETED("Tag Removed")

            Action(String description) {
                this.description = description
            }

            private final String description

            public String description() { return description }

            public JSONObject asJSON() {
                new JSONObject(name: name(), description: description)
            }
	}

    /**
     * An enumeration of valid sorting directions
     */
    public static final enum SortDirection {
        ASC, DESC;
    }

	public static final enum CustomFieldDefinitionStyleType {
		TEXT("Text", TextCustomFieldDefinition.class, TextCustomField.class),
		TEXT_AREA("Text Area", TextAreaCustomFieldDefinition.class, TextAreaCustomField.class),
		DROP_DOWN("Drop Down", DropDownCustomFieldDefinition.class, DropDownCustomField.class),
		IMAGE_URL("Image URL", ImageURLCustomFieldDefinition.class, ImageURLCustomField.class),
		CHECK_BOX("Checkbox", CheckBoxCustomFieldDefinition.class, CheckBoxCustomField.class)

		CustomFieldDefinitionStyleType(String styleTypeName, Class<CustomFieldDefinition> cfdClass, Class<CustomField> cfClass){
			this.styleTypeName = styleTypeName
			this.fieldDefinitionClass = cfdClass
			this.fieldClass = cfClass
		}

		private final String styleTypeName
		public final Class<CustomFieldDefinition> fieldDefinitionClass
		public final Class<CustomField> fieldClass

		public String styleTypeName(){ return styleTypeName }
		public CustomFieldDefinition newFieldDefinition(def params) {  return fieldDefinitionClass.newInstance(params) }
		public CustomField newField(def params) { return fieldClass.newInstance(params) }
	}

	public static final String getCFDStyleTypeListString(){
		StringBuffer styleTypeBuff = new StringBuffer()
		for(def styleType : CustomFieldDefinitionStyleType.values()){
			styleTypeBuff.append("${styleType},")
		}
		return styleTypeBuff.toString().substring(0, (styleTypeBuff.length() - 1))
	}

	public static final List<String> translateApprovalStatusList(List<String> approvalStatusListToTranslate){
		if(approvalStatusListToTranslate != null){
			List<String> translatedList = new ArrayList<String>()
			for(String currApprovalStatusToTranslate : approvalStatusListToTranslate){
				translatedList.add(translateApprovalStatus(currApprovalStatusToTranslate))
			}
			return translatedList
		}
		return null
	}
}
