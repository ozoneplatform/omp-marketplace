package marketplace.grails.service

import spock.lang.Ignore
import spock.lang.Specification

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest

import marketplace.ChangeDetail
import marketplace.ItemComment
import marketplace.ItemCommentService
import marketplace.Profile
import marketplace.ProfileService
import marketplace.ServiceItem
import marketplace.ServiceItemActivity
import marketplace.ServiceItemService
import marketplace.Types
import marketplace.rest.ServiceItemActivityInternalService

//TODO BVEST Come back
//@TestMixin(DomainClassUnitTestMixin)
class ItemCommentServiceSpec extends Specification implements DataTest, ServiceUnitTest<ItemCommentService>{
    List<Class> getDomainClasses() {[ItemComment,Types,Profile,ServiceItemActivity,ChangeDetail,ProfileService,ServiceItemService,ServiceItemActivityInternalService, ServiceItem]}

//    ItemCommentService service

    def item, serviceItemServiceMock, session, activities, profileServiceMock

    private static Profile createProfile(String name) {
        new Profile(username: name, createdDate: new Date())
    }

    private static ServiceItem createServiceItem(String title) {
        def type = new Types(title: 'sometype')
        def profile = createProfile('theowner')
        new ServiceItem(title: title, types: type, owners: [profile], launchUrl: 'https://')
    }

    void setupData(username) {
        activities = []
        if(username == '' || username == null)
            profileServiceMock = Mock(ProfileService)
        else {
            profileServiceMock = Mock(ProfileService) {
                findByUsername(_) >> { name -> createProfile(username) }
            }
        }
        serviceItemServiceMock = Mock(ServiceItemService) {
            getAllowableItem(*_) >> {id, params, rules -> item}
        }
//        activityServiceMock = Mock(ServiceItemActivityInternalService) {
//            addServiceItemActivity(*_) >> {item, activity -> activities << activity}
//        }

        item = createServiceItem('the item')

        mockDomain(ServiceItem, [item])

        service.serviceItemActivityInternalService = Mock(ServiceItemActivityInternalService) {
            addServiceItemActivity(*_) >> {item, activity -> activities << activity}
        }
        service.serviceItemService = serviceItemServiceMock
        service.profileService = profileServiceMock

        service.retrieveHttpSession()  >> { -> session }

//        service.serviceItemService = serviceItemServiceMock
//        service.serviceItemActivityInternalService = activityServiceMock

        //service.retrieveHttpSession() { -> session }
//       service.retrieveHttpSession = { -> session }
    }

    private ItemComment createComment() {
        def text = 'This is the best app in the world!! #loveit'
        def username = 'someone'

        session = [username: username, isAdmin: true]

        def params = [useSystemUser   : false,
                      commentTextInput: text,
                      newUserRating   : 3.0,
                      serviceItemId   : item.id]

        ItemComment comment = service.saveItemComment(params, username, false)

        assert comment.text == text
        assert comment.author.username == username
        assert item.itemComments?.size() == 1
        assert item.avgRate == 3.0

        return comment
    }

    //TODO BVEST Review
    @Ignore
    public void testSaveNewItemComment() {
        setup:
        setupData()
        expect:
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
