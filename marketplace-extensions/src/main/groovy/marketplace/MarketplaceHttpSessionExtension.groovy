package marketplace

import groovy.transform.CompileStatic
import javax.servlet.http.HttpSession


@CompileStatic
class MarketplaceHttpSessionExtension {

    public static final String ALLOW_SHOW_ACCESS_ALERT = 'allowShowAccessAlert'
    public static final String SHOW_ACCESS_ALERT = 'showAccessAlert'
    public static final String ACCESS_ALERT_SHOWN = 'accessAlertShown'
    public static final String ACCESS_ALERT_MSG = 'accessAlertMsg'

    public static final String INITIAL_REQUEST_URL = 'initalRequestURL'
    public static final String REDIRECT_TO_SHOW_ALERT_VIEW_INITIATED = 'redirectToShowAlertViewInitiated'

    public static final String PROFILE_ID = 'profileID'
    public static final String ACCESS_TYPE = 'accessType'
    public static final String USERNAME = 'username'
    public static final String FULLNAME = 'fullname'
    public static final String IS_ADMIN = 'isAdmin'
    public static final String ANIMATIONS_ENABLED = 'animationsEnabled'


    static boolean getAllowShowAccessAlert(final HttpSession self) {
        self.getAttribute(ALLOW_SHOW_ACCESS_ALERT) as boolean
    }

    static void setAllowShowAccessAlert(final HttpSession self, boolean value) {
        self.setAttribute(ALLOW_SHOW_ACCESS_ALERT, value)
    }

    static boolean getShowAccessAlert(final HttpSession self) {
        self.getAttribute(SHOW_ACCESS_ALERT) as boolean
    }

    static void setShowAccessAlert(final HttpSession self, boolean value) {
        self.setAttribute(SHOW_ACCESS_ALERT, value)
    }

    static boolean getAccessAlertShown(final HttpSession self) {
        self.getAttribute(ACCESS_ALERT_SHOWN) as boolean
    }

    static void setAccessAlertShown(final HttpSession self, boolean value) {
        self.setAttribute(ACCESS_ALERT_SHOWN, value)
    }

    static String getAccessAlertMsg(final HttpSession self) {
        self.getAttribute(ACCESS_ALERT_MSG) as String
    }

    static void setAccessAlertMsg(final HttpSession self, String value) {
        self.setAttribute(ACCESS_ALERT_MSG, value)
    }


    static String getInitalRequestURL(final HttpSession self) {
        self.getAttribute(INITIAL_REQUEST_URL) as String
    }

    static void setInitalRequestURL(final HttpSession self, String value) {
        self.setAttribute(INITIAL_REQUEST_URL, value)
    }

    static boolean getRedirectToShowAlertViewInitiated(final HttpSession self) {
        self.getAttribute(REDIRECT_TO_SHOW_ALERT_VIEW_INITIATED) as boolean
    }

    static void setRedirectToShowAlertViewInitiated(final HttpSession self, boolean value) {
        self.setAttribute(REDIRECT_TO_SHOW_ALERT_VIEW_INITIATED, value)
    }


    static Long getProfileID(final HttpSession self) {
        self.getAttribute(PROFILE_ID) as Long
    }

    static void setProfileID(final HttpSession self, Long value) {
        self.setAttribute(PROFILE_ID, value)
    }

    static String getAccessType(final HttpSession self) {
        self.getAttribute(ACCESS_TYPE) as String
    }

    static void setAccessType(final HttpSession self, String value) {
        self.setAttribute(ACCESS_TYPE, value)
    }

    static String getUsername(final HttpSession self) {
        self.getAttribute(USERNAME) as String
    }

    static void setUsername(final HttpSession self, String value) {
        self.setAttribute(USERNAME, value)
    }

    static String getFullname(final HttpSession self) {
        self.getAttribute(FULLNAME) as String
    }

    static void setFullname(final HttpSession self, String value) {
        self.setAttribute(FULLNAME, value)
    }

    static boolean getIsAdmin(final HttpSession self) {
        self.getAttribute(IS_ADMIN) as boolean
    }

    static void setIsAdmin(final HttpSession self, boolean value) {
        self.setAttribute(IS_ADMIN, value)
    }

    static boolean getAnimationsEnabled(final HttpSession self) {
        self.getAttribute(ANIMATIONS_ENABLED) as boolean
    }

    static void setAnimationsEnabled(final HttpSession self, boolean value) {
        self.setAttribute(ANIMATIONS_ENABLED, value)
    }

}
