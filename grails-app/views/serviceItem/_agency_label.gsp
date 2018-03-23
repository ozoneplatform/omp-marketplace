<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<%
	def store = serviceItem?.agency?.title;
	def storeIcon = serviceItem?.agency?.iconUrl;

%>

<g:if test="${store}">
    <img src="${storeIcon.encodeAsHTML()}" onerror="this.src='${request.contextPath}/public/themes/common/images/agency/agencyDefault.png';" title="${store.encodeAsHTML()}"
        class="agency-image"  id="agency_icon_${serviceItem?.id}"/>


	<common:truncateText text="${store}" truncateAt="12" var="title">
		<span class="agency_text" title="${store?.encodeAsHTML()}">${title?.encodeAsHTML()}</span>
	</common:truncateText>
</g:if>

