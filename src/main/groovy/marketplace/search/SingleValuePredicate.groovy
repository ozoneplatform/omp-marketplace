package marketplace.search

/**
 * Represents a search criterion with a single value
 */
class SingleValuePredicate extends AbstractPredicate {
    String singleValue

    @Override
    Predicate initializeFromParameters(params) {
        if (params[(paramName)]) this.singleValue = params[(paramName)]
        return this
    }

    @Override
    def getSearchClause() {
        return {
            must {
                if (isCustomField)
                    query_string(default_field: "_all", query: "(customFieldName:${indexFieldName} AND fieldValueText:${singleValue})")
                else
                    query_string(default_field: this.indexFieldName, query: singleValue)
            }
        }
    }

    String getStringValue() {
        return singleValue
    }

    def getValue() {
        singleValue
    }
}
