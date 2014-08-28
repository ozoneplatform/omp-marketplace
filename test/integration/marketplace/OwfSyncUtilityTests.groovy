package marketplace

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin

import util.MockHttpReceiver
import static groovyx.net.http.ContentType.*

@TestMixin(IntegrationTestMixin)
class OwfSyncUtilityTests {

    def mockHttpReceiver = new MockHttpReceiver(9999, "/")

    void tearDown() {
        mockHttpReceiver.stop()
    }

    void testThatSyncIdentifiesSuccessfulResponse() {
        def guid = "5175359b-9387-45ec-a6e2-4633be08653d"
        mockHttpReceiver.contentType = JSON
        mockHttpReceiver.response = "{ updatedGuid: \"$guid\" }"
        mockHttpReceiver.status = 200

        def result = OwfSyncUtility.newSyncRequest("http://localhost:9999", guid).call()
        assert result.success == true
    }

    void testThatSyncIdentifiesUpdateDisabled() {
        def guid = "1135354b-9887-45ec-a6e2-4233be08653d"
        mockHttpReceiver.contentType = JSON
        mockHttpReceiver.response = "{ updateDisabled: \"Some sort of message\" }"
        mockHttpReceiver.status = 200

        def result = OwfSyncUtility.newSyncRequest("http://localhost:9999", guid).call()
        assert result.success == false
        assert result.updateDisabled == true
    }

    void testThatSyncIdentifiesMismatchedGuid() {
        def expectedGuid = "5175359b-9387-45ec-a6e2-4633be08653d"
        def receivedGuid = "4175759b-4387-45ec-a6g2-4633be04653d"
        mockHttpReceiver.contentType = JSON
        mockHttpReceiver.response = "{ updatedGuid: \"$receivedGuid\" }"
        mockHttpReceiver.status = 200

        def result = OwfSyncUtility.newSyncRequest("http://localhost:9999", expectedGuid).call()
        assert result.success == false
        assert result.invalidData == true
    }

    void testThatSyncIdentifiesInvalidJson() {
        def guid = "5175359b-9387-45ec-a6e2-4633be08653d"
        mockHttpReceiver.contentType = JSON
        mockHttpReceiver.response = "{ foo: \"bar\" }"
        mockHttpReceiver.status = 200

        def result = OwfSyncUtility.newSyncRequest("http://localhost:9999", guid).call()
        assert result.success == false
        assert result.invalidData == true
    }

    void testThatSyncIdentifiesAFailedUpdate() {
        def guid = "5175359b-9387-45ec-a6e2-4633be08653d"
        mockHttpReceiver.contentType = JSON
        mockHttpReceiver.response = "{ updateFailed: \"$guid not found\" }"
        mockHttpReceiver.status = 500

        def result = OwfSyncUtility.newSyncRequest("http://localhost:9999", guid).call()
        assert result.success == false
        assert result.failedUpdate == true
    }

    void testThatSyncHandlesNonJsonContent() {
        def guid = "5175359b-9387-45ec-a6e2-4633be08653d"
        mockHttpReceiver.contentType = TEXT
        mockHttpReceiver.response = "Server Error"

        def result = OwfSyncUtility.newSyncRequest("http://localhost:9999", guid).call()
        assert result.success == false
        assert result.error == true
    }

    void testThatSyncHandlesAConnectionRelatedError() {
        def guid = "5175359b-9387-45ec-a6e2-4633be08653d"
        mockHttpReceiver.contentType = JSON

        def result = OwfSyncUtility.newSyncRequest("http://lochost:9999", guid).call()
        assert result.success == false
        assert result.error == true
    }
}
