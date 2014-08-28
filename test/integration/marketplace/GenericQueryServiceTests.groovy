package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import grails.test.*

@TestMixin(IntegrationTestMixin)
class GenericQueryServiceTests {

    def genericQueryService
    def itemCommentService
    def profileService

    def owner

    private void setUpSortingTests(){
        for(i in 1..10){
            def profile = new Profile(username: "testRatingUser${i}", displayName: "Marketplace Tester ${i}")
            profile.save()
        }

        owner = new Profile(username: 'testOwner').save(failOnError:true)

        Types type = new Types(title: 'test type').save(failOnError:true)

        ServiceItem fiveStarSI = new ServiceItem(
            owners: [owner],
            title:"5 star",
            description:"A",
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: type,
            launchUrl: 'https:///'
        ).save(failOnError:true)
        ServiceItem oneStarSI = new ServiceItem(
            owners: [owner],
            title:"1 star",
            description:"B",
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: type,
            launchUrl: 'https:///'
        ).save(failOnError:true)
        ServiceItem threeStarSIw2 = new ServiceItem(
            owners: [owner],
            title:"3 star with 2 votes",
            description:"C",
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"],
            types: type,
            launchUrl: 'https:///'
        ).save(failOnError:true)
        ServiceItem threeStarSIw5 = new ServiceItem(
            owners: [owner],
            title:"3 star with 5 votes",
            description:"D",
            launchUrl: 'https:///',
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem threeStarSIw10 = new ServiceItem(
            owners: [owner],
            title:"3 star with 10 votes",
            description:"E",
            launchUrl: 'https:///',
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)

		def author1 = profileService.findByUsername("testRatingUser1")

		def itemComment1_u1si5star = new ItemComment(author: author1, serviceItem: fiveStarSI)
		itemCommentService.rate(itemComment1_u1si5star,5)

        oneStarSI.save()
		def itemComment2_u1si1star = new ItemComment(author: author1, serviceItem: oneStarSI)
		itemCommentService.rate(itemComment2_u1si1star,1)

        threeStarSIw2.save()
		for(i in 1..2){
			def currAuthor = profileService.findByUsername("testRatingUser${i}")
			def itemComment = new ItemComment(author: currAuthor, serviceItem: threeStarSIw2)
			itemCommentService.rate(itemComment,3)
        }

        threeStarSIw5.save()
        for(i in 1..5){
			def currAuthor = profileService.findByUsername("testRatingUser${i}")
			def itemComment = new ItemComment(author: currAuthor, serviceItem: threeStarSIw5)
			itemCommentService.rate(itemComment,3)
        }

        threeStarSIw10.save()
        for(i in 1..10){
			def currAuthor = profileService.findByUsername("testRatingUser${i}")
			def itemComment = new ItemComment(author: currAuthor, serviceItem: threeStarSIw10)
			itemCommentService.rate(itemComment,3)
        }
    }

    void testTotalVoteSort() {
        setUpSortingTests()

        def params = [sort_0: "totalVotes", order_0: "desc"]
        def list = genericQueryService.serviceItems(params)['serviceItemList']
        assert "3 star with 10 votes" == list[0].title
        assert "3 star with 5 votes" == list[1].title
        assert "3 star with 2 votes" == list[2].title

        params = [sort_0: "totalVotes", order_0: "asc"]
        list = genericQueryService.serviceItems(params)['serviceItemList']
        assert "3 star with 2 votes" == list[2].title
        assert "3 star with 5 votes" == list[3].title
        assert "3 star with 10 votes" == list[4].title
    }

    void testAverageRatingandTotalVoteSort() {
        setUpSortingTests()

        def params = [sort_0: "avgRate", sort_1: "totalVotes", order_0: "desc", order_1: "desc"]
        def list = genericQueryService.serviceItems(params)['serviceItemList']
        assert "5 star" == list[0].title
        assert "3 star with 10 votes" == list[1].title
        assert "3 star with 5 votes" == list[2].title
        assert "3 star with 2 votes" == list[3].title
        assert "1 star" == list[4].title

        params = [sort_0: "avgRate", sort_1: "totalVotes", order_0: "asc", order_1: "asc"]
        list = genericQueryService.serviceItems(params)['serviceItemList']
        assert "1 star" == list[0].title
        assert "3 star with 2 votes" == list[1].title
        assert "3 star with 5 votes" == list[2].title
        assert "3 star with 10 votes" == list[3].title
        assert "5 star" == list[4].title
    }

    void testThreeSortValues() {

        Types typesAAA = new Types(title:"AAA").save(failOnError:true)
        Types typesBBB = new Types(title:"BBB").save(failOnError:true)
        Types typesCCC = new Types(title:"CCC").save(failOnError:true)
        owner = new Profile(username: 'testOwner').save(failOnError:true)

        ServiceItem si4 = new ServiceItem(
            owners: [owner],
            title:"NNN",
            types: typesCCC,
            description:"D",
            launchUrl: 'https:///',
            isHidden: 0,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si1 = new ServiceItem(
            owners: [owner],
            title:"MMM",
            description:"A",
            types: typesAAA,
            launchUrl: 'https:///',
            isHidden: 0,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si3 = new ServiceItem(
            owners: [owner],
            title:"MMM",
            types: typesCCC,
            description:"C",
            launchUrl: 'https:///',
            isHidden: 0,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si2 = new ServiceItem(
            owners: [owner],
            title:"MMM",
            description:"A",
            types: typesBBB,
            launchUrl: 'https:///',
            isHidden: 0,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true, flush: true)

        def params = [sort_0: "title", sort_1: "description", sort_2: "types_title", order_0: "asc", order_1: "asc", order_2: "asc"]
        def list = genericQueryService.serviceItems(params)['serviceItemList']
        assert list[0].id == si1.id
        assert list[1].id == si2.id
        assert list[2].id == si3.id
        assert list[3].id == si4.id

        params = [sort_2: "types_title", sort_0: "title", sort_1: "description", order_2: "desc", order_0: "desc", order_1: "desc",accessType: Constants.VIEW_USER]
        list = genericQueryService.serviceItems(params)['serviceItemList']
        assert list[0].id == si4.id
        assert list[1].id == si3.id
        assert list[2].id == si2.id
        assert list[3].id == si1.id
    }

    void testSearchByCategory() {
        State stateA = new State(title:"State A").save(failOnError:true)
        State stateB = new State(title:"State B").save(failOnError:true)

        Category catA = new Category(title:"State A").save(failOnError:true)
        Category catB = new Category(title:"State B").save(failOnError:true)

        Types type = new Types(title: 'test type').save(failOnError:true)

        ServiceItem si1 = new ServiceItem(
            title: 'si1',
            owners: [owner],
            categories: [catA],
            state: stateA,
            types: type,
            launchUrl: 'https:///',
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si2 = new ServiceItem(
            title: 'si2',
            owners: [owner],
            categories: [catA, catB],
            state: stateB,
            types: type,
            launchUrl: 'https:///',
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si3 = new ServiceItem(
            title: 'si3',
            owners: [owner],
            launchUrl: 'https:///',
            categories: [catB],
            types: type,
            state: stateA,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true, flush:true)

        println "catA.id = ${catA.id}"
        def params = [categories_id: catA.id, sort_0: "state_title", order_0: "desc"]
        def list = genericQueryService.serviceItems(params)['serviceItemList']
        assert 2 == list.size()
        assert list[0].id == si2.id
        assert list[1].id == si1.id
    }

    void testSearchByUserName(){
        Profile profile1 = new Profile(username: "Test User 1", displayName: "Test User 1")
        Profile profile2 = new Profile(username: "Test User 2", displayName: "Test User 2")
        profile1.save()
        profile2.save()

        Types type = new Types(title: 'test type').save(failOnError:true)

        ServiceItem si1 = new ServiceItem(
            owners: [profile1] ,
            launchUrl: 'https:///',
            title:"D",
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si2 = new ServiceItem(
            owners: [profile1],
            launchUrl: 'https:///',
            title:"C",
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si3 = new ServiceItem(
            owners: [profile1],
            launchUrl: 'https:///',
            title:"B",
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si4 = new ServiceItem(
            owners: [profile1],
            launchUrl: 'https:///',
            title:"A",
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)
        ServiceItem si5 = new ServiceItem(
            owners: [profile2],
            launchUrl: 'https:///',
            title:"Z",
            types: type,
            approvalStatus:Constants.APPROVAL_STATUSES["APPROVED"]
        ).save(failOnError:true)

        si1.save()
        si2.save()
        si3.save()
        si4.save()
        si5.save(flush:true)

        def params = [author_username: "Test User 1", sort_0: "title", order_0: "asc"]
        def list = genericQueryService.serviceItems(params)['serviceItemList']
        assert 4 == list.size()
        assert list[0].id == si4.id
        assert list[1].id == si3.id
        assert list[2].id == si2.id
        assert list[3].id == si1.id

        params = [author_displayName: "Test User 1", sort_0: "title", order_0: "asc"]
        list = genericQueryService.serviceItems(params)['serviceItemList']
        assert 4 == list.size()
        assert list[0].id == si4.id
        assert list[1].id == si3.id
        assert list[2].id == si2.id
        assert list[3].id == si1.id
    }
}
