package marketplace.rest

import marketplace.AdminObjectFormatter
import marketplace.ChangeDetail
import marketplace.Constants
import marketplace.Profile
import marketplace.ServiceItem
import marketplace.ServiceItemActivity

import static com.google.common.base.MoreObjects.toStringHelper
import static marketplace.AdminObjectFormatter.fullDateDisplay


class Formatters {

    static String formatServiceItemActivity(ServiceItemActivity activity) {
        """ServiceItemActivity{id: ${activity.id}}
          |\tserviceItem:        ${serviceItemRef(activity.serviceItem)}
          |\tauthor:             ${profileRef(activity.author)}
          |\taction:             ${formatAction(activity.action)}
          |\tactivityTimestamp:  ${fullDateDisplay(activity.activityTimestamp)}
          |\tchangeDetails:      ${changeDetailsList(activity.changeDetails)}""".stripMargin()
    }

    private static String changeDetailsList(Collection<ChangeDetail> details) {
        if (details == null) return "<null>"
        if (details.isEmpty()) return "<empty>"

        def str = "\n"
        details.each { str += "\t\t- ${formatChangeDetail(it)}\n" }
    }

    static String serviceItemRef(ServiceItem item) {
        toStringHelper(ServiceItem)
                .add("id", item.id)
                .add("title", item.title).toString();
    }

    static String profileRef(Profile profile) {
        toStringHelper(Profile)
                .add("id", profile.id)
                .add("username", profile.username).toString()
    }

    static String formatAction(Constants.Action action) {
        toStringHelper(Constants.Action)
                .add("name", action.name())
                .add("description", action.description()).toString()
    }

    static String formatChangeDetail(ChangeDetail detail) {
        toStringHelper(ChangeDetail)
                .add("id", detail.id)
                .add("fieldName", detail.fieldName)
                .add("oldValue", detail.oldValue)
                .add("newValue", detail.newValue).toString()
    }

}
