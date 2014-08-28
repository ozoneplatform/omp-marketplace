package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import marketplace.rest.ServiceItemActivityInternalService

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin(DomainClassUnitTestMixin)
class ItemCommentServiceUnitTests {

    ItemCommentService service

    def profileServiceMock, item, serviceItemServiceMock, activityServiceMock, session, activities

    private static Profile createProfile(String name) {
        new Profile(username: name, createdDate: new Date())
    }

    private static ServiceItem createServiceItem(String title) {
        def type = new Types(title: 'sometype')
        def profile = createProfile('theowner')
        new ServiceItem(title: title, types: type, owners: [profile], launchUrl: 'https://')
    }

    void setUp() {
        FakeAuditTrailHelper.install()

        mockDomain(ItemComment)
        mockDomain(Types)
        mockDomain(Profile)
        mockDomain(ServiceItemActivity)
        mockDomain(ChangeDetail)
        profileServiceMock = mockFor(ProfileService)
        serviceItemServiceMock = mockFor(ServiceItemService)
        activityServiceMock = mockFor(ServiceItemActivityInternalService)

        item = createServiceItem('the item')
        mockDomain(ServiceItem, [item])

        service = new ItemCommentService()
        service.profileService = profileServiceMock.createMock()
        service.serviceItemService = serviceItemServiceMock.createMock()
        service.serviceItemActivityInternalService = activityServiceMock.createMock()

        serviceItemServiceMock.demand.getAllowableItem(0..99) { id, params, rules -> item }
        ItemCommentService.metaClass.retrieveHttpSession = { -> session }

        activities = []
        activityServiceMock.demand.addServiceItemActivity(0..99) { item, activity -> activities << activity }
    }

    private ItemComment createComment() {
        def text = 'This is the best app in the world!! #loveit'
        def username = 'someone'

        session = [username: username, isAdmin: true]

        ItemCommentService.metaClass.getHttpSession = { [username: username, isAdmin: false] }
        profileServiceMock.demand.findByUsername(1) { name -> createProfile(username) }

        def params = [
                userSystemUser: false,
                commentTextInput: text,
                newUserRating: 3.0,
                serviceItemId: item.id
        ]

        ItemComment comment = service.saveItemComment(params)

        assert comment.text == text
        assert comment.author.username == username
        assert item.itemComments?.size() == 1
        assert item.avgRate == 3.0

        return comment
    }

    public void testSaveNewItemComment() {
        createComment()
    }

    public void testEditExistingItemCommentBySameUser() {
        def comment = createComment()
        def profile = comment.author

        def text = 'new Text'
        def username = profile.username

        session = [username: username, isAdmin: false]

        def params = [
                userSystemUser: false,
                commentTextInput: text,
                newUserRating: 5.0,
                serviceItemId: item.id,
                id: comment.id
        ]

        service.saveItemComment(params)

        assert comment.text == text
        assert item.itemComments?.size() == 1
        assert item.avgRate == 5.0
        assert activities.size() == 0
    }

    public void testEditExistingItemCommentByAnotherUser() {
        def comment = createComment()

        def text = 'new Text'
        def username = 'someoneElse'

        session = [username: username, isAdmin: true]

        profileServiceMock.demand.findByUsername(1) { name -> createProfile(username) }

        def params = [
                userSystemUser: false,
                commentTextInput: text,
                newUserRating: 3.0,
                serviceItemId: item.id,
                id: comment.id
        ]

        service.saveItemComment(params)

        assert comment.text == text
        assert item.itemComments?.size() == 1
        assert item.avgRate == 3.0
        assert activities.size() == 1
    }

    public void testItemCommentDelete() {
        def comment = createComment()

        def username = 'someoneElse'

        session = [username: username, isAdmin: true]
        profileServiceMock.demand.findByUsername(1) { name -> createProfile(username) }

        def params = [
                userSystemUser: false,
                serviceItemId: item.id,
                itemCommentId: comment.id
        ]

        service.findAndDeleteItemComment(params)

        assert item.itemComments?.size() == 0
        assert item.avgRate == 0.0
        assert activities.size() == 1
    }
}
