<%
    def searchId = si?.owfProperties?.isStack() ?
        si?.owfProperties?.stackContext :
        si?.uuid
%>
<td>
    <div class="mini_badge_extended_div ${si?.isOWFAddable() ? 'ozone_aware_service_item': null}
        ${si?.isLaunchable() ? 'launchable_service_item': ''} ${(si?.types?.title == 'Web Apps') ? 'web_app_service_item' : ''} ${(si?.owfProperties?.isStack()) ? 'stack_service_item' : ''}  ${(si?.statApproved()) ? 'approved' : ''} ${(si?.state?.isPublished) ? 'published' : ''} ${(si?.isHidden()) ? 'hidden_service_item' : ''}" data-id="${si.id}">
        <table class="discover_market_items_table">
            <tr>
                <td class="discover discover_icon_image" id="icon_image_id_${si.id}">
                <a href="#quickview/${si.id}">
                    <div style="position:relative;">
                        <myui:itemIcon item="${si}" clss="listing_profile_table_thumbnail" width="128" height="128"/>
                        <div class="discover_favorite_icon_image discover_favorite_icon_${searchId?.encodeAsHTML()}"></div>
                    </div>
                </a>
                </td>
            </tr>
            <tr>
                <td class="discover">
                    <div class="discover discover_item_title">
                        <a href="#quickview/${si.id}">
                            <div class="auto_title_text ellipsis" title="${si.title?.encodeAsHTML()}">${si.title?.encodeAsHTML()}</div>
                        </a>

                    </div>
                </td>
            </tr>
            <g:if test="${si.agency?.title?.encodeAsHTML()}">
                <tr>
                    <td class="discover">
                        <!-- agency -->
                        <div class="agency" id="agency_${si.id}">
                            <g:render template="/serviceItem/agency_label" var="serviceItem" bean="${si}"/>
                        </div>
                    </td>
                </tr>
            </g:if>
            <tr>
                <td class="discover rating">
                    <g:set var="readonly" value="true" scope="request"/>
                    <g:render template="/serviceItem/rating_stars" var="serviceItem" bean="${si}" />
                    <span class="rating_total_votes rating_total_votes_count">(${si.totalVotes.encodeAsHTML()})</span>
                </td>
            </tr>
            <tr>
                <td class="discover">
                    <div class="numRatings_comment_text"><g:if test="${si?.totalComments == 1}">${si.totalComments.encodeAsHTML()} comment
                            </g:if><g:else>${si.totalComments.encodeAsHTML()} comments</g:else></div>
                </td>
            </tr>
            <tr class="hidden_row x-hide-display">
                <td class="discover discover_item_description" id="description_${si.id}">
                    <myui:itemDescBlock item="${si}" truncateAt="300" readMore="true"/>
                </td>
            </tr>
            <tr class="hidden_row x-hide-display">
                <td class="discover discover_item_types" id="type_${si.id}">
                    ${si.types?.title?.encodeAsHTML()}
                </td>
            </tr>
            <tr class="hidden_row x-hide-display">
                <td class="discover discover_item_launchUrl" id="type_${si.id}">${si.launchUrl?.encodeAsHTML()}</td>
            </tr>
            <tr class="hidden_row x-hide-display">
                <td class="discover discover_item_uuid" id="type_${si.id}">${si.uuid?.encodeAsHTML()}</td>
            </tr>
            <tr class="hidden_row x-hide-display">
                <td class="discover discover_item_categories" id="categories_${si.id}">
                    <myui:itemCategoriesBlock item="${si}" truncateAt="90"/>
                </td>
            </tr>
        </table>
        <script type="text/javascript">
            ;(function($) {
                var searchId = "${searchId?.encodeAsJavaScript()}";
                Marketplace.widget.isAlreadyInOwf(searchId, function(isInOwf) {
                    if (isInOwf) {
                        $(document).ready(function() {
                            $('.discover_favorite_icon_' + searchId).show();
                        });
                    }
                });
            })(jQuery);
        </script>
    </div>
</td>
