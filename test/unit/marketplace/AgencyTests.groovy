
package marketplace

import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject
import org.junit.Test

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import marketplace.testutil.FakeAuditTrailHelper

@TestFor(Agency)
class AgencyTests {

    void setUp() {
        FakeAuditTrailHelper.install()
    }

    @Test
    public void testToStringIsIdAndTitle(){
        def agency = new Agency(title: "Agency Title")
        agency.id = 1000
        assert agency.toString() == "1000 : Agency Title"
    }


	@Test
	//This test seems silly but the class needs this method because this class is treated like the other type domain objects (types, states, categories etc)
	//which have this field.  We should really have an abstract class below these type domain objects.
	public void testGetDescriptionReturnsNull(){
		assert new Agency().getDescription() == null
	}


	@Test
	public void testAsJSON(){
		Agency agency = new Agency(title: "Title", iconUrl: "iconUrl.png")
        agency.id = 1


		JSONObject json = agency.asJSON()

		assert agency.id == json.id
		assert agency.title == json.title
		assert agency.iconUrl == json.iconUrl
	}

    @Test
    public void testCaseInsensitiveAgencies() {
        def agency1 = new Agency([
                title: 'Agency 1',
                iconUrl: 'https://www.owfgoss.org/example'
        ])
        def agency2 = new Agency([
                title: 'Agency 2',
                iconUrl: 'https://www.owfgoss.org/example'
        ])

        mockDomain(Agency.class, [agency1, agency2])

        def agency = new Agency([
                title: 'agency 1',
                iconUrl: 'https://www.owfgoss.org/example'
        ])
        assert !agency.validate()
    }
}
