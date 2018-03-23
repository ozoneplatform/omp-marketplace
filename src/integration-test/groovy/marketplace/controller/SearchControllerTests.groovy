package marketplace.controller

import grails.gorm.transactions.Rollback
import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import grails.testing.mixin.integration.Integration
import marketplace.Category
import marketplace.Profile
import marketplace.SearchController
import marketplace.ServiceItem
import marketplace.State
import marketplace.Types
import marketplace.controller.ControllerTestMixin
import spock.lang.Ignore
import grails.test.*
import grails.converters.JSON;
import ozone.marketplace.enums.DefinedDefaultTypes
import spock.lang.Specification

@Ignore
@Rollback
@Integration
class SearchControllerTests extends Specification implements ControllerTestMixin<SearchController>{

	static final String SERVICE_ITEM_TITLE_A = "My Widget1"
	static final String SERVICE_ITEM_TITLE_B = "My Widget2"
	static final String SERVICE_ITEM_TITLE_C = "My Widget3"

    def sessionFactory
	def searchableService
	def searchNuggetService
	def serviceItemService
    // the My Widgets
	def serviceItem1
	def serviceItem2
	def serviceItem3

    // the Caminos
    def serviceItem4
	def serviceItem5
	def serviceItem6
	def serviceItem7

    void setup() {
        //super.setUp()
        setUpController()

		serviceItemService.getSession().username = 'userA'
		createTestListings()

		indexFlushAndRefresh()
    }

    protected void tearDown() {
		unindexFlushAndRefresh()
        super.tearDown()
    }

	void unindexFlushAndRefresh(){
		searchableService.unindexAll()
		searchableService.flushAndRefreshIndex()
	}

	void indexFlushAndRefresh(Integer loop = 2){
		for(int idx = 0; idx < loop; idx++){
			searchableService.reindexAll()
			def failedShards = searchableService.flushAndRefreshIndex()
			assert 0 == failedShards
			Thread.sleep(500)
		}
	}

    def logIt(def strIn)
    {
        controller.logIt(strIn)
    }

    @Ignore
    void testGetListAsJSON() {
        when:
		logIt 'testGetListAsJSON'
		controller.params.queryString = 'Widget3'
		controller.params.sort = 'title'

		controller.getListAsJSON()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		def total = controllerResponse.total
		def item = controllerResponse.data[0]
		then:
		1 == total
		SERVICE_ITEM_TITLE_C == item.title
		"Active" == item.state.title
		"Microformats" == item.types.title
		"Geospatial" == item.categories[0].title
		"Approved" == item.approvalStatus
    }

    @Ignore
    void testGetListAsJSON2() {
        when:
		logIt 'testGetListAsJSON2'
		controller.params.queryString = 'Cami*'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
        controller.params.accessType = Constants.VIEW_USER

		controller.getListAsJSON()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		def total = controllerResponse.total
		def item = controllerResponse.data[0]

		then:
		4 == total
		'Camino 21' == item.title

// TODO: figure out why this doesn't work - charlie
//      controller.params.queryString = 'Cami*'
//		controller.params.sort = 'title'
//      controller.params.accessType = 'Analyst'
//		controller.getListAsJSON()
//      logIt controller.response.contentAsString
//		def controllerResponse2 = JSON.parse(controller.response.contentAsString)
//		def total2 = controllerResponse2.total
//		assert 2 == total2
    }

    @Ignore
	void testGetListAsJSONWithWildCard() {
        when:
		logIt 'testGetListAsJSONWithWildCard'
		controller.params.queryString = 'Camino'
		controller.params.sort = 'title'
		controller.getListAsJSON()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		def total = controllerResponse.total
		def item = controllerResponse.data[1]

		then:
		4 == total

		when:
		controller.params.queryString = 'Cam*'
		controller.getListAsJSON()
		controllerResponse = JSON.parse(controller.response.contentAsString)
		total = controllerResponse.total
		then:
		4 == total
	}

    @Ignore
	void testOpenSearchDefault() {
		when:
		controller.params.queryString = 'My'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
		controller.openSearch()
		def feed = new XmlParser().parseText(controller.response.contentAsString)
		then:
		"feed" == feed.name().localPart

		when:
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")
		then:
		null !=(feed.title.text())
		"My" == feed[opensearchNs.Query][0].'@searchTerms'
		serviceItem2.title == feed.entry[0].title.text()
        containsAuthor(serviceItem2, feed.entry[0].author[0].name[0].text())
	}

    @Ignore
	void testOpenSearchAtom() {
		when:
		controller.params.queryString = 'Widget2'
		controller.params.format = 'atom'
		controller.params.sort = 'title'
		controller.openSearch()

		def feed = new XmlParser().parseText(controller.response.contentAsString)
		then:
		"feed" == feed.name().localPart

		when:
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")

		then:
		null !=(feed.title.text())
		"Widget2" == feed[opensearchNs.Query][0].'@searchTerms'
		serviceItem2.title == feed.entry[0].title.text()
		serviceItem2.description == feed.entry[0].summary.text()
		containsAuthor(serviceItem2, feed.entry[0].author[0].name[0].text())
	}

