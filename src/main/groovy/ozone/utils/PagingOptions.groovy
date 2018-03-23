package ozone.utils


abstract class PagingOptions {

    static Map<String, ?> pagingOptions(Map params) {
        Map paging = [:]

        def max = params.asInteger('limit')
        def start = params.asInteger('start')

        if (max != null) paging.put('max', max)
        if (start != null) paging.put('offset', max)

        paging
    }

}
