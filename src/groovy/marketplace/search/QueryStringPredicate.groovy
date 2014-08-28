package marketplace.search

import marketplace.CustomFieldDefinition

class QueryStringPredicate extends SingleValuePredicate {

    QueryStringPredicate() {
        this.isFilter = false
    }

    @Override
    Predicate initializeFromParameters(params) {
        super.initializeFromParameters(params)
        this.includeComments = params.includeComments
        return this
    }

    /**
     * Handle a query string-based search which may or may not include user comments.
     * @return
     */
    @Override
    def getSearchClause() {
        def includeComments = this.includeComments
        def expandedQueryString = getExpandedQueryString(singleValue)
        return {
            if (includeComments) {
                should {
                    query_string(default_field: "itemComments.text", query: expandedQueryString)
                }
                should {
                    query_string(default_field: this.indexFieldName, query: expandedQueryString ?: '*')
                }
            } else {
                must {
                    query_string(default_field: this.indexFieldName, query: expandedQueryString ?: '*')
                }
            }
        }
    }

    /**
     * Handle custom field search converting search in the form of "color:blue"
     * into "(customFieldName:color AND fieldValueText:blue)"
     * @param originalString
     * @return
     */
    String getExpandedQueryString(String originalString) {
        if (originalString?.contains(':')) {

            return originalString.replaceAll(/[\w]+:[\w]+/,
                    {
                        def matcher = it =~ /([\w]+):([\w]+)/
                        if (matcher) {
                            String fieldName = matcher[0][1]
                            String fieldValue = matcher[0][2]
                            if (isCustomFieldName(fieldName)) {
                                return "(customFieldName:${fieldName} AND fieldValueText:${fieldValue})"
                            }
                            else {
                                return it
                            }
                        }
                    })
        }
        return originalString
    }

    private boolean isCustomFieldName(String name) {
        CustomFieldDefinition.findByName(name) != null
    }
}