    boolean containsAuthor(ServiceItem serviceItem, String authorDisplayName) {
        serviceItem2.owners.find {it.displayName == authorDisplayName} as boolean
    }

	/**
	 * If format is not valid (i.e., other than 'rss', 'atom' and 'html'), the feed should default to 'atom'
	 */
    @Ignore
	void testOpenSearchInvalidFormat() {
		when:
		controller.params.queryString = 'My'
		controller.params.format = 'xml'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
        controller.params.accessType = Constants.VIEW_USER
		controller.openSearch()
		def feed = new XmlParser().parseText(controller.response.contentAsString)
		then:
		"feed" == feed.name().localPart

		when:
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")

		then:
		null !=(feed.title.text())
		"My" == feed[opensearchNs.Query][0].'@searchTerms'
		serviceItem2.title == feed.entry[0].title.text()
        containsAuthor(serviceItem2, feed.entry[0].author[0].name[0].text())
	}

    @Ignore
	void testOpenSearchRss() {
		when:
		controller.params.queryString = 'Widget*'
		controller.params.format = 'rss'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
		controller.openSearch()
		def rss = new XmlParser().parseText(controller.response.contentAsString)
		then:
		"rss" == rss.name()

		when:
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")
		def dcNs = new groovy.xml.Namespace("http://purl.org/dc/elements/1.1/")

		then:
		null !=(rss.channel.title.text())
		"Widget*" == rss.channel[opensearchNs.Query][0].'@searchTerms'

		serviceItem2.title == rss.channel.item[0].title.text()
		serviceItem2.description == rss.channel.item[0].description.text()
		serviceItem3.title == rss.channel.item[1].title.text()
		serviceItem3.description == rss.channel.item[1].description.text()
        containsAuthor(serviceItem3, rss.channel.item[dcNs.creator][0].text())
	}

	private void createTestListings() {
		Profile profile = new Profile(username: 'userA', displayName: 'Marketplace Tester A', createdDate: new Date())
		profile.save(flush:true)

		State state = State.findByTitle("Active")
        assert null !=(state)

		Category catA = Category.findByTitle("Geospatial")
		Category catB = Category.findByTitle("Reporting")

		assert null !=(catA)
        assert null !=(catB)

		Types typeMicroformat = Types.findByTitle(DefinedDefaultTypes.MICROFORMATS.title)
        assert null !=(typeMicroformat)
		Types typeWebapps = Types.findByTitle(DefinedDefaultTypes.WEB_APP.title)
        typeWebapps.save(failOnError: true, flush:true)
        assert null !=(typeWebapps)


		serviceItem1 = ServiceItem.build(title:SERVICE_ITEM_TITLE_A, description: 'Service Item 1', approvalStatus:Constants.APPROVAL_STATUSES["PENDING"], types:typeMicroformat, categories:[catA], state:state, owners: [profile], isOutside: true)
		serviceItem2 = ServiceItem.build(title:SERVICE_ITEM_TITLE_B, description: 'Service Item 2', approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"], types:typeWebapps, categories:[catB], state:state, owners: [profile], isOutside: true)
		serviceItem3 = ServiceItem.build(title:SERVICE_ITEM_TITLE_C, description: 'Service Item 3', approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"], types:typeMicroformat, categories:[catA,catB], state:state, owners: [profile], isOutside: true)
		serviceItem1.save(failOnError: true, flush:true)
		serviceItem2.save(failOnError: true, flush:true)
		serviceItem3.save(failOnError: true, flush:true)
		assert (3 == ServiceItem.count())

        serviceItem4 = ServiceItem.build(title:'Camino 21', description: 'Service Item 4', approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"], types:typeMicroformat, categories:[catA,catB], state:state, owners: [profile], isOutside: true)
		serviceItem4.save(failOnError: true, flush:true)
        serviceItem5 = ServiceItem.build(title:'Camino 22', description: 'Service Item 5', approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"], types:typeMicroformat, categories:[catA,catB], state:state, owners: [profile], isOutside: true)
		serviceItem5.save(failOnError: true, flush:true)
        serviceItem6 = ServiceItem.build(title:'Camino 23', description: 'Service Item 6', approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"], types:typeWebapps, categories:[catA,catB], state:state, owners: [profile], isOutside: true)
		serviceItem6.save(failOnError: true, flush:true)
        serviceItem7 = ServiceItem.build(title:'Camino 24', description: 'Service Item 7', approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"], types:typeWebapps, categories:[catA,catB], state:state, owners: [profile], isOutside: true)
		serviceItem7.save(failOnError: true, flush:true)

		assert (7 == ServiceItem.count())
	}

	private void setUpController() {
        sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
		controller.searchableService = searchableService
		controller.serviceItemService = serviceItemService
		controller.searchNuggetService = searchNuggetService
	}
}
