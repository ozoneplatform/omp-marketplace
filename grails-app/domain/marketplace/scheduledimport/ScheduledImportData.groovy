package marketplace.scheduledimport

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
