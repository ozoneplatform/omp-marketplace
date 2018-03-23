package marketplace.domain.builders

import marketplace.Constants
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity


class ServiceItemActivityBuilder implements Builder<ServiceItemActivity> {

    static final Constants.Action CREATED = Constants.Action.CREATED
    static final Constants.Action SUBMITTED = Constants.Action.SUBMITTED
    static final Constants.Action REJECTED = Constants.Action.REJECTED

    Constants.Action action
    ServiceItem serviceItem
    Date activityTimestamp
    Profile author

    ServiceItemActivity build() {
        if (!action) action = CREATED

        new ServiceItemActivity(action: action,
                                serviceItem: serviceItem,
                                activityTimestamp: activityTimestamp,
                                author: author)
    }

    void setTimestamp(Date timestamp) {
        activityTimestamp = timestamp
    }

}
