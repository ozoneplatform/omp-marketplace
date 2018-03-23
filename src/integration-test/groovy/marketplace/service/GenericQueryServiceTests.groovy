package marketplace.service

import spock.lang.Ignore
import spock.lang.Specification

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration

import marketplace.Category
import marketplace.Constants
import marketplace.GenericQueryService
import marketplace.ItemComment
import marketplace.ItemCommentService
import marketplace.Profile
import marketplace.ProfileService
import marketplace.ServiceItem
import marketplace.State
import marketplace.Types


@Integration
@Rollback
class GenericQueryServiceTests extends Specification {

    GenericQueryService genericQueryService
    ItemCommentService itemCommentService
    ProfileService profileService

    void "serviceItems: sort by 'totalVotes' descending"() {
        given:
        createServiceItemsForSortingTests()

        when:
        def params = [sort_0: "totalVotes", order_0: "desc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list[0..2].title == ["3 star with 10 votes",
                             "3 star with 5 votes",
                             "3 star with 2 votes"]
    }

    void "serviceItems: sort by 'totalVotes' ascending"() {
        given:
        createServiceItemsForSortingTests()

        when:
        def params = [sort_0: "totalVotes", order_0: "asc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list[2..4].title == ["3 star with 2 votes",
                             "3 star with 5 votes",
                             "3 star with 10 votes"]
    }

    void "serviceItems: sort by 'avgRate' descending, 'totalVotes' descending"() {
        given:
        createServiceItemsForSortingTests()

        when:
        def params = [sort_0: "avgRate", sort_1: "totalVotes", order_0: "desc", order_1: "desc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list*.title == ["5 star",
                        "3 star with 10 votes",
                        "3 star with 5 votes",
                        "3 star with 2 votes",
                        "1 star"]
    }

    void "serviceItems: sort by 'avgRate' ascending, 'totalVotes' descending"() {
        given:
        createServiceItemsForSortingTests()

        when:
        def params = [sort_0: "avgRate", sort_1: "totalVotes", order_0: "asc", order_1: "asc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list*.title == ["1 star",
                        "3 star with 2 votes",
                        "3 star with 5 votes",
                        "3 star with 10 votes",
                        "5 star"]
    }

