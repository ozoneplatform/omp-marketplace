package marketplace.search

/**
 * Predicate for exact value searches
 */
class ExactValuePredicate extends SingleValuePredicate {

    @Override
    def getSearchClause() {
        return {
            must {
                query_string(default_field: this.indexFieldName, query: "\"${singleValue}\"")
            }
        }
    }

}
