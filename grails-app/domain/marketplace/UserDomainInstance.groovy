package marketplace

/**
 * Domain class to save information related to user preferences within marketplace
 *
 */
@gorm.AuditStamp
class UserDomainInstance implements Serializable {

	String username
	Map preferences = [:]

    static constraints = {
        username(blank: false, nullable: false, minSize: 1)
    }

    static mapping = {
        cache true
        table 'U_DOMAIN'
    }

    /* Key for preference for the selected theme for a particular user */
    public static String SELECTED_THEME = 'ozone.marketplace.theme.selected'

    public static String ENABLE_ANIMATIONS = 'ozone.marketplace.animations.enabled'

    public static String ENABLE_SPA = 'ozone.marketplace.spa.enabled'

    /**
     */
    def getTheme() {
        return preferences[SELECTED_THEME]
    }

    /**
     */
    def setTheme(String themeIn) {
        if (themeIn) {
            preferences[SELECTED_THEME] = themeIn

        } else {
            preferences.remove SELECTED_THEME
        }
    }

    // want this to always return true or false, never null
    def getAnimationsEnabled() {
        if (preferences[ENABLE_ANIMATIONS] == 'true')
            return true
        else
            return false;
    }

    def setAnimationsEnabled(def enabled) {
        preferences[ENABLE_ANIMATIONS] = enabled
    }

    def getSPAEnabled() {
        if (preferences[ENABLE_SPA] == 'true')
            return true
        else
            return false;
    }

    def setSPAEnabled(def enabled) {
        preferences[ENABLE_SPA] = enabled
    }

	def setHelpModalActive(String page, String show) {
		preferences[page] = show
	}

	def getHelpModalActive(String page) {
		return preferences[page]
	}
}
