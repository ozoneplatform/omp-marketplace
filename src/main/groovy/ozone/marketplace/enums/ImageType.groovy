package ozone.marketplace.enums

/**
 *
 * @author rbonefas
 */
public enum ImageType {

    SERVICEITEM("ServiceItem"),
	AVATAR("Avatar"),
	MARKETPLACE_APP("MarketplaceApp"),
	TYPES("Types")
        
	ImageType(String description) {
		this.description = description;
	}
	private final String description
        String toString(){ description }
        String prettyPrint() { toString() }
	public String description() { return description }
}

