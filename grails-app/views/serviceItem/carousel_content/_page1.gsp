<div style="height: 268px; width: auto; text-align: center; padding: 0px 0px 0px 5px">
	<g:link controller="media" action='show'>
		<div class="carousel_getstarted">
			<div class="carousel_getstarted_text"></div>
		</div>
	</g:link>
	<div class="carousel_categories">
		<h1>Browse</h1>
		<div style="width: 100%;">
			<div class="carousel_category_browselink">
				<p:image src="carousel_content/all-listings.png" class="carousel_category_image"/>
				<g:link action="search" controller="serviceItem"
					params="[sort:'title',order:'asc',offset:0,accessType:session.accessType]">
				    <span class="carousel_category">All Listings</span>
				</g:link>
			</div>
			<div class="carousel_category_browselink">
				<p:image src="carousel_content/star-rating.png" class="carousel_category_image"/>
				<g:link action="search" controller="serviceItem"
					params="[sort:'avgRate',order:'desc',offset:0,accessType:session.accessType,status_any_checkbox:'on']">
					<span class="carousel_category">Highest Rated</span> 
				</g:link>				
			</div>
		</div>
		<div style="width: 100%;">
			<div class="carousel_category_browselink">
				<p:image src="carousel_content/calendar.png" class="carousel_category_image"/>
				<g:link action="search" controller="serviceItem"
					params="[sort:'approvedDate',order:'desc',offset:0,accessType:session.accessType,status_any_checkbox: 'on']">
					<span class="carousel_category">New Arrivals</span> 
				</g:link>				
			</div>

		</div>
	</div>
</div>



