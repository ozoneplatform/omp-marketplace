package marketplace

import grails.test.mixin.TestFor

@TestFor(UserDomainInstance)
class UserDomainInstanceTests {

	void testSelectedTheme() {
		def selectedTheme = "blueAndGold"

		def preferencesMap = [ (UserDomainInstance.SELECTED_THEME):selectedTheme ]
		def u1 = new UserDomainInstance ( username: "foo", preferences: preferencesMap )
		assertEquals selectedTheme, u1.getTheme()
	}

	void testGetThemeUndefined() {
		def preferencesMap = [ "keybar":"valFoo" ]
		def u1 = new UserDomainInstance ( username: "foo", preferences: preferencesMap)
		assertNull u1.getTheme()
	}
}
