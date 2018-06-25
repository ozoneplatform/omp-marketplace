package ozone.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

public interface RESTInterceptor extends Filter {
	
	
	/***
	 *
	 * @param json
	 * @return
	 */
	public Map processIncoming(Map json);

	/***
	 *
	 * @param json
	 * @return
	 */
	public void decorateOutgoing(Map json);
	
	
	/***
	 * Filter Related Items
	 */
	public void destroy();

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException;

	public void init(FilterConfig arg0) throws ServletException;
	
}
