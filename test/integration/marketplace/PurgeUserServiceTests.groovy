package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*
import org.junit.Ignore
import ozone.marketplace.enums.DefinedDefaultTypes
import ozone.marketplace.service.AuthorizationException;

@TestMixin(IntegrationTestMixin)
class PurgeUserServiceTests extends MarketplaceIntegrationTestCase {
    def purgeUserService
    def profileService
    def itemCommentService
    def grailsApplication

    def testUser1
    def testUser2
    def dummyServiceItem
    Profile systemUser

    void setUp() {
        super.setUp()

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
        ServiceItem ownedByPurgedUser = createServiceItemBy(testUser1)
        ServiceItem ownedByOtherUser = createServiceItemBy(testUser2)

        purgeUserService.purgeUser(testUser1)

        assert true ==  ownedByPurgedUser.isAuthor(systemUser)
        assert true ==  ownedByOtherUser.isAuthor(testUser2)
    }

    @Ignore
    void testPurgeServiceItemActivity() {
        ServiceItemActivity ownedByPurgedUser = createServiceItemActivityBy(testUser1)
        ServiceItemActivity ownedByOtherUser = createServiceItemActivityBy(testUser2)

        purgeUserService.purgeUser(testUser1)

        assert systemUser == ownedByPurgedUser.author
        assert testUser2 == ownedByOtherUser.author
    }

    @Ignore
    void testPurgeItemComment() {
        createCommentBy(testUser1, 2)
        ItemComment ownedByOtherUser = createCommentBy(testUser2, 3)
        assert 2 == ItemComment.findAll().size()

        purgeUserService.purgeUser(testUser1)

        assert 1 == ItemComment.findAll().size()
        assert null == ItemComment.findByAuthor(testUser1)
        assert ownedByOtherUser == ItemComment.findByAuthor(testUser2)
        assert 3 == dummyServiceItem.avgRate
        assert 0 == dummyServiceItem.totalRate2
        assert 1 == dummyServiceItem.totalRate3
    }

    @Ignore
    void testPurgeRejectionListing() {
        RejectionListing ownedByPurgedUser = createRejectionListingBy(testUser1)
        RejectionListing ownedByOtherUser = createRejectionListingBy(testUser2)

        purgeUserService.purgeUser(testUser1)

        assert systemUser == ownedByPurgedUser.author
        assert testUser2 == ownedByOtherUser.author
    }

    @Ignore
    void testPurgeCreatedByAndEditedBy() {
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

        assert systemUser == avatarOwnedByPurgedUser.createdBy
        assert systemUser == avatarOwnedByPurgedUser.editedBy
        assert systemUser == categoryOwnedByPurgedUser.createdBy
        assert systemUser == categoryOwnedByPurgedUser.editedBy
        assert testUser2 == avatarOwnedByOtherUser.createdBy
        assert testUser2 == avatarOwnedByOtherUser.editedBy
        assert testUser2 == categoryOwnedByOtherUser.createdBy
        assert testUser2 == categoryOwnedByOtherUser.editedBy
    }

    @Ignore
    void testOnlySystemUserCanPurge() {
        switchUser("keepUser2")
        shouldFail(AuthorizationException) {
            purgeUserService.purgeUser(testUser1)
        }
    }

    @Ignore
    void testPurgeUserAccount() {
        createUserAccount(testUser1)
        UserAccount ownedByOtherUser = createUserAccount(testUser2)
        assert 2 == UserAccount.findAll().size()

        purgeUserService.purgeUser(testUser1)

        assert 1 == UserAccount.findAll().size()
        assert null == UserAccount.findByUsername(testUser1.username)
        assert ownedByOtherUser == UserAccount.findByUsername(testUser2.username)
    }

    @Ignore
    void testPurgeUserDomainInstance() {
        createUserDomainInstance(testUser1)
        UserDomainInstance ownedByOtherUser = createUserDomainInstance(testUser2)
        assert 2 == UserDomainInstance.findAll().size()

        purgeUserService.purgeUser(testUser1)

        assert 1 == UserDomainInstance.findAll().size()
        assert null == UserDomainInstance.findByUsername(testUser1.username)
        assert ownedByOtherUser == UserDomainInstance.findByUsername(testUser2.username)
    }

    @Ignore
    void testPurgeProfile() {
        purgeUserService.purgeUser(testUser1)

        assert null == Profile.findByUsername(testUser1.username)
        assert testUser2 == Profile.findByUsername(testUser2.username)
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
                        fail "Class ${domainClass.name} has Profile property '${property.name}' that will not be purged when the profile is purged"
                    }
                }
            }
        }
    }

    @Ignore
    void testGetInactiveAccounts_NoUsers() {
        def inactiveAccounts = purgeUserService.getInactiveAccounts(90)
        assert 0 == inactiveAccounts.size()
    }

    @Ignore
    void testGetInactiveAccounts_SelectsCorrectUsers() {
        def today = new Date()
        def thresholdInDays = 90
        def inactiveUser = createUserAccountWithLastLogin(today.minus(thresholdInDays+1))
        createUserAccountWithLastLogin(today.minus(thresholdInDays-1))

        def inactiveAccounts = purgeUserService.getInactiveAccounts(thresholdInDays)
        assert 1 == inactiveAccounts.size()
        assert inactiveUser == inactiveAccounts[0]
    }

    @Ignore
    void testPurgeInactiveUsers() {
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

        assert null == UserAccount.findByUsername(inactiveUser.username)
        assert null == Profile.findByUsername(inactiveProfile.username)
        assert true ==  inactiveServiceItem.isAuthor(systemUser)
        assert systemUser == inactiveServiceItem.createdBy
        assert systemUser == inactiveServiceItem.editedBy
        assert activeUser == UserAccount.findByUsername(activeUser.username)
        assert activeProfile == Profile.findByUsername(activeProfile.username)
        assert true ==  activeServiceItem.isAuthor(activeProfile)
        assert activeProfile == activeServiceItem.createdBy
        assert activeProfile == activeServiceItem.editedBy
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
