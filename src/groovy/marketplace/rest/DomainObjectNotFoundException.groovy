package marketplace.rest


/**
 * This class replaces org.grails.jaxrs.provider.DomainObjectNotFoundException
 * in our application.  That exception, since it is a subclass of
 * WebApplicationException, is not handled correctly by the ExceptionMapper system
 */
class DomainObjectNotFoundException extends RuntimeException {
    DomainObjectNotFoundException(clazz, id) {
        super(notFoundMessage(clazz, id))
    }

    private static String notFoundMessage(clazz, id) {
        "${clazz.simpleName} with id $id not found"
    }
}
