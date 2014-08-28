package marketplace

import java.net.URL

import grails.test.mixin.TestFor
import grails.test.mixin.support.GrailsUnitTestMixin

@TestMixin(GrailsUnitTestMixin)
class WebUtilTests {

	void testIsRESTRequest() {
		assertTrue(WebUtil.isRESTRequest("/public/search"))
		assertFalse(WebUtil.isRESTRequest("/serviceItem/search"))
	}

	void testIsAjaxRequest() {
		assertTrue(WebUtil.isAjaxRequest("/affiliatedMarketplace/listAsJSON"))
		assertFalse(WebUtil.isAjaxRequest("/serviceItem/search"))
	}

	void testIsResourceRequest() {
		assertTrue(WebUtil.isResourceRequest("/images/get/2"))
		assertFalse(WebUtil.isResourceRequest("/serviceItem/search"))
	}

    void testIsSameDomain() {
        def test = { a, b ->
            WebUtil.isSameDomain(new URL(a), new URL(b))
        }

        assertTrue(test('https://localhost:8443', 'https://localhost:8443'))
        assertTrue(test('https://localhost:8443/asdf', 'https://localhost:8443/zxcv'))
        assertTrue(test('https://localhost/', 'https://localhost:443/'))
        assertTrue(test('http://localhost/', 'http://localhost:80/'))

        assertFalse(test('https://localhost/', 'http://localhost/'))
        assertFalse(test('https://127.0.0.1/', 'https://localhost/'))
        assertFalse(test('https://www.example.com/', 'https://localhost/'))
        assertFalse(test('https://localhost:1/', 'https://localhost:2/'))
    }
}
