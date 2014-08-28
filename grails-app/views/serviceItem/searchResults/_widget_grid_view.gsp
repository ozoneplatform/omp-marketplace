<div class="widget_grid_view_table">
	<common:convertListToGrid listSize="6" elementList="${serviceItemList}" var="gridRowLists">
	    <table class="widget_grid_view_table">
		    <g:each in="${gridRowLists}" var="row">
			      <tr>
				      <g:each status="i" in="${row}" var="item">
					  		<g:render template="/serviceItem/badge/item_mini_badge_block_view" var="si" bean="${item}"  /> 		
				      </g:each>
		      	  </tr>
	    	</g:each>
	    </table>
	</common:convertListToGrid>
</div>