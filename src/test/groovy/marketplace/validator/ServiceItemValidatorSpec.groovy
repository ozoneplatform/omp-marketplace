package marketplace.validator

import spock.lang.Specification
import spock.lang.Unroll

import grails.testing.gorm.DataTest
import grails.testing.spring.AutowiredTest
import grails.web.databinding.DataBinder

import org.springframework.security.access.AccessDeniedException

import marketplace.AccountService
import marketplace.Constants
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomField
import marketplace.DropDownCustomFieldDefinition
import marketplace.FieldValue
import marketplace.ServiceItem
import marketplace.data.CustomFieldDefinitionDataService
import marketplace.rest.AuditableDataBindingListener
import marketplace.rest.ServiceItemDataBindingListener

import static java.util.Collections.emptyList


class ServiceItemValidatorSpec
        extends Specification
        implements AutowiredTest, DataTest, DataBinder
{

    private static final IN_PROGRESS = Constants.APPROVAL_STATUSES['IN_PROGRESS']
    private static final PENDING = Constants.APPROVAL_STATUSES['PENDING']
    private static final APPROVED = Constants.APPROVAL_STATUSES['APPROVED']
    private static final REJECTED = Constants.APPROVAL_STATUSES['REJECTED']

    Closure doWithSpring() {
        { ->
            auditableDataBindingListener(AuditableDataBindingListener)
            serviceItemDataBindingListener(ServiceItemDataBindingListener)
        }
    }

    AccountService accountService = Mock()
    CustomFieldDefinitionDataService dataService = Mock()

    ServiceItemValidator validator

    boolean $checkAdmin_isAdmin = true

    def setup() {
        mockDomain ServiceItem
        mockDomain CustomFieldDefinition
        mockDomain DropDownCustomFieldDefinition
        mockDomain DropDownCustomField
        mockDomain FieldValue

        // Mocks
        accountService.checkAdmin(*_) >> { List args ->
            def message = args.size() == 1 ? args.get(0) as String : "Attempt to access Admin-only functionality"

            if (!$checkAdmin_isAdmin) throw new AccessDeniedException(message)
        }

        dataService.findAllRequiredByType(*_) >> { List args -> emptyList() }

        // Validator
        validator = new ServiceItemValidator()
        validator.accountService = accountService
        validator.customFieldDefinitionDataService = dataService
    }

    private void validateChanges(ServiceItem original, Map<String, ?> changes) {
        bindData(original, changes)
        validator.validateChanges(original)
    }

    def "validateNew(): approvalStatus In Progress is valid"() {
        when:
        validator.validateNew(new ServiceItem(approvalStatus: IN_PROGRESS))

        then:
        notThrown(IllegalArgumentException)
    }

    @Unroll
    def "validateNew(): approvalStatus #status is invalid"(String status) {
        when:
        validator.validateNew(new ServiceItem(approvalStatus: status))

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "New ServiceItems cannot have an approvalStatus other than In Progress"

        where:
        status << [PENDING, REJECTED, APPROVED]
    }

    def "validateChanges(): as admin, approve listing fails if isOutside not set"() {
        given:
        def existing = new ServiceItem([approvalStatus: PENDING, isOutside: null]).save(validate: false)

        when:
        validateChanges(existing, [approvalStatus: APPROVED])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message =~ /Cannot approve listing \d+ because isOutside is null/
    }

    def "validateChanges(): as admin, approve listing succeeds if isOutside is #value"(boolean value) {
        given:
        def existing = new ServiceItem([approvalStatus: PENDING, isOutside: value]).save(validate: false)

        when:
        validateChanges(existing, [approvalStatus: APPROVED])

        then:
        notThrown(IllegalArgumentException)

        where:
        value << [true, false]
    }

    @Unroll
    def "validateChanges(): as #user, approvalStatus #fromStatus -> #toStatus is valid"(String user,
                                                                                        String fromStatus,
                                                                                        String toStatus)
    {
        given:
        $checkAdmin_isAdmin = (user == "admin")

        def existing = new ServiceItem([approvalStatus: fromStatus, isOutside: true]).save(validate: false)

        when:
        validateChanges(existing, [approvalStatus: toStatus])

        then:
        notThrown(IllegalArgumentException)

        where:
        user    | fromStatus  | toStatus
        "admin" | APPROVED    | APPROVED
        "admin" | IN_PROGRESS | IN_PROGRESS
        "admin" | PENDING     | PENDING
        "admin" | REJECTED    | REJECTED

        "admin" | IN_PROGRESS | PENDING
        "admin" | REJECTED    | PENDING

        "user"  | IN_PROGRESS | PENDING
        "user"  | REJECTED    | PENDING

        "admin" | PENDING     | APPROVED
    }

    @Unroll
    def "validateChanges(): as #user, approvalStatus #fromStatus -> #toStatus is invalid"(String user,
                                                                                          String fromStatus,
                                                                                          String toStatus,
                                                                                          Class exception,
                                                                                          String message)
    {
        given:
        $checkAdmin_isAdmin = (user == "admin")

        def existing = new ServiceItem([approvalStatus: fromStatus, isOutside: true]).save(validate: false)

        when:
        validateChanges(existing, [approvalStatus: toStatus])

        then:
        def ex = thrown(exception)
        ex.message =~ message

        where:
        user    | fromStatus  | toStatus    || exception                | message
        "admin" | IN_PROGRESS | APPROVED    || IllegalArgumentException | /^Invalid approval status transition from In Progress to Approved/
        "admin" | IN_PROGRESS | REJECTED    || IllegalArgumentException | /^Invalid approval status transition from In Progress to Rejected/

        "admin" | PENDING     | IN_PROGRESS || IllegalArgumentException | /^Invalid approval status transition from Pending to In Progress/
        "admin" | PENDING     | REJECTED    || IllegalArgumentException | /^Invalid approval status transition from Pending to Rejected/

        "admin" | APPROVED    | IN_PROGRESS || IllegalArgumentException | /^Invalid approval status transition from Approved to In Progress/
        "admin" | APPROVED    | PENDING     || IllegalArgumentException | /^Invalid approval status transition from Approved to Pending/
        "admin" | APPROVED    | REJECTED    || IllegalArgumentException | /^Invalid approval status transition from Approved to Rejected/

        "admin" | REJECTED    | IN_PROGRESS || IllegalArgumentException | /^Invalid approval status transition from Rejected to In Progress/
        "admin" | REJECTED    | APPROVED    || IllegalArgumentException | /^Invalid approval status transition from Rejected to Approved/

        "user"  | PENDING     | APPROVED    || AccessDeniedException    | /^Illegal attempt to change approvalStatus of ServiceItem with id \d+/
    }


    @Unroll
    def "validateChanges(): isOutside #oldValue -> #newValue is valid"(boolean oldValue,
                                                                       boolean newValue)
    {
        given:
        def existing = new ServiceItem([isOutside: oldValue]).save(validate: false)

        when:
        validateChanges(existing, [isOutside: newValue])

        then:
        notThrown(IllegalArgumentException)

        where:
        oldValue | newValue
        true     | true
        true     | false
        false    | false
        false    | true
    }

    @Unroll
    def "validateChanges(): isOutside #oldValue -> null is invalid"(boolean oldValue) {
        given:
        def existing = new ServiceItem([isOutside: oldValue]).save(validate: false)

        when:
        validateChanges(existing, [isOutside: null])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message =~ /^Attempt to unset isOutside on ServiceItem \d+/

        where:
        oldValue << [true, false]
    }

}
