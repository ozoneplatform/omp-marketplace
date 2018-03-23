package marketplace.service

import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.Category
import marketplace.ExportService
import marketplace.ItemComment
import marketplace.Profile
import marketplace.SecurityMixin
import marketplace.State
import marketplace.Types
import marketplace.domain.builders.DomainBuilderMixin

import ozone.marketplace.dataexchange.ExportSettings


@Integration
@Rollback
class ExportServiceTests extends Specification implements DomainBuilderMixin, SecurityMixin {

    ExportService exportService

    Profile adminUser
    Types type1
    State state1

    void setupData() {
        adminUser = $adminProfile { username = 'exportTestAdmin' }

        type1 = $type { title = 'export test type' }
        state1 = $state { title = 'export test state' }

        loggedInAs(adminUser)
        // switchAdmin("testAdmin1")
    }

    void testEmptyExport() {
        given:
        setupData()

        when:
        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        then:
        exportData != null
        exportData.serviceItems.size() == 0
        exportData.relationships.size() == 0
        exportData.customFieldDefs.size() == 0
        exportData.types.size() == Types.count()
        exportData.states.size() == State.count()
        exportData.categories.size() == Category.count()
        exportData.profiles.size() == Profile.count()
    }

    void testExportSimpleListing() {
        given:
        setupData()

        def listing = $serviceItem {
            title = 'listing'
            owner = adminUser
            type = type1
            state = state1
        }

        when:
        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        then:
        exportData.serviceItems.size() == 1
        def listingJson = exportData.serviceItems[0]

        listing.uuid == listingJson.uuid
        listing.title == listingJson.title
        listing.types.title == listingJson.types.title
        listing.state.title == listingJson.state.title
    }

    void testExportSpecificListing() {
        given:
        setupData()

        $serviceItem {
            title = 'excluded'
            owner = adminUser
            type = type1
        }

        def requestedListing = $serviceItem {
            owner = adminUser
            title = 'requested'
            type = type1
        }

        when:
        ExportSettings settings = fullExportSettings()
        settings.serviceItemIds = [requestedListing.id]
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        then:
        exportData.serviceItems.size() == 1

        def listingJson = exportData.serviceItems[0]
        requestedListing.uuid == listingJson.uuid
    }

    void testExportCommentOnListing() {
        given:
        setupData()

        def listing = $serviceItem {
            title = 'listing'
            owner = adminUser
            type = type1
            state = state1
        }

        ItemComment comment = $itemComment {
            text = 'My comment'
            rate = 4.0
            author = adminUser
            serviceItem = listing
        }

        when:
        ExportSettings settings = fullExportSettings()
        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        then:
        exportData.serviceItems.size() == 1

        def listingJson = exportData.serviceItems[0]
        listingJson.itemComments.size() == 1
        listingJson.itemComments[0].text == comment.text
        listingJson.itemComments[0].rate == comment.rate
        listingJson.itemComments[0].author.username == adminUser.username
    }

    void testExportAllProfiles() {
        given:
        setupData()

        String ownerName = "exportOwner1"
        String commenterName = "exportCommenter1"
        createListingWithOwnerAndCommenter(ownerName, commenterName)

        when:
        ExportSettings settings = fullExportSettings()

        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        then: "verify unused profiles are returned"
        exportData.profiles.
                size() >= 3 // testAdmin1 owner1, and commenter1.  Don't know who else is also in the system.
        exportData.profiles.find { it.username == ownerName }
        exportData.profiles.find { it.username == commenterName }
        exportData.profiles.find { it.username == adminUser.username }
    }

    void testExportAssociatedProfiles() {
        given:
        setupData()

        String ownerName = "exportOwner1"
        String commenterName = "exportCommenter1"
        createListingWithOwnerAndCommenter(ownerName, commenterName)

        when:
        ExportSettings settings = fullExportSettings()
        settings.includeProfiles = ExportSettings.Profiles.ASSOCIATED

        def exportData = exportService.retrieveExportData(settings, true, adminUser.username)

        then: 'verify ASSOCIATED profiles returns only owners and commenters'
        exportData.profiles.size() == 2
        exportData.profiles.find { it.username == ownerName }
        exportData.profiles.find { it.username == commenterName }
    }

    private void createListingWithOwnerAndCommenter(String ownerName, String commenterName) {
        def owningUser = $userProfile { username = ownerName }
        def commentingUser = $userProfile { username = commenterName }

        def listing = $serviceItem {
            title = 'test listing'
            type = type1
            state = state1
            owner = owningUser
        }

        $itemComment {
            text = 'My comment'
            rate = 4.0
            author = commentingUser
            serviceItem = listing
        }
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
