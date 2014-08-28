<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="org.apache.commons.lang.WordUtils" %>
<table id="item_table_${item.id}" class="widget_item_table">
  <tr>

    <!-- Thumbnail -->
    <td id="item_left_${item.id}" class="widget_item_left">
      <a href="#quickview/${item.id}">
          <myui:itemIcon item="${item}" clss="listing_profile_table_thumbnail franchise_list_search_result_icon" width="96" height="96"/>
      </a>
    </td>

    <!-- Listing name, agency badge, description -->
    <td id="item_center_${item.id}" class="widget_item_center">
      <div class="item_title">

        <!-- Item Title -->
          <a href="#quickview/${item.id}">
			<span class="listing_short_title_text">${item?.title?.encodeAsHTML()}</span>
          </a>
	  </div>

      <!-- agency -->
      <div class="agency" id="agency_${item.id}">
        <g:render template="/serviceItem/agency_label" var="serviceItem" bean="${item}"/>
      </div>

      <!-- Item Description -->
      <div id="itemDescBlock_${item.id}" class="item_description_block item_text">
        <common:truncateText text="${item?.description}" truncateAt="300" lineCount="8" var="text">
      		<g:if test="${item?.description.equals(text.toString())}">
      			${text?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
      		</g:if>
      		<g:else>
      			${text?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
      			<!-- 'read more' link -->

		    	<a href="#quickview/${item.id}">
		          Read more
		        </a>

      		</g:else>
     	</common:truncateText>
      </div>

    </td>
    <td id="item_right_${item.id}" class="widget_item_right">
      <div class="rating" id="rating_${item.id}">
	    <g:render template="/serviceItem/rating" var="serviceItem" bean="${item}" />
      </div>
    </td>
  </tr>
</table>
