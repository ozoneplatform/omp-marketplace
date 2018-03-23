package marketplace.grails.service

import spock.lang.Specification

import grails.config.Config

import org.hibernate.Session
import org.hibernate.SessionFactory

import marketplace.AccountService


class OwfUnitSpecification extends Specification {

    Config mockConfig(Map<String, Object> values) {
        Config config = Mock(Config)
        values.forEach { key, value ->
            config.get(key) >> value
        }
        config
    }

    SessionFactory mockSessionFactory() {
        SessionFactory sessionFactory = Mock(SessionFactory)
        sessionFactory.getCurrentSession() >> Mock(Session)
        sessionFactory
    }

    AccountService mockAccountService(String loggedInUser) {
        AccountService accountService = Mock(AccountService)
        accountService.getLoggedInUsername() >> loggedInUser
        accountService.getProperty('loggedInUsername') >> loggedInUser
        accountService
    }

}
