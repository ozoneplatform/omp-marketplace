package ozone.marketplace.enums

/**
 * Default types information used in test data generation.
 */
public enum DefinedDefaultTypes {
	
	SERVICE_REST("Service:REST", "services"),
	WEB_APP("Web App", "web app"),
	DESKTOP_APPS("Desktop Apps", "desktop apps"),
	WIDGET("Widget", "widgets"),
	APP_COMPONENT("App Component", "app component"),
	SERVICE_SOAP("Service:SOAP", "services"),
	PLUGIN("Plugin", "plugins"),
	MICROFORMATS("Microformats", "microformats"),
	AVATAR("Avatar", "avatar image"),
	SERVICEITEM_ICON("ServiceItemIcon", "serviceitem icon"),
	MARKETPLACE_ICON("MarketplaceIcon", "marketplace icon")
        
	DefinedDefaultTypes(String title, String description) {
		this.title = title
		this.description = description
	}
	private final String description
	private final String title
        String toString(){ title }
        String prettyPrint() { toString() }
	public String getDescription() { return description }
	public String getTitle() { return title }
}
