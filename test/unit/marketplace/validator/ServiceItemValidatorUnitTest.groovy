package marketplace.validator

import grails.test.mixin.TestFor
import grails.test.mixin.support.GrailsUnitTestMixin

import marketplace.ServiceItem
import marketplace.CustomFieldDefinition
import marketplace.CustomField
import marketplace.DropDownCustomFieldDefinition
import marketplace.DropDownCustomField
import marketplace.FieldValue
import marketplace.AccountService

@TestMixin(GrailsUnitTestMixin)
class ServiceItemValidatorUnitTest {

    ServiceItemValidator validator

    void setUp() {
        validator = new ServiceItemValidator()
    }

    void testValidateApprovalStatus() {
        def customFieldDefinitionMock = mockFor(CustomFieldDefinition)
        customFieldDefinitionMock.demand.static.findAllRequiredByType(1..1000) { [] }

        boolean isAdmin = true

        def accountServiceMock = mockFor(AccountService)
        accountServiceMock.demand.checkAdmin(1..1000) {
            if (!isAdmin) throw new IllegalArgumentException("checkAdmin test")
        }

        validator.accountService = accountServiceMock.createMock()

        ServiceItem existing = new ServiceItem(), dto = new ServiceItem()

        /*
         * Test that approval only works if isOutside is set
         */

        existing.approvalStatus = 'Pending'
        dto.approvalStatus = 'Approved'
        dto.isOutside = null
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.isOutside = true
        validator.validateChanges(existing, dto)

        dto.isOutside = false
        validator.validateChanges(existing, dto)

        /**
         * Test different transitions
         */

        existing.approvalStatus = 'In Progress'
        dto.approvalStatus = 'In Progress'
        validator.validateChanges(existing, dto)

        dto.approvalStatus = 'Pending'
        validator.validateChanges(existing, dto)

        dto.approvalStatus = 'Rejected'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Approved'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        existing.approvalStatus = 'Pending'
        dto.approvalStatus = 'In Progress'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Pending'
        validator.validateChanges(existing, dto)

        //Rejection must be done by creating a RejectionListing. Simply setting approvalStatus
        //to Rejected should not work
        dto.approvalStatus = 'Rejected'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Approved'
        validator.validateChanges(existing, dto)

        isAdmin = false
        dto.approvalStatus = 'Approved'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        existing.approvalStatus = 'Rejected'
        dto.approvalStatus = 'In Progress'
        isAdmin = true
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Rejected'
        validator.validateChanges(existing, dto)

        dto.approvalStatus = 'Pending'
        validator.validateChanges(existing, dto)

        dto.approvalStatus = 'Approved'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        isAdmin = false
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        existing.approvalStatus = 'Approved'
        dto.approvalStatus = 'In Progress'
        isAdmin = true
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Rejected'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Pending'
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.approvalStatus = 'Approved'
        validator.validateChanges(existing, dto)

        isAdmin = false
        validator.validateChanges(existing, dto)

        /* approval status being a valid value at all is comstrained at the domain object level */
    }

    void testValidateInsideOutside() {
        def customFieldDefinitionMock = mockFor(CustomFieldDefinition)
        customFieldDefinitionMock.demand.static.findAllRequiredByType(1..1000) { [] }

        ServiceItem existing = new ServiceItem(), dto = new ServiceItem()

        existing.isOutside = true
        dto.isOutside = null
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        existing.isOutside = false
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.isOutside = false
        validator.validateChanges(existing, dto)

        dto.isOutside = true
        validator.validateChanges(existing, dto)

        existing.isOutside = null
        validator.validateChanges(existing, dto)

        dto.isOutside = false
        validator.validateChanges(existing, dto)
    }

    void testValidateNewApprovalStatus() {
        def customFieldDefinitionMock = mockFor(CustomFieldDefinition)
        customFieldDefinitionMock.demand.static.findAllRequiredByType(1..1000) { [] }

        ServiceItem dto = new ServiceItem()

        dto.approvalStatus = 'In Progress'
        validator.validateNew(dto)

        dto.approvalStatus = 'Pending'
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }

