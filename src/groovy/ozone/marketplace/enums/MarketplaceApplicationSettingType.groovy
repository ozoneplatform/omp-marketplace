package ozone.marketplace.enums

import org.ozoneplatform.appconfig.server.domain.model.ApplicationSettingType

public enum MarketplaceApplicationSettingType implements ApplicationSettingType  {

	FRANCHISE_AFFILIATION("FRANCHISE_AFFILIATION"),
	FRANCHISE_STORE_COMMUNICATION("FRANCHISE_STORE_COMMUNICATION"),
	FEATURES("FEATURES"),
	BRANDING("BRANDING"),
	AFFILIATION("FRANCHISE_AFFILIATION"),
	ADDITIONAL_CONFIGURATION("ADDITIONAL_CONFIGURATION"),
	SCORECARD("SCORECARD"),
    THEMING("THEMING"),
	HIDDEN("HIDDEN"),
	LISTING("LISTING_MANAGEMENT"),
    // OP-1318
    USER_ACCOUNT_SETTINGS("USER_ACCOUNT_SETTINGS"),
    // OP-1072
    AUDITING("AUDITING"),
    NOTIFICATIONS("NOTIFICATIONS")

	MarketplaceApplicationSettingType(String description) {
		this.description = description;
	}
	
	private final String description
	

	public String getDescription() {
		return description
	}

}

