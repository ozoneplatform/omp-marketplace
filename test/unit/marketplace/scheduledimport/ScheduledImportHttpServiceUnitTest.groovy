package marketplace.scheduledimport

import java.text.DateFormat
import java.text.SimpleDateFormat

import java.security.KeyStore

import grails.test.mixin.services.ServiceUnitTestMixin
import grails.test.mixin.TestMixin
import grails.test.mixin.TestFor

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLContexts
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.HttpEntity
import org.apache.http.Header
import org.apache.http.StatusLine

import marketplace.ImportTask
import marketplace.ImportTaskResult

import marketplace.Constants

import marketplace.rest.CustomDomainObjectReader

@Mock([ImportTask, ImportTaskResult])
@TestFor(ScheduledImportHttpService)
@TestMixin(ServiceUnitTestMixin)
class ScheduledImportHttpServiceUnitTest {
    def importTaskProperties = [
        updateType: Constants.IMPORT_TYPE_FULL,
        url: 'https://localhost/test-store',
        keystorePath: 'path/to/secrets.jks',
        keystorePass: 'passwordpassword',
        truststorePath: 'path/to/trust.jks'
    ]

    void testGetRemoteUri() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        df.setTimeZone(TimeZone.getTimeZone('UTC'))

        ImportTask task = new ImportTask(importTaskProperties)
        String dateStr
        Date runDate = df.parse('2014-08-20T12:28:17Z')

        assert service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?accessAlertShown=true')

        task = new ImportTask(importTaskProperties + [updateType: Constants.IMPORT_TYPE_DELTA])

        //no lastSuccessfulRun, date should not be added to URL
        assert service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?accessAlertShown=true')


        task.addToRuns(new ImportTaskResult(runDate: runDate, result: true))
        assert service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?editedSinceDate=2014-08-20T12%3A28%3A17Z&accessAlertShown=true')

        //add another result, dated 1 second later, that failed.  Failed results should
        //have no effect
        task.addToRuns(
            new ImportTaskResult(runDate: new Date(runDate.time + 1000), result: false))

        assert service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?editedSinceDate=2014-08-20T12%3A28%3A17Z&accessAlertShown=true')
    }

    private static class FakeFileInputStream {
        File file

        FakeFileInputStream(File f) {
            file = f
        }
    }

    void testCreateHttpsClient() {
        def trustMaterial
        def keyMaterial
        def keyMaterialPassword
        def builtSslContext = SSLContexts.createDefault()
        def clientSslContext
        def builtClient = HttpClients.createDefault()
        def returnedClient

        mockFor(FileInputStream)
        FileInputStream.metaClass.constructor = { file ->
            [ file: file, close: {} ]
        }

        mockFor(KeyStore)
        KeyStore.metaClass.load = { storeStream, char[] streamPassword ->
            assert streamPassword == null
            delegate.metaClass.file = storeStream.file
        }

        mockFor(SSLContexts)
        SSLContexts.metaClass.'static'.custom = { -> [
            loadTrustMaterial: {
                trustMaterial = it

                //the return value isn't actually used, but it needs to be a builder
                //in order to typecheck
                return SSLContexts.custom()
            },
            loadKeyMaterial: { keystore, password ->
                keyMaterial = keystore
                keyMaterialPassword = password

                return SSLContexts.custom()
            },
            build: {
                builtSslContext
            }
        ] as SSLContextBuilder }

        mockFor(HttpClients)
        HttpClients.metaClass.'static'.custom = { [
            setSslcontext: {
                ctx -> clientSslContext = ctx

                return HttpClients.custom()
            },
            build: { builtClient }
        ] as CloseableHttpClient }

        ImportTask task = new ImportTask(importTaskProperties)

        returnedClient = service.createHttpsClient(task)
        assert builtSslContext == clientSslContext
        assert builtClient == returnedClient
        assert trustMaterial.file.toString() == task.truststorePath
        assert keyMaterial.file.toString() == task.keystorePath
        assert keyMaterialPassword == task.keystorePass.toCharArray()

        trustMaterial = keyMaterial = keyMaterialPassword =
            clientSslContext = returnedClient = null

        //test without truststore
        task = new ImportTask(importTaskProperties + [truststorePath: null])
        returnedClient = service.createHttpsClient(task)
        assert builtSslContext == clientSslContext
        assert builtClient == returnedClient
        assert trustMaterial == null
        assert keyMaterial.file.toString() == task.keystorePath
        assert keyMaterialPassword == task.keystorePass.toCharArray()

        trustMaterial = keyMaterial = keyMaterialPassword =
            clientSslContext = returnedClient = null

        //test without keystore or truststore
        task = new ImportTask(importTaskProperties + [
            truststorePath: null,
            keystorePath: null,
            keystorePass: null
        ])
        returnedClient = service.createHttpsClient(task)
        assert builtSslContext == clientSslContext
        assert builtClient == returnedClient
        assert trustMaterial == null
        assert keyMaterial == null
        assert keyMaterialPassword == null
    }

    void testRetrieveRemoteImportData() {
        def responseContentType = 'application/json'
        def responseStatusCode = 200
        def importContent = '{"import": "content"}'
        def interceptorResult = true
        def interceptorMessage = 'success'
        def readerRetval = new ScheduledImportData()

        def retrievedUri

        def execute = { method ->
            assert method instanceof HttpGet
            retrievedUri = method.URI

            return [
                getStatusLine: { -> [getStatusCode: { responseStatusCode}] as StatusLine},
                getEntity: {
                    [
                        getContentType: { -> [getValue: { responseContentType }] as Header },
                        getContent: { ->new ByteArrayInputStream(importContent.getBytes('UTF-8'))}
                    ] as HttpEntity
                },
                close: {}
            ] as CloseableHttpResponse
        }

        //couldn't figure out how to mock these methods on the built in service object
        def httpService = [
            getRemoteUri: { task -> new URI(task.url) },
            createHttpsClient: { task ->
                [ execute: execute ] as DefaultHttpClient
            }
        ] as ScheduledImportHttpService

        def readerMock = mockFor(CustomDomainObjectReader)
        readerMock.demand.readFrom(2..2) { cls, type, annotations, mediaType, headers, stream ->
            assert cls == ScheduledImportData
            assert mediaType.subtype.contains('json')
            assert stream.text == importContent

            return readerRetval
        }

        httpService.customDomainObjectReader = readerMock.createMock()

        httpService.mp_RESTInterceptorService = [
            processIncoming: {
                [continueProcessing: interceptorResult, message: interceptorMessage]
            }
        ]

        ImportTask task = new ImportTask(importTaskProperties)
        def retval = httpService.retrieveRemoteImportData(task)
        assert retval.is(readerRetval)
        assert retrievedUri == new URI(task.url)

        interceptorResult = false
        interceptorMessage = 'interceptor failure'
        shouldFail() {
            httpService.retrieveRemoteImportData(task)
        }

        interceptorResult = true
        interceptorMessage = 'success'
        responseStatusCode = 400
        shouldFail() {
            httpService.retrieveRemoteImportData(task)
        }

        responseStatusCode = 200
        responseContentType = 'text/html'
        shouldFail() {
            httpService.retrieveRemoteImportData(task)
        }
    }
}
