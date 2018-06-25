package ozone.marketplace.enums

import org.apache.commons.lang.WordUtils

public enum CustomFieldSection {
	
	primaryCharacteristics(),
    technicalProperties(),
    typeProperties(),
    marketplaceReferences()

    CustomFieldSection(displayName = "", description = "") {
        this.displayName = displayName ?: convertToDisplayName(name())
        this.description = description ?: this.displayName
    }

    String displayName
    String description

    String toString() { displayName }
    String getKey() { name() }

    static String convertToDisplayName(String s) {

        return WordUtils.capitalize(
                s.replaceAll(
                    String.format("%s|%s|%s",
                            "(?<=[A-Z])(?=[A-Z][a-z])",
                            "(?<=[^A-Z])(?=[A-Z])",
                            "(?<=[A-Za-z])(?=[^A-Za-z])"),
                    " ")
        )
    }
}