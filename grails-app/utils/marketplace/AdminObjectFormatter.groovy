package marketplace

import java.text.DateFormat
import org.apache.commons.lang.time.FastDateFormat
import java.util.Calendar


class AdminObjectFormatter {

    static FastDateFormat fullDateFormat = FastDateFormat.getInstance("EEEE MMMM dd, yyyy hh:mm:ss aa zzz ");
    // Used in log files
    static FastDateFormat simpleFullDateFormat = FastDateFormat.getInstance("EEE MMM dd, yyyy ");
    // Used in JSON
    static FastDateFormat simpleDateFormat = FastDateFormat.getInstance("MMM dd, yyyy hh:mm:ss aa zzz ");

    static FastDateFormat longDateFormat = FastDateFormat.getInstance("EEEE MMMM d, yyyy h:mm aa zzz ");
    static FastDateFormat shortDateFormat = FastDateFormat.getInstance("MM/dd/yyyy hh:mm aa zzz");

    static FastDateFormat miniDateFormat = FastDateFormat.getInstance("MM/dd/yyyy");

    static FastDateFormat shortDateNoTimeZoneFormat = FastDateFormat.getInstance("MM/dd/yyyy hh:mm aa");

    static boolean isSameDay(Date dateObj1, Date dateObj2){
        if((dateObj1 == null) || (dateObj2 == null)){return false}
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(dateObj1);
        cal2.setTime(dateObj2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay
    }

    static String creatorUsername(def adminObject) {
        def retVal
        if (adminObject.createdBy) {
            retVal = adminObject.createdBy?.username
        }
        return retVal
    }

    static String creatorNameDisplay(def adminObject) {
        if (adminObject.createdBy == null) {
            return ''
        }
        def returnVal
        returnVal = adminObject.createdBy?.displayName
        if (!returnVal) {
            returnVal = adminObject.createdBy?.username
        }
        if (returnVal == null) {
            // TODO: get this string from properties file
            returnVal = 'System'
        }

        return returnVal
    }

    static String editorUsername(def adminObject) {
        def retVal
        if (adminObject.editedBy) {
            retVal = adminObject.editedBy?.username
        }
        return retVal
    }

    static String editorNameDisplay(def adminObject) {
        if (adminObject.editedBy == null) {
            return ''
        }
        def returnVal
        returnVal = adminObject.editedBy?.displayName
        if (!returnVal) {
            returnVal = adminObject.editedBy?.username
        }
        if (returnVal == null) {
            // TODO: get this string from properties file
            returnVal = 'System'
        }

        return returnVal
    }

    static String simpleFullDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return simpleFullDateFormat.format(dateIn)
    }

    static String fullDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return fullDateFormat.format(dateIn)
    }

    static String standardDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return simpleDateFormat.format(dateIn)
    }

    static String standardMiniDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return miniDateFormat.format(dateIn)
    }

    static String standardShortDateDisplay(def dateIn) {
        if (dateIn == null) {
            return ''
        }
        return shortDateFormat.format(dateIn)
    }

    static String createdDateDisplay(def adminObject) {
        if (adminObject.createdDate == null) {
            return ''
        }
        return longDateFormat.format(adminObject.createdDate)
    }

    static String editedDateDisplay(def adminObject) {
        if (adminObject.editedDate == null) {
            return ''
        }
        return longDateFormat.format(adminObject.editedDate)
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
        return shortDateNoTimeZoneFormat.format(dateIn)
    }
}
