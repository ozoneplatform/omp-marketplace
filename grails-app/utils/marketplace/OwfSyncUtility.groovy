package marketplace

import groovyx.net.http.HTTPBuilder
import org.apache.commons.logging.LogFactory

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.GET

class OwfSyncUtility {
    private static def log = LogFactory.getLog(this)

    static newSyncRequest(base, guid) {
        return {
            def result = [success: false]
            try {
                def http = new HTTPBuilder("${base.endsWith("/") ? base : base + "/"}marketplace/sync/$guid")
                http.request(GET, JSON) { req ->
                    response.success = { resp, content ->
                        if (content?.updatedGuid == guid) {
                            result.success = true
                            log.info "OWF sync with $base successful for item ${content.updatedGuid}"
                        } else if (content?.updateDisabled) {
                            result.updateDisabled = true
                            log.info "OWF sync with $base unsuccessful because OWF has disabled update: ${content.updateDisabled}"
                        } else {
                            result.invalidData = true
                            log.error "Invalid content received from sync with $base for item $guid: $content"
                        }

                    }
                    response.failure = { resp, content ->
                        if (content?.updateFailed) {
                            result.failedUpdate = true
                            log.error "OWF sync with $base for item $guid failed. Response: ${resp?.statusLine?.statusCode}: ${content?.updateFailed}"
                        } else {
                            result.invalidData = true
                            log.error "Invalid content received from sync with $base for item $guid: $content"
                        }
                    }
                }
            }
            catch (all) {
                result.error = true
                log.error "OWF Sync with $base for $guid failed with error: ${all.message}"
            }
            result
        }
    }
}