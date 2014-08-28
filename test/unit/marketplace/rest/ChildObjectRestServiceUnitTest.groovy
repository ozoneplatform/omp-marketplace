package marketplace.rest

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import org.springframework.security.access.AccessDeniedException

import marketplace.ServiceItem
import marketplace.Profile
import marketplace.ItemComment
import marketplace.Types

import marketplace.testutil.FakeAuditTrailHelper

@TestMixin(DomainClassUnitTestMixin)
class ChildObjectRestServiceUnitTest {
    GrailsApplication grailsApplication

    ChildObjectRestService<ServiceItem, ItemComment> service

    Profile currentUser, adminUser, nonAdminUser

    private class TestService extends ChildObjectRestService<ServiceItem, ItemComment> {
        TestService(GrailsApplication grailsApplication,
                ServiceItemRestService serviceItemRestService) {
            super(ServiceItem.class, 'serviceItem', 'itemComments', grailsApplication,
                ItemComment.class, serviceItemRestService, null, null)
        }

        ItemComment save(ItemComment comment) {
            comment.author = currentUser

            super.save(comment)
        }
    }

    private ItemComment makeCommentDto(id=null) {
        ItemComment comment = new ItemComment(
            text: 'test comment text'
        )
        comment.id = id

        return comment
    }

    private void makeService(serviceItemServiceMock) {
        service = new TestService(grailsApplication, serviceItemServiceMock.createMock())
    }

    void setUp() {
        adminUser = new Profile(username: 'admin')
        nonAdminUser = new Profile(username: 'user')

        Types type = new Types(title: 'test type')

        ServiceItem si1 = new ServiceItem(
            title: 'Service Item 1',
            owners: [adminUser],
            types: type,
            launchUrl: 'https:///'
        )
        si1.id = 1

        ServiceItem si2 = new ServiceItem(
            title: 'Service Item 2',
            owners: [nonAdminUser],
            launchUrl: 'https:///',
            types: type
        )
        si2.id = 2

        FakeAuditTrailHelper.install()

        mockDomain(ItemComment.class)
        mockDomain(Profile.class, [adminUser, nonAdminUser])
        mockDomain(ServiceItem.class)
        [si1, si2].each { it.save(failOnError: true) }


        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.refresh()

        grailsApplication.addArtefact(ServiceItem.class)
        grailsApplication.addArtefact(ItemComment.class)
        grailsApplication.addArtefact(Profile.class)

    }

    void testCreateFromParentIdAndDto() {
        currentUser = adminUser

        def viewAllowed = true

        def serviceItemServiceMock = mockFor(ServiceItemRestService)
        serviceItemServiceMock.demand.canView(2..2) { it -> viewAllowed }
        makeService(serviceItemServiceMock)

        ItemComment dto = makeCommentDto()

        def returnedDto = service.createFromParentIdAndDto(1, dto)

        assert returnedDto.serviceItem == ServiceItem.get(1)
        assert returnedDto.id != null
        assert returnedDto.text == dto.text


        //test unauthorized create
        viewAllowed = false
        shouldFail(AccessDeniedException) {
            service.createFromParentIdAndDto(1, dto)
        }
    }

    void testUpdateByParentId() {
        currentUser = adminUser

        def viewAllowed = true

        def serviceItemServiceMock = mockFor(ServiceItemRestService)
        serviceItemServiceMock.demand.canView(3..100) { it -> viewAllowed }
        makeService(serviceItemServiceMock)

        ItemComment dto = makeCommentDto()

        def returnedDto = service.createFromParentIdAndDto(1, dto)
        def id = returnedDto.id

        dto = makeCommentDto(id)
        dto.rate = 3
        returnedDto = service.updateByParentId(1, id, dto)

        assert returnedDto.id == id
        assert returnedDto.text == makeCommentDto().text
        assert returnedDto.rate == 3
        assert returnedDto.serviceItem == ServiceItem.get(1)

        viewAllowed = false
        shouldFail(AccessDeniedException) {
            returnedDto = service.updateByParentId(1, id, makeCommentDto(id))
        }
    }

    //This test doesn't work because createCriteria is not mocked
    //void testGetByParentId() {
        //currentUser = adminUser

        //def serviceItemServiceMock = mockFor(ServiceItemRestService)
        //serviceItemServiceMock.demand.canView(4..4) { it -> true }
        //serviceItemServiceMock.demand.getById(6..6) { id -> ServiceItem.get(id) }
        //makeService(serviceItemServiceMock)

        //service.createFromParentIdAndDto(1, makeCommentDto())
        //service.createFromParentIdAndDto(1, makeCommentDto())
        //service.createFromParentIdAndDto(1, makeCommentDto())
        //service.createFromParentIdAndDto(1, makeCommentDto())

        //assert service.getByParentId(1).size() == 4
        //assert service.getByParentId(2).size() == 0

        ////test paging
        //def singleton = service.getByParentId(1, 0, 1)
        //assert singleton.size() == 1
        //assert singleton[0].id == 1
        //assert singleton instanceof ItemComment

        //singleton = service.getByParentId(1, 1, 1)
        //assert singleton.size() == 1
        //assert singleton[0].id == 2

        //def two = service.getByParentId(1, 1, 2)
        //assert two.size() == 2
        //assert two*.id as Set == [2,3] as Set

        //def many = service.getByParentId(1, 1, 500)
        //assert many.size() == 3
        //assert many*.id as Set == [2,3,4] as Set
    //}

    void testCreateFromDto() {
        def serviceItemServiceMock = mockFor(ServiceItemRestService)
        makeService(serviceItemServiceMock)

        //createFromDto is forbidden
        shouldFail(UnsupportedOperationException) {
            service.createFromDto(makeCommentDto())
        }
    }
}
