package marketplace.grails.domain

import marketplace.UserDomainInstance
import spock.lang.Specification

class UserDomainInstanceSpec extends Specification {

	void testSelectedTheme() {
		setup:
		def selectedTheme = "blueAndGold"

		def preferencesMap = [ (UserDomainInstance.SELECTED_THEME):selectedTheme ]
		def u1 = new UserDomainInstance ( username: "foo", preferences: preferencesMap )
		expect:
		assert selectedTheme == u1.getTheme()
	}

	void testGetThemeUndefined() {
		setup:
		def preferencesMap = [ "keybar":"valFoo" ]
		def u1 = new UserDomainInstance ( username: "foo", preferences: preferencesMap)

		expect:
		assert u1.getTheme() == null
	}
}
