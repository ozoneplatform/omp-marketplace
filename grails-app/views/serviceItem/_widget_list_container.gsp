<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<g:render template="/serviceItem/widget_list" model="['serviceItemList':serviceItemList, 'listSize':listSize, 'nuggets': nuggets]" />

<script type="text/javascript" >
	jQuery(document).ready(function() {
		Marketplace.applyAutoEllipsis(".listing_short_title_text", 32);
	}); 
</script>