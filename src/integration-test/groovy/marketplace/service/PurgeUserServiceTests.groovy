package marketplace.service

import grails.gorm.transactions.Rollback
import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import grails.testing.mixin.integration.Integration
import marketplace.Avatar
import marketplace.Category
import marketplace.ItemComment
import marketplace.Profile
import marketplace.RejectionListing
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.UserAccount
import marketplace.UserDomainInstance
import marketplace.controller.MarketplaceIntegrationTestCase
import spock.lang.Ignore
import ozone.marketplace.enums.DefinedDefaultTypes
import ozone.marketplace.service.AuthorizationException;

@Integration
@Rollback
class PurgeUserServiceTests extends MarketplaceIntegrationTestCase {
    def purgeUserService
    def profileService
    def itemCommentService
    def grailsApplication

    def testUser1
    def testUser2
    def dummyServiceItem
    Profile systemUser

    void setup() {
//        super.setUp()

        systemUser = Profile.getSystemUser()

        testUser1 = new Profile(username: 'purgeUser1', displayName: 'Toby Purged', createdDate: new Date())
        testUser1.save(flush:true)

        testUser2 = new Profile(username: 'keepUser2', displayName: 'Dont Purge', createdDate: new Date())
        testUser2.save(flush:true)

        dummyServiceItem = createServiceItemBy(systemUser)

        switchUser(Profile.SYSTEM_USER_NAME)

        assert null != systemUser
    }

    @Ignore
    void testPurgeServiceItemAuthor() {
        when:
        ServiceItem ownedByPurgedUser = createServiceItemBy(testUser1)
        ServiceItem ownedByOtherUser = createServiceItemBy(testUser2)

        purgeUserService.purgeUser(testUser1)

        then:
        ownedByPurgedUser.isAuthor(systemUser)
        ownedByOtherUser.isAuthor(testUser2)
    }

    @Ignore
    void testPurgeServiceItemActivity() {
        when:
        ServiceItemActivity ownedByPurgedUser = createServiceItemActivityBy(testUser1)
        ServiceItemActivity ownedByOtherUser = createServiceItemActivityBy(testUser2)

        purgeUserService.purgeUser(testUser1)

        then:
        systemUser == ownedByPurgedUser.author
        testUser2 == ownedByOtherUser.author
    }

    @Ignore
    void testPurgeItemComment() {
        when:
        createCommentBy(testUser1, 2)
        ItemComment ownedByOtherUser = createCommentBy(testUser2, 3)
        then:
        2 == ItemComment.findAll().size()

        when:
        purgeUserService.purgeUser(testUser1)

        then:
        1 == ItemComment.findAll().size()
        null == ItemComment.findByAuthor(testUser1)
        ownedByOtherUser == ItemComment.findByAuthor(testUser2)
        3 == dummyServiceItem.avgRate
        0 == dummyServiceItem.totalRate2
        1 == dummyServiceItem.totalRate3
    }

    @Ignore
    void testPurgeRejectionListing() {
        when:
        RejectionListing ownedByPurgedUser = createRejectionListingBy(testUser1)
        RejectionListing ownedByOtherUser = createRejectionListingBy(testUser2)

        purgeUserService.purgeUser(testUser1)

        then:
        systemUser == ownedByPurgedUser.author
        testUser2 == ownedByOtherUser.author
    }

    @Ignore
    void testPurgeCreatedByAndEditedBy() {
        when:
        // We're just testing Avatar and Category because they're easy to create, but the purge
        // should catch all domain classes that have createdBy and editedBy
        switchUser("purgeUser1")
        Avatar avatarOwnedByPurgedUser = createAvatar()
        Category categoryOwnedByPurgedUser = createCategory()
        switchUser("keepUser2")
        Avatar avatarOwnedByOtherUser = createAvatar()
        Category categoryOwnedByOtherUser = createCategory()

        switchUser(Profile.SYSTEM_USER_NAME)
        purgeUserService.purgeUser(testUser1)

        then:
        systemUser.id == avatarOwnedByPurgedUser.createdBy
        systemUser.id == avatarOwnedByPurgedUser.editedBy
        systemUser.id == categoryOwnedByPurgedUser.createdBy
        systemUser.id == categoryOwnedByPurgedUser.editedBy
        testUser2.id == avatarOwnedByOtherUser.createdBy
        testUser2.id == avatarOwnedByOtherUser.editedBy
        testUser2.id == categoryOwnedByOtherUser.createdBy
        testUser2.id == categoryOwnedByOtherUser.editedBy
    }

    @Ignore
    void testOnlySystemUserCanPurge() {
        when:
        switchUser("keepUser2")
        purgeUserService.purgeUser(testUser1)

        then:
        thrown(AuthorizationException)
    }

    @Ignore
    void testPurgeUserAccount() {
        when:
        createUserAccount(testUser1)
        UserAccount ownedByOtherUser = createUserAccount(testUser2)
        then:
        2 == UserAccount.findAll().size()

        when:
        purgeUserService.purgeUser(testUser1)

        then:
        1 == UserAccount.findAll().size()
        null == UserAccount.findByUsername(testUser1.username)
        ownedByOtherUser == UserAccount.findByUsername(testUser2.username)
    }

