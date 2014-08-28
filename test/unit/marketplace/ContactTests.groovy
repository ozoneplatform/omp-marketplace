package marketplace

import grails.test.mixin.TestFor

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(Contact)
class ContactTests {

    Contact contact
    ContactType contactType
    ServiceItem serviceItem

    void setUp() {
        FakeAuditTrailHelper.install()

        mockForConstraintsTests(ContactType)
        mockForConstraintsTests(Contact)
        mockDomain(ServiceItem)

        contactType = new ContactType(title: "a type", required: false)
        serviceItem = new ServiceItem()
        contact = new Contact(name: "Bob", email: "abc@owfgoss.org", type: contactType, securePhone: '555-5555')

        serviceItem.addToContacts(contact)
    }

    void testValidContactPassesValidation() {
        assertTrue contact.validate()
    }

    void testValidPhoneNumbersPassValidation() {
        contact.securePhone = "410-444-1111"
        assertTrue contact.validate()
        contact.unsecurePhone = "4104441111"
        assertTrue contact.validate()
        contact.unsecurePhone = "4104441111"
        assertTrue contact.validate()
        contact.securePhone = "(410) 444-1111"
        assertTrue contact.validate()
        contact.unsecurePhone = "+4104441111"
        assertTrue contact.validate()
        contact.securePhone = "+02 41.0 444-1111"
        assertTrue contact.validate()
    }

    void testInvalidPhoneNumbersFailValidation() {
        contact.unsecurePhone = "41"
        assertFalse contact.validate()
        contact.unsecurePhone = "410-444-11111"
        assertFalse contact.validate()
        contact.unsecurePhone = "xxx-x4x-13a1"
        assertFalse contact.validate()
        contact.unsecurePhone = null

        contact.securePhone = "41"
        assertFalse contact.validate()
        contact.securePhone = "410-444-11111"
        assertFalse contact.validate()
        contact.securePhone = "xxx-x4x-13a1"
        assertFalse contact.validate()
        contact.securePhone = "+.444-4444"
        assertFalse contact.validate()
        contact.securePhone = "+4..444 4444"
        assertFalse contact.validate()
    }

    void testValidEmailsPassValidation() {
        contact.email = 'test@example.org'
        assertTrue contact.validate()

        //We are allowing invalid tlds
        contact.email = 'test@test.test'
        assertTrue contact.validate()
    }

    void testValidEmailsFailValidation() {
        contact.email = 'testexample.org'
        assertFalse contact.validate()

        contact.email = 'test!test.test'
        assertFalse contact.validate()

        contact.email = 'test'
        assertFalse contact.validate()

        contact.email = '@test.test'
        assertFalse contact.validate()
    }
}
