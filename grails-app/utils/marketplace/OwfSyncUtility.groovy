package marketplace

import org.apache.commons.logging.LogFactory

class OwfSyncUtility {
    private static def log = LogFactory.getLog(this)

    static newSyncRequest(base, uuid) {
        return {
            try {
                def http = new SystemPropertyHTTPBuilder("${base.endsWith('/') ? base : base + '/'}")

                http.get(path: "marketplace/sync/$uuid") { resp, content ->
                    log.info "Attempted to sync $uuid with $base. Response code was ${resp.status} and content was $content"
                }
            }
            catch (all) {
                log.error "Error occured while attempting to sync $uuid with $base: ${all.message}"
            }
        }
    }
}