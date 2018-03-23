package marketplace.rest

import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONObject

import org.springframework.http.HttpStatus

import marketplace.Agency
import marketplace.Profile

import static marketplace.rest.JsonMatchers.*


@Integration
class AgencyControllerSpec extends RestSpec {

    Profile admin
    Profile user

    Agency agency1
    Agency agency2
    Agency agency3

    List<Agency> agencies
    List<JSONObject> agenciesJson

    @Transactional
    def setup() {
        assert countAgencies() == 0

        admin = Profile.findByUsername('testAdmin1')
        user = Profile.findByUsername('testUser1')

        agency1 = new Agency(title: 'Alpha', iconUrl: 'http://foo.com/icon.png').save()
        agency2 = new Agency(title: 'Bravo', iconUrl: 'http://bar.com/icon.png').save()
        agency3 = new Agency(title: 'Charlie', iconUrl: 'http://baz.com/icon.png').save()

        agencies = [agency1, agency2, agency3]
        agenciesJson = agencies.collect { it.asJSON() }
    }

    @Transactional
    int countAgencies() {
        Agency.count()
    }

    @Transactional
    void cleanup() {
        Agency.findAll().each { it.delete() }

        logout()
    }

    def 'index - GET /api/agency/'() {
        when:
        def response = get("/api/agency/")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, agenciesJson)
    }

    def 'show - GET /api/agency/{id}'() {
        when:
        def response = get("/api/agency/$agency1.id")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, agenciesJson[0])
    }

    def 'show - GET /api/agency/{id} - not found'() {
        when:
        def response = get("/api/agency/-1")

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def 'create - POST /api/agency/ - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def agency = [title  : 'SHIELD',
                      iconUrl: 'http://shield.org/icon.png']

        def response = post("/api/agency/", agency)

        then:
        response.statusCode == HttpStatus.CREATED

        strictlyMatches(response.json) {
            agency + hasId() + hasAuditStamp(admin)
        }
    }

    def 'create - POST /api/agency/ - with USER role should fail'() {
        given:
        loginAsUser()

        when:
        def agency = [title  : 'SHIELD',
                      iconUrl: 'http://shield.org/icon.png']

        def response = post("/api/agency/", agency)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'update - PUT /api/agency/{id} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def agency = [id     : agency1.id,
                      title  : 'SHIELD',
                      iconUrl: 'http://shield.org/icon.png']

        def response = put("/api/agency/$agency.id", agency)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            agency + hasAuditStamp(user, admin)
        }
    }

    def 'update - PUT /api/agency/{id} - without command id'() {
        given:
        loginAsAdmin()

        when:
        def agency = [title  : 'SHIELD',
                      iconUrl: 'http://shield.org/icon.png']

        def response = put("/api/agency/$agency1.id", agency)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            // Todo: Why are the audit stamps different on this?
            agency + hasId(agency1.id) + hasAuditStamp(admin, admin)
        }
    }

    def 'update - PUT /api/agency/{id} - with USER role should fail'() {
        given:
        loginAsUser()

        when:
        def agency = [id     : agency1.id,
                      title  : 'SHIELD',
                      iconUrl: 'http://shield.org/icon.png']

        def response = put("/api/agency/$agency.id", agency)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED
    }

    def 'delete - DELETE /api/agency/{id} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def response = delete("/api/agency/$agency1.id")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countAgencies() == agencies.size() - 1
    }

    def 'delete - DELETE /api/agency/{id} - with USER role should fail'() {
        given:
        loginAsUser()

        when:
        def response = delete("/api/agency/$agency1.id")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countAgencies() == agencies.size()
    }

}
