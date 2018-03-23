
package marketplace

import grails.test.hibernate.HibernateSpec
import grails.test.mixin.TestFor
import marketplace.grails.domain.DomainConstraintsUnitTest
import org.grails.web.json.JSONObject
import org.junit.Test
import org.spockframework.compiler.model.Spec
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

import marketplace.testutil.FakeAuditTrailHelper

//@TestFor(Agency)
class AgencySpec  extends Specification implements DomainConstraintsUnitTest<Agency> {

    List<Class> getDomainClasses() {[Agency]}
    Closure doWithSpring() {{ ->  auditTrailHelper(FakeAuditTrailHelper) { bean -> bean.autowire = true}}}
    def agencyMock
//    Class[] getDomainClassesToMock() {
//        [Agency]
//    }

    void setup() {
        //FakeAuditTrailHelper.install()
        agencyMock =  Mock(Agency)
    }

    //@Test
    void testToStringIsIdAndTitle(){
        setup:
        def agency = new Agency(title: "Agency Title")
        //def agency = new Agency(title: "Agency Title")
        agency.id = 1000
        when:
        def agencyStr = agency.toString()

        then:
        agencyStr == "1000 : Agency Title"
    }


	//@Test
	//This test seems silly but the class needs this method because this class is treated like the other type domain objects (types, states, categories etc)
	//which have this field.  We should really have an abstract class below these type domain objects.
    void testGetDescriptionReturnsNull(){
         expect:
         new Agency().getDescription() == null
	}


	//@Test
	public void testAsJSON(){
		given:
        Agency agency = new Agency(title: "Title", iconUrl: "iconUrl.png")
        agency.id = 1

        when:
		JSONObject json = agency.asJSON()

        then:
        agency.id == json.id
        agency.title == json.title
        agency.iconUrl == json.iconUrl
	}

    //@Test
    void testCaseInsensitiveAgencies() {
        when:
        def agency1 = new Agency(title: "Agency 1", iconUrl: "https://www.owfgoss.org/example")

        then:
        agency1.validate()
        and:
        agency1.save()

        and:
        Agency.count() == old(Agency.count()) + 1

        when:
        def agencyFail = new Agency(title: "agency 1", iconUrl: "https://www.owfgoss.org/example")

        then:
        !agencyFail.validate(['title'])

        and:
        !agencyFail.save()
        Agency.count() == old(Agency.count())
    }
}
