package marketplace.search

class IsEnabledPredicate extends SingleValuePredicate {

    @Override
    def getSearchClause() {
        return {
            must {
                term(isHidden: 0)
            }
        }
    }
}
