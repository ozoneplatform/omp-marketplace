<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="marketplace.*"%>

<%

  def imgSearchDflt = p.imageLink(src:"icons/search_button_up.png")
  def imgSearchDown = p.imageLink(src:"icons/search_button_down.png")
  def imgSearchOver = p.imageLink(src:"icons/search_button_over.png")
  def imgSearchLeft = p.imageLink(src:"icons/search_box_left.png")

%>
<script type="text/javascript">
    jQuery(document).ready(function() {
        var currentQueryString = jQuery('#simple_search > .searchField').val();
        if(currentQueryString !== "") {
            jQuery('.clearField').removeClass("clearFieldHidden");
        }
        jQuery('.searchField').bind('input keyup change paste', function() {
            var hasText = jQuery('.searchField').val() !== "";
            jQuery('.clearField')[hasText ? 'removeClass' : 'addClass']("clearFieldHidden");
        });
    });

    var clear_input = function() {
        jQuery('.clearField').addClass("clearFieldHidden");
        jQuery('.searchField').val("");
        jQuery('.searchField').focus();
    }

</script>
<!-- Search -->

<div class="search-bar-left"><img src=${imgSearchLeft}></div>
<div class="search-bar-middle">

<g:form id="simple_search" class="simple_search_form" name="search" method="get"
        url="[controller:'serviceItem', action:'search']">
    <input type="text" name="queryString" class="searchField"
        value="${(queryString ?: '').encodeAsHTML()}" >
    <input type="button" value="" alt="clear" class="clearField clearFieldHidden"
           onclick="clear_input()">
</g:form>
</div>
<div class="search_controls">
    <div id="icon_search_id" class="icon_search">
        <a href="#" onclick="getElementById('simple_search').submit();">
            <img alt="Search Icon" ignore_fix_flag="true" id="icon_search"
                src=${imgSearchDflt} title="Search" alt="Search"
                onmouseover="Marketplace.imageRoll('icon_search', ${imgSearchOver})"
                onmouseout="Marketplace.imageRoll('icon_search', ${imgSearchDflt})"
                onmousedown="Marketplace.imageRoll('icon_search', ${imgSearchDown})"
                onmouseup="Marketplace.imageRoll('icon_search', ${imgSearchDflt})"
            />
        </a>
    </div>

    <div class="clear"></div>
</div>

