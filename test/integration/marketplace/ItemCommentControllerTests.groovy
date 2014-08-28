package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.converters.JSON;
/**
 * Created by IntelliJ IDEA.
 * Date: Jun 30, 2010
 * Time: 5:41:18 PM
 */

@TestMixin(IntegrationTestMixin)
class ItemCommentControllerTests extends MarketplaceIntegrationTestCase {

  def controller
  def itemCommentService
  def searchableService
  def profileService
  def serviceItemService
  def sessionFactory

  void setUp() {
    super.setUp();
    controller = new ItemCommentController()
	controller.sessionFactory = sessionFactory
    controller.itemCommentService = itemCommentService
	controller.serviceItemService = serviceItemService
	controller.searchableService = searchableService
	controller.profileService = profileService
  }

  /**
   * Test for a valid comment that can be seen by a user
   * that was created by a different person other than
   * the one issuing the request
   */
  void testGetValidComment() {
    createComments(1, true, Constants.VIEW_ADMIN, "david", null);

    controller.getUserComments()
    println controller.response.contentAsString
    def controllerResponse = JSON.parse(controller.response.contentAsString)

    // We should get one comment back with a success return
    assert 1 == controllerResponse.totalCount
    assert true ==  controllerResponse.success
  }

  /**
   * Test for a comment that has been requested by a user
   * that is an admin where the comment is on a non approved
   * item
   * approved
   */
  void testGetNonApprovedAdminComment() {
    createComments(0, true, Constants.VIEW_ADMIN, "david", null);

    controller.getUserComments()
    println controller.response.contentAsString
    def controllerResponse = JSON.parse(controller.response.contentAsString)

    // We should get one comment back with a success return
    assert 1 == controllerResponse.totalCount
    assert true ==  controllerResponse.success
  }

  /**
   * Test for a comment that has been created by the user issuing
   * the request but would normally not be available to the
   * user due to the item not being approved
   */
  void testGetNonApprovedOwnerComment() {
    createComments(0, true, Constants.VIEW_ADMIN, "john", null);

    controller.getUserComments()
    println controller.response.contentAsString
    def controllerResponse = JSON.parse(controller.response.contentAsString)

    // We should get one comment back with a success return
    assert 1 == controllerResponse.totalCount
    assert true ==  controllerResponse.success
  }

  /**
   * Test for a comment that has been created by a different user
   * than the one issuing the request but would normally not be available to the
   * user due to the item not being
   * approved
   */
  void testGetNonApprovedDevComment() {
    controller = createComments(0, false, Constants.USER, "david", null);

    controller.getUserComments()
    println controller.response.contentAsString
    def controllerResponse = JSON.parse(controller.response.contentAsString)

    // We should get one comment back with a success return
    assert 0 == controllerResponse.totalCount
    assert true ==  controllerResponse.success
  }

  /**
   * This helper method is used to create various type of service items with
   * comments.
   * @param approved - Boolean to set the service item approval (0 or 1)
   * @param isAdmin - Boolean to set whether the creator is an Admin (true or false)
   * @param view - Developer, Analyst or Administrator
   * @param username - Name of the user creating the service item
   * @param type - The type of service item being created
   */
  def createComments(def approved, def isAdmin, def view, def username, def type) {
    State state = new State(title: "Active").save(failOnError:true)
    Category cat = new Category(title: "Category A").save(failOnError:true)
    Types types1 = (type) ? (type) : (new Types(title: "Widget").save(failOnError:true))
    //if (!type){
    //types1 = new Types(title: "Widget").save(failOnError:true)
    //}
    def avatar = Avatar.findByIsDefault(true)
    def profile = new Profile(username: "john", displayName: "john test")
	profile.createdDate = new Date()
    profile.avatar = avatar
    profile.save(flush: true)

    ItemComment itemComment1 = new ItemComment(createdDate: new Date(), author: profile, text: "This is an example text string")

    def apprStatus = approved?Constants.APPROVAL_STATUSES["APPROVED"]:Constants.APPROVAL_STATUSES["IN_PROGRESS"]
    ServiceItem serviceItem1 = new ServiceItem(
        title: "My Widget1",
        approvalStatus: apprStatus,
        isHidden: 1,
        owners: [profile],
        types: types1,
        categories: [cat],
        state: state,
        launchUrl: 'https:///'
    ).save(failOnError:true)
    serviceItem1.addToItemComments(itemComment1)
    serviceItem1.save(flush: true)
    itemComment1.save()

    // Create the controller object
    controller.params.id = "john"
    controller.params.limit = "1"
    controller.params.start = "0"
    controller.params.dir = "asc"
    controller.params.sort = "name"
    controller.session.accessType = view
    controller.session.isAdmin = isAdmin
    controller.session.username = username

    return controller
  }


  void testAccessControlSaveCommentAsUser() {
	  def user1 = new Profile(username: 'User1', displayName: 'User1', createdDate: new Date()).save(failOnError:true)
	  user1.save(flush:true)
	  def result
	  Profile user = new Profile(username: 'testAdmin1', displayName: 'Test Admin', createdDate: new Date()).save(failOnError:true)
	  user.save(flush:true)
	  ServiceItem item = new ServiceItem(
        title:"My Item",
        description:"This is the description",
        owners: [user],
        types: new Types(title: 'test type').save(failOnError:true),
        launchUrl: 'https:///'
      ).save(failOnError:true)
	  item.save(flush:true)
	  controller.params.serviceItemId = item.id
	  controller.params.commentTextInput = "This is a comment"
	  controller.session.username = "User1"
	  switchUser('User1')
	  controller.saveItemComment()
	  result = JSON.parse(controller.response.contentAsString)
	  assert true ==  result.message.contains("User is not authorized to access")
  }

  void testAccessControlDeleteCommentAsUser() {
	  def user1 = new Profile(username: 'User1', displayName: 'User1', createdDate: new Date()).save(failOnError:true)
	  user1.save(flush:true)
	  def result
	  Profile user = new Profile(username: 'testAdmin1', displayName: 'Test Admin', createdDate: new Date()).save(failOnError:true)
	  user.save(flush:true)
	  ServiceItem item = new ServiceItem(
        title:"My Item",
        description:"This is the description",
        owners: [user],
        types: new Types(title: 'test type').save(failOnError:true),
        launchUrl: 'https:///'
      ).save(failOnError:true)
	  item.save(flush:true)
	  ItemComment comment = new ItemComment(text:"This is a comment", serviceItem:item, author:user, createdDate:new Date()).save(failOnError:true)
	  comment.save(flush:true)
	  controller.params.serviceItemId = item.id
	  controller.params.itemCommentId = comment.id
	  controller.session.username = "User1"
	  switchUser('User1')
	  controller.deleteItemComment()
	  result = JSON.parse(controller.response.contentAsString)
	  assert true ==  result.message.contains("User is not authorized to access")
  }
}
