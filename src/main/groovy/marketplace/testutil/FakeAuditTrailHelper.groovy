package marketplace.testutil

import grails.boot.config.GrailsAutoConfiguration
import marketplace.ServiceItem

/**
 * This class is used in the unit tests as a mocked version
 * of the AuditTrailHelper from the audit-trail plugin
 */
class FakeAuditTrailHelper extends GrailsAutoConfiguration {

    void initializeFields(obj) {
        obj.metaClass.createdDate = new Date()
        obj.metaClass.editedDate = new Date()

        //hack to work around unit tests not callingF
        //beforeValidate on nested objects
        if (obj instanceof ServiceItem && obj.owfProperties) {
            initializeFields(obj.owfProperties)
        }
    }
    static void install() {
       //Holders.grailsApplication.mainContext.registerSingleton('auditTrailHelper', FakeAuditTrailHelper)
    }
}
