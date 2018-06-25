package ozone.utils


class SortOptions {

    final String field
    final String order

    SortOptions(String field, String order) {
        this.field = field
        this.order = order
    }

    static SortOptions sortOptions(Map params, String defaultField, Map fields) {
        new SortOptions(fields.asString(params.asString('sort'), defaultField),
                        params.asString('dir', 'desc').toLowerCase())
    }

}
