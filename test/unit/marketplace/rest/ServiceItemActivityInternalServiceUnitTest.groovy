package marketplace.rest

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.GrailsApplication

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import grails.orm.PagedResultList
import marketplace.Contact
import marketplace.ServiceItemDocumentationUrl
import marketplace.Screenshot
import marketplace.ItemComment
import marketplace.OwfProperties
import marketplace.ServiceItem
import marketplace.ServiceItemDocumentationUrl
import marketplace.ServiceItemSnapshot
import marketplace.ServiceItemActivity
import marketplace.ChangeDetail
import marketplace.Profile
import marketplace.RejectionActivity
import marketplace.ModifyRelationshipActivity
import marketplace.RejectionListing
import marketplace.Constants
import marketplace.Types
import marketplace.MarketplaceMessagingService

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin(DomainClassUnitTestMixin)
class ServiceItemActivityInternalServiceUnitTest {
    ServiceItemActivityInternalService service

    Profile currentUser
    ServiceItem serviceItem1
    ServiceItem serviceItem2
    List serviceItemActivities
    List changeDetails

    private ServiceItem makeServiceItem() {
        ServiceItem serviceItem = new ServiceItem(
            title: "test service item",
            description: "a test service item",
            uuid: '54c12429-82a5-4b9c-b83e-9b5a71c9ccdf',
            launchUrl: "https://localhost/asf",
            versionName: '1',
            approvalStatus: Constants.APPROVAL_STATUSES['IN_PROGRESS'],
            version: 1,
            owfProperties: new OwfProperties(height: 200, width: 300)
        )

        return serviceItem
    }

    void setUp() {
        serviceItem1 = makeServiceItem()
        serviceItem2 = makeServiceItem()
        currentUser = new Profile(username: 'admin')

        def serviceItemActivity1 = new ServiceItemActivity(
            action: Constants.Action.MODIFIED,
            serviceItem: serviceItem1
        )
        def serviceItemActivity2 = new ServiceItemActivity(
            action: Constants.Action.ENABLED,
            serviceItem: serviceItem1
        )
        def serviceItemActivity3 = new ServiceItemActivity(
                action: Constants.Action.CREATED,
                serviceItem: serviceItem2
        )
        def serviceItemActivity4 = new ServiceItemActivity(
                action: Constants.Action.MODIFIED,
                serviceItem: serviceItem2
        )

        def changeDetail1 = new ChangeDetail(
            fieldName: 'Made Up field',
            oldValue: 'null',
            newValue: 'New Value'
        )
        def changeDetail2 = new ChangeDetail(
            fieldName: 'Made Up field 2',
            oldValue: 'old value',
            newValue: 'New Value 2'
        )

        serviceItemActivities = [serviceItemActivity1, serviceItemActivity2,
            serviceItemActivity3, serviceItemActivity4]
        changeDetails = [changeDetail1, changeDetail2]

        FakeAuditTrailHelper.install()

        mockDomain(ItemComment)
        mockDomain(RejectionActivity)
        mockDomain(Contact)
        mockDomain(Screenshot)
        mockDomain(ServiceItemDocumentationUrl)
        mockDomain(ChangeDetail, changeDetails)
        mockDomain(ServiceItemActivity, serviceItemActivities)
        mockDomain(ServiceItem.class, [serviceItem1, serviceItem2])

        currentUser = new Profile(username: 'admin')

        def serviceItemRestService = [
            getById: { ServiceItem.get(it) },
        ] as ServiceItemRestService


        service = new ServiceItemActivityInternalService()

        service.profileRestService = [
            getCurrentUserProfile: { currentUser }
        ] as ProfileRestService

        def marketplaceMessagingServiceMock = mockFor(MarketplaceMessagingService)
        service.marketplaceMessagingService = marketplaceMessagingServiceMock.createMock()
        marketplaceMessagingServiceMock.demand.sendNotificationOfChange(999) { serviecItem, activity -> [] }
    }

    public void testAddServiceItemActivityByAction() {
        ServiceItem si = makeServiceItem()

        service.addServiceItemActivity(si, Constants.Action.DISABLED)

        assert si.serviceItemActivities.size() == 1
        assert si.serviceItemActivities[0].action == Constants.Action.DISABLED
        assert si.serviceItemActivities[0].author == currentUser

        assert si.lastActivity == si.serviceItemActivities[0]
    }

    public void testAdminServiceItemActivity() {
        ServiceItem si = makeServiceItem()
        ServiceItemActivity activity = new ServiceItemActivity(
            description: 'desc',
            action: Constants.Action.OUTSIDE
        )

        service.addServiceItemActivity(si, activity)

        assert si.serviceItemActivities.size() == 1
        assert si.serviceItemActivities[0] == activity

        assert si.lastActivity == activity
    }

