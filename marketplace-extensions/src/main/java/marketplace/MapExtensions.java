package marketplace;

import groovy.lang.GString;
import org.codehaus.groovy.runtime.NullObject;

import java.util.Map;


public class MapExtensions {

    public static String asString(final Map<String, ?> self, String property) {
        Object value = self.get(property);

        if (value == null) return null;

        if (value instanceof String) return (String) value;
        if (value instanceof GString) return ((GString) value).toString();

        return null;
    }

    public static String asString(final Map<String, ?> self, String property, String defaultValue) {
        String result = asString(self, property);

        return result != null ? result : defaultValue;
    }

    public static boolean getBoolean(final NullObject self, String property, boolean defaultValue) {
        return defaultValue;
    }

    public static boolean getBoolean(final Map<String, ?> self, String property, boolean defaultValue) {
        if (!self.containsKey(property)) return defaultValue;

        Object value = self.get(property);

        return value instanceof Boolean ? (boolean) value : defaultValue;
    }

    public static Integer asInteger(final Map<String, ?> self, String property) {
        Object value = self.get(property);

        if (value == null) return null;

        if (value instanceof Number) return ((Number) value).intValue();

        if (value instanceof String) return parseInteger((String) value);
        if (value instanceof GString) return parseInteger(((GString) value).toString());

        return null;
    }

    public static Long asLong(final Map<String, ?> self, String property) {
        Object value = self.get(property);

        if (value == null) return null;

        if (value instanceof Number) return ((Number) value).longValue();

        if (value instanceof String) return parseLong((String) value);
        if (value instanceof GString) return parseLong(((GString) value).toString());

        return null;
    }

    private static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value, 10);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static Long parseLong(String value) {
        try {
            return Long.parseLong(value, 10);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

}
