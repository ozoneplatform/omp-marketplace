package marketplace.search

import marketplace.Constants

class AccessTypePredicate extends SingleValuePredicate {

    @Override
    Predicate initializeFromParameters(params) {
        super.initializeFromParameters(params)
        this.username = params.username
        this.accessType = params.accessType

        return this
    }

    @Override
    def getSearchClause() {
        def username = this.username
        def accessType = this.accessType
        return {
            switch (accessType) {
                case Constants.VIEW_USER:
                    must {
                        query_string(query: "(approvalStatus: ${Constants.APPROVAL_STATUSES["APPROVED"]} AND isHidden: 0) OR owners.username: \"${username}\"")
                    }
                    break
                case Constants.VIEW_EXTERNAL:
                    must {
                        query_string(query: "(approvalStatus: ${Constants.APPROVAL_STATUSES["APPROVED"]} AND isHidden: 0)")
                    }
                    break
                case Constants.VIEW_ADMIN:
                    break
            }
        }
    }
}
