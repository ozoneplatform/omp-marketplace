
package marketplace;

import org.junit.Test
import ozone.banner.MP_BannerBean
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat

class MP_BannerBeanSpec extends Specification {


	void testHasBanner() {
		
		when:
		MP_BannerBean bean = new MP_BannerBean()
		then:
		!bean.hasBanner()

		when:
		bean.header = "FOO BAR BAZ"
		then:
		bean.hasBanner()

		when:
		bean.header = null
		bean.footer = "FOO BAR BAZ"		
		then:
		bean.hasBanner()
	}

}
