/**
 *
 */
package marketplace;

import org.apache.commons.logging.Log
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager

/**
 * @author kent.butler@gmail.com
 *
 */
class TransactionUtils {

    /**
     * Given a global sessionFactory, ensure that a HBM session is bound to the
     * current thread. May be required for threads of execution outside of
     * request-handling threads. This is JTA-compliant such that installation
     * of a JTA TransactionManager will enlist the session into a JTA Transaction.
     * @param sessionFactory
     * @param logger
     * @return boolean was a HBM session bound to the thread
     */
    static boolean ensureSession(SessionFactory sessionFactory, Log logger) {
        assert sessionFactory != null
        def boundSession = false

        // Ensure we have a Hibernate Session bound to the thread
        //   This may not be the case if we are running from non-request threads
        if (TransactionSynchronizationManager.getResource(sessionFactory) == null) {
            // No sessionFactory bound to the thread -- we must do this ourselves
            def session = getHbmSession(sessionFactory, logger)

            if (session) {
                logger.debug "Binding Hibernate session to the thread"
                TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
                boundSession = true
            } else {
                logger?.error "Unable to retrieve a Hibernate session from $sessionFactory; please contact technical support"
            }
        } else {
            logger?.debug "SessionFactory already bound to thread"
        }

        return boundSession
    }

    /**
     * Given a global sessionFactory, return the most appropriate Hibernate session possible.
     * If given a logger in debug mode, logs verbosely.
     *
     * @param sessionFactory
     * @param logger
     * @return org.hibernate.Session
     */
    static Session getHbmSession(SessionFactory sessionFactory, Log logger) {
        //   Try first getting the current session on the sessionFactory - this appears to be
        //   what the GORM components are using; otherwise, using Spring to find one
        Session session

        try {
            session = sessionFactory?.getCurrentSession()

            if (logger?.isDebugEnabled()) {
                logger?.debug "Getting Hibernate session via sessionFactory.getCurrentSession"
                if (session) {
                    logger?.debug "---Session Details via getCurrentSession ---\n\tSession [${java.lang.Integer.toHexString(session.hashCode())}]\n\tSessionFactory [${Integer.toHexString(session.getSessionFactory().hashCode())}] \n\tTransaction: ${Integer.toHexString(session?.transaction?.hashCode())}"
                } else {
                    logger?.debug "sessionFactory.getCurrentSession(): Session not available"
                }
                // Test the session available to the domain objects
                ImportTask.withSession { localSession ->
                    if (localSession) {
                        logger?.debug "---Session Details via withSession ---\n\tSession [${java.lang.Integer.toHexString(localSession.hashCode())}]\n\tSessionFactory [${Integer.toHexString(localSession.getSessionFactory().hashCode())}] \n\tTransaction: ${Integer.toHexString(session?.transaction?.hashCode())}"
                    } else {
                        logger?.debug "DomainClass.withSession: Session not available"
                    }
                }
            }
        } catch (Exception e) {
            // If there is no session bound to the thread, Spring's ORM code throws a
            //    HibernateException here -- in that case, ask Spring for a session
            logger.debug "${e.message} -- requesting Hibernate session from Spring"
            session = SessionFactoryUtils.getSession(sessionFactory, true)
            logger?.debug "---Session Details via SessionFactoryUtils ---\n\tSession [${java.lang.Integer.toHexString(session.hashCode())}]\n\tSessionFactory [${Integer.toHexString(session.getSessionFactory().hashCode())}] \n\tTransaction: ${Integer.toHexString(session?.transaction?.hashCode())}"
        }
        return session
    }

    public static void closeAndUnbindSession(SessionFactory sessionFactory) {
        sessionFactory.getCurrentSession().close()
        TransactionSynchronizationManager.unbindResource(sessionFactory)
    }
}
