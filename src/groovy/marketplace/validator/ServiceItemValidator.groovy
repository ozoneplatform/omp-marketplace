package marketplace.validator

import javax.annotation.PostConstruct

import marketplace.AccountService
import marketplace.Constants
import marketplace.ServiceItem
import marketplace.DropDownCustomField
import marketplace.CustomFieldDefinition
import marketplace.FieldValue

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ServiceItemValidator implements DomainValidator<ServiceItem> {

    @Autowired
    AccountService accountService


    /**
     * Ensures that any change in approval status is valid and allowed for this user.
     * Also ensures that approval preprequisites are met
     */
    private void validateApprovalStatus(ServiceItem existing, ServiceItem dto) {
        def newApprovalStatus = dto.approvalStatus
        def oldApprovalStatus = existing.approvalStatus

        def inProgress = Constants.APPROVAL_STATUSES['IN_PROGRESS']
        def pending = Constants.APPROVAL_STATUSES['PENDING']
        def approved = Constants.APPROVAL_STATUSES['APPROVED']
        def rejected = Constants.APPROVAL_STATUSES['REJECTED']

        def validUserTransitions = [[inProgress, pending], [rejected, pending]]

        //NOTE although [pending, rejected] is valid generally, it is not valid for it to
        //be explicitly changed.  Instead, rejections are performed by POSTing
        //a RejectionListing
        def validAdminTransitions = validUserTransitions + [[pending, approved]]

        def transition = [oldApprovalStatus, newApprovalStatus]

        //if approvalStatus has changed
        if (newApprovalStatus != oldApprovalStatus) {
            if (!validUserTransitions.contains(transition)) {
                if (validAdminTransitions.contains(transition)) {
                    accountService.checkAdmin("Illegal attempt to change approvalStatus of " +
                        "ServiceItem with id ${existing.id}")
                }
                else {
                    throw new IllegalArgumentException("Invalid approval status transition from" +
                        " ${oldApprovalStatus} to ${newApprovalStatus}")
                }
            }

            if (newApprovalStatus == approved && dto.isOutside == null) {
                throw new IllegalArgumentException("Cannot approve listing ${existing.id} " +
                    "because isOutside is null")
            }
        }
    }

    /**
     * Ensures that once set, isOutside cannot be unset
     */
    private void validateInsideOutside(ServiceItem existing, ServiceItem dto) {
      if (existing.isOutside != null && dto.isOutside == null) {
            throw new IllegalArgumentException("Attempt to unset isOutside on ServiceItem " +
                "${existing.id}")
        }
    }

    /**
     * Ensures that a new ServiceItem does not have an approval status other that In Progress
     */
    private void validateNewApprovalStatus(dto) {
       if (dto.approvalStatus != Constants.APPROVAL_STATUSES['IN_PROGRESS']) {
            throw new IllegalArgumentException("New ServiceItems cannot have an " +
                "approvalStatus other than ${Constants.APPROVAL_STATUSES['IN_PROGRESS']}")
        }
    }

    /**
     * Validate custom fields.  Currently this validation consists of making sure that disabled
     * DropDownCustomField values cannot be selected.
     */
    private void validateDisabledCustomFields(ServiceItem existing, ServiceItem dto) {
        def findCustomField = { ServiceItem item, CustomFieldDefinition definition ->
            item?.customFields?.find { it.customFieldDefinition == definition }
        }

        dto.customFields.eachWithIndex { customField, i ->
            if (customField instanceof DropDownCustomField) {
                def existingField = findCustomField(existing, customField.customFieldDefinition)

                Collection<FieldValue> existingValues = existingField?.value ?
                    [existingField.value] : existingField?.fieldValueList
                Collection<FieldValue> currentValues = customField.value ?
                    [customField.value] : customField.fieldValueList

                //values that are disabled and that weren't already selected are not allowed
                //to be selected
                List<FieldValue> notAllowed = currentValues.grep { !it.isEnabled } -
                    existingValues

                if (notAllowed.size()) {
                    throw new IllegalArgumentException("Value <${notAllowed[0]}> for " +
                        "Custom Field <$customField> is disabled")
                }
            }
        }
    }

    private void validateRequiredCustomFields(ServiceItem existing, ServiceItem dto) {
        Set<CustomFieldDefinition> requiredCustomFields =
                CustomFieldDefinition.findAllRequiredByType(dto.types) as Set,
            oldFields = existing ? existing.customFields*.customFieldDefinition as Set : null,

            /*
             * Fields which are marked required and which previously existed on this listing.
             * Fields which were not previously present on this listing are not required in order
             * to allow unrelated updates to still occur without having to mess with the
             * custom fields.  Specifically, this allows the Inside/Outside switch and similar
             * elements in the UI to work.
             */
            currentlyRequiredFields = existing ? requiredCustomFields.intersect(oldFields) :
                requiredCustomFields,

            dtoFields = dto.customFields*.customFieldDefinition as Set,
            missingRequiredFields = currentlyRequiredFields - dtoFields


        if (!missingRequiredFields.isEmpty()) {
            throw new IllegalArgumentException("Missing the following required Custom Fields: " +
                missingRequiredFields*.name.join(', '))
        }
    }

    private void validateCustomFields(ServiceItem existing, ServiceItem dto) {
        validateDisabledCustomFields(existing, dto)
        validateRequiredCustomFields(existing, dto)
    }

    @Override
    public void validateNew(ServiceItem dto) {
        validateNewApprovalStatus(dto)
        validateCustomFields(null, dto)
    }


    @Override
    public void validateChanges(ServiceItem existing, ServiceItem dto) {
        validateInsideOutside(existing, dto)
        validateApprovalStatus(existing, dto)
        validateCustomFields(existing, dto)
    }
}
