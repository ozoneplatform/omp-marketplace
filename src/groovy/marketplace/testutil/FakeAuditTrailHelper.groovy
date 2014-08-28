package marketplace.testutil

import grails.util.Holders

import marketplace.ServiceItem

/**
 * This class is used in the unit tests as a mocked version
 * of the AuditTrailHelper from the audit-trail plugin
 */
class FakeAuditTrailHelper {
    void initializeFields(obj) {
        obj.createdDate = new Date()
        obj.editedDate = new Date()

        //hack to work around unit tests not calling
        //beforeValidate on nested objects
        if (obj instanceof ServiceItem && obj.owfProperties) {
            initializeFields(obj.owfProperties)
        }
    }

    static void install() {
        Holders.grailsApplication.mainContext.registerSingleton('auditTrailHelper', FakeAuditTrailHelper)
    }
}
