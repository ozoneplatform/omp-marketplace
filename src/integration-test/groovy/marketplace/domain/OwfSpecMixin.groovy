package marketplace.domain

import marketplace.AccountService
import marketplace.Constants
import org.springframework.beans.factory.annotation.Autowired

import org.hibernate.SessionFactory

import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormValidateable

import ozone.utils.User

//import ozone.owf.grails.domain.ERoleAuthority
//import ozone.owf.grails.domain.Person
//import ozone.owf.grails.domain.Role


trait OwfSpecMixin {

    @Autowired
    SessionFactory sessionFactory

    void flushSession() {
        sessionFactory.currentSession.flush()
    }

//	int testAdmin1ID
//	int testUser1ID
//	int testUser2ID
//	def accountService

//	void setUp() {
//        accountService.serviceModelService = new ServiceModelService()
//
//        //needed for the following 3 createOrUpdate calls
//        loginAsAdmin()
//
//		accountService.createOrUpdate(createTestUser1())
//		accountService.createOrUpdate(createTestUser2())
//		accountService.createOrUpdate(createTestAdmin())
//
//        //clear admin login
//		SCH.clearContext()
//
//		testAdmin1ID = Person.findByUsername("testAdmin1").id
//		testUser1ID = Person.findByUsername("testUser1").id
//		testUser2ID = Person.findByUsername("testUser2").id
//	}

//	def createTestAdmin(){
//			Map map =
//					[
//							data: [[
//										   username: 'testAdmin1',
//										   userRealName: 'Test Admin',
//										   enable: true,
//										   email: 'something@something.com'
//								   ]].asType(JSON).toString()
//      ]
//	}
//
//	def createTestUser1(){
//      Map map =
//      [
//         data:[[
//              username: 'testUser1',
//              userRealName: 'Test User 1',
//              enable: true,
//              email: 'something@something.com'
//         ]].asType(JSON).toString()
//      ]
//    }
//
//	def createTestUser2(){
//      Map map =
//      [
//         data: [[
//                 username: 'testUser2',
//                 userRealName: 'Test User 2',
//                 enable: true,
//                 email: 'something@something.com'
//         ]].asType(JSON).toString()
//      ]
//	}
//
//	def createWidgetDefinition() {
//		def json =
//		[
//		 	imageUrlSmall: "../images/blue/icons/widgetContainer/widgetEmptysm.gif",
//		 	imageUrlLarge: "../images/blue/icons/widgetIcons/nearlyEmpty.gif",
//		 	checkedTargets:  new JSON([['id':testAdmin1ID], ['id':testUser1ID], ['id':testUser2ID] ]).toString(),
//		 	width:200,
//		 	uncheckedTargets:4,
//		 	height:200,
//		 	widgetUrl: "../examples/walkthrough/widgets/NearlyEmptyWidget.html",
//		 	isExtAjaxFormat:true,
//		 	action: "createOrUpdateWidgetDefinition",
//		 	widgetGuid: "6859f52e-2196-4880-8871-ba7b0ab057b7",
//            universalName: "6859f52e-2196-4880-8871-ba7b0ab057b7",
//            widgetVersion: "1.0",
//		 	controller: "administration",
//		 	displayName:"New Empty Widget 1",
//		 	adminEnabled:true
//		]
//		return json
//	}

    static <T extends GormEntity<T>> T save(T object) {
        assert validateAndPrintErrors(object)

        T result = object.save(flush: true)
        assert result != null

        result
    }

    static boolean validateAndPrintErrors(GormValidateable object) {
        def isValid = object.validate()
        if (!isValid) {
            object.errors.allErrors.each { println it }
        }
        isValid
    }

    static <T> T verifyEqualTo(T expectedValue, Closure<T> closure) {
        T actualValue = closure.call()

        assert actualValue == expectedValue

        actualValue
    }

    static <T> T verifyNotNull(Closure<T> closure) {
        T value = closure.call()

        assert value != null

        value
    }

    private String cachedAdminRole;
    private String cacheUserRole;

    String demandUserRole() {
        if (cacheUserRole == null) {
            cacheUserRole =
                    Constants.USER
//                    new Role(authority: ERoleAuthority.ROLE_USER.strVal, description: "User Role")
//                            .save(flush: true, failOnError: true)
        }
        cacheUserRole
    }

    String demandAdminRole() {
        if (cachedAdminRole == null) {
            cachedAdminRole = Constants.ADMIN
//                    new Role(authority: ERoleAuthority.ROLE_ADMIN.strVal, description: "Admin Role")
//                            .save(flush: true, failOnError: true)
        }
        cachedAdminRole
    }

    User createUser(name = "testUser1") {
        def user = new User(
                username: name,
                name: "${name} the User",
                email: "${name}@example.com"
        )
        AccountService accountService = new AccountService()
        accountService.getUserDomain(user)
        save(user)
        return user
    }

    User createAdmin(String name = "testAdmin1") {
        def user = new User(
                username: name,
                name: "${name} the Admin",
                email: "${name}@example.com",
        )
        AccountService accountService = new AccountService()
        accountService.getUserDomain(user)
        save(user)
        return user
    }

//	def queryDashboardByUser(username, dashboardname) {
//		def person = Person.createCriteria().list {
//			and {
//				eq('username', username)
//				dashboards {
//					eq('name', dashboardname)
//				}
//			}
//		}
//
//		return person.dashboards
//	}

}
