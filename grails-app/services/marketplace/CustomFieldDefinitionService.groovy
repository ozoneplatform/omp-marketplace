package marketplace

import grails.gorm.transactions.Transactional
import grails.orm.PagedResultList

import org.hibernate.FlushMode
import org.hibernate.SessionFactory

import org.apache.commons.lang.exception.ExceptionUtils

import ozone.marketplace.domain.ValidationException


class CustomFieldDefinitionService extends MarketplaceService {

    SessionFactory sessionFactory

    @Transactional(readOnly = true)
    List<CustomFieldDefinition> list(def params = [sort: 'label', order: 'asc']) {
        def results
        def dateSearch = parseEditedSinceDate(params)
        if (dateSearch) {
            def criteria = CustomFieldDefinition.createCriteria()
            results = onOrAfterEditedDate(criteria, params, dateSearch)
        } else {
            results = CustomFieldDefinition.list(params)
        }
        return results
        //return CustomFieldDefinition.list(sort:params?.sort, order: params?.order, offset:params?.offset, max:params?.max)
    }

    @Transactional(readOnly = true)
    def countTypes() {
        return CustomFieldDefinition.count()
    }

    /**
     * There are two cases to consider:
     * 1. Custom Field Definition with no listing associated with it. Just delete it
     * 2. Custom Field Definition with active listing associated with it. Throw error, don't delete it
     */
   @Transactional
    def delete(CustomFieldDefinition cfd) {

        try {

            if (!cfd) {
                throw new ValidationException(message: "objectNotFound")
            }

            def cfCriteria = CustomField.createCriteria()
            def customFieldList = cfCriteria.list{
                customFieldDefinition {
                    eq('id', cfd.id as Long)
                }
            }

            //JH:  I dont like this at all.  However when I would do a criteria query
            //     to get only the service items that have this custom field, the customFields list
            //     in service item would only be partially populated and I could not get it fully hydrated.
            //     If this becomes a problem, which it shouldnt, we can use DetatchedCriteria or a jdbctemplate.
            ServiceItem.list().each{ServiceItem serviceItem ->
                customFieldList.each{
                    if(serviceItem.customFields.contains(it)){
                       serviceItem.customFields.remove(it)
                       serviceItem.save(flush:true)
                    }

                }
            }

            //customFieldList*.delete()
            cfd.delete()
        }
        catch (ValidationException ve) {
            throw ve
        }
        catch (Exception ex) {
            String message = ExceptionUtils.getRootCauseMessage(ex)
            log.error(message, ex)
            // Need this to prevent flush exception. See http://jira.codehaus.org/browse/GRAILS-5865
            def session = sessionFactory.currentSession
            session.setFlushMode(FlushMode.MANUAL)
            throw new ValidationException(message: "delete.failure", args: [cfd?.toString(), message])
        }
    }


    @Transactional(readOnly = true)
    def getDropDownCFDFieldValues(def params) {
        def cfd = CustomFieldDefinition.get(params.id)
        if (!cfd) {
            throw new ValidationException(message: "CustomFieldDefinition '${id}' objectNotFound", args: [id])
        }

        if (cfd.instanceOf(DropDownCustomFieldDefinition)) {
            def cfdFVList = cfd.fieldValues?.findAll { it?.isEnabled == 1 }
            if ((!cfdFVList) || (cfdFVList.size() == 0)) {
                throw new ValidationException(message: "DropDownCustomFieldDefinition '${id}' has no Field Values", args: [id])
            }
            return new PagedResultList(cfdFVList, cfdFVList.size())
        } else {
            throw new ValidationException(message: "CustomFieldDefinition '${id}' is Not a DROP DOWN Custom Field Definition.", args: [id])
        }
    }

    // Do it this way until we resolve the problem with using the criteria query show in findByType2 below.
    @Transactional(readOnly = true)
    def findByType(Types type) {
        def customFields = CustomFieldDefinition.list()
        def customFieldsHasType = []
        customFields.each { cf ->
            if (cf.allTypes) {
                customFieldsHasType << cf
            } else {
                cf.types.each {
                    // Need ? for integration tests that don't set the ServiceItem type.
                    if (type?.id == it.id) {
                        customFieldsHasType << cf
                    }
                }
            }
        }

        return customFieldsHasType
    }
// The query below will return a cfd that only has the matching type in the cfd.types list and
// sometimes includes nulls in the list!
//    def findByType2(Types type) {
//
//        def results = CustomFieldDefinition.withCriteria(){
//            types {
//                eq("id", type?.id)
//            }
//            join "types"
//            cache(true)
//            cacheMode(CacheMode.GET) // prevent Hibernate/2nd level cache bug
//        }
//
//        return results
//    }

    @Transactional(readOnly = true)
    def swapFieldValues(def fieldValues, def pos1, def pos2) {
        log.debug "swapFieldValues: swapping ${pos1} and ${pos2}"
        def fieldValue1 = fieldValues[pos1]

        fieldValues[pos1] = fieldValues[pos2]
        fieldValues[pos2] = fieldValue1
    }

    @Transactional(readOnly = true)
    int getPosition(List<FieldValue> fieldValues, String valueIn) {
        fieldValues.findIndexOf {
            it.displayText == valueIn
        }
    }

