package ozone.banner;

import java.io.InputStream;
import java.util.List;

public interface BannerBean {
	
	public List<String> getCssContents();
	
	public List<String> getJsContents();
	
	public InputStream getBannerBeanUINorthAsStream();
	
	public InputStream getBannerBeanUISouthAsStream();
}
