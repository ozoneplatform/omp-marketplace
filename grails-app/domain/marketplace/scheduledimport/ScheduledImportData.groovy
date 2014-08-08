package marketplace.scheduledimport

import marketplace.Profile
import marketplace.Types
import marketplace.Category
import marketplace.State
import marketplace.ServiceItem
import marketplace.CustomFieldDefinition
import marketplace.Relationship

/**
 * A NON-PERSISTENT domain class representing the format
 * of scheduled import data
 */
class ScheduledImportData {
    //Tells gorm not to persist this class
    static mapWith = "none"

    static hasMany = [
        profiles: Profile,
        types: Types,
        categories: Category,
        states: State,
        serviceItems: ServiceItem,
        customFieldDefs: CustomFieldDefinition,
        relationships: Relationship
    ]

    String maxClassification
}
