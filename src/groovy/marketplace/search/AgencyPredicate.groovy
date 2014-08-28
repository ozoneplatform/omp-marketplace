package marketplace.search

/**
 * Predicate for Agency searches
 * Using term rather than query strings to allow for multi word agencies
 */
class AgencyPredicate extends SingleValuePredicate {

    @Override
    def getSearchClause() {
        return {
            must {
                term(agency: singleValue)
            }
        }
    }

}
