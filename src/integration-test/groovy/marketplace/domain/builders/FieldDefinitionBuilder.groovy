package marketplace.domain.builders

import marketplace.Constants
import marketplace.CustomFieldDefinition


class FieldDefinitionBuilder implements Builder<CustomFieldDefinition> {

    static final def TEXT = Constants.CustomFieldDefinitionStyleType.TEXT
    static final def TEXT_AREA = Constants.CustomFieldDefinitionStyleType.TEXT_AREA
    static final def DROP_DOWN = Constants.CustomFieldDefinitionStyleType.DROP_DOWN
    static final def IMAGE_URL = Constants.CustomFieldDefinitionStyleType.IMAGE_URL
    static final def CHECK_BOX = Constants.CustomFieldDefinitionStyleType.CHECK_BOX

    String uuid
    String name
    String label
    Constants.CustomFieldDefinitionStyleType styleType

    CustomFieldDefinition build() {
        if (!uuid) uuid = UUID.randomUUID().toString()
        if (!label) label = 'Test Label'
        if (!styleType) styleType = TEXT

        new CustomFieldDefinition([uuid     : uuid,
                                   name     : name,
                                   label    : label,
                                   styleType: styleType])
    }

}
