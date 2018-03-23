package marketplace.validator

import groovy.transform.CompileStatic
import javax.annotation.Nonnull
import javax.annotation.Nullable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import marketplace.AccountService
import marketplace.Constants
import marketplace.CustomField
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomField
import marketplace.FieldValue
import marketplace.ServiceItem
import marketplace.data.CustomFieldDefinitionDataService

import static com.google.common.base.Preconditions.checkNotNull
import static java.util.Collections.emptyList
import static java.util.Collections.emptyMap
import static java.util.Collections.emptySet


@Component
class ServiceItemValidator implements DomainValidator<ServiceItem> {

    @Autowired
    AccountService accountService

    @Autowired
    CustomFieldDefinitionDataService customFieldDefinitionDataService

    @Override
    void validateNew(ServiceItem serviceItem) {
        checkNotNull(serviceItem, "serviceItem must not be null")

        validateNewApprovalStatus(serviceItem)
        validateCustomFields(serviceItem)
    }


    @Override
    void validateChanges(ServiceItem serviceItem) {
        checkNotNull(serviceItem, "serviceItem must not be null")

        validateInsideOutside(serviceItem)
        validateApprovalStatus(serviceItem)
        validateCustomFields(serviceItem)
    }

    /**
     * Ensures that a new ServiceItem does not have an approval status other that In Progress*/
    private void validateNewApprovalStatus(@Nonnull ServiceItem serviceItem) {
        if (serviceItem.approvalStatus != Constants.APPROVAL_STATUSES['IN_PROGRESS']) {
            throw new IllegalArgumentException("New ServiceItems cannot have an approvalStatus other than ${Constants.APPROVAL_STATUSES['IN_PROGRESS']}")
        }
    }

    private void validateCustomFields(@Nonnull ServiceItem serviceItem) {
        validateDisabledCustomFields(serviceItem)
        validateRequiredCustomFields(serviceItem)
    }

    private static final IN_PROGRESS = Constants.APPROVAL_STATUSES['IN_PROGRESS']
    private static final PENDING = Constants.APPROVAL_STATUSES['PENDING']
    private static final APPROVED = Constants.APPROVAL_STATUSES['APPROVED']
    private static final REJECTED = Constants.APPROVAL_STATUSES['REJECTED']

    private static final VALID_USER_TRANSITIONS =
            [[IN_PROGRESS, PENDING],
             [REJECTED, PENDING]]

    //NOTE although [pending, rejected] is valid generally, it is not valid for it to
    //be explicitly changed.  Instead, rejections are performed by POSTing
    //a RejectionListing

    private static final VALID_ADMIN_TRANSITIONS =
            VALID_USER_TRANSITIONS + [[PENDING, APPROVED]]


    /**
     * Ensures that any change in approval status is valid and allowed for this user.
     * Also ensures that approval preprequisites are met*/
    private void validateApprovalStatus(@Nonnull ServiceItem serviceItem) {
        if (!serviceItem.isPropertyChanged('approvalStatus')) return

        def newApprovalStatus = serviceItem.approvalStatus
        def oldApprovalStatus = serviceItem.getOldValue('approvalStatus', String)

        if (newApprovalStatus == oldApprovalStatus) return

        def transition = [oldApprovalStatus, newApprovalStatus]

        if (!VALID_USER_TRANSITIONS.contains(transition)) {
            if (VALID_ADMIN_TRANSITIONS.contains(transition)) {
                accountService.checkAdmin(
                        "Illegal attempt to change approvalStatus of ServiceItem with id ${serviceItem.id}")
            }
            else {
                throw new IllegalArgumentException(
                        "Invalid approval status transition from ${oldApprovalStatus} to ${newApprovalStatus}")
            }
        }

        if (newApprovalStatus == APPROVED && serviceItem.isOutside == null) {
            throw new IllegalArgumentException(
                    "Cannot approve listing ${serviceItem.id} because isOutside is null")
        }
    }

    /**
     * Ensures that once set, isOutside cannot be unset
     **/
    private void validateInsideOutside(@Nonnull ServiceItem serviceItem) {
        def oldValue = serviceItem.getOldValue('isOutside', Boolean)
        if (oldValue != null && serviceItem.isOutside == null) {
            throw new IllegalArgumentException("Attempt to unset isOutside on ServiceItem ${serviceItem.id}")
        }
    }

    /**
     * Validate custom fields.  Currently this validation consists of making sure that disabled
     * DropDownCustomField values cannot be selected.
     **/
    private void validateDisabledCustomFields(@Nonnull ServiceItem serviceItem) {
        if (serviceItem.customFields == null || serviceItem.customFields.isEmpty()) return

        Map<Long, CustomField> oldFields = mapFieldsByDefinition(serviceItem.getOldValue('customFields', List))

        for (CustomField customField : serviceItem.customFields) {
            if (customField instanceof DropDownCustomField) {
                def existingField = oldFields.get(customField.customFieldDefinitionId) as DropDownCustomField

                def existingValues = getValuesAsList(existingField)
                def currentValues = getValuesAsList(customField)

                //values that are disabled and that weren't already selected are not allowed to be selected
                def notAllowed = currentValues.findAll { it.isEnabled == 0 } - existingValues

                if (notAllowed.size()) {
                    throw new IllegalArgumentException(
                            "Value <${notAllowed[0]}> for Custom Field <$customField> is disabled")
                }
            }

        }
    }

    private void validateRequiredCustomFields(@Nonnull ServiceItem serviceItem) {
        Set<CustomFieldDefinition> allRequired = customFieldDefinitionDataService.findAllRequiredByType(serviceItem.types)
        if (allRequired == null || allRequired.isEmpty()) return

        def oldFields = serviceItem.getOldValue('customFields', List)

        def currentlyRequired = oldFields != null ? intersect(allRequired, definitionsOf(oldFields))
                                                  : allRequired

        def missingRequiredFields = currentlyRequired - definitionsOf(serviceItem.customFields)

        if (!missingRequiredFields.isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing the following required Custom Fields: " + missingRequiredFields*.name.join(', '))
        }
    }

    @CompileStatic
    private static Map<Long, CustomField> mapFieldsByDefinition(@Nullable List<CustomField> fields) {
        if (fields == null || fields.isEmpty()) return emptyMap()

        fields.collectEntries { [it.customFieldDefinitionId, it] }
    }

    @CompileStatic
    @Nonnull
    private static Set<CustomFieldDefinition> definitionsOf(@Nullable Collection<CustomField> fields) {
        if (fields == null || fields.isEmpty()) return emptySet()

        fields*.customFieldDefinition as Set
    }

    @Nonnull
    private static List<FieldValue> getValuesAsList(@Nullable DropDownCustomField field) {
        if (!field) return emptyList()

        def result = field?.value ? [field.value] : field.fieldValueList

        result ?: emptyList()
    }

    @CompileStatic
    @Nonnull
    private static <T> Set<T> intersect(@Nonnull Set<T> left, @Nonnull Set<T> right) {
        def result = new HashSet(left)
        result.retainAll(right)
        result
    }

}
