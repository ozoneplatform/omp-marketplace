package marketplace

import org.grails.web.json.JSONObject

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


class Contact extends AuditStamped implements ToJSON {

    static final String PHONE_REGEX = /(^\+\d((([\s.-])?\d+)?)+$)|(^(\(\d{3}\)\s?|^\d{3}[\s.-]?)?\d{3}[\s.-]?\d{4}$)/
    static final String EMAIL_REGEX = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/

    static searchable = {
        root false
        securePhone index: 'not_analyzed', excludeFromAll: true
        unsecurePhone index: 'not_analyzed', excludeFromAll: true
        email index: 'not_analyzed', excludeFromAll: true
        name index: 'not_analyzed', excludeFromAll: true
        organization index: 'not_analyzed', excludeFromAll: true
        type component: true, excludeFromAll: true
        only = ['securePhone', 'unsecurePhone', 'email', 'name', 'organization', 'type']
    }

    String securePhone
    String unsecurePhone
    String email
    String name
    String organization
    ContactType type

    static belongsTo = [serviceItem: ServiceItem]

    static bindableProperties = ['type', 'securePhone', 'unsecurePhone', 'email', 'name', 'organization']
    static modifiableReferenceProperties = []

    //If a phone number is blank or null, then the other phone number must not be blank
    static phoneNumberValidator(String otherProp, String val, Contact obj) {
        if(!val || val.trim().size() == 0) {
            return obj."$otherProp" && obj."$otherProp".trim().size() > 0
        }
        true
    }

    static constraints = {
        unsecurePhone nullable: true, blank: true, maxSize: 50, matches: PHONE_REGEX,
                validator: Contact.&phoneNumberValidator.curry('securePhone')

        securePhone nullable: true, blank: true, maxSize: 50, matches: PHONE_REGEX,
                validator: Contact.&phoneNumberValidator.curry('unsecurePhone')

        email nullable: false, blank: false, maxSize: 100, matches: EMAIL_REGEX
        name nullable: false, blank: false, maxSize: 100
        organization nullable: true, blank: true, maxSize: 100
        type nullable: false
    }

    @Override
    int hashCode() {
        new HashCodeBuilder()
                .append(type)
                .append(securePhone)
                .append(unsecurePhone)
                .append(email)
                .append(name)
                .append(organization)
                .toHashCode()
    }

    @Override
    boolean equals(other) {

        // Since contacts are typically in a lazy loaded collection, the instances could be
        // hibernate proxies, so use the GORM 'instanceOf' method
        Boolean sameType
        try {
            sameType = other.instanceOf(Contact)
        } catch(MissingMethodException mme) {
            sameType = false
        }

        if (sameType) {
            return new EqualsBuilder()
                    .append(type, other.type)
                    .append(securePhone, other.securePhone)
                    .append(unsecurePhone, other.unsecurePhone)
                    .append(email, other.email)
                    .append(name, other.name)
                    .append(organization, other.organization)
                    .isEquals()
        }
        return false
    }

    @Override
    JSONObject asJSON() {
        marshall([id           : id,
                  type         : type.asJSON(),
                  securePhone  : securePhone,
                  unsecurePhone: unsecurePhone,
                  email        : email,
                  name         : name,
                  organization : organization])
    }

    @Override
    String toString() {
        String val = "$name, $email"
        val += organization ? ", $organization" : ""
        val += securePhone ? ", $securePhone (secure)" : ""
        val += unsecurePhone ? ", $unsecurePhone (unsecure)" : ""

        return val
    }


    /**
     * This method is used by the import logic to create a Domain object.
     */
    void bindFromJSON(JSONObject obj) {
        this.with {
            securePhone = obj.securePhone
            unsecurePhone = obj.unsecurePhone
            email = obj.email
            name = obj.name
            organization = obj.organization
            type = obj.type

        }
    }
}
