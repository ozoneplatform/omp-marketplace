package marketplace.rest

import grails.gorm.transactions.Transactional
import grails.testing.mixin.integration.Integration
import org.grails.web.json.JSONObject

import org.springframework.http.HttpStatus

import marketplace.Tag

import static marketplace.rest.JsonMatchers.strictlyMatches


@Integration
class TagControllerSpec extends RestSpec {

    Tag tag1
    Tag tag2
    Tag tag3

    List<Tag> tags
    List<JSONObject> tagsJson

    @Transactional
    def setup() {
        assert countTags() == 0

        tag1 = save new Tag(title: "Foo")
        tag2 = save new Tag(title: "Bar")
        tag3 = save new Tag(title: "Baz")

        tags = [tag1, tag2, tag3]
        tagsJson = tags.collect { it.asJSON() }
    }

    @Transactional
    int countTags() {
        Tag.count()
    }

    @Transactional
    void cleanup() {
        Tag.findAll().each { it.delete() }

        logout()
    }

    def 'index - GET /api/tag/'() {
        when:
        def response = get("/api/tag/")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            [tagsJson[0] + [itemCount: 0],
             tagsJson[1] + [itemCount: 0],
             tagsJson[2] + [itemCount: 0]]
        }
    }


    def 'show - GET /api/tag/{id}'() {
        when:
        def response = get("/api/tag/${tag1.id}")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            tagsJson[0] + [itemCount: 0]
        }
    }

    def 'show - GET /api/tag/{id} - not found'() {
        when:
        def response = get("/api/tag/-1")

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def 'search - GET /api/tag/search'() {
        when:
        def response = get("/api/tag/search")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json, [])
    }

    def 'search - GET /api/tag/search?title=f - find by title like'() {
        when:
        def response = get("/api/tag/search?title=f")

        then:
        response.statusCode == HttpStatus.OK

        strictlyMatches(response.json) {
            [tagsJson[0] + [itemCount: 0]]
        }
    }

    def 'delete - DELETE /api/tag/{id} - with ADMIN role'() {
        given:
        loginAsAdmin()

        assert countTags() == tags.size()

        when:
        def response = delete("/api/tag/${tag1.id}")

        then:
        response.statusCode == HttpStatus.NO_CONTENT

        countTags() == tags.size() - 1
    }

    def 'delete - DELETE /api/tag/{id} - with USER role should fail'() {
        given:
        loginAsUser()

        assert countTags() == tags.size()

        when:
        def response = delete("/api/tag/${tag1.id}")

        then:
        response.statusCode == HttpStatus.UNAUTHORIZED

        countTags() == tags.size()
    }

}
