package marketplace

import java.text.DateFormat
import org.apache.commons.lang.time.FastDateFormat


class AdminObjectFormatter {

    private static final FastDateFormat FULL_DATE_FORMAT =
            FastDateFormat.getInstance("EEEE MMMM dd, yyyy hh:mm:ss aa zzz ")

    // Used in log files
    private static final FastDateFormat SIMPLE_FULL_DATE_FORMAT =
            FastDateFormat.getInstance("EEE MMM dd, yyyy ")

    // Used in JSON
    private static final FastDateFormat SIMPLE_DATE_FORMAT =
            FastDateFormat.getInstance("MMM dd, yyyy hh:mm:ss aa zzz ")

    private static final FastDateFormat LONG_DATE_FORMAT =
            FastDateFormat.getInstance("EEEE MMMM d, yyyy h:mm aa zzz ")

    private static final FastDateFormat SHORT_DATE_FORMAT =
            FastDateFormat.getInstance("MM/dd/yyyy hh:mm aa zzz")

    private static final FastDateFormat MINI_DATE_FORMAT =
            FastDateFormat.getInstance("MM/dd/yyyy")

    private static final FastDateFormat SHORT_DFATE_NO_TZ_FORMAT =
            FastDateFormat.getInstance("MM/dd/yyyy hh:mm aa")

    static boolean isSameDay(Date dateObj1, Date dateObj2){
        if((dateObj1 == null) || (dateObj2 == null)){return false}
        Calendar cal1 = Calendar.getInstance()
        Calendar cal2 = Calendar.getInstance()
        cal1.setTime(dateObj1)
        cal2.setTime(dateObj2)
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        return sameDay
    }

    static String creatorUsername(AuditStamped adminObject) {
        if (!adminObject.createdBy) return null

        return adminObject.findCreatedByProfile()?.username
    }

    static String creatorNameDisplay(AuditStamped adminObject) {
        if (!adminObject.createdBy) return ''

        Profile creator = adminObject.findCreatedByProfile()

        return getProfileDisplayName(creator)
    }

    static String editorUsername(AuditStamped adminObject) {
        if (!adminObject.editedBy) return null

        return adminObject.findEditedByProfile()?.username
    }

    static String editorNameDisplay(AuditStamped adminObject) {
        if (!adminObject.editedBy) return ''

        Profile editor = adminObject.findEditedByProfile()

        return getProfileDisplayName(editor)
    }

    static String getProfileDisplayName(Profile profile) {
        profile?.displayName ?: profile?.username ?: Profile.SYSTEM_USER_NAME
    }

    static String simpleFullDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return SIMPLE_FULL_DATE_FORMAT.format(dateIn)
    }

    static String fullDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return FULL_DATE_FORMAT.format(dateIn)
    }

    static String standardDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return SIMPLE_DATE_FORMAT.format(dateIn)
    }

    static String standardMiniDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return MINI_DATE_FORMAT.format(dateIn)
    }

    static String standardShortDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return SHORT_DATE_FORMAT.format(dateIn)
    }

    static String createdDateDisplay(AuditStamped adminObject) {
        if (adminObject.createdDate == null) {
            return ''
        }
        return LONG_DATE_FORMAT.format(adminObject.createdDate)
    }

    static String editedDateDisplay(AuditStamped adminObject) {
        if (adminObject.editedDate == null) {
            return ''
        }
        return LONG_DATE_FORMAT.format(adminObject.editedDate)
    }

    static String lastLoginDateDisplay(def adminObject) {
        if(adminObject.lastLogin == null) {
            return ''
        }
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(adminObject.lastLogin)
    }

    static String noTimeZoneDisplay(long dateIn){
        if(dateIn == null) {
            return ''
        }
        return SHORT_DFATE_NO_TZ_FORMAT.format(dateIn)
    }

}
