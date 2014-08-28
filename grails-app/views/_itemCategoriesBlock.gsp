<%@ page import="marketplace.*" %>
<span>
    <g:if test="${seeAll}">
        ${categories?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
        <g:link controller="serviceItem" action="show" id="${itemId}" class="read_more_link" elementId="see_all_${itemId}"
                params="[accessType:session.accessType, sorttype:customList, tab:'specifications-tab']">
            ${linkText}
        </g:link>
    </g:if>
    <g:else>
        ${categories?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
    </g:else>
</span>