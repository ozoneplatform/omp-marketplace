package marketplace

/**
 * Domain Validation helper methods
 */
class ValidationUtil {
    /**
     * Trims and validates a basic url string against the following protocols:
     * HTTPS, HTTP, FTP, SFTP, file.
     * @param url String The URL string to validate
     * @return Boolean True, if the string is valid; false, otherwise.
     */
    static Boolean validateUrl(String url) {
        return url?.trim()?.matches(Constants.URL_PATTERN)
    }

    /**
     * A function that can be used as a custom validator in a grails domain
     * constraint in order to validate the URL
     * @param errorCode The error code string to use to denote that a URL is invalid
     * @param url The URL string to validate
     * @return the errorCode string if there is a failure, null otherwise
     */
    static String validateUrlConstraint(String errorCode, String url) {
        if (!validateUrl(url))
            return errorCode
    }
}
