package marketplace.grails.domain

import spock.lang.Specification

import marketplace.WebUtil


class WebUtilTests extends Specification {

    def isRestRequest() {
        expect:
        WebUtil.isRESTRequest("/public/search")
    }

    def isNotRestRequest() {
        expect:
        !WebUtil.isRESTRequest("/serviceItem/search")
    }

    def isAjaxRequest() {
        expect:
        WebUtil.isAjaxRequest("/affiliatedMarketplace/listAsJSON")
    }

    def isNotAjaxRequest() {
        expect:
        !WebUtil.isAjaxRequest("/serviceItem/search")
    }

    def isResourceRequest() {
        expect:
        WebUtil.isResourceRequest("/images/get/2")
    }

    def isNotResourceRequest() {
        expect:
        !WebUtil.isResourceRequest("/serviceItem/search")
    }

    def isSameDomain(String a, String b) {
        expect:
        WebUtil.isSameDomain(new URL(a), new URL(b))

        where:
        a                             | b
        'https://localhost:8443'      | 'https://localhost:8443'
        'https://localhost:8443/asdf' | 'https://localhost:8443/zxcv'
        'https://localhost/'          | 'https://localhost:443/'
        'http://localhost/'           | 'http://localhost:80/'
    }

    def isNotSameDomain(String a, String b) {
        expect:
        !WebUtil.isSameDomain(new URL(a), new URL(b))

        where:
        a                          | b
        'https://localhost/'       | 'http://localhost/'
        'https://127.0.0.1/'       | 'https://localhost/'
        'https://www.example.com/' | 'https://localhost/'
        'https://localhost:1/'     | 'https://localhost:2/'
    }

}
