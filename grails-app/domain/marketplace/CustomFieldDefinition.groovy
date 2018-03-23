package marketplace

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

import org.hibernate.criterion.CriteriaSpecification

import marketplace.JSONUtil as JS

import ozone.marketplace.enums.CustomFieldSection
import ozone.utils.Utils


class CustomFieldDefinition extends AuditStamped implements Serializable, ToJSON {

    static bindableProperties = [
        'name', 'label',
        'tooltip', 'description',
        'isRequired', 'uuid',
        'allTypes', 'isPermanent',
        'section', 'types'
    ]

    static modifiableReferenceProperties = []

    String name
    String label
    String tooltip
    String description
    boolean isRequired = false
    String uuid

    def beforeInsert() {
        if (!uuid) {
            uuid = Utils.generateUUID();
        }
    }

    // The name of the section where the field should be displayed on the Service Item Create/Edit screens.
    CustomFieldSection section = CustomFieldSection.typeProperties

    boolean allTypes = false
    Boolean isPermanent = false

    Constants.CustomFieldDefinitionStyleType styleType

    static hasMany = [types: Types]

    List<Types> types

    static constraints = {
        name(blank: false, maxSize: 50)
        label(blank: false, maxSize: 50)
        tooltip(maxSize: 50, nullable: true)
        description(nullable: true, maxSize: 250)
        styleType(nullable: false)
        uuid(nullable: true, unique: true)
        section(nullable: true)
        isPermanent(nullable: true)
    }

    static mapping = {
        types column: 'cf_definition_types_id'
        types cache: true
        types batchSize: 50
        cache true
        tablePerHierarchy false
    }

    String toString() { "${this.class} : ${name}" }

    String prettyPrint() {
        toString()
    }

    boolean belongsToType(Types type) {
        if (allTypes) {
            return true
        }
        return !!types.grep(type)
    }

    void scrubCR() {
        if (this.description) {
            this.description = this.description.replaceAll("\r", "")
        }
    }

    def asJSONRef() {
        new JSONObject(
            id: id,
            uuid: uuid,
            name: name,
            label: label,
            fieldType: styleType.styleTypeName,
            section: section?.name() ?: CustomFieldSection.typeProperties.name()
        )
    }

    @Override
    JSONObject asJSON() {
        marshall([id         : id,
                  class      : getClass(),
                  uuid       : uuid,
                  name       : name,
                  label      : label,
                  tooltip    : tooltip,
                  description: description,
                  isRequired : isRequired,
                  isPermanent: isPermanent,
                  allTypes   : allTypes,
                  types      : ((types == null) ? new JSONArray() : new JSONArray(types?.collect { it?.asJSONRef() })),
                  fieldType  : styleType?.name(),
                  section    : section?.name() ?: CustomFieldSection.typeProperties.name()])
    }

    protected bindFromJSON(JSONObject json) {
        [
            "name",
            "label",
            "tooltip",
            "description",
            "uuid"
        ].each(JS.optStr.curry(json, this))

        [
            "isRequired",
            "isPermanent",
            "allTypes"
        ].each(JS.optBoolean.curry(json, this))

        if (json.section) this.section = CustomFieldSection.valueOf(json.section)

        if (this.types) {
            this.types.clear() // remove all the types from the list
        }
        json.types.each() {
            def typeIn = Types.findByUuid(it.uuid)
            if (typeIn) {
                log.info "adding type with uuid = ${it.uuid} to cfd"
                this.addToTypes(typeIn)
            } else {
                // TODO: this should probably throw an exception
                log.warn "unable to find type with uuid = ${it.uuid}"
            }
        }
    }

    static boolean findDuplicates(def obj) {
        if (obj?.has('uuid')) {
            //Finds CFDs with matching uuids
            def allUuids = findAllByUuid(obj.uuid);
            if (allUuids.size() == 0) {
                if (obj.has('name') && obj.has('fieldType')) {
                    def allNames = findAllByName(obj.name), duplicate = false;
                    //Finds CFDs with matching names and style types. If it is a drop down
                    //CFD then it checks the isMultiSelect flag as well
                    allNames.each {
                        if (obj.fieldType == "DROP_DOWN") {
                            if (obj.has("isMultiSelect")) {
                                if ((it.styleType.styleTypeName == "Drop Down") && (obj.isMultiSelect == it.isMultiSelect)) {
                                    duplicate = true;
                                }
                            }
                        } else if (Constants.CustomFieldDefinitionStyleType."${obj.fieldType}".styleTypeName == it.styleType.styleTypeName) {
                            duplicate = true;
                        }
                    }
                    return duplicate;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    boolean equals(other) {
        other?.class == this.class && other.uuid == this.uuid
    }

}
