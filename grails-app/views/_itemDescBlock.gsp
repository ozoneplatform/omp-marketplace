
<%@ page import="marketplace.*" %>
<p>
<g:if test="${expandable}">
  ${desc?.encodeAsHTML()?.replaceAll(/\n/,"<br />")} [<g:link class="more" controller="serviceItem" action="more" id="${itemId}">more</g:link>]
</g:if>
<g:elseif test="${collapsable}">
  ${desc?.encodeAsHTML()?.replaceAll(/\n/,"<br />")} [<g:link class="less" controller="serviceItem" action="less" id="${itemId}">less</g:link>]
</g:elseif>
<g:elseif test="${readMore}">
    ${desc?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
    <g:link controller="serviceItem" action="show" id="${itemId}" class="read_more_link" elementId="read_more_${itemId}"
            params="[accessType:session.accessType,sorttype:customList]">
        more
    </g:link>
</g:elseif>
<g:else>
  ${desc?.encodeAsHTML()?.replaceAll(/\n/,"<br />")}
</g:else>
</p>

<script>
    ;(function () {
        jQuery(document).on('click', 'a.more, a.less', function (e) {
            e.preventDefault();
            var $a = jQuery(e.target),
                href = $a.attr('href'),
                id = $a.attr('id');
            jQuery.ajax({
                url: href,
                dataType: 'html'
            })
            .done(function (data, textStatus, jqXHR) {
                $a.parents('.item_description_block').html(data);
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                Marketplace.error(textStatus);
            });
        });
    })();
</script>