package marketplace.search

/**
 * Represents a range-based search criterion. The criterion may specify lower and upper boundaries, or just one of them.
 */
class RangePredicate extends AbstractPredicate {

    String lowerBoundary
    String upperBoundary
    String lowerBoundaryParamName
    String upperBoundaryParamName

    @Override
    Predicate initializeFromParameters(Object params) {
        if (params[(lowerBoundaryParamName)]) this.lowerBoundary = params[(lowerBoundaryParamName)]
        if (params[(upperBoundaryParamName)]) this.upperBoundary = params[(upperBoundaryParamName)]
        this
    }

    @Override
    def getSearchClause() {
        return {
            if (lowerBoundary || upperBoundary) {
                must {
                    range {
                        if (lowerBoundary && upperBoundary) "$indexFieldName"(gte: lowerBoundary, lte: upperBoundary)
                        else if (lowerBoundary) "$indexFieldName"(gte: lowerBoundary)
                        else if (upperBoundary) "$indexFieldName"(lte: upperBoundary)
                    }
                }
            }
        }
    }

    String getStringValue() {
        if (lowerBoundary && upperBoundary) "$lowerBoundary - $upperBoundary"
        else if (lowerBoundary) lowerBoundary
        else if (upperBoundary) upperBoundary
        else ''
    }

    def getValue() {
        getStringValue()
    }
}