    void "serviceItems: sort by 3 columns ascending"() {
        given:
        def serviceItems = createServiceItemsFor3ColumnSorting()

        when:
        def params = [sort_0: "title", sort_1: "description", sort_2: "types_title", order_0: "asc", order_1: "asc", order_2: "asc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list*.id == serviceItems*.id

    }

    void "serviceItems: sort by 3 columns descending"() {
        given:
        def serviceItems = createServiceItemsFor3ColumnSorting()

        when:
        def params = [sort_2: "types_title", sort_0: "title", sort_1: "description", order_2: "desc", order_0: "desc", order_1: "desc", accessType: Constants.VIEW_USER]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list*.id == serviceItems.reverse()*.id
    }

    void "serviceItems: search by 'categories_id'"() {
        when:
        Profile owner = new Profile(username: 'testOwner').save(failOnError: true)

        State stateA = new State(title: "State A").save(failOnError: true)
        State stateB = new State(title: "State B").save(failOnError: true)

        Category catA = new Category(title: "State A").save(failOnError: true)
        Category catB = new Category(title: "State B").save(failOnError: true)

        Types type = new Types(title: 'test type').save(failOnError: true)

        ServiceItem si1 = new ServiceItem(
                title: 'si1',
                owners: [owner],
                categories: [catA],
                state: stateA,
                types: type,
                launchUrl: 'https:///',
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)
        ServiceItem si2 = new ServiceItem(
                title: 'si2',
                owners: [owner],
                categories: [catA, catB],
                state: stateB,
                types: type,
                launchUrl: 'https:///',
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)
        ServiceItem si3 = new ServiceItem(
                title: 'si3',
                owners: [owner],
                launchUrl: 'https:///',
                categories: [catB],
                types: type,
                state: stateA,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true, flush: true)

        def params = [categories_id: catA.id, sort_0: "state_title", order_0: "desc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list.size() == 2
        list[0].id == si2.id
        list[1].id == si1.id
    }

    void "serviceItems: search by 'author_username'"() {
        given:
        def serviceItems = createServiceItemsForUsernameSearch()

        when:
        def params = [author_username: "Test User 1", sort_0: "title", order_0: "asc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list*.id == serviceItems[0..3].reverse()*.id
    }


    void "serviceItems: search by 'author_displayName'"() {
        given:
        def serviceItems = createServiceItemsForUsernameSearch()

        when:
        def params = [author_displayName: "Test User 1", sort_0: "title", order_0: "asc"]
        def list = genericQueryService.serviceItems(params, "admin", Constants.VIEW_ADMIN).items

        then:
        list*.id == serviceItems[0..3].reverse()*.id
    }

    private void createServiceItemsForSortingTests() {
        for (i in 1..10) {
            def profile = new Profile(username: "testRatingUser${i}", displayName: "Marketplace Tester ${i}")
            profile.save()
        }

        Profile owner = new Profile(username: 'testOwner').save(failOnError: true)

        Types type = new Types(title: 'test type').save(failOnError: true)

        ServiceItem fiveStarSI = new ServiceItem(
                owners: [owner],
                title: "5 star",
                description: "A",
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"],
                types: type,
                launchUrl: 'https:///').save(failOnError: true, flush: true)
        ServiceItem oneStarSI = new ServiceItem(
                owners: [owner],
                title: "1 star",
                description: "B",
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"],
                types: type,
                launchUrl: 'https:///').save(failOnError: true, flush: true)
        ServiceItem threeStarSIw2 = new ServiceItem(
                owners: [owner],
                title: "3 star with 2 votes",
                description: "C",
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"],
                types: type,
                launchUrl: 'https:///').save(failOnError: true, flush: true)
        ServiceItem threeStarSIw5 = new ServiceItem(
                owners: [owner],
                title: "3 star with 5 votes",
                description: "D",
                launchUrl: 'https:///',
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true, flush: true)
        ServiceItem threeStarSIw10 = new ServiceItem(
                owners: [owner],
                title: "3 star with 10 votes",
                description: "E",
                launchUrl: 'https:///',
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true, flush: true)

        def author1 = profileService.findByUsername("testRatingUser1")

        def itemComment1_u1si5star = new ItemComment(author: author1, serviceItem: fiveStarSI)
        itemCommentService.rate(itemComment1_u1si5star, 5)

        oneStarSI.save(flush: true)
        def itemComment2_u1si1star = new ItemComment(author: author1, serviceItem: oneStarSI)
        itemCommentService.rate(itemComment2_u1si1star, 1)

        threeStarSIw2.save(flush: true)
        for (i in 1..2) {
            def currAuthor = profileService.findByUsername("testRatingUser${i}")
            def itemComment = new ItemComment(author: currAuthor, serviceItem: threeStarSIw2)
            itemCommentService.rate(itemComment, 3)
        }

        threeStarSIw5.save(flush: true)
        for (i in 1..5) {
            def currAuthor = profileService.findByUsername("testRatingUser${i}")
            def itemComment = new ItemComment(author: currAuthor, serviceItem: threeStarSIw5)
            itemCommentService.rate(itemComment, 3)
        }

        threeStarSIw10.save(flush: true)
        for (i in 1..10) {
            def currAuthor = profileService.findByUsername("testRatingUser${i}")
            def itemComment = new ItemComment(author: currAuthor, serviceItem: threeStarSIw10)
            itemCommentService.rate(itemComment, 3)
        }
        threeStarSIw5.save(flush: true)
        threeStarSIw2.save(flush: true)
        threeStarSIw10.save(flush: true)
    }

    private static List<ServiceItem> createServiceItemsFor3ColumnSorting() {
        Profile profile1 = new Profile(username: 'testOwner').save(failOnError: true)

        Types typesAAA = new Types(title: "AAA").save(failOnError: true)
        Types typesBBB = new Types(title: "BBB").save(failOnError: true)
        Types typesCCC = new Types(title: "CCC").save(failOnError: true)

        ServiceItem si4 = new ServiceItem(
                owners: [profile1],
                title: "NNN",
                types: typesCCC,
                description: "D",
                launchUrl: 'https:///',
                isHidden: 0,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)

        ServiceItem si1 = new ServiceItem(
                owners: [profile1],
                title: "MMM",
                description: "A",
                types: typesAAA,
                launchUrl: 'https:///',
                isHidden: 0,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)

        ServiceItem si3 = new ServiceItem(
                owners: [profile1],
                title: "MMM",
                types: typesCCC,
                description: "C",
                launchUrl: 'https:///',
                isHidden: 0,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)

        ServiceItem si2 = new ServiceItem(
                owners: [profile1],
                title: "MMM",
                description: "A",
                types: typesBBB,
                launchUrl: 'https:///',
                isHidden: 0,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true, flush: true)

        return [si1, si2, si3, si4]
    }

    private static List<ServiceItem> createServiceItemsForUsernameSearch() {
        Profile profile1 = new Profile(username: "Test User 1", displayName: "Test User 1").save()
        Profile profile2 = new Profile(username: "Test User 2", displayName: "Test User 2").save()

        Types type = new Types(title: 'test type').save(failOnError: true)

        ServiceItem si1 = new ServiceItem(
                owners: [profile1],
                launchUrl: 'https:///',
                title: "D",
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)
        ServiceItem si2 = new ServiceItem(
                owners: [profile1],
                launchUrl: 'https:///',
                title: "C",
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)
        ServiceItem si3 = new ServiceItem(
                owners: [profile1],
                launchUrl: 'https:///',
                title: "B",
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)
        ServiceItem si4 = new ServiceItem(
                owners: [profile1],
                launchUrl: 'https:///',
                title: "A",
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(failOnError: true)

        ServiceItem si5 = new ServiceItem(
                owners: [profile2],
                launchUrl: 'https:///',
                title: "Z",
                types: type,
                approvalStatus: Constants.APPROVAL_STATUSES["APPROVED"]).save(flush: true, failOnError: true)

        return [si1, si2, si3, si4, si5]
    }

}
