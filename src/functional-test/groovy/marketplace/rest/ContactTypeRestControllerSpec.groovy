package marketplace.rest

import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONObject

import org.springframework.http.HttpStatus

import marketplace.ContactType
import marketplace.Profile

import static marketplace.rest.JsonMatchers.*


@Integration
class ContactTypeRestControllerSpec extends RestSpec {

    Profile admin
    Profile user

    ContactType contactType1
    ContactType contactType2
    ContactType contactType3

    List<ContactType> contactTypes
    List<JSONObject> contactTypesJson

    @Transactional
    def setup() {
        assert countContactTypes() == 0

        admin = Profile.findByUsername("testAdmin1")
        user = Profile.findByUsername("testUser1")

        contactType1 = new ContactType(title: "Foo").save()
        contactType2 = new ContactType(title: "Bar").save()
        contactType3 = new ContactType(title: "Baz", required: true).save()

        contactTypes = [contactType1, contactType2, contactType3]

        contactTypesJson = contactTypes.collect { it.asJSON() }
    }

    @Transactional
    int countContactTypes() {
        ContactType.count()
    }

    @Transactional
    def cleanup() {
        ContactType.findAll().each { it.delete() }

        logout()
    }

    def 'index - GET /api/contactType/'() {
        when:
        def response = get("/api/contactType/")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, contactTypesJson)
    }

    def 'show - GET /api/contactType/{id}'() {
        when:
        def response = get("/api/contactType/${contactType1.id}")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, contactTypesJson[0])
    }

    def 'show - GET /api/contactType/{id} - not found'() {
        when:
        def response = get("/api/contactType/-1")

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def 'create - POST /api/contactType/ - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def contactType = [title: "Fizz"]

        def response = post("/api/contactType/", contactType)

        then:
        response.statusCode == HttpStatus.CREATED

        strictlyMatches(response.json) {
            contactType + hasId() + [required: false] + hasAuditStamp(admin)
        }

        countContactTypes() == contactTypes.size() + 1
    }

    def 'create - POST /api/contactType/ - with USER role should fail'() {
        given:
        loginAsUser()

        when:
        def contactType = [title: "Fizz"]

        def response = post("/api/contactType/", contactType)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countContactTypes() == contactTypes.size()
    }

    def 'update - PUT /api/contactType/{id} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def contactType = [title   : "Fizz",
                           required: true]

        def response = put("/api/contactType/${contactType1.id}", contactType)

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            contactTypesJson[0] + contactType + hasAuditStamp(user, admin)
        }

        countContactTypes() == contactTypes.size()
    }

    def 'update - PUT /api/contactType/{id} - with USER role should fail'() {
        given:
        loginAsUser()

        when:
        def contactType = [title   : "Fizz",
                           required: true]

        def response = put("/api/contactType/${contactType1.id}", contactType)

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countContactTypes() == contactTypes.size()
    }

    def 'delete - DELETE /api/contactType/{id} - with ADMIN role'() {
        given:
        loginAsAdmin()

        when:
        def response = delete("/api/contactType/${contactType1.id}")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countContactTypes() == contactTypes.size() - 1
    }

    def 'delete - DELETE /api/contactType/{id} - with USER role should fail'() {
        given:
        loginAsUser()

        when:
        def response = delete("/api/contactType/${contactType1.id}")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countContactTypes() == contactTypes.size()
    }

}
