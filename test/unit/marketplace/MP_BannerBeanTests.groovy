
package marketplace;

import org.junit.Test
import ozone.banner.MP_BannerBean
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat

class MP_BannerBeanTests {

	@Test
	public void testHasBanner() {		
		
		MP_BannerBean bean = new MP_BannerBean()
		assertThat(bean.hasBanner(), is(equalTo(false)))

		bean.header = "FOO BAR BAZ"
		assertThat(bean.hasBanner(), is(equalTo(true)))

		
		bean.header = null
		bean.footer = "FOO BAR BAZ"		
		assertThat(bean.hasBanner(), is(equalTo(true)))
		
	}

}