    public void testAddRejectionActivity() {
        ServiceItem si = makeServiceItem()
        RejectionListing rejectionListing = new RejectionListing(
            description: 'rejected'
        )

        mockDomain(RejectionListing, [rejectionListing])

        service.addRejectionActivity(si, rejectionListing)

        assert si.serviceItemActivities.size() == 1
        assert si.serviceItemActivities[0].author == currentUser
        assert si.serviceItemActivities[0] instanceof RejectionActivity
        assert si.serviceItemActivities[0].rejectionListing == rejectionListing

        assert si.lastActivity == si.serviceItemActivities[0]
    }

    public void testAddRelationshipActivities() {
        ServiceItem parent = makeServiceItem()
        ServiceItem child1 = makeServiceItem()
        ServiceItem child2 = makeServiceItem()
        ServiceItem child3 = makeServiceItem()
        ServiceItem child4 = makeServiceItem()

        def snapshotsOf = { siList ->
            siList.collect { new ServiceItemSnapshot(title: it.title, serviceItem: it) }
        }

        service.addRelationshipActivities(parent, [child1, child2], [])

        ModifyRelationshipActivity parentActivity = parent.serviceItemActivities[0]

        assert parent.serviceItemActivities.size() == 1
        assert parent.serviceItemActivities[0].author == currentUser
        assert parent.serviceItemActivities[0] instanceof ModifyRelationshipActivity
        assert parent.serviceItemActivities[0].action == Constants.Action.ADDRELATEDITEMS
        assert parent.serviceItemActivities[0].items == snapshotsOf([child1, child2])
        assert parent.lastActivity == parent.serviceItemActivities[0]

        assert child1.serviceItemActivities.size() == 1
        assert child1.serviceItemActivities[0].author == currentUser
        assert child1.serviceItemActivities[0] instanceof ModifyRelationshipActivity
        assert child1.serviceItemActivities[0].items == snapshotsOf([parent])
        assert child1.serviceItemActivities[0].action == Constants.Action.ADDRELATEDTOITEM
        assert child1.lastActivity == child1.serviceItemActivities[0]

        assert child2.serviceItemActivities.size() == 1
        assert child2.serviceItemActivities[0].author == currentUser
        assert child2.serviceItemActivities[0] instanceof ModifyRelationshipActivity
        assert child2.serviceItemActivities[0].items == snapshotsOf([parent])
        assert child2.serviceItemActivities[0].action == Constants.Action.ADDRELATEDTOITEM
        assert child2.lastActivity == child2.serviceItemActivities[0]

        service.addRelationshipActivities(parent, [], [child1])

        assert parent.serviceItemActivities.size() == 2
        assert parent.serviceItemActivities[1].author == currentUser
        assert parent.serviceItemActivities[1] instanceof ModifyRelationshipActivity
        assert parent.serviceItemActivities[1].action == Constants.Action.REMOVERELATEDITEMS
        assert parent.serviceItemActivities[1].items == snapshotsOf([child1])
        assert parent.lastActivity == parent.serviceItemActivities[1]

        assert child1.serviceItemActivities.size() == 2
        assert child1.serviceItemActivities[1].author == currentUser
        assert child1.serviceItemActivities[1] instanceof ModifyRelationshipActivity
        assert child1.serviceItemActivities[1].items == snapshotsOf([parent])
        assert child1.serviceItemActivities[1].action == Constants.Action.REMOVERELATEDTOITEM
        assert child1.lastActivity == child1.serviceItemActivities[1]

        service.addRelationshipActivities(parent, [child1], [child2])

        assert parent.serviceItemActivities.size() == 4
        def addActivity = parent.serviceItemActivities[2]
        def removeActivity = parent.serviceItemActivities[3]

        //since the activities were done at pretty much the same time, they could be in
        //either order
        if (addActivity.action == Constants.Action.REMOVERELATEDITEMS) {
            def tmp = removeActivity
            addActivity = removeActivity
            removeActivity = tmp
        }

        assert addActivity.author == currentUser
        assert addActivity instanceof ModifyRelationshipActivity
        assert addActivity.action == Constants.Action.ADDRELATEDITEMS
        assert addActivity.items == snapshotsOf([child1])

        assert removeActivity.author == currentUser
        assert removeActivity instanceof ModifyRelationshipActivity
        assert removeActivity.action == Constants.Action.REMOVERELATEDITEMS
        assert removeActivity.items == snapshotsOf([child2])

        assert child1.serviceItemActivities.size() == 3
        assert child1.serviceItemActivities[2].author == currentUser
        assert child1.serviceItemActivities[2] instanceof ModifyRelationshipActivity
        assert child1.serviceItemActivities[2].items == snapshotsOf([parent])
        assert child1.serviceItemActivities[2].action == Constants.Action.ADDRELATEDTOITEM
        assert child1.lastActivity == child1.serviceItemActivities[2]

        assert child2.serviceItemActivities.size() == 2
        assert child2.serviceItemActivities[1].author == currentUser
        assert child2.serviceItemActivities[1] instanceof ModifyRelationshipActivity
        assert child2.serviceItemActivities[1].items == snapshotsOf([parent])
        assert child2.serviceItemActivities[1].action == Constants.Action.REMOVERELATEDTOITEM
        assert child2.lastActivity == child2.serviceItemActivities[1]
    }

