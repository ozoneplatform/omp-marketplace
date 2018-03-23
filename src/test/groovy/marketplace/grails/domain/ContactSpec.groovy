package marketplace.grails.domain

import grails.test.mixin.TestFor
import marketplace.Contact
import marketplace.ContactType
import marketplace.ServiceItem
import marketplace.grails.domain.DomainConstraintsUnitTest
import marketplace.testutil.FakeAuditTrailHelper
import spock.lang.Specification

import javax.xml.ws.Service

//@TestFor(Contact)
class ContactSpec extends Specification  implements DomainConstraintsUnitTest<Contact>{

    List<Class> getDomainClasses() {[Contact, ContactType]}
    Contact contact
    ContactType contactType
    ServiceItem serviceItem
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true }}}


    void setup() {
        Mock(Contact)
        Mock(ContactType)
        Mock(ServiceItem)
        //FakeAuditTrailHelper.install()

        serviceItem = new ServiceItem()
        contactType = new ContactType(title: "a type", required: false)
        contact = new Contact(name: "Bob", email: "abc@owfgoss.org", type: contactType, securePhone: '555-5555', serviceItem: serviceItem)
    }


    void testValidContactPassesValidation() {
        expect:
        contact.validate()
    }

    void testValidPhoneNumbersPassValidation() {
        when:
        contact.securePhone = "410-444-1111"
        then:
        contact.validate()

        when:
        contact.unsecurePhone = "4104441111"
        then:
        contact.validate()

        when:
        contact.unsecurePhone = "4104441111"
        then:
        contact.validate()

        when:
        contact.securePhone = "(410) 444-1111"
        then:
        contact.validate()

        when:
        contact.unsecurePhone = "+4104441111"
        then:
        contact.validate()

        when:
        contact.securePhone = "+02 41.0 444-1111"
        then:
        contact.validate()
    }

    void testInvalidPhoneNumbersFailValidation() {
        when:
        contact.unsecurePhone = "41"
        then:
        !contact.validate()

        when:
        contact.unsecurePhone = "410-444-11111"
        then:
        !contact.validate()

        when:
        contact.unsecurePhone = "xxx-x4x-13a1"
        then:
        !contact.validate()

        when:
        contact.unsecurePhone = null
        contact.securePhone = "41"
        then:
        !contact.validate()

        when:
        contact.securePhone = "410-444-11111"
        then:
        !contact.validate()

        when:
        contact.securePhone = "xxx-x4x-13a1"
        then:
        !contact.validate()

        when:
        contact.securePhone = "+.444-4444"
        then:
        !contact.validate()

        when:
        contact.securePhone = "+4..444 4444"
        then:
        !contact.validate()
    }

    void testValidEmailsPassValidation() {
        when:
        contact.email = 'test@example.org'
        then:
        contact.validate()

        //We are allowing invalid tlds
        when:
        contact.email = 'test@test.test'
        then:
        contact.validate()
    }

    void testValidEmailsFailValidation() {
        when:
        contact.email = 'testexample.org'
        then:
        !contact.validate()

        when:
        contact.email = 'test!test.test'
        then:
        !contact.validate()

        when:
        contact.email = 'test'
        then:
        !contact.validate()

        when:
        contact.email = '@test.test'
        then:
        !contact.validate()
    }
}
