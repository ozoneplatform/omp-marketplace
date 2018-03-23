package marketplace.rest

import grails.testing.gorm.DataTest
import marketplace.Agency
import spock.lang.Specification

import static javax.servlet.http.HttpServletResponse.SC_CREATED

//use Agency as an example domain object
class DomainResourceSpec extends Specification implements DataTest{
    Class<?>[] getDomainClassesToMock() {[Agency]}
    DomainResource<Agency> domainResource

    def restService

    private static final agencyProps = [
        title: "Agency Name",
        iconUrl: "https://localhost/icon.png"
    ]

    void setup() {
        mockDomain(Agency)
    }

    //call this method after setting up mocking demands in
    //each test
    private void setUpResource() {

        domainResource = new DomainResource(Agency.class,restService)
    }

    void testCreate() {
        setup:
        restService = [
            createFromDto: { dto ->
                dto.id = 1
                dto
            },
            authorizeUpdate: { a -> }
        ] as RestService

        setUpResource()

        when:
        def agency = new Agency(agencyProps)
        def response = domainResource.create(agency)

        then:
        response.status == SC_CREATED
        response.entity == agency
    }

    void testReadAll() {
        when:
        def newAgency = new Agency(agencyProps)
        restService = [
            getAll: { offset, max -> [newAgency] }
        ] as RestService

        setUpResource()

        Collection<Agency> retval = domainResource.readAll(null, null)

        then:
        retval instanceof Collection
        retval.size() == 1

        when:
        def agency = retval.iterator().next()
        then:
        agency instanceof Agency
        agency == newAgency
    }

    void testRead() {
        when:
        final id = 1
        def newAgency = new Agency(agencyProps + [id: id])
        restService = [
            getById: { serviceId ->
                assert serviceId == id
                newAgency
            }
        ] as RestService

        setUpResource()

        Agency agency = domainResource.read(id)
        then:
        agency instanceof Agency
        agency == newAgency
    }

    void testUpdate() {
        when:
        final id = 1
        final newName = 'New Name'
        final newIcon = 'icon2.png'

        def existingAgency = new Agency(agencyProps)
        existingAgency.id = id
        def updates = new Agency([
            id: id,
            title: newName,
            iconUrl: newIcon
        ])

        restService = [
            updateById: { serviceId, dto ->
                assert serviceId == id
                existingAgency.properties = dto.properties
                existingAgency
            }
        ] as RestService

        setUpResource()

        Agency agency = domainResource.update(id, updates)
        then:
        agency instanceof Agency
        agency.title == newName
        agency.iconUrl == newIcon
        agency.id == id
    }

    void testDelete() {
        when:
        final id = 1

        restService = [
            deleteById: { serviceId ->
                assert serviceId == id
            }
        ] as RestService

        setUpResource()

        then:
        domainResource.delete(id)
    }
}
