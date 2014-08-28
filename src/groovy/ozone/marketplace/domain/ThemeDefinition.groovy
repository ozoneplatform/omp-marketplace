package ozone.marketplace.domain

import java.util.List

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject
import grails.converters.JSON

class ThemeDefinition implements Serializable {
	String name
	String display_name
	String author
	String contact_email
	String created_date
	String modified_date
	String description
	String css
	String base_url
	String thumb
	String contrast
	int font_size
	List screenshots
	
	public ThemeDefinition(JSONObject json) {
		this.name = json.get('name')
		this.display_name = json.get('display_name')
		this.author = json.get('author')
		this.contact_email = json.get('contact_email')
		this.created_date = json.get('created_date')
		this.modified_date = json.get('modified_date')
		this.description = json.get('description')
		this.css = json.get('css')
		this.base_url = json.get('base_url')
		this.thumb = json.get('thumb')
		this.contrast = json.get('contrast')
		this.font_size = json.getInt('font_size')
		this.screenshots = json.getJSONArray('screenshots').collect {
			new ThemeScreenshotDefinition(it)
		}
	}
	
	public String toString() {
		(this as JSON).toString()
	}
}
