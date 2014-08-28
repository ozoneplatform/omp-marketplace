<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<%
	def starRateVal = serviceItem.avgRate.round(2)
%>
<span class="rating_total_votes rating_star_group">
	<g:each var="idx" in="${(1..5)}">
	<% double halfStarLow = (idx - 1) + 0.25 %>
	<% double halfStarHigh = (idx - 1) + 0.99 %>


	  	  	<g:if test="${starRateVal > halfStarHigh}">
		      <!-- Full Star Case -->
		      <p:image src="activeStar.png" title="Average Rating: ${starRateVal}" id="read_only_rank_${serviceItem.id}.${idx}"/>
		    </g:if>
		    <g:else>
		      <g:if test="${((halfStarLow <= starRateVal) && (starRateVal <= halfStarHigh)) }">
		        <!-- Half Star Case -->
		        <p:image src="activeHalfStar.png" title="Average Rating: ${starRateVal}" id="read_only_rank_${serviceItem.id}.${idx}"/>
		      </g:if>
		      <g:else>
		        <p:image src="inActiveStar.png" title="Average Rating: ${starRateVal}" id="read_only_rank_${serviceItem.id}.${idx}"/>
		      </g:else>
		    </g:else>
		    <% def totalStarIdx = serviceItem."totalRate${idx}" %>
		    <div style="display: none;" id="rating_total${idx}_id_${serviceItem.id}">${totalStarIdx}</div>
	 </g:each>
	<div style="display: none;" id="rating_average_id_${serviceItem.id}">${serviceItem.avgRate.round(2)}</div>
</span>
