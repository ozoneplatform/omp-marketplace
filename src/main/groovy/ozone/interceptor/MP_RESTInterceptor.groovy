package ozone.interceptor

import java.io.IOException
import java.util.Map

import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

public class MP_RESTInterceptor implements RESTInterceptor {

	String configMessage
	
	@Override
	public Map processIncoming(Map json) {
		return [continueProcessing : true, message: "JSON is Valid"]
	}

	@Override
	public void decorateOutgoing(Map json) {
		//no change to Outgoing JSON
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
			//Set attributes as necessary for arg0
			arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
