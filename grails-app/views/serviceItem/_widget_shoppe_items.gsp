<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<%
    //add an extra css class if the carousel has multiple pages
    def multiPageCls = items.size() > (size as Integer) ? 'multi-page' : ''
%>

<div class="widget_shoppe_items ${name.encodeAsHTML()}" style="padding-top: 30px;">
	<div class="carousel carousel-invisible ${multiPageCls}" style="height:270px;" >
		<ul>
			<common:convertListToGrid listSize="${size}"  elementList="${items}" var="returnedLists" >
				<g:each in="${returnedLists}" var="returnedList" status="i">
					<li>
						<div class="carousel-content">
							<table  class="discover_market_table" >
								<tr>
									<g:each in="${returnedList}" var="si" status="j">
										<g:render template="/serviceItem/badge/item_mini_badge_block_view" var="si" bean="${si}"  />
									</g:each>
								</tr>
							</table>
						</div>
					</li>
				</g:each>
			</common:convertListToGrid>
		</ul>
	</div>
</div>