    public void testChangeLogNotCreatedIfThereAreNoChanges() {
        def (updated, old) = [makeServiceItem(), makeServiceItem()]
        def (activityCountBefore, activityCountAfter) = doChangeLog(updated, old.properties)
        assertEquals activityCountBefore, activityCountAfter
    }

    public void testChangelogNotCreatedIfChangesAreNotAuditable() {
        // This test assumes these are some of the properties in the auditable.ignore list.
        assert ServiceItem.auditable.ignore.containsAll(['avgRate', 'itemComments', 'totalVotes', 'approvalStatus', 'isOutside'])

        def (updated, old) = [makeServiceItem(), makeServiceItem()]

        updated.with {
            avgRate = old.avgRate + 1.0
            totalVotes = old.totalVotes + 1
            isOutside = !old.isOutside
            approvalStatus = old.approvalStatus == 'In Progress' ? 'Approved' : 'In Progress'
            addToItemComments(new ItemComment(rate: 1.0, text: 'review'))
        }

        def (activityCountBefore, activityCountAfter) = doChangeLog(updated, old.properties)
        assert activityCountBefore == activityCountAfter
    }

    public void testChangelogIsCreatedWhenAuditableFieldsAreModified() {
        // This test assumes these are some of some of the auditable properties of a ServiceItem.
        Set fieldsToTest = ['description', 'types', 'title']
        assert ServiceItem.auditable.ignore.disjoint(fieldsToTest)

        def (updated, old) = [makeServiceItem(), makeServiceItem()]

        updated.with {
            title = old.title + ' new!'
            description = old.description + 'new!'
            types = new Types(title: 'new type!!')
        }

        def (activityCountBefore, activityCountAfter, activity) = doChangeLog(updated, old.properties)
        assert activityCountBefore + 1 == activityCountAfter
        assert activity.changeDetails.size() == 3
        assert activity.changeDetails*.fieldName as Set == fieldsToTest

    }

    public void testChangelogIsCreateWhenAuditableCollectionsAreModified() {
        // This test assumes these are some of some of the auditable collection properties of a ServiceItem.
        Set fieldsToTest = ['docUrls', 'contacts', 'screenshots']
        assert ServiceItem.auditable.ignore.disjoint(fieldsToTest)

        def (updated, old) = [makeServiceItem(), makeServiceItem()]

        old.with {
            addToContacts(new Contact(name: "name1"))
            addToDocUrls(new ServiceItemDocumentationUrl(url: "https://url1"))
            if(screenshots) screenshots.clear()
        }

        updated.with {
            if(contacts) contacts.clear()
            addToContacts(new Contact(name: "name2"))
            addToDocUrls(new ServiceItemDocumentationUrl(url: "https://url2"))
            addToScreenshots(new Screenshot(largeImageUrl: 'https://urlurlurlurl'))
        }

        def (activityCountBefore, activityCountAfter, activity) = doChangeLog(updated, old.properties)
        assert activityCountBefore + 1 == activityCountAfter
        assert activity.changeDetails.size() == 3
        assert activity.changeDetails*.fieldName as Set == fieldsToTest
    }

    public testChangelogIsCreateWhenOwfPropertiesAreModified() {
        //this test assumes the following are auditable properties of OwfProperties
        Set fieldsToTest = ['stackDescriptor', 'singleton']
        assert OwfProperties.changeLogProperties.containsAll(fieldsToTest)

        def (updated, old) = [makeServiceItem(), makeServiceItem()]

        old.with {
            owfProperties ?: new OwfProperties()
            owfProperties.singleton = false
            owfProperties.stackDescriptor = "stack1"
        }

        updated.with {
            owfProperties ?: new OwfProperties()
            owfProperties.singleton = true
            owfProperties.stackDescriptor = "stack2"
        }

        def (activityCountBefore, activityCountAfter, activity) = doChangeLog(updated, old.properties)
        assert activityCountBefore + 1 == activityCountAfter
        assert activity.changeDetails.size() == 2
        assert activity.changeDetails*.fieldName as Set == fieldsToTest
    }

    private doChangeLog(updated, old) {
        def countActivities = { updated.serviceItemActivities?.size() ?: 0 }
        def initialCount = countActivities()
        def activity = service.createChangeLog(updated, old)

        [initialCount, countActivities(), activity]
    }
}
