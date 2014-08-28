package marketplace.rest

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider

/**
 * Serializer for throwables
 */
@Provider
@Produces(['text/x-json', 'application/json'])
class ThrowableWriter extends ThrowableWriterSupport<Throwable> {}
