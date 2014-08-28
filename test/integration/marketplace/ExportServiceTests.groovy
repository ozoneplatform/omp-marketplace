package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import org.junit.Ignore
import ozone.marketplace.dataexchange.ExportSettings

@TestMixin(IntegrationTestMixin)
class ExportServiceTests extends MarketplaceIntegrationTestCase {
    def exportService
    def relationshipService
    Profile adminUser

    void setUp() {
        super.setUp()

        adminUser = new Profile(username: "testAdmin1")
        adminUser.save()
        switchAdmin("testAdmin1")
    }

    @Ignore
    void testEmptyExport() {
        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        assert null != exportData
        assert 0 == exportData.serviceItems.size()
        assert 0 == exportData.relationships.size()
        assert 0 == exportData.customFieldDefs.size()
        assert Types.count() == exportData.types.size()
        assert State.count() == exportData.states.size()
        assert Category.count() == exportData.categories.size()
        assert Profile.count() == exportData.profiles.size()
    }

    void testExportSimpleListing() {
        ServiceItem listing = new ServiceItem(
            owners: [adminUser],
            types: new Types(title: 'test type').save(),
            title: 'listing',
            launchUrl: 'https:///',
            state: State.findByTitle("Active")
        )
        listing.save(failOnError: true)

        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        assert 1 == exportData.serviceItems.size()
        def listingJson = exportData.serviceItems[0]
        assert listing.uuid == listingJson.uuid
        assert listing.title == listingJson.title
        assert listing.types.title == listingJson.types.title
        assert listing.state.title == listingJson.state.title
    }

    void testExportSpecificListing() {
        Types type = new Types(title: 'test type').save()

        ServiceItem excludedListing = new ServiceItem(
            owners: [adminUser],
            types: type,
            title: 'excluded',
            launchUrl: 'https:///'
        )
        excludedListing.save(failOnError: true)
        ServiceItem requestedListing = new ServiceItem(
            owners: [adminUser],
            types: type,
            title: 'requested',
            launchUrl: 'https:///'
        )
        requestedListing.save(failOnError: true)

        ExportSettings settings = fullExportSettings()
        settings.serviceItemIds = [requestedListing.id]
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        assert 1 == exportData.serviceItems.size()
        def listingJson = exportData.serviceItems[0]
        assert requestedListing.uuid == listingJson.uuid
    }

    void testExportCommentOnListing() {
        ServiceItem listing = new ServiceItem(
            owners: [adminUser],
            types: new Types(title: 'test type').save(),
            title: 'listing',
            launchUrl: 'https:///',
            state: State.findByTitle("Active")
        )
        listing.save(failOnError: true)
        def comment = new ItemComment(text: "My comment", rate: 4.0, author: adminUser, serviceItem: listing)
        listing.addToItemComments(comment)
        listing.save()

        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        assert 1 == exportData.serviceItems.size()
        def listingJson = exportData.serviceItems[0]
        assert 1 == listingJson.itemComments.size()
        assert comment.text == listingJson.itemComments[0].text
        assert comment.rate == listingJson.itemComments[0].rate
        assert adminUser.username == listingJson.itemComments[0].author.username
    }

    void testExportAllProfiles() {
        String ownerName = "owner1"
        String commenterName = "commenter1"
        createListingWithOwnerAndCommenter(ownerName, commenterName)

        // Verify unused profiles are returned
        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)
        assert(exportData.profiles.size() >= 3) // testAdmin1 owner1, and commenter1.  Don't know who else is also in the system.
        assert(exportData.profiles.find {it.username == ownerName})
        assert(exportData.profiles.find {it.username == commenterName})
        assert(exportData.profiles.find {it.username == adminUser.username})
    }

    void testExportAssociatedProfiles() {
        String ownerName = "owner1"
        String commenterName = "commenter1"
        createListingWithOwnerAndCommenter(ownerName, commenterName)

        // Verify ASSOCIATED profiles returns only owners and commenters
        ExportSettings settings = fullExportSettings()
        settings.includeProfiles = ExportSettings.Profiles.ASSOCIATED;
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)
        assert(exportData.profiles.size() == 2)
        assert(exportData.profiles.find {it.username == ownerName})
        assert(exportData.profiles.find {it.username == commenterName})
    }

    private void createListingWithOwnerAndCommenter(String ownerName, String commenterName) {
        Profile owner = new Profile(username: ownerName)
        owner.save()
        Profile commenter = new Profile(username: commenterName)
        commenter.save()
        ServiceItem listing = new ServiceItem(
            owners: [adminUser],
            types: new Types(title: 'test type').save(),
            title: 'listing',
            launchUrl: 'https:///',
            state: State.findByTitle("Active")
        )
        listing.owners = [owner]
        listing.save(failOnError: true)
        ItemComment comment = new ItemComment(text: "My comment", rate: 4.0, author: commenter, serviceItem: listing)
        listing.addToItemComments(comment)
        listing.save()
    }

    private ExportSettings fullExportSettings() {
        ExportSettings settings = new ExportSettings()
        settings.includeServiceItems = true
        settings.includeStates = true
        settings.includeTypes = true
        settings.includeCategories = true
        settings.includeCustomFieldDefs = true
        settings.includeProfiles = ExportSettings.Profiles.ALL
        settings.includeComments = true
        settings.includeRequired = true
        return settings
    }
}
