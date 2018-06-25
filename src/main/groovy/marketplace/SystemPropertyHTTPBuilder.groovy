package marketplace

import groovyx.net.http.HTTPBuilder
import org.apache.http.client.HttpClient
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.params.HttpParams
import org.apache.http.impl.client.HttpClients

class SystemPropertyHTTPBuilder extends HTTPBuilder {

    SystemPropertyHTTPBuilder(defaultUri) {
        super(defaultUri)
    }

    @Override
    HttpClient createClient(HttpParams params) {
        HttpClients.custom().setSSLSocketFactory(SSLConnectionSocketFactory.getSystemSocketFactory()).build()
    }
}