    @Ignore
    void testPurgeUserDomainInstance() {
        when:
        createUserDomainInstance(testUser1)
        UserDomainInstance ownedByOtherUser = createUserDomainInstance(testUser2)
        then:
        2 == UserDomainInstance.findAll().size()

        when:
        purgeUserService.purgeUser(testUser1)

        then:
        1 == UserDomainInstance.findAll().size()
        null == UserDomainInstance.findByUsername(testUser1.username)
        ownedByOtherUser == UserDomainInstance.findByUsername(testUser2.username)
    }

    @Ignore
    void testPurgeProfile() {
        when:
        purgeUserService.purgeUser(testUser1)

        then:
        null == Profile.findByUsername(testUser1.username)
        testUser2 == Profile.findByUsername(testUser2.username)
    }

    @Ignore
    void testAllDomainClassesHandled() {
        // This test checks to see if Profile is used in any domain object that PurgeUserService does not
        // handle.
        def purgedClasses = [ItemComment, RejectionListing, ServiceItem, ServiceItemActivity]
        grailsApplication.getDomainClasses().each { domainClass ->
            // If this class isn't one of the ones already handled by PurgeUserService
            if (!purgedClasses*.isAssignableFrom(domainClass.getClazz()).any()) {
                // ... and it has a Profile property that is not one of the automatically-handled ones
                domainClass.getProperties().each { property ->
                    if (property.name != "createdBy" && property.name != "editedBy" && property.type == Profile) {
                        // If this line fails, it means you need to figure out what the offending domain object should do when the user
                        // is deleted. Update PurgeUserService to handle that domain object, and then add it to the list of purgedClasses
                        expect:
                        false
                        //fail "Class ${domainClass.name} has Profile property '${property.name}' that will not be purged when the profile is purged"
                    }
                }
            }
        }
    }

    @Ignore
    void testGetInactiveAccounts_NoUsers() {
        when:
        def inactiveAccounts = purgeUserService.getInactiveAccounts(90)
        then:
        0 == inactiveAccounts.size()
    }

    @Ignore
    void testGetInactiveAccounts_SelectsCorrectUsers() {
        when:
        def today = new Date()
        def thresholdInDays = 90
        def inactiveUser = createUserAccountWithLastLogin(today.minus(thresholdInDays+1))
        createUserAccountWithLastLogin(today.minus(thresholdInDays-1))

        def inactiveAccounts = purgeUserService.getInactiveAccounts(thresholdInDays)

        then:
        1 == inactiveAccounts.size()
        inactiveUser == inactiveAccounts[0]
    }

    @Ignore
    void testPurgeInactiveUsers() {
        when:
        def today = new Date()
        def thresholdInDays = 90
        def inactiveUser = createUserAccountWithLastLogin(today.minus(thresholdInDays+1))
        def inactiveProfile = Profile.build(username: inactiveUser.username)
        switchUser(inactiveProfile.username)
        def inactiveServiceItem = createServiceItemBy(inactiveProfile)
        def activeUser = createUserAccountWithLastLogin(today.minus(thresholdInDays-1))
        def activeProfile = Profile.build(username: activeUser.username)
        switchUser(activeProfile.username)
        def activeServiceItem = createServiceItemBy(activeProfile)

        switchUser(systemUser.username)
        purgeUserService.purgeInactiveAccounts()

        then:
        null == UserAccount.findByUsername(inactiveUser.username)
        null == Profile.findByUsername(inactiveProfile.username)
        inactiveServiceItem.isAuthor(systemUser)
        systemUser == inactiveServiceItem.createdBy
        systemUser == inactiveServiceItem.editedBy
        activeUser == UserAccount.findByUsername(activeUser.username)
        activeProfile == Profile.findByUsername(activeProfile.username)
        activeServiceItem.isAuthor(activeProfile)
        activeProfile == activeServiceItem.createdBy
        activeProfile == activeServiceItem.editedBy
    }

    private ServiceItem createServiceItemBy(Profile user) {
        def type = Types.findByTitle(DefinedDefaultTypes.DESKTOP_APPS.title)
        ServiceItem item = ServiceItem.build(title: "T", owners: [user], types: type)
        return item
    }

    private ServiceItemActivity createServiceItemActivityBy(Profile user) {
        ServiceItemActivity activity = ServiceItemActivity.build(action: Constants.Action.APPROVED, author: user, serviceItem: dummyServiceItem)
        return activity
    }

    private ItemComment createCommentBy(Profile user, Integer rating) {
        ItemComment comment = ItemComment.build(author: user, serviceItem: dummyServiceItem)
        itemCommentService.rate(comment, rating)
        return comment
    }

    private RejectionListing createRejectionListingBy(Profile user) {
        RejectionListing rejection = RejectionListing.build(author: user, serviceItem: dummyServiceItem)
        return rejection
    }

    private Avatar createAvatar() {
        Avatar avatar = Avatar.build()
        return avatar
    }

    private Category createCategory() {
        Category category = Category.build()
        return category
    }

    private UserAccount createUserAccount(Profile user) {
        UserAccount account = UserAccount.build(username: user.username)
        return account
    }

    private UserDomainInstance createUserDomainInstance(Profile user) {
        UserDomainInstance domainInstance = UserDomainInstance.build(username: user.username)
        return domainInstance
    }

    private UserAccount createUserAccountWithLastLogin(Date lastLoginDate) {
        UserAccount account = UserAccount.build(lastLogin: lastLoginDate)
        return account
    }
}
