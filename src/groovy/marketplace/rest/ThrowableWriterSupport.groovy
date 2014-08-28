package marketplace.rest

import javax.ws.rs.Produces
import javax.ws.rs.ext.Provider

/**
 * Superclass for writers of throwables.
 */
@Provider
@Produces(['text/x-json', 'application/json'])
class ThrowableWriterSupport<T extends Throwable> extends AbstractMessageBodyWriter<T> {
    ThrowableWriterSupport() {
        super(Throwable.class)
    }

    @Override
    protected Map toBodyMap(T exception) {
        [
            error: true,
            message: exception.message ?: exception.cause?.message ?: exception.getClass().name
        ]
    }
}
