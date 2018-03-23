package ozone.marketplace.domain

import org.grails.web.json.JSONObject

class ThemeScreenshotDefinition implements Serializable {
	String url
	String description

	public ThemeScreenshotDefinition(JSONObject json) {
		this.url = json.get('url')
		this.description = json.get('description')
	}
}