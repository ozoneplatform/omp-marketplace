package util

import com.sun.net.httpserver.*
import static groovyx.net.http.ContentType.*

class MockHttpReceiver {
    String response = '{foo: "bar"}'
    HttpServer server = HttpServer.create()
    String contentType = JSON
    Integer status = HttpURLConnection.HTTP_OK

    def MockHttpReceiver(Integer port, String context) {
        server.bind(new InetSocketAddress(port), 0)
        server.createContext(context, myHandler as HttpHandler)
        server.start()
    }

    def setResponseHeaders(Headers headers) {
        headers['Content-Type'] = [contentType]
    }

    def stop() {
        if (server) server.stop(0)
    }

    def myHandler = { HttpExchange exchange ->
        setResponseHeaders(exchange.responseHeaders)
        exchange.sendResponseHeaders(status, response.bytes.length)
        exchange.responseBody.write(response.bytes)
        exchange.responseBody.close()
    }
}
