<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<script type="text/javascript">
    function getAdminViewApproveRejectUpdaterContentArray(params){
        var updaterContentArray = new Array();

        //Render Updated Content to Activity List
        var updaterContent_2 = {
            updaterContainerId:'activity_list',
            updaterActionUrl:'${createLink(controller:"serviceItem", action: "activityList")}',
            updaterParams: {},
            onSuccessMethod:null,
            onFailureMethod:null
        }
        updaterContentArray.push(updaterContent_2);//Add to Array.

        var uparms = {max:params.max, offset:params.offset, order:params.order}
        if (params.sort) {
            uparms.sort = params.sort
        } else if (params.sort_0) {
            uparms.sort_0 = params.sort_0
        }

        //Render Updated Content to pending_list
        var updaterContent_3 = {
            updaterContainerId:'pending_list',
            updaterActionUrl:'${createLink(controller:"serviceItem", action: "adminList")}',
            updaterParams: uparms,
            onSuccessMethod:null,
            onFailureMethod:null
        }
        updaterContentArray.push(updaterContent_3);//Add to Array.TODO: Fix b/c causing exception.

        return updaterContentArray;
    }
</script>

<div id="item_badge_${item?.id}" class="item ${ (i % 2) == 0 ? 'even' : 'odd'}">
    <myui:serviceItemBadge model="[item: item, model_param_getUpdaterContentArrayMethod: 'getAdminViewApproveRejectUpdaterContentArray']" isAdmin="${session.isAdmin}" isOwner="${ item?.isAuthor(session.username) }" />
</div>