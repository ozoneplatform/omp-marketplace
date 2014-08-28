<div  class="widget_list_view_table">
	<g:each status="i" in="${serviceItemList}" var="item">
		<!-- renders items in a list -->
	     <div id="item_${item.id}">
			<g:set var="comingFromShort" value="true" scope="request"/>
			<div id="item_badge_${item.id}" class="widget_list_item" data-id="${item.id}">
				<%-- added score to invalidate cache on scorecard change --%>
			    <myui:serviceItemBadge template="/serviceItem/badge/item_mini_badge_list_view" model="[item: item, model_param_getUpdaterContentArrayMethod:'getShortListingApproveRejectUpdaterContentArray']" isWidget="${params.widget ?: false}" isAdmin="${session.isAdmin}" isOwner="${item?.isAuthor(session.username) }"/>
			    <br>
			</div>
	     </div>
	</g:each>
</div>