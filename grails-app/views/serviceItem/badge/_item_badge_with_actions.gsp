<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="org.apache.commons.lang.WordUtils" %>
<table id="item_table_${item.id}" class="item_table">
  <tr>

    <!-- Thumbnail -->
    <td id="item_left_${item.id}" class="item_left">
      <g:link controller="serviceItem" action="show" id="${item.id}" params="[accessType:session.accessType,sorttype:customList]" class="quickview-link" data-id="${item.id}">
          <myui:itemIcon item="${item}" clss="listing_profile_table_thumbnail" width="96" height="96"/>
      </g:link>

    </td>
    <td id="item_center_${item.id}" class="item_center">
      <div class="item_title">

        <!-- Item Title -->
          <g:link controller="serviceItem" action="show" id="${item.id}" title="${item?.title?.encodeAsHTML()}" params="[accessType:session.accessType,sorttype:customList]" class="quickview-link" data-id="${item.id}">
			<span class="listing_short_title_text">${item?.title?.encodeAsHTML()}</span>
          </g:link>
	  </div>

	  <div class="item_agency_block" style="padding-bottom:5px;">
		  <!-- agency -->
	      <div class="agency" id="agency_${item.id}">
			<g:render template="/serviceItem/agency_label" var="serviceItem" bean="${item}"/>
		  </div>
	 </div>

	 <div class="item_listing_meta_block">
	 	  <!-- Item Type -->
	      <div class="item_type item_text"><span class="item_bold"><g:message code="label.type" encodeAs="HTML" /></span> ${item?.types?.title?.encodeAsHTML()}</div>

	  	  <!-- Item Categories -->
	      <div class="item_categories_block item_text">
	        <span class="item_bold"><span class="item_bold"><g:message code="label.categories" encodeAs="HTML" /></span></span>
            <span class="item_category_text">
                <g:join in="${item.categories*.title*.encodeAsHTML()}" delimiter="${message(code:'item.separator')} "/>
            </span>
	      </div>

	    <div class="clear"></div>

	    <!-- Item Version & State -->
	  	<div class="item_version_state_block">
			<!-- Item Version -->
			<div class="item_version"><span class="item_bold"><g:message code="label.version" encodeAs="HTML" /></span> ${item?.versionName?.encodeAsHTML()},</div>
			<!-- Item State -->
			<div class="item_state"><span id="disableEnableStateOrStatus-${item.id}">${WordUtils.capitalize(item?.state?.title?.encodeAsHTML()?.toLowerCase())}<g:if test="${item.isHidden == 1}"> / <g:message code="label.disabled"  encodeAs="HTML" /></g:if></span></div>
			<div class="clear"></div>
		</div>
		<!-- Item Release Date -->
		<div class="item_released"><span class="item_bold"><g:message code="label.released"/></span> <g:formatDate format="MM/dd/yyyy" date="${item.releaseDate}" encodeAs="HTML" /></div>
	  	<!-- Last Edited Date i.e. Date the status was set -->
      	<div class="item_modified"><span class="item_bold"><g:message code="label.modified" default="Modified"/></span> <g:formatDate format="MM/dd/yyyy" date="${item.editedDate}" encodeAs="HTML" /></div>

      	<div class="clear"></div>
	  </div>

	  <g:if test="${!item.statApproved()}">
	  <!--  Item Status -->
	  <div class="item_status">
		<g:if test="${item.statInProgress()}">
			<span class="omp_status_image  omp_status_in_progress"></span><span class="omp_status_text"><g:message code="status.inProgress" default="In Progress" encodeAs="HTML" /></span>
		</g:if>
		<g:else>
			<g:if test="${item.statPending()}">
				<span class="omp_status_image  omp_status_pending"></span><span class="omp_status_text"><g:message code="status.pending" default="Pending" encodeAs="HTML" /></span>
			</g:if>
			<g:else>
				<g:if test="${item.statRejected()}">
					<span class="omp_status_image  omp_status_rejected"></span><span class="omp_status_text"><g:message code="status.rejected" default="Rejected" encodeAs="HTML" /></span>
				</g:if>
			</g:else>
		</g:else>
     </div>
     </g:if>

      <!-- Item Description -->
      <div id="itemDescBlock_${item.id}" class="item_description_block item_text">
       <myui:itemDescBlock item="${item}" truncateAt="500" lineCount="6"/>
      </div>



	   <!-- Docs and Links -->
	   <g:if test="${session.username && comingFromShort != 'true'}">
	      <div class="item_docs_links_block">
			  <g:if test="${item.installUrl}">
				<g:if test="${item.docUrls}">
				  <g:set var="sepClass" value="first"/>
				</g:if>
				<g:else>
				  <g:set var="sepClass" value=""/>
				</g:else>
				<span class="item_docks_links">
				  <a target="top" href="${item?.installUrl?.encodeAsHTML()}" class="${sepClass}">Install Instructions</a>
				</span>
			  </g:if>
			  <g:if test="${item.docUrls}">
				<span class="item_docks_links">
				  <a target="top" href="${item?.docUrls?.url[0]?.encodeAsHTML()}" class="">Documentation</a>
				</span>
			  </g:if>
			</div>
		</div>
	</g:if>

    </td>
    <td id="item_right_${item.id}" class="item_right">
      <div class="rating" id="rating_${item.id}">
	    <g:render template="/serviceItem/rating" var="serviceItem" bean="${item}" />
      </div>
      <div class="numRatings_comment_text"><g:if test="${item.totalComments == 1}">${item.totalComments} comment</g:if><g:else>${item.totalComments} comments</g:else></div>
    </td>
  </tr>
</table>
