package marketplace.dataexchange

import grails.testing.gorm.DataTest
import marketplace.*
import org.grails.web.json.JSONObject
import ozone.marketplace.dataexchange.CustomFieldDefImporter
import spock.lang.Specification

import java.text.SimpleDateFormat

class CustomFieldDefImporterTest extends Specification implements DataTest{
    CustomFieldDefImporter customFieldImporter
    final String futureDate =  "2051-10-21T04:29:00Z" // Back to the future!!
    final String oldDate =  "1970-01-01T06:00:00Z"
    def dateFormat

    void setup() {
//        FakeAuditTrailHelper.install()

        mockDomain(FieldValue)
        mockDomain(TextCustomFieldDefinition)
        mockDomain(TextAreaCustomFieldDefinition)
        mockDomain(CheckBoxCustomFieldDefinition)
        mockDomain(DropDownCustomFieldDefinition)
        mockDomain(CustomFieldDefinition)
        customFieldImporter = new CustomFieldDefImporter()
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    void testImportNewCustomTextField() {
        when:
        def json = new JSONObject([
                uuid: "1",
                editedDate: "2005-01-31T01:00:00",
                fieldType: "TEXT",
                name: "thename",
                label: "Text Label",
                tooltip: "The tooltip",
                description: "The description",
                isRequired: false,
                section: "primaryCharacteristics",
                allTypes: false,
                isPermanent: false,
                types: []
        ])

        ImportStatus stats = new ImportStatus()
        then:
        0 == TextCustomFieldDefinition.count()
        
        when:
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        0 == stats.customFieldDefs.failed
        1 == stats.customFieldDefs.created
        0 == stats.customFieldDefs.updated
        0 == stats.customFieldDefs.notUpdated

        1 == TextCustomFieldDefinition.count()
        assertJsonEquals(json, TextCustomFieldDefinition.findByUuid(json.uuid))
    }

    void testDoNotImportPermanentCustomTextField() {
        when:
        def json = new JSONObject([
                uuid: "1",
                editedDate: futureDate,
                fieldType: "TEXT",
                name: "thename",
                label: "Text Label",
                tooltip: "The tooltip",
                description: "The description",
                isRequired: false,
                section: "primaryCharacteristics",
                allTypes: false,
                types: []
        ])

        def existingLabel = "Existing label"
        TextCustomFieldDefinition existingDef = new TextCustomFieldDefinition(
                uuid: json.uuid,
                editedDate: dateFormat.parse(oldDate),
                name: json.name,
                label: existingLabel,
                isPermanent: true
        )
        existingDef.save(failOnError: true, flush: true)

        ImportStatus stats = new ImportStatus()
        then:
        1 == TextCustomFieldDefinition.count()
        
        when:
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        0 == stats.customFieldDefs.failed
        0 == stats.customFieldDefs.created
        0 == stats.customFieldDefs.updated
        1 == stats.customFieldDefs.notUpdated

        1 == TextCustomFieldDefinition.count()
        existingLabel == TextCustomFieldDefinition.findByUuid(json.uuid).label
    }

    void testImportNewCustomTextAreaField() {
        when:
        def json = new JSONObject([
                uuid: "1",
                editedDate: "2005-01-31T01:00:00",
                fieldType: "TEXT_AREA",
                name: "thename",
                label: "Text Label",
                tooltip: "The tooltip",
                description: "The description",
                isRequired: false,
                section: "primaryCharacteristics",
                allTypes: false,
                isPermanent: false,
                types: []
        ])

        ImportStatus stats = new ImportStatus()
        then:
        0 == TextCustomFieldDefinition.count()
        
        when:
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        0 == stats.customFieldDefs.failed
        1 == stats.customFieldDefs.created
        0 == stats.customFieldDefs.updated
        0 == stats.customFieldDefs.notUpdated

        1 == TextAreaCustomFieldDefinition.count()
        assertJsonEquals(json, TextAreaCustomFieldDefinition.findByUuid(json.uuid))
    }

    void testImportNewCustomCheckBox() {
        when:
        def json = new JSONObject([
                uuid: "1",
                editedDate: "2005-01-31T01:00:00",
                fieldType: "CHECK_BOX",
                name: "thename",
                label: "Text Label",
                tooltip: "The tooltip",
                description: "The description",
                isRequired: false,
                section: "primaryCharacteristics",
                allTypes: false,
                isPermanent: false,
                types: [],
                selectedByDefault: true
        ])

        ImportStatus stats = new ImportStatus()
        then:
        0 == CheckBoxCustomFieldDefinition.count()
        
        when:
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        0 == stats.customFieldDefs.failed
        1 == stats.customFieldDefs.created
        0 == stats.customFieldDefs.updated
        0 == stats.customFieldDefs.notUpdated

        1 == CheckBoxCustomFieldDefinition.count()
        assertJsonEquals(json, CheckBoxCustomFieldDefinition.findByUuid(json.uuid))
        json.selectedByDefault == CheckBoxCustomFieldDefinition.findByUuid(json.uuid).selectedByDefault
    }

    void testImportNewCustomDropDown() {
        when:
        JSONObject json = dropDownJSON()

        ImportStatus stats = new ImportStatus()
        then:
        0 == DropDownCustomFieldDefinition.count()

        when:
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        0 == stats.customFieldDefs.failed
        1 == stats.customFieldDefs.created
        0 == stats.customFieldDefs.updated
        0 == stats.customFieldDefs.notUpdated

        1 == DropDownCustomFieldDefinition.count()
        assertJsonEquals(json, DropDownCustomFieldDefinition.findByUuid(json.uuid))
        json.isMultiSelect == DropDownCustomFieldDefinition.findByUuid(json.uuid).isMultiSelect
        json.fieldValues.size() == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues.size()
        json.fieldValues[0].displayText == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].displayText
        json.fieldValues[0].isEnabled == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].isEnabled
        json.fieldValues[1].displayText == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].displayText
        json.fieldValues[1].isEnabled == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].isEnabled
    }

    void testImportUpdatedCustomDropDown() {
        when:
        JSONObject json = dropDownJSON()
        json.editedDate = futureDate

        DropDownCustomFieldDefinition existingDef = new DropDownCustomFieldDefinition(
                uuid: json.uuid,
                name: json.name,
                label: json.label
        )
        existingDef.addToFieldValues(new FieldValue(
            displayText: json.fieldValues[0].displayText,
            isEnabled: json.fieldValues[0].isEnabled,
            createdDate: new Date(),
            editedDate: new Date()
        ))
        // omit second field value
        existingDef.save(failOnError: true, flush: true)

        ImportStatus stats = new ImportStatus()
        then:
        1 == DropDownCustomFieldDefinition.count()

        when:
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        0 == stats.customFieldDefs.failed
        0 == stats.customFieldDefs.created
        1 == stats.customFieldDefs.updated
        0 == stats.customFieldDefs.notUpdated

        1 == DropDownCustomFieldDefinition.count()
        assertJsonEquals(json, DropDownCustomFieldDefinition.findByUuid(json.uuid))
        json.isMultiSelect == DropDownCustomFieldDefinition.findByUuid(json.uuid).isMultiSelect
        json.fieldValues.size() == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues.size()
        json.fieldValues[0].displayText == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].displayText
        json.fieldValues[0].isEnabled == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].isEnabled
        json.fieldValues[1].displayText == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].displayText
        json.fieldValues[1].isEnabled == DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].isEnabled
    }

    void testDoNotImportInvalidFieldType() {
        when:
        JSONObject json = new JSONObject([
                uuid: "1",
                fieldType: "INVALID"
        ])
        ImportStatus stats = new ImportStatus()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        then:
        1 == stats.customFieldDefs.failed
        0 == stats.customFieldDefs.created
        0 == stats.customFieldDefs.updated
        0 == stats.customFieldDefs.notUpdated
    }

    private JSONObject dropDownJSON() {
        return new JSONObject([
                uuid: "1",
                editedDate: "2005-01-31T01:00:00",
                fieldType: "DROP_DOWN",
                name: "thename",
                label: "Combo Label",
                tooltip: "The tooltip",
                description: "The description",
                isRequired: false,
                section: "primaryCharacteristics",
                allTypes: false,
                isPermanent: false,
                types: [],
                isMultiSelect: true,
                fieldValues: [
                        [
                                displayText: "First option",
                                isEnabled: 1
                        ],
                        [
                                displayText: "Second option",
                                isEnabled: 0
                        ]
                ]
        ])
    }

    private void assertJsonEquals(def json, def customFieldDef) {
        assert json.fieldType == customFieldDef.styleType.name()
        assert json.name == customFieldDef.name
        assert json.label == customFieldDef.label
        assert json.description == customFieldDef.description
        assert json.tooltip == customFieldDef.tooltip
        assert json.isRequired == customFieldDef.isRequired
        assert json.section == customFieldDef.section.name()
        assert json.allTypes== customFieldDef.allTypes
        assert json.isPermanent == customFieldDef.isPermanent
        assert customFieldDef.types == null
    }

}