        dto.approvalStatus = 'Rejected'
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }

        dto.approvalStatus = 'Approved'
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }
    }

    void testValidateDisabledCustomFields() {
        def customFieldDefinitionMock = mockFor(CustomFieldDefinition)
        customFieldDefinitionMock.demand.static.findAllRequiredByType(1..1000) { [] }

        ServiceItem existing = new ServiceItem(), dto = new ServiceItem()
        FieldValue enabled = new FieldValue(), disabled = new FieldValue(isEnabled: 0)
        CustomFieldDefinition definition = new DropDownCustomFieldDefinition(
            fieldValues: [enabled, disabled]
        )

        def makeCustomField = { params ->
            def field = new DropDownCustomField(params)
            field.customFieldDefinition = definition
            return field
        }

        Collection<CustomField> enabledSelection = [makeCustomField(value: enabled)],
            disabledSelection = [makeCustomField(value: disabled)],
            bothSelection = [makeCustomField(
                fieldValueList: [enabled, disabled]
            )],
            emptySelection = [makeCustomField([:])]

        /*
         * Test validate new
         */

        dto.customFields = emptySelection
        validator.validateNew(dto)

        dto.customFields = enabledSelection
        validator.validateNew(dto)

        dto.customFields = disabledSelection
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }

        dto.customFields = bothSelection
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }


        /*
         * Test validateChanges
         */

        existing.customFields = emptySelection

        dto.customFields = emptySelection
        validator.validateChanges(existing, dto)

        dto.customFields = enabledSelection
        validator.validateChanges(existing, dto)

        dto.customFields = disabledSelection
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.customFields = bothSelection
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        existing.customFields = enabledSelection

        dto.customFields = emptySelection
        validator.validateChanges(existing, dto)

        dto.customFields = enabledSelection
        validator.validateChanges(existing, dto)

        dto.customFields = disabledSelection
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.customFields = bothSelection
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        existing.customFields = disabledSelection

        dto.customFields = emptySelection
        validator.validateChanges(existing, dto)

        dto.customFields = enabledSelection
        validator.validateChanges(existing, dto)

        //IMPORTANT! This should pass because having a disabled selection that
        //you already had is acceptable
        dto.customFields = disabledSelection
        validator.validateChanges(existing, dto)

        dto.customFields = bothSelection
        validator.validateChanges(existing, dto)

        existing.customFields = bothSelection

        dto.customFields = emptySelection
        validator.validateChanges(existing, dto)

        dto.customFields = enabledSelection
        validator.validateChanges(existing, dto)

        dto.customFields = disabledSelection
        validator.validateChanges(existing, dto)

        dto.customFields = bothSelection
        validator.validateChanges(existing, dto)
    }

    void testValidateRequiredCustomFields() {
        ServiceItem existing = new ServiceItem(), dto = new ServiceItem()
        CustomFieldDefinition requiredDef = new CustomFieldDefinition(isRequired: true, name: 'required', uuid: '1'),
            nonRequiredDef = new CustomFieldDefinition(name: 'nonRequired', uuid: '2')

        def customFieldDefinitionMock = mockFor(CustomFieldDefinition)
        customFieldDefinitionMock.demand.static.findAllRequiredByType(1..1000) { [requiredDef] }

        def makeCustomField = { definition ->
            new CustomField(
                customFieldDefinition: definition
            )
        }

        Collection<CustomField> onlyRequired = [makeCustomField(requiredDef)],
            withoutRequired = [makeCustomField(nonRequiredDef)],
            bothFields = onlyRequired + withoutRequired,
            noFields = []

        /*
         * Test validateNew
         */

        dto.customFields = noFields
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }

        dto.customFields = withoutRequired
        shouldFail(IllegalArgumentException) {
            validator.validateNew(dto)
        }

        dto.customFields = onlyRequired
        validator.validateNew(dto)

        dto.customFields = bothFields
        validator.validateNew(dto)

        /*
         * Test validateChanges
         */

        existing.customFields = noFields

        dto.customFields = noFields
        validator.validateChanges(existing, dto)

        dto.customFields = withoutRequired
        validator.validateChanges(existing, dto)

        dto.customFields = onlyRequired
        validator.validateChanges(existing, dto)

        dto.customFields = bothFields
        validator.validateChanges(existing, dto)


        existing.customFields = onlyRequired

        dto.customFields = noFields
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.customFields = withoutRequired
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.customFields = onlyRequired
        validator.validateChanges(existing, dto)

        dto.customFields = bothFields
        validator.validateChanges(existing, dto)


        existing.customFields = withoutRequired

        dto.customFields = noFields
        validator.validateChanges(existing, dto)

        dto.customFields = withoutRequired
        validator.validateChanges(existing, dto)

        dto.customFields = onlyRequired
        validator.validateChanges(existing, dto)

        dto.customFields = bothFields
        validator.validateChanges(existing, dto)


        existing.customFields = bothFields

        dto.customFields = noFields
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.customFields = withoutRequired
        shouldFail(IllegalArgumentException) {
            validator.validateChanges(existing, dto)
        }

        dto.customFields = onlyRequired
        validator.validateChanges(existing, dto)

        dto.customFields = bothFields
        validator.validateChanges(existing, dto)
    }
}
