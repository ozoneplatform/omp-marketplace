package marketplace.scheduledimport

import java.text.DateFormat
import java.text.SimpleDateFormat

import java.security.KeyStore

import javax.net.ssl.SSLContext

import groovyx.net.http.URIBuilder

import org.codehaus.groovy.grails.web.json.JSONObject

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.client.methods.HttpGet

import marketplace.ImportTask
import marketplace.Constants

protected class ScheduledImportHttpService {

    private static final DateFormat QUERY_PARAM_DATE_FORMAT =
        new SimpleDateFormat(Constants.EXTERNAL_DATE_FORMAT)

    private HttpClient createHttpClient(ImportTask task) {
        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        KeyStore keyStore  = KeyStore.getInstance(KeyStore.getDefaultType());

        FileInputStream trustStoreStream = new FileInputStream(new File(task.truststorePath));
        FileInputStream keyStoreStream = new FileInputStream(new File(task.keystorePath));

        try {
            trustStore.load(trustStoreStream, null);
            keyStore.load(keyStoreStream, task.keystorePass);
        } finally {
            trustStoreStream.close();
            keyStoreStream.close();
        }

        SSLContext sslcontext = SSLContexts.custom()
            .loadTrustMaterial(trustStore)
            .loadKeyMaterial(keyStore, task.keystorePass)
            .build();

        SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext)

        return HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .build();
    }


    /**
     * @return the URL to use to fetch import data for this ImportTask
     */
    private URL getRemoteUrl(ImportTask task) {
        URIBuilder uriBuilder = new URIBuilder(task.url)
        Date lastRunDate = task.lastRunResult.runDate

        if (task.updateType == Constants.IMPORT_TYPE_DELTA &&
            lastRunDate != null) {

            uriBuilder.addQueryParam(Constants.OMP_IMPORT_DELTA_DATE_FIELD,
                QUERY_PARAM_DATE_FORMAT.format(lastRunDate))
        }

        return uriBuilder.toURL()
    }

    public JSONObject retrieveRemoteImportData(ImportTask task) {
        HttpClient client = createHttpClient(task)
        HttpGet httpget = new HttpGet(getRemoteUrl(task));

        HttpResponse response = client.execute(httpget)

        if (response.statusLine.statusCode != 200) {
            //TODO error handling
        }
        else {
            String contentType = response.contentType?.value
            String encoding = (contentType =~ /charset=(\S)/)[0][1] ?: "utf-8"

            return (JSONObject)JSON.parse(response.entity.content, encoding)
        }
    }
}
