package marketplace.search

/**
 * Represents a predicate which may have multiple values joined by an OR or AND boolean operator.
 */
class MultiValueFilterPredicate extends MultiValuePredicate {
    @Override
    def getSearchClause() {
        return {
            valueList.each { value ->
                must {
                    nested {
                        path = this.predicateName
                        query {
                            query_string(default_field: this.indexFieldName, query: value)
                        }
                    }                    
                }                    
            }
        }
    }
}