package marketplace

import org.grails.web.json.JSONObject


class ExtServiceItem extends ServiceItem implements ToJSON {

    String systemUri
    String externalId
    String externalViewUrl
    String externalEditUrl

	static constraints = {
		systemUri(nullable: false, maxSize:256)

        externalId(unique:'systemUri', nullable: true, maxSize:256)

		externalViewUrl(nullable:true, maxSize: Constants.MAX_URL_SIZE, validator:{
			val, obj ->
			// lets make sure it's an http or https URL here
			if(val != null && val.trim().size() > 0 && !val.matches(/http(s*)(:\/\/)(.*)/)) {
				return ['externalContext.externalViewUrl.url.invalid']
			}
		})
		externalEditUrl(nullable:true, maxSize: Constants.MAX_URL_SIZE, validator:{
			val, obj ->
			// lets make sure it's an http or https URL here
			if(val != null && val.trim().size() > 0 && !val.matches(/http(s*)(:\/\/)(.*)/)) {
				return ['externalContext.externalViewUrl.url.invalid']
			}
		})
	}
	static mapping = {
		cache true
	}

	String toString(){
		return "${super.toString()} || ${systemUri}, ${externalId}, ${externalViewUrl}, ${externalEditUrl}"
	}

	@Override
	JSONObject asJSON() {
		def json = super.asJSON()
		json.putAll([systemUri      : systemUri,
					 externalId     : externalId,
					 externalViewUrl: externalViewUrl,
					 externalEditUrl: externalEditUrl,
					 id             : id])
		json
	}

	String propertyMissing(String name) {
		super.getCustomFieldValue(name)
	}
}
