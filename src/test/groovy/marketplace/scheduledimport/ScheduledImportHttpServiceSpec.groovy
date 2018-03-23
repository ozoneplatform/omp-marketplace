package marketplace.scheduledimport

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import marketplace.Constants
import marketplace.ImportTask
import marketplace.ImportTaskResult
import marketplace.rest.CustomDomainObjectReader
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.StatusLine
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import spock.lang.Specification

import java.text.DateFormat
import java.text.SimpleDateFormat

class ScheduledImportHttpServiceSpec extends Specification implements DataTest, ServiceUnitTest<ScheduledImportHttpService>{
    def importTaskProperties = [
        updateType: Constants.IMPORT_TYPE_FULL,
        url: 'https://localhost/test-store',
        keystorePath: 'path/to/secrets.jks',
        keystorePass: 'passwordpassword',
        truststorePath: 'path/to/trust.jks'
    ]

    void setup() {
        mockDomain(ImportTask)
        mockDomain(ImportTaskResult)
    }
    void testGetRemoteUri() {
        when:
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        df.setTimeZone(TimeZone.getTimeZone('UTC'))

        ImportTask task = new ImportTask(importTaskProperties)
        String dateStr
        Date runDate = df.parse('2014-08-20T12:28:17Z')

        then:
        service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?accessAlertShown=true')

        when:
        task = new ImportTask(importTaskProperties + [updateType: Constants.IMPORT_TYPE_DELTA])

        //no lastSuccessfulRun, date should not be added to URL
        then:
        service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?accessAlertShown=true')


        when:
        task.addToRuns(new ImportTaskResult(runDate: runDate, result: true))
        then:
        service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?editedSinceDate=2014-08-20T12%3A28%3A17Z&lastPull=2014-08-20T12%3A28%3A17Z&accessAlertShown=true')

        //add another result, dated 1 second later, that failed.  Failed results should
        //have no effect
        when:
        task.addToRuns(
            new ImportTaskResult(runDate: new Date(runDate.time + 1000), result: false))

        then:
        service.getRemoteUri(task) ==
            new URI('https://localhost/test-store?editedSinceDate=2014-08-20T12%3A28%3A17Z&lastPull=2014-08-20T12%3A28%3A17Z&accessAlertShown=true')
    }

    private static class FakeFileInputStream {
        File file

        FakeFileInputStream(File f) {
            file = f
        }
    }

    //TODO BVEST Revisit
//    void testCreateHttpsClient() {
//        setup:
//        def trustMaterial
//        def keyMaterial
//        def keyMaterialPassword
//        def builtSslContext = SSLContexts.createDefault()
//        def clientSslContext
//        def builtClient = HttpClients.createDefault()
//        def returnedClient
//
//        Mock(FileInputStream)
//        FileInputStream.metaClass.constructor  = { file -> [file: file, close: {}]}
//
////        FileInputStream.metaClass.constructor >> { file -> [file: file, close: {}]}
//
////        mockFor(FileInputStream)
////        FileInputStream.metaClass.constructor = { file ->
////            [ file: file, close: {} ]
////        }
//
//
//        Mock(KeyStore) {
//            load(*_) >> { KeyStore storeStream, char[] streamPassword ->
//                assert streamPassword == null
//                delegate.metaClass.file = storeStream.file
//            }
//        }
////        mockFor(KeyStore)
////        KeyStore.metaClass.load = { storeStream, char[] streamPassword ->
////            assert streamPassword == null
////            delegate.metaClass.file = storeStream.file
////        }
//
//        Mock(SSLContexts) {
//            custom(*_) >> { -> [
//                    loadTrustMaterial: {
//                        trustMaterial = it
//
//                        //the return value isn't actually used, but it needs to be a builder
//                        //in order to typecheck
//                        return SSLContexts.custom()
//                    },
//                    loadKeyMaterial: { keystore, password ->
//                        keyMaterial = keystore
//                        keyMaterialPassword = password
//
//                        return SSLContexts.custom()
//                    },
//                    build: {
//                        builtSslContext
//                    }
//            ] as SSLContextBuilder }
//        }
//
//        Mock(HttpClients) {
//            custom(*_) >> { [
//                    setSslcontext: {
//                        ctx -> clientSslContext = ctx
//
//                            return HttpClients.custom()
//                    },
//                    build: { builtClient }
//            ] as CloseableHttpClient
//            }
//        }
//
//        when:
//        ImportTask task = new ImportTask(importTaskProperties)
//        returnedClient = service.createHttpsClient(task)
//        then:
//        builtSslContext == clientSslContext
//        builtClient == returnedClient
//        trustMaterial.file.toString() == task.truststorePath
//        keyMaterial.file.toString() == task.keystorePath
//        keyMaterialPassword == task.keystorePass.toCharArray()
//
//
//        when:
//        trustMaterial = keyMaterial = keyMaterialPassword =
//            clientSslContext = returnedClient = null
//
//        //test without truststore
//        task = new ImportTask(importTaskProperties + [truststorePath: null])
//        returnedClient = service.createHttpsClient(task)
//        then:
//        builtSslContext == clientSslContext
//        builtClient == returnedClient
//        trustMaterial == null
//        keyMaterial.file.toString() == task.keystorePath
//        keyMaterialPassword == task.keystorePass.toCharArray()
//
//        when:
//        trustMaterial = keyMaterial = keyMaterialPassword =
//            clientSslContext = returnedClient = null
//
//        //test without keystore or truststore
//        task = new ImportTask(importTaskProperties + [
//            truststorePath: null,
//            keystorePath: null,
//            keystorePass: null
//        ])
//        returnedClient = service.createHttpsClient(task)
//        then:
//        builtSslContext == clientSslContext
//        builtClient == returnedClient
//        trustMaterial == null
//        keyMaterial == null
//        keyMaterialPassword == null
//    }

    void testRetrieveRemoteImportData() {
        setup:
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

        def readerMock = Mock(CustomDomainObjectReader) {
            readFrom(*_) >> { cls, type, annotations, mediaType, headers, stream ->
                assert cls == ScheduledImportData
                assert mediaType.subtype.contains('json')
                assert stream.text == importContent
                return readerRetval
            }
        }

        httpService.customDomainObjectReader = readerMock

        httpService.mp_RESTInterceptorService = [
            processIncoming: {
                [continueProcessing: interceptorResult, message: interceptorMessage]
            }
        ]

        when:
        ImportTask task = new ImportTask(importTaskProperties)
        def retval = httpService.retrieveRemoteImportData(task)

        then:
        retval.is(readerRetval)
        retrievedUri == new URI(task.url)

        when:
        interceptorResult = false
        interceptorMessage = 'interceptor failure'
        httpService.retrieveRemoteImportData(task)
        then:
        def ex = thrown(Exception)
        ex.message == "Import rejected by interceptor: interceptor failure"

        when:
        interceptorResult = true
        interceptorMessage = 'success'
        responseStatusCode = 400
        httpService.retrieveRemoteImportData(task)
        then:
        thrown(Exception)

        when:
        responseStatusCode = 200
        responseContentType = 'text/html'
        httpService.retrieveRemoteImportData(task)
        then:
        thrown(Exception)
    }
}
