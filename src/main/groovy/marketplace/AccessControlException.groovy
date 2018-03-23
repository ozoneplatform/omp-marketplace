package marketplace

/**
 * Extends RuntimeException so this can be unchecked to avoid boiler plate exception handling code.
 */
class AccessControlException extends RuntimeException {
    AccessControlException(String msg) {
        super(msg)
    }
}

