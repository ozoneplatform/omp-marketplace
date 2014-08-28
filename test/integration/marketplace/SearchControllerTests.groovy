package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import org.junit.Ignore
import grails.test.*
import grails.converters.JSON;
import ozone.marketplace.enums.DefinedDefaultTypes

@Ignore
@TestMixin(IntegrationTestMixin)
class SearchControllerTests {

	static final String SERVICE_ITEM_TITLE_A = "My Widget1"
	static final String SERVICE_ITEM_TITLE_B = "My Widget2"
	static final String SERVICE_ITEM_TITLE_C = "My Widget3"

    def sessionFactory
	def searchableService
	def searchNuggetService
	def serviceItemService
	def controller

    // the My Widgets
	def serviceItem1
	def serviceItem2
	def serviceItem3

    // the Caminos
    def serviceItem4
	def serviceItem5
	def serviceItem6
	def serviceItem7

    void setUp() {
        super.setUp()
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
        logIt 'testGetListAsJSON'
		controller.params.queryString = 'Widget3'
		controller.params.sort = 'title'

		controller.getListAsJSON()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		def total = controllerResponse.total
		def item = controllerResponse.data[0]
		assert 1 == total
		assert SERVICE_ITEM_TITLE_C == item.title
		assert "Active" == item.state.title
		assert "Microformats" == item.types.title
		assert "Geospatial" == item.categories[0].title
		assert "Approved" == item.approvalStatus
    }

    @Ignore
    void testGetListAsJSON2() {
        logIt 'testGetListAsJSON2'
		controller.params.queryString = 'Cami*'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
        controller.params.accessType = Constants.VIEW_USER

		controller.getListAsJSON()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		def total = controllerResponse.total
		def item = controllerResponse.data[0]

        println item
		assert 4 == total
		assert 'Camino 21' == item.title

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
        logIt 'testGetListAsJSONWithWildCard'
		controller.params.queryString = 'Camino'
		controller.params.sort = 'title'
		controller.getListAsJSON()
		def controllerResponse = JSON.parse(controller.response.contentAsString)
		def total = controllerResponse.total
		def item = controllerResponse.data[1]

		assert 4 == total

		controller.params.queryString = 'Cam*'
		controller.getListAsJSON()
		controllerResponse = JSON.parse(controller.response.contentAsString)
		total = controllerResponse.total
		assert 4 == total
	}

    @Ignore
	void testOpenSearchDefault() {
		controller.params.queryString = 'My'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
		controller.openSearch()
		def feed = new XmlParser().parseText(controller.response.contentAsString)
		assert ("feed" == feed.name().localPart)
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")
		assert null !=(feed.title.text())
		assert ("My" == feed[opensearchNs.Query][0].'@searchTerms')
		assert (serviceItem2.title == feed.entry[0].title.text())
        assert true == (containsAuthor(serviceItem2, feed.entry[0].author[0].name[0].text()))
	}

    @Ignore
	void testOpenSearchAtom() {
		controller.params.queryString = 'Widget2'
		controller.params.format = 'atom'
		controller.params.sort = 'title'
		controller.openSearch()

		def feed = new XmlParser().parseText(controller.response.contentAsString)
		assert ("feed" == feed.name().localPart)
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")

        println feed.entry[0].title.text()

		assert null !=(feed.title.text())
		assert ("Widget2" == feed[opensearchNs.Query][0].'@searchTerms')
		assert (serviceItem2.title == feed.entry[0].title.text())
		assert (serviceItem2.description == feed.entry[0].summary.text())
		assert true == (containsAuthor(serviceItem2, feed.entry[0].author[0].name[0].text()))
	}

    boolean containsAuthor(ServiceItem serviceItem, String authorDisplayName) {
        serviceItem2.owners.find {it.displayName == authorDisplayName} as boolean
    }

	/**
	 * If format is not valid (i.e., other than 'rss', 'atom' and 'html'), the feed should default to 'atom'
	 */
    @Ignore
	void testOpenSearchInvalidFormat() {
		controller.params.queryString = 'My'
		controller.params.format = 'xml'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
        controller.params.accessType = Constants.VIEW_USER
		controller.openSearch()
		def feed = new XmlParser().parseText(controller.response.contentAsString)
		assert ("feed" == feed.name().localPart)
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")

		assert null !=(feed.title.text())
		assert ("My" == feed[opensearchNs.Query][0].'@searchTerms')
		assert (serviceItem2.title == feed.entry[0].title.text())
        assert true == (containsAuthor(serviceItem2, feed.entry[0].author[0].name[0].text()))
	}

    @Ignore
	void testOpenSearchRss() {
		controller.params.queryString = 'Widget*'
		controller.params.format = 'rss'
		controller.params.sort = 'title'
		controller.params.order = 'asc'
		controller.openSearch()
		def rss = new XmlParser().parseText(controller.response.contentAsString)
		assert ("rss" == rss.name())
		def opensearchNs = new groovy.xml.Namespace("http://a9.com/-/spec/opensearch/1.1/")
		def dcNs = new groovy.xml.Namespace("http://purl.org/dc/elements/1.1/")

		assert null !=(rss.channel.title.text())
		assert ("Widget*" == rss.channel[opensearchNs.Query][0].'@searchTerms')

        println("serviceItem2.title = " + serviceItem2.title)
        println("other stuff = " + rss.channel.item[0].title.text())

		assert (serviceItem2.title == rss.channel.item[0].title.text())
		assert (serviceItem2.description == rss.channel.item[0].description.text())
		assert (serviceItem3.title == rss.channel.item[1].title.text())
		assert (serviceItem3.description == rss.channel.item[1].description.text())
        assert true == (containsAuthor(serviceItem3, rss.channel.item[dcNs.creator][0].text()))
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
		controller = new SearchController()
        sessionFactory.currentSession.flush()
		sessionFactory.currentSession.clear()
		controller.searchableService = searchableService
		controller.serviceItemService = serviceItemService
		controller.searchNuggetService = searchNuggetService
	}
}
