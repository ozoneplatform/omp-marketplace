import ozone.utils.Utils
import static org.apache.commons.lang.StringEscapeUtils.escapeSql

//Creates a mysql script for inserting some sample custom fields. To generate the script,
//execute grails run-script resources/CreateSampleCf.groovy

new File('sampleCustomFields.sql').withWriter { out ->

    def writeCfd = { cfd ->
        out.writeLine "INSERT INTO custom_field_definition (name, label, tooltip, description, " +
                      "section, is_required, all_types, style_type, uuid, version) VALUES (${cfd}, 0);"
    }

    def writeStyleCfd = { uuid, style ->
        out.writeLine "INSERT INTO ${style}_cfd SELECT id FROM custom_field_definition WHERE " +
                      "uuid = '${uuid}';"
    }

    def sqlStringify = { cfd ->
        "'${escapeSql(cfd.name)}', '${escapeSql(cfd.label)}', '${escapeSql(cfd.tool)}', " +
        "'${escapeSql(cfd.desc)}', '${escapeSql(cfd.section)}', ${cfd.required}, ${cfd.all}"
    }

    [[name: 'Alternate POC', label: 'Alternate POC', tool: 'Alternate POC',
      desc: 'Alternate POC', section: 'primaryCharacteristics', required: 'true', all: 'true'],

      [name: 'EC_SupportGroup', label: 'Support Group/POC', tool: "Name of the widget's support structure?",
      desc: "Entrance Criteria: Name of the POC or Group for support to the widget in case required.",
      section: 'primaryCharacteristics', required: 'true', all: 'true'],

     [name: 'Technical POC', label: 'Technical POC', tool: 'Technical POC', desc: 'Technical POC for the listing.',
      section: 'primaryCharacteristics', required: 'true', all: 'true']

    ].collect { sqlStringify it }
     .each {
         String uuid = Utils.generateUUID()
         writeCfd "${it}, 'TEXT', '${uuid}'"
         writeStyleCfd(uuid, 'text')
    }

    [[name: 'Alternate POC Info', label: 'Alternate POC Info', tool: 'Alternate POC Info',
      desc: 'Alternate POC Info', section: 'primaryCharacteristics', required: 'true', all: 'true'],

     [name: 'EC_SupportGroupContact', label: 'Support POC Info', tool: 'Contact info for widget support',
      desc: 'Entrance Criteria: Phone and email to contact support in case of issues or questions with the widget.',
      section: 'primaryCharacteristics', required: 'true', all: 'true'],

     [name: 'Tags', label: 'Tags', tool: 'Allows user to assign custom tags to a listing.', desc: 'Tags for defining listings.',
      section: 'typeProperties', required: 'false', all: 'true'],

     [name: 'Technical POC Info', label: 'Technical POC Info', tool: 'Technical POC Info',
      desc: 'Contact information for the Technical POC.', section: 'primaryCharacteristics', required: 'true', all: 'true']

    ].collect { sqlStringify it }
     .each {
         String uuid = Utils.generateUUID()
         writeCfd "${it}, 'TEXT_AREA', '${uuid}'"
         writeStyleCfd(uuid, 'text_area')
    }
}