    @Transactional(readOnly = true)
    def updateFieldValsInToFieldValueIdsMap(def params, String valueToAdd, HashMap<String, Long> fieldValsInToFieldValueIds) {
        if (params["_fv_hidden_id_${valueToAdd}"] != null) {
            fieldValsInToFieldValueIds.put(valueToAdd, Long.valueOf(params["_fv_hidden_id_${valueToAdd}"]))
        }
    }

    @Transactional
    def update(def fieldDef, def params) {
        log.debug "update: params.fieldOptions = ${params.fieldOptions}"

        if (fieldDef.instanceOf(DropDownCustomFieldDefinition)) {
            def fieldValuesIn = params.fieldOptions
            // Key is the fieldValue's name and the value is its database id.
            def fieldValsInToFieldValueIds = new HashMap<String, Long>()
            // If the user only specifies one option, then the value is a String not a List!
            if (params.fieldOptions instanceof java.lang.String) {
                fieldValuesIn = [params.fieldOptions]
                updateFieldValsInToFieldValueIdsMap(params, params.fieldOptions, fieldValsInToFieldValueIds)
            } else {
                fieldValuesIn.each {
                    updateFieldValsInToFieldValueIdsMap(params, it, fieldValsInToFieldValueIds)
                }
            }

            fieldValuesIn = fieldValuesIn ?: new String[0]
            // Without the following save I was getting an InvalidDataAccessApiUsageException:
            //     Write operations are not allowed in read-only mode (FlushMode.MANUAL):...
            // in the case where there is no Name specified and no Options.
            fieldDef.save(failOnError: true)

            updateFieldValues(fieldDef, fieldValuesIn, fieldValsInToFieldValueIds, params.valueEnableFlags)
        }

        fieldDef.save(failOnError: true)
    }

    @Transactional
    void updateFieldValues(CustomFieldDefinition fieldDef, String[] fieldValueStringsIn,
             Map<String, Integer>fieldValsInToFieldValueIds, String enableFlagsIn) {

        List<FieldValue> existingFieldValues = fieldDef.fieldValues

        Collection<FieldValue> fieldValuesToRemove = []

        //go through all existing fieldvalues and update or remove them
        existingFieldValues.each { existingValue ->
            String newFieldValueStr = fieldValueStringsIn.find {
                existingValue.id == fieldValsInToFieldValueIds.get(it)
            }

            //if this value is not in the input, delete it
            if (!newFieldValueStr) {
                if (isFieldValueInUse(existingValue.id.toString())) {
                    throw new ozone.marketplace.domain.ValidationException(
                        message: "Field value '${fieldValue.displayText}' is being referenced " +
                            "by a listing and so cannot be deleted. Disabling is an " +
                            "alternative to deleting.",
                        args: [existingValue.displayText]
                    )
                }
                fieldValuesToRemove << existingValue
            }
            //if it is in the input, update it
            else {
                existingValue.displayText = newFieldValueStr
            }
        }

        //remove the values. This has to be done as a separate step, outside of the
        //iteration above, to avoid a ConcurrentModificationException
        fieldValuesToRemove.each { fieldDef.removeFromFieldValues(it) }

        // add new fieldValues
        fieldValueStringsIn.each {
            FieldValue existingValue = existingFieldValues.find { efv ->
                    efv.id == fieldValsInToFieldValueIds.get(it)
            }

            if (!existingValue) {
                log.debug "fieldValue ${it} has been added by the user"
                FieldValue newField = new FieldValue(
                    displayText: it
                )
                fieldDef.addToFieldValues(newField)
                newField.save(failOnError:true)
            }
        }


        // reorder any fieldValues that have had their positions changed
        fieldValueStringsIn.eachWithIndex { valueIn, i ->
            int j = getPosition(existingFieldValues, valueIn)
            if (j != -1 && i != j) {
                swapFieldValues(existingFieldValues, i, j)
            }
        }

        // enableFlagsIn is parallel list of 1s and 0s specifying whether or not each
        // fieldValue is enabled.
        List<String> enableFlags = enableFlagsIn.tokenize(',')
        existingFieldValues.eachWithIndex { existingValue, i ->
            Integer enabledFl = Integer.valueOf(enableFlags[i])
            log.debug "fieldValue - ${existingValue} enabledFl = ${enabledFl} "

            existingValue.isEnabled = enabledFl
        }

        fieldDef.save(failOnError:true)
    }

    @Transactional
    def save(def fieldDef) {
        fieldDef.save(failOnError: true)
    }

    @Transactional
    def markForDeletion(CustomField cf) {
        log.debug "customField to be marked for deletion: ${cf}, ${cf?.customFieldDefinition?.id}"
        cf.value = null
    }

    @Transactional
    def removeNullCustomFields(ServiceItem svc) {
        if (svc.customFields == null) {
            return
        }
        def removals = svc.customFields.findAll { it.isEmpty() }
        svc.customFields.retainAll { !it.isEmpty() }
        removals.each { it.delete() }
    }

    @Transactional(readOnly = true)
    boolean isFieldValueInUse(String fieldValueId) {
        def criteria = DropDownCustomField.createCriteria()
        Long id = Long.parseLong(fieldValueId)
        //def equal = criteria.eq('id', id)

        def cnt = criteria.get {
            projections {
                count('id')
            }
            value {
                eq('id',id)
            }
        }
        log.debug "isFieldValueInUse: fieldValueId = ${fieldValueId} cnt = ${cnt}"
        return cnt > 0
    }
}
