package marketplace.search

/**
 * Represents an exact value predicate which may have multiple values joined by an OR or AND boolean operator.
 */
class MultiExactValuePredicate extends MultiValuePredicate {

    @Override
    def initializeValue(String value) {
        this.valueList = value.split(",")
    }

    @Override
    def initializeValue(List<String> value) {
        this.valueList = value
    }

    @Override
    def getSearchClause() {
        String queryString = buildQuery()
        return {
            must {
                if (isCustomField)
                    query_string(default_field: "_all", query: "(customFieldName:${indexFieldName} AND fieldValueText:${queryString})")
                else
                    query_string(default_field: this.indexFieldName, query: queryString)
            }
        }
    }

    private String buildQuery() {
        String query = ''
        valueList.eachWithIndex { value, index ->
            if (index > 0) query += " $operator "
            query += "\"${value}\""
        }
        query
    }

}