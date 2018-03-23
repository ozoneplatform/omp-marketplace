package marketplace.search

/**
 * Represents a predicate which may have multiple values joined by an OR or AND boolean operator.
 */
class MultiValuePredicate extends AbstractPredicate {
    List<String> valueList
    Operator operator = Operator.AND

    public static final enum Operator {
        AND,
        OR
    }

    @Override
    Predicate initializeFromParameters(params) {
        if (params[(paramName)]) initializeValue(params[(paramName)])
        return this
    }

    def initializeValue(String value) {
        this.valueList = [value]
    }

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

    def addValue(String val) {
        if (!valueList) valueList = []
        valueList << val
    }

    def removeValue(String val) {
        if (valueList) {
            if (val.contains(',')) {
                val.split(',').each {
                    valueList -= it
                }
            } else {
                valueList -= val
            }
        }
    }

    def hasValues() {
        return valueList as boolean
    }

    private String buildQuery() {
        String query = ''
        valueList.eachWithIndex { value, index ->
            if (index > 0) query += " $operator "
            query += value
        }
        query
    }

    String getStringValue() {
        return valueList?.join(",")
    }

    def getValue() {
        valueList
    }
}