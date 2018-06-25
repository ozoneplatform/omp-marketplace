package marketplace.rest

import groovy.transform.CompileStatic

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import com.google.common.collect.Sets
import marketplace.Profile

import static marketplace.rest.JsonAssertionError.formatValue


@CompileStatic
class JsonMatchers {

    static JsonMatcher isAnyValue() {
        new AnyValueMatcher()
    }

    static JsonMatcher isNotNull() {
        new NotNullMatcher()
    }

    static Map<String, ?> hasId() {
        [id: isNotNull()]
    }

    static Map<String, ?> hasId(Long id) {
        [id: id]
    }

    static Map<String, ?> hasAuditStamp(Profile createdAndEditedBy) {
        hasAuditStamp(createdAndEditedBy, createdAndEditedBy)
    }

    static Map<String, ?> hasAuditStamp(Profile createdBy, Profile editedBy) {
        [createdDate: isNotNull(),
         createdBy  : [id      : createdBy.id,
                       username: createdBy.username,
                       name    : createdBy.display()],
         editedDate : isNotNull(),
         editedBy   : [id      : editedBy.id,
                       username: editedBy.username,
                       name    : editedBy.display()]]
    }

    static void matches(Object actualValue, Object expectedValue) {
        matchInternal(actualValue, expectedValue, false)
    }

    static void matches(Object actualValue, Closure<?> closure) {
        matchInternal(actualValue, closure.call(), false)
    }

    static void strictlyMatches(Object actualValue, Object expectedValue) {
        matchInternal(actualValue, expectedValue, true)
    }

    static void strictlyMatches(Object actualValue, Closure<?> closure) {
        matchInternal(actualValue, closure.call(), true)
    }

    private static void matchInternal(Object actualValue, Object expectedValue, boolean strict = true, String path = '$') {
        if (expectedValue instanceof JsonMatcher) {
            expectedValue.match(actualValue, path)
        }
        else if (expectedValue instanceof Map) {
            assert actualValue instanceof JSONObject
            matchesObject(actualValue, expectedValue, strict, path)
        }
        else if (expectedValue instanceof List) {
            assert actualValue instanceof JSONArray
            matchesArray(actualValue, expectedValue, strict, path)
        }
        else if (expectedValue instanceof Date) {
            String expectedDateString = JsonUtil.formatIsoDate(expectedValue)
            if (actualValue != expectedDateString) {
                throw JsonAssertionError.wrongValue(path, expectedDateString, actualValue)
            }
        }
        else if (actualValue != expectedValue) {
            throw JsonAssertionError.wrongValue(path, expectedValue, actualValue)
        }
    }

    private static void matchesObject(JSONObject actual, Map<String, ?> expected, boolean strict, String path) {
        validateObjectKeys(actual, expected, strict, path)

        expected.forEach { key, expectedValue ->
            matchInternal(actual.get(key), expectedValue, strict, path + ".$key")
        }
    }

    private static void validateObjectKeys(JSONObject actual, Map<String, ?> expected, boolean strict, String path) {
        Set<String> actualKeys = actual.keySet()
        Set<String> expectedKeys = expected.keySet()
        if (strict && actualKeys != expectedKeys) {
            throw JsonAssertionError.wrongKeys(path, expectedKeys, actualKeys)
        }
        else if (!strict) {
            Set<String> missingKeys = Sets.difference(expectedKeys, actualKeys)
            if (!missingKeys.isEmpty()) {
                throw JsonAssertionError.missingKeys(path, missingKeys)
            }
        }
    }

    private static void matchesArray(JSONArray actual, List expected, boolean strict, String path) {
        validateArraySize(strict, actual, expected, path)

        expected.eachWithIndex{ expectedValue, i ->
            matchInternal(actual.get(i), expectedValue, strict, path + ".$i")
        }
    }

    private static void validateArraySize(boolean strict, JSONArray actual, List expected, String path) {
        if (strict && actual.size() != expected.size()) {
            throw JsonAssertionError.wrongSize(path, expected.size(), actual.size())
        }
    }

}

interface JsonMatcher {

    void match(Object actualValue, String path)

}


class NotNullMatcher implements JsonMatcher {

    @Override
    void match(Object actualValue, String path) {
        if (actualValue == null) {
            throw new JsonAssertionError("$path: expected <not null>, got ${formatValue(actualValue)}")
        }
    }

    @Override
    String toString() {
        "<not null>"
    }

}

class AnyValueMatcher implements JsonMatcher {

    @Override
    void match(Object actualValue, String path) {
    }

    @Override
    String toString() {
        "<any value>"
    }

}
