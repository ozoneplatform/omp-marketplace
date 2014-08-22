package marketplace.scheduledimport

import java.text.DateFormat
import java.text.SimpleDateFormat

import java.security.KeyStore

import javax.net.ssl.SSLContext

import javax.ws.rs.core.MediaType

import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.annotation.Propagation

import groovyx.net.http.URIBuilder

import org.codehaus.groovy.grails.web.json.JSONObject

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLContexts
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.client.methods.HttpGet

import marketplace.ImportTask
import marketplace.Constants

import marketplace.rest.CustomDomainObjectReader

/**
 * This class manages the HTTP retrieval of scheduled import data.  It would
 * be labeled 'protected' but that breaks CGLIB
 */

//doesn't need a transaction
@Transactional(propagation=Propagation.SUPPORTS)
class ScheduledImportHttpService {

    CustomDomainObjectReader customDomainObjectReader
    def mp_RESTInterceptorService

    protected CloseableHttpClient createHttpsClient(ImportTask task) {
        KeyStore trustStore
        KeyStore keyStore
        char[] passwordArray = task.keystorePass?.toCharArray()

        if (task.truststorePath) {
            def trustStoreStream

            try {
                trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStoreStream = new FileInputStream(new File((String)task.truststorePath));
                trustStore.load(trustStoreStream, null);
            } finally {
                trustStoreStream?.close();
            }
        }

        if (task.keystorePath) {
            def keyStoreStream

            try {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStoreStream = new FileInputStream(new File((String)task.keystorePath));
                keyStore.load(keyStoreStream, null);
            } finally {
                keyStoreStream?.close();
            }
        }

        SSLContextBuilder sslBuilder = SSLContexts.custom()

        if (trustStore) {
            sslBuilder.loadTrustMaterial(trustStore)
        }

        if (keyStore) {
            sslBuilder.loadKeyMaterial(keyStore, passwordArray)
        }

        SSLContext sslcontext = sslBuilder.build();

        return HttpClients.custom()
                .setSslcontext(sslcontext)
                .build();
    }

    /**
     * @return the URL to use to fetch import data for this ImportTask
     */
    protected URI getRemoteUri(ImportTask task) {
        URIBuilder uriBuilder = new URIBuilder(task.url)
        Date lastRunDate = task.lastSuccessfulRunResult?.runDate

        if (task.updateType == Constants.IMPORT_TYPE_DELTA &&
            lastRunDate != null) {

            DateFormat queryParamDateFormat =
                new SimpleDateFormat(Constants.EXTERNAL_DATE_FORMAT)

            queryParamDateFormat.setTimeZone(TimeZone.getTimeZone('UTC'))

            uriBuilder.addQueryParam(Constants.OMP_IMPORT_DELTA_DATE_FIELD,
                queryParamDateFormat.format(lastRunDate))
        }

        //skip access alert
        uriBuilder.addQueryParam("accessAlertShown", "true")

        return uriBuilder.toURI()
    }

    public ScheduledImportData retrieveRemoteImportData(ImportTask task) throws IOException {
        CloseableHttpClient client = createHttpsClient(task)

        try {
            HttpGet httpget = new HttpGet(getRemoteUri(task))

            CloseableHttpResponse response = client.execute(httpget)

            try {
                String contentType = response.entity.contentType.value

                if (response.statusLine.statusCode != 200 || !contentType.contains('json')) {
                    throw new Exception(
                        "Invalid response from server - status ${response.statusLine.statusCode}:"
                        + "${EntityUtils.toString(response.entity)}")
                }
                else {
                    InputStream inputStream = response.entity.content
                    ScheduledImportData data = customDomainObjectReader.readFrom(
                        ScheduledImportData.class,
                        ScheduledImportData.class,
                        null,
                        MediaType.APPLICATION_JSON_TYPE,
                        null,
                        inputStream
                    )

                    //check the maxClassification
                    def interceptorResult =
                        mp_RESTInterceptorService.processIncoming(data.properties)

                    if (!interceptorResult.continueProcessing) {
                        throw new Exception(
                            "Import rejected by interceptor: ${interceptorResult.message}")
                    }

                    return data
                }
            }
            finally {
                response.close()
            }
        }
        finally {
            client.close()
        }
    }
}
