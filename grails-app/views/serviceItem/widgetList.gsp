<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="${session.marketplaceLayout}"/>
    <meta name="totalResults" content="${listSize.encodeAsHTML()}"/>
    <meta name="startIndex" content="${params.offset?.toInteger() + 1}"/>
    <meta name="itemsPerPage" content="${params.max.encodeAsHTML()}"/>
</head>

<body>

    <div id="marketContentWrapper" class="widget-marketContentWrapper-wleftbar">
        <div class="body">
            <div id="marketContent">
                <div id="filter_menu">
                    <g:render template="/filter_menu"/>
                </div>

                <div id="pending_list">
                    <g:render template="/serviceItem/widget_list"
                              model="['serviceItemList': serviceItemList, 'listSize': listSize, 'numShownResults':numShownResults]"/>
                </div>
            </div>

        </div>
    </div>
</body>
</html>
