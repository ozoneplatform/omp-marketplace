package marketplace

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

class ModifyRelationshipActivity extends ServiceItemActivity implements ToJSON {

    List items = []

    static hasMany = [items: ServiceItemSnapshot]

    static mapping = {
        items joinTable: [name: "relationship_activity_log", key: 'mod_rel_activity_id']
        items batchSize: 50
    }

    @Override
    JSONObject asJSON() {
        def json = super.asJSON()

        json.put('relatedItems', new JSONArray(items.collect { ServiceItemSnapshot snapshot ->
            snapshot.asJSON()
        }))

        json
    }
}
