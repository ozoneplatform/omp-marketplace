package ozone.marketplace.dataexchange

import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import marketplace.CheckBoxCustomFieldDefinition
import marketplace.CustomFieldDefinition
import marketplace.DropDownCustomFieldDefinition
import marketplace.FieldValue
import marketplace.ImportStatus
import marketplace.TextAreaCustomFieldDefinition
import marketplace.TextCustomFieldDefinition
import org.codehaus.groovy.grails.web.json.JSONObject

import marketplace.testutil.FakeAuditTrailHelper

import java.text.SimpleDateFormat


@TestMixin(DomainClassUnitTestMixin)
class CustomFieldDefImporterTest {
    CustomFieldDefImporter customFieldImporter
    final String futureDate =  "2051-10-21T04:29:00Z" // Back to the future!!
    final String oldDate =  "1970-01-01T06:00:00Z"
    def dateFormat

    void setUp() {
        FakeAuditTrailHelper.install()

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
        assertEquals 0, TextCustomFieldDefinition.count()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 0, stats.customFieldDefs.failed
        assertEquals 1, stats.customFieldDefs.created
        assertEquals 0, stats.customFieldDefs.updated
        assertEquals 0, stats.customFieldDefs.notUpdated

        assertEquals 1, TextCustomFieldDefinition.count()
        assertJsonEquals(json, TextCustomFieldDefinition.findByUuid(json.uuid))
    }

    void testDoNotImportPermanentCustomTextField() {
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
        assertEquals 1, TextCustomFieldDefinition.count()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 0, stats.customFieldDefs.failed
        assertEquals 0, stats.customFieldDefs.created
        assertEquals 0, stats.customFieldDefs.updated
        assertEquals 1, stats.customFieldDefs.notUpdated

        assertEquals 1, TextCustomFieldDefinition.count()
        assertEquals existingLabel, TextCustomFieldDefinition.findByUuid(json.uuid).label
    }

    void testImportNewCustomTextAreaField() {
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
        assertEquals 0, TextCustomFieldDefinition.count()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 0, stats.customFieldDefs.failed
        assertEquals 1, stats.customFieldDefs.created
        assertEquals 0, stats.customFieldDefs.updated
        assertEquals 0, stats.customFieldDefs.notUpdated

        assertEquals 1, TextAreaCustomFieldDefinition.count()
        assertJsonEquals(json, TextAreaCustomFieldDefinition.findByUuid(json.uuid))
    }

    void testImportNewCustomCheckBox() {
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
        assertEquals 0, CheckBoxCustomFieldDefinition.count()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 0, stats.customFieldDefs.failed
        assertEquals 1, stats.customFieldDefs.created
        assertEquals 0, stats.customFieldDefs.updated
        assertEquals 0, stats.customFieldDefs.notUpdated

        assertEquals 1, CheckBoxCustomFieldDefinition.count()
        assertJsonEquals(json, CheckBoxCustomFieldDefinition.findByUuid(json.uuid))
        assertEquals json.selectedByDefault, CheckBoxCustomFieldDefinition.findByUuid(json.uuid).selectedByDefault
    }

    void testImportNewCustomDropDown() {
        JSONObject json = dropDownJSON()

        ImportStatus stats = new ImportStatus()
        assertEquals 0, DropDownCustomFieldDefinition.count()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 0, stats.customFieldDefs.failed
        assertEquals 1, stats.customFieldDefs.created
        assertEquals 0, stats.customFieldDefs.updated
        assertEquals 0, stats.customFieldDefs.notUpdated

        assertEquals 1, DropDownCustomFieldDefinition.count()
        assertJsonEquals(json, DropDownCustomFieldDefinition.findByUuid(json.uuid))
        assertEquals json.isMultiSelect, DropDownCustomFieldDefinition.findByUuid(json.uuid).isMultiSelect
        assertEquals json.fieldValues.size(), DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues.size()
        assertEquals json.fieldValues[0].displayText, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].displayText
        assertEquals json.fieldValues[0].isEnabled, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].isEnabled
        assertEquals json.fieldValues[1].displayText, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].displayText
        assertEquals json.fieldValues[1].isEnabled, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].isEnabled
    }

    void testImportUpdatedCustomDropDown() {
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
        assertEquals 1, DropDownCustomFieldDefinition.count()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 0, stats.customFieldDefs.failed
        assertEquals 0, stats.customFieldDefs.created
        assertEquals 1, stats.customFieldDefs.updated
        assertEquals 0, stats.customFieldDefs.notUpdated

        assertEquals 1, DropDownCustomFieldDefinition.count()
        assertJsonEquals(json, DropDownCustomFieldDefinition.findByUuid(json.uuid))
        assertEquals json.isMultiSelect, DropDownCustomFieldDefinition.findByUuid(json.uuid).isMultiSelect
        assertEquals json.fieldValues.size(), DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues.size()
        assertEquals json.fieldValues[0].displayText, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].displayText
        assertEquals json.fieldValues[0].isEnabled, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[0].isEnabled
        assertEquals json.fieldValues[1].displayText, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].displayText
        assertEquals json.fieldValues[1].isEnabled, DropDownCustomFieldDefinition.findByUuid(json.uuid).fieldValues[1].isEnabled
    }

    void testDoNotImportInvalidFieldType() {
        JSONObject json = new JSONObject([
                uuid: "1",
                fieldType: "INVALID"
        ])
        ImportStatus stats = new ImportStatus()
        customFieldImporter.importFromJSON(json, stats.customFieldDefs)
        assertEquals 1, stats.customFieldDefs.failed
        assertEquals 0, stats.customFieldDefs.created
        assertEquals 0, stats.customFieldDefs.updated
        assertEquals 0, stats.customFieldDefs.notUpdated
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
        assertEquals json.fieldType, customFieldDef.styleType.name()
        assertEquals json.name, customFieldDef.name
        assertEquals json.label, customFieldDef.label
        assertEquals json.description, customFieldDef.description
        assertEquals json.tooltip, customFieldDef.tooltip
        assertEquals json.isRequired, customFieldDef.isRequired
        assertEquals json.section, customFieldDef.section.name()
        assertEquals json.allTypes, customFieldDef.allTypes
        assertEquals json.isPermanent, customFieldDef.isPermanent
        assertNull customFieldDef.types
    }

}
