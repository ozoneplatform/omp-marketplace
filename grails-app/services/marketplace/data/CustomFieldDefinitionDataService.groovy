package marketplace.data

import grails.gorm.transactions.ReadOnly

import org.hibernate.criterion.CriteriaSpecification

import marketplace.CustomFieldDefinition
import marketplace.Types


class CustomFieldDefinitionDataService {

    @ReadOnly
    int countHasTypeById(long typeId) {
        CustomFieldDefinition.where { types { id == typeId} }.count()
    }

    /**
     * @return CustomFieldDefinitions that are required for the given type
     */
    @ReadOnly
    Set<CustomFieldDefinition> findAllRequiredByType(Types type) {
        CustomFieldDefinition.createCriteria().list {
            or {
                if (type) {
                    //use an outer join
                    createAlias("types", 't', CriteriaSpecification.LEFT_JOIN)
                    eq('t.id', type.id)
                }

                eq('allTypes', true)
            }

            eq('isRequired', true)
        } as Set
    }

}
