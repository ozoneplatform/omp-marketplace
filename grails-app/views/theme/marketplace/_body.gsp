<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="utils.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<div id="omp_body_wrapper_right">
	<!-- Main Content Area -->
	<div class="omp_main_panel" id="omp_main_panel">
		<g:if test="${comingFromLogout != 'true'}">
			<!-- Center Body Content Panel -->
			<g:layoutBody/>
		</g:if>			
		<g:else>
			<p class="message-logged-out"><g:message code="logout.text.msg" encodeAs="HTML" /></p>
		</g:else>
	</div>
</div>

<div class="clear"></div>