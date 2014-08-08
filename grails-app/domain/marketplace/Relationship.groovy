package marketplace

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

import ozone.marketplace.enums.RelationshipType

class Relationship implements Serializable {

    static bindableProperties = ['relatedItems', 'relationshipType']
    static modifiableReferenceProperties = []


    List<ServiceItem> relatedItems
    RelationshipType relationshipType

    static hasMany = [relatedItems: ServiceItem]
    static belongsTo = [owningEntity: ServiceItem]

    static constraints = {
        owningEntity(nullable: false)
    }

    JSONObject asJSON() {
        new JSONObject(
            relatedItems: (this.getRelatedItems().collect { it?.asJSONMinimum() }.findAll { it != null }) as JSONArray,
            relationshipType: relationshipType.name()
        )
    }

    /**
     * Find all Relationships whose relatedItems include the specificed ServiceItem
     */
    static Collection<Relationship> findRelationshipsByRelatedItem(ServiceItem related) {
        Relationship.createCriteria().list {
            relatedItems {
                eq('id', related.id)
            }
        }
    }

    /**
     * Setters for legacy format compatibility (scheduled import)
     */

    public void setRequires(Collection<ServiceItem> serviceItems) {
        relatedItems.clear()
        relatedItems << serviceItems
    }
    public void setServiceItem(ServiceItem owningEntity) {
        this.owningEntity = owningEntity
    }
}
