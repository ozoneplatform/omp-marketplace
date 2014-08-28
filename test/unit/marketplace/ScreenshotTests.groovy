package marketplace

import grails.test.mixin.TestFor
import org.junit.Test

@TestFor(Screenshot)
class ScreenshotTests {

    private final String LARGE_URL = 'https://localhost/large.png'
    private final String SMALL_URL = 'https://localhost/small.png'

    @Test
    public void testGetLargeImageUrl() {
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL,
            largeImageUrl: LARGE_URL
        )

        assert screenshot.largeImageUrl == LARGE_URL
    }

    @Test
    public void testGetLargeImageUrlMissing() {
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL
        )

        //should use the small url when the large url is not set
        assert screenshot.largeImageUrl == SMALL_URL
    }

    @Test
    public void testAsJSON() {
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL,
            largeImageUrl: LARGE_URL
        )

        screenshot.id = 1

        def json = screenshot.asJSON()

        assert json.largeImageUrl == LARGE_URL
        assert json.smallImageUrl == SMALL_URL
        assert json.id == 1
    }

    @Test
    public void testAsJSONWithoutLargeUrl() {
        Screenshot screenshot = new Screenshot(
            smallImageUrl: SMALL_URL
        )

        screenshot.id = 1

        def json = screenshot.asJSON()

        assert json.largeImageUrl == SMALL_URL
        assert json.smallImageUrl == SMALL_URL
        assert json.id == 1
    }

    @Test
    public void testSerializable() {
        assert (new Screenshot()) instanceof Serializable
    }
}
