package marketplace

import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import org.junit.Test
import spock.lang.Specification

//@TestFor(Screenshot)
class ScreenshotSpec extends Specification implements DomainConstraintsUnitTest<Screenshot>{

    private final String LARGE_URL = 'https://localhost/large.png'
    private final String SMALL_URL = 'https://localhost/small.png'

    void testGetLargeImageUrl() {
        setup:
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL,
            largeImageUrl: LARGE_URL
        )
        expect:
        assert screenshot.largeImageUrl == LARGE_URL
    }

    void testGetLargeImageUrlMissing() {
        setup:
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL
        )
        expect:
        //should use the small url when the large url is not set
        assert screenshot.largeImageUrl == SMALL_URL
    }

    void testAsJSON() {
        setup:
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL,
            largeImageUrl: LARGE_URL
        )

        screenshot.id = 1

        when:
        def json = screenshot.asJSON()

        then:
        assert json.largeImageUrl == LARGE_URL
        assert json.smallImageUrl == SMALL_URL
        assert json.id == 1
    }

    void testAsJSONWithoutLargeUrl() {
        setup:
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL
        )

        screenshot.id = 1

        when:
        def json = screenshot.asJSON()

        then:
        assert json.largeImageUrl == SMALL_URL
        assert json.smallImageUrl == SMALL_URL
        assert json.id == 1
    }

    void testSerializable() {
        expect:
        assert (new Screenshot()) instanceof Serializable
    }
}
