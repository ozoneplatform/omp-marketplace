
<div style="height: 200px; width: auto; text-align: center; padding: 0px 0px 0px 5px">
	<div style="vertical-align: top;height: 225px;width: 30%;float: left; padding-left: 50px">
		<g:render template="/serviceItem/badge/item_mini_badge_block_view" var="si" bean="${best[0]}"  />
	</div>
	<div  style="padding-left: 4px;text-align: left;width: 55%;float: left;">
		<br/>
		<h1 style="font-weight: bold;">Highest Rated</h1>
		<div style="width: 100%;">
			<div>
	      		<div class="item_description_block item_text">
			        <common:truncateText text="${best[0]?.description}" truncateAt="500" lineCount="4" var="text">
			       		<g:if test="${item?.description.equals(text.toString())}">
			       			${text?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
			       		</g:if>
			       		<g:else>
			       			${text?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
			       			<!-- 'read more' link -->
						    <div class="read_more_link" data-id="${best[0].id}">
                                <a href="#quickview/${best[0].id}">Read more</a>
						    </div>
			       		</g:else>
			      	</common:truncateText>
			    </div>
			</div>
		</div>

	</div>
</div>
