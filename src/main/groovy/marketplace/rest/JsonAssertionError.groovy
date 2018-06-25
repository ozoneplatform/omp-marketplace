package marketplace.rest

import com.google.common.collect.Sets


class JsonAssertionError extends RuntimeException {

    JsonAssertionError(String message) {
        super(message)
    }

    static wrongValue(String path, Object expected, Object actual) {
        new JsonAssertionError("$path: wrong value - expected ${formatValue(expected)}, got ${formatValue(actual)}")
    }

    static wrongKeys(String path, Set<String> expectedKeys, Set<String> actualKeys) {
        Set<String> missingKeys = Sets.difference(expectedKeys, actualKeys)
        Set<String> extraKeys = Sets.difference(actualKeys, expectedKeys)

        String message = "$path: wrong object keys -"
        if (missingKeys.size() > 0) {
            message += " missing: [${missingKeys.join(', ')}]"
        }
        if (extraKeys.size() > 0) {
            message += " extra: [${extraKeys.join(', ')}]"
        }

        new JsonAssertionError(message)
    }

    static missingKeys(String path, Set<String> missingKeys) {
        new JsonAssertionError("$path: wrong object keys - missing: [${missingKeys.join(', ')}]")
    }

    static wrongSize(String path, int expected, int actual) {
        new JsonAssertionError("$path: wrong array size - expected $expected, got $actual")
    }

    static String formatValue(Object value) {
        value != null ? "${value.class.simpleName} '$value'" : '<null>'
    }

}
