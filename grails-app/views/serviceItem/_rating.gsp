<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<div class="numRatings_star_div">
	<g:render template="/serviceItem/rating_stars" model="[serviceItem: serviceItem]"/>
	<g:if test="${!Boolean.valueOf(useUserRating)}">
		<span class="rating_total_votes"> (${serviceItem.totalVotes}) </span>
	</g:if>
</div>
