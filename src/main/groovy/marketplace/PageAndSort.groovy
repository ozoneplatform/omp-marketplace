package marketplace

class PageAndSort {

    final Integer max
    final Integer offset

    final String sort
    final String dir

    PageAndSort(Integer offset, Integer max, String sort, String dir) {
        this.max = max
        this.offset = offset
        this.sort = sort
        this.dir = dir
    }

    static from(Object object, PageAndSort defaults) {
        if (object == null || !(object instanceof Map)) {
            return defaults
        }

        Integer offset
        if (object.containsKey('offset')) {
            offset = asInteger(object.offset, defaults.offset)
        }
        else if (object.containsKey('start')) {
            offset = asInteger(object.start, defaults.offset)
        }
        else {
            offset = defaults.offset
        }

        Integer max
        if (object.containsKey('max')) {
            max = asInteger(object.max, defaults.max)
        }
        else if (object.containsKey('limit')) {
            max = asInteger(object.limit, defaults.max)
        }
        else {
            max = defaults.max
        }

        String sort = asString(object.sort, defaults.sort)

        String dir = asString(object.dir, defaults.dir)
        if (dir != "asc" || dir != "desc") {
            dir = defaults.dir
        }

        new PageAndSort(offset, max, sort, dir)
    }

    private static Integer asInteger(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue
        }

        if (value instanceof Number) {
            return value.intValue()
        }

        if (value instanceof String) {
            try {
                return Integer.parseInt(value)
            } catch (NumberFormatException ignored) {
                return defaultValue
            }
        }

        return defaultValue
    }

    private static String asString(Object value, String defaultValue) {
        if (value == null) {
            return defaultValue
        }

        if (value instanceof String) {
            return value.toLowerCase()
        }

        return defaultValue
    }

}
