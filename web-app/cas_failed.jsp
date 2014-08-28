<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@ page import="java.lang.String" %>

<html>
<head>
    <title>Login to CAS failed!</title>
</head>

<body style="background:#3f6ee6 url(images/blue/wallpapers/blueGrad1920x1200.jpg) repeat left top;
	width: 100%;
	height: 100%;
	border: 0 none;
	position: relative;
	zoom:1;">
	
<div style="width:600px; margin-left:auto; margin-right:auto; margin-top:40px;">
	
<h1 style="text-align:center; width:100%; margin-bottom:2px; border-bottom: 1px solid #CCCCCC;">Login via CAS failed!</h1>

<span style="font-weight:bold">
	<br/><br/>
    Your CAS credentials were rejected.<br/><br/>
    Reason: <%= ((AuthenticationException) session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>
</span>
</div>

</body>
</html>