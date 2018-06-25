<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>


<!-- Top Header Panel -->
<div id="header" class="one-edge-shadow">
	<div class="header_content">
        <g:if test="${comingFromLogout != 'true'}">
		<div id="omp_header_logo" class="omp_header_logo bootstrap-active">
			<div id="omp_header_logo_img" class="omp_header_logo_img">
				<a id="omp_market_box_img_link" href='${request.contextPath}${session.spaEnabled ? "/spa" : "/serviceItem/shoppe?max=5&offset=0"}'>
					<img src=<marketplaceTheme:imageLink src="Mp_logo.png"/> class="omp_header_logo_img_cls"/>
				</a>
			</div>
			<div class="omp_header_logo_text">
				<a id="omp_market_box_text_link" href='${request.contextPath}${session.spaEnabled ? "/spa" : "/serviceItem/shoppe?max=5&offset=0"}'>
				${g.message(code: 'marketplace.title', encodeAs: 'HTML')}
				</a>
			</div>
		</div>
        <div class="omp_header_nav">
            <div class="omp_nav_item omp_user_box">
                <div class="omp_item_right">
                    <!--  User menu -->
                    <div id="userMenu" class="userMenu"></div>
                </div>
                <div class="omp_item_right omp_user_search">
                    <g:render template="/search"/>
                </div>
            </div>
        </div>
		</g:if>
	</div>
</div>
