<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>

<%
  def selRel = ""
  def selCom = ""
  def selRec = ""
  def selRad = ""
  def selRat = ""
  def selAup = ""
  def selAdw = ""
  def selTup = ""
  def selTdw = ""
  def selSup = ""
  def selSdw = ""
  def selOup = ""
  def selOdw = ""
  def sel5 = ""
  def sel10 = ""
  def sel25 = ""

  if (params) {
	if ("score" == params.sort) { selRel = "selected" }
	if ("totalComments" == params.sort) { selCom = "selected" }
	if ("editedDate" == params.sort) { selRec = "selected" }
	if ("approvalDate" == params.sort) { selRad = "selected" }
	if ("avgRate" == params.sort) { selRat = "selected" }
	if ("title" == params.sort) {
	  if ("asc" == params.order) {
		selAup = "selected"
	  } else {
		selAdw = "selected"
	  }
	}
	
	//Type
	if ("typesTitle" == params.sort) {
		if ("asc" == params.order) {
		  selTup = "selected"
		} else {
		  selTdw = "selected"
		}
	}
	
	//State
	if ("stateTitle" == params.sort) {
		if ("asc" == params.order) {
		  selSup = "selected"
		} else {
		  selSdw = "selected"
		}
	}
	
	//Owner Display Name
	if ("profileDisplayName" == params.sort) {
		if ("asc" == params.order) {
		  selOup = "selected"
		} else {
		  selOdw = "selected"
		}
	}
	
	if ("5" == params.max) { sel5 = "selected" }
	if ("10" == params.max) { sel10 = "selected" }
	if ("25" == params.max) { sel25 = "selected" }
  }
  
  def pageFirstPrevButtonDisabled = (!(params.offset?.toInteger() > 0))
  def pageLastNextButtonDisabled = (!((params.offset?.toInteger() + params.max?.toInteger()) <= listSize-1))
  
  def pageFirstButtonQtip = g.message(code: "page.first.button.qtip")
  def pagePrevButtonQtip = g.message(code: "page.prev.button.qtip")
  def pageNextButtonQtip = g.message(code: "page.next.button.qtip")
  def pageLastButtonQtip = g.message(code: "page.last.button.qtip")
  
  def maxItemsPerPage = params.offset?.toInteger() + params.max?.toInteger()
  
  def currentItemsOnPage = ((maxItemsPerPage > listSize) ? listSize : maxItemsPerPage)
  def currentPage = (Math.ceil(params.offset?.toInteger() / params.max?.toInteger()) + 1) as int
  def pageCount = (Math.ceil(listSize / params.max?.toInteger())) as int
%>
<script type="text/javascript">
	Marketplace.refreshListContent = function(data, textStatus, jqXHR){
		jQuery("#${container}").html(data);
		Marketplace.widget.addWidgetParameter();
	}; 
	jQuery(document).ready(function(){
		jQuery(".extjs-toolbar-page-button.x-item-enabled").hover(function(){
			jQuery(this).addClass('x-item-over');
		}, function(){
			jQuery(this).removeClass('x-item-over');
		}).mouseup(function(){
			jQuery(this).addClass('x-item-over');
			jQuery(this).removeClass('x-item-pressed');
		}).mousedown(function(){
			jQuery(this).removeClass('x-item-over');
			jQuery(this).addClass('x-item-pressed');
		});
		jQuery(".toolbar-paging-info input").focus(function(){
			jQuery(this).addClass("input-focus");
		});
		jQuery(".toolbar-paging-info input").blur(function(){
			jQuery(this).removeClass("input-focus");
		});

		Marketplace.handleToolbarCustomInput(
			".toolbar-paging-info input",
			${pageCount},
			{
				onSuccessCallback: Marketplace.refreshListContent,
				ajaxRequestURL: "${baseListURL}",
				maxParamName: "max", 				//Max Param Name
				maxParamVal: ${params.max?.toInteger()},		//Max Param Val
				offsetParamName: "offset",			//Offset Param Name
				sortParam: "${params.sort}",	//Sort Param Val
				orderParamName: "order",			//Order Param Name
				orderParamVal: "${params.order}",  //Order Param Val
				otherParams: (Marketplace.widget.isWidget() ? "&widget=true" : "") // Other Params
			},
			null
		);
	});		
</script>
<div class="page_sort">
  <div class="page_sort_inner">
	<table class="page_sort_table">
	  <tr>
		<td>
		  <g:set var="minOffset" value="${params.offset?.toInteger()-params.max?.toInteger()}"/>
		  <!-- FIRST BUTTON -->
		  <g:render model="[button: [disabled: pageFirstPrevButtonDisabled, 
									  id: 'pg-srt-first-button-id', 
								  	  qtip: pageFirstButtonQtip, 
								  	  iconCls: 'x-tbar-page-first', 
								  	  cls: 'pg-srt-page-first-button',
						    		  paramz: [max:params.max,offset:0,sort:params.sort,order:params.order]],
						    controller: controller,
						    action: action]" 
						    template="/theme/buttons/extjsToolbarPageButton"/>
		<!-- PREV BUTTON -->	
		<g:render model="[button: [disabled: pageFirstPrevButtonDisabled, 
										  id: 'pg-srt-prev-button-id', 
										  qtip: pagePrevButtonQtip, 
									  	  iconCls: 'x-tbar-page-prev', 
									  	  cls: 'pg-srt-page-prev-button',
						    			 paramz: [max:params.max,offset:(minOffset<0?0:minOffset),sort:params.sort,order:params.order]],
						    controller: controller,
						    action: action]" 
						    template="/theme/buttons/extjsToolbarPageButton"/>
		 <span class="toolbar-page-button-delimiter"></span>
		 <span class="toolbar-paging-info">Page <input type="text" value="${currentPage}"/> of ${pageCount}</span>
		 <span class="toolbar-page-button-delimiter"></span>
		 <g:set var="maxOffset" value="${params.max?.toInteger() * (pageCount - 1)}"/>
		 <!-- NEXT BUTTON -->	
		 <g:render model="[button: [disabled: pageLastNextButtonDisabled, 
									  id: 'pg-srt-next-button-id', 
									  qtip: pageNextButtonQtip, 
									  iconCls: 'x-tbar-page-next', 
									  cls: 'pg-srt-page-next-button',
						    		  paramz: [max:params.max,offset:params.offset?.toInteger()+params.max?.toInteger(),sort:params.sort,order:params.order]],
						    controller: controller,
						    action: action]" 
						    template="/theme/buttons/extjsToolbarPageButton"/>
		<!-- LAST BUTTON -->	
		<g:render model="[button: [disabled: pageLastNextButtonDisabled, 
										  id: 'pg-srt-last-button-id', 
										  qtip: pageLastButtonQtip, 
										  iconCls: 'x-tbar-page-last', 
										  cls: 'pg-srt-page-last-button',
						    			  paramz: [max:params.max,offset:maxOffset,sort:params.sort,order:params.order]],
						    controller: controller,
						    action: action]" 
						    template="/theme/buttons/extjsToolbarPageButton"/>
		</td>
		<td>
			<span class="page_sort_results_display">Showing ${params.offset?.toInteger() + 1}-${currentItemsOnPage} of ${listSize}</span>
		</td>
		<td class="dd">
		  <g:message code="sort.by" default="Sort by"/>
		  <select name="sort_${instance}" size="1" class="dd_sort" onchange="Marketplace.sortItemList('${container}', '${controller}', '${action}', {'max':'${params.max}', 'offset':'${params.offset}', 'sort':Marketplace.parseSortValue(this.value).sort, 'order':Marketplace.parseSortValue(this.value).order});">
		  	<g:if test="${comingFromPendingListings != 'true'}">
		  		<option value="score|auto" ${selRel}><g:message code="sort.mostRelevant" default="most relevant"/></option>
				<option value="avgRate|desc" ${selRat}><g:message code="sort.highestRated" default="highest rated"/></option>
	  		    <option value="totalComments|desc" ${selCom}><g:message code="sort.mostCommented" default="most commented"/></option>
            </g:if>
			<option value="typesTitle|asc" ${selTup}><g:message code="sort.type.asc" default="type [a-z]"/></option>
	  		<option value="typesTitle|desc" ${selTdw}><g:message code="sort.type.desc" default="type [z-a]"/></option>

			<option value="profileDisplayName|asc" ${selOup}><g:message code="sort.owner.asc" default="owner [a-z]"/></option>
			<option value="profileDisplayName|desc" ${selOdw}><g:message code="sort.owner.desc" default="owner [z-a]"/></option>
		  	<option value="editedDate|desc" ${selRec}><g:message code="sort.recentlyUpdated" default="recently updated"/></option>
            <g:if test="${comingFromPendingListings != 'true'}">
                <option value="approvalDate|desc" ${selRad}><g:message code="sort.recentlyAdded" default="recently added"/></option>
            </g:if>
			<option value="title|asc" ${selAup}><g:message code="sort.title.asc" default="alphabetical [a-z]"/></option>
			<option value="title|desc" ${selAdw}><g:message code="sort.title.desc" default="alphabetical [z-a]"/></option>
		  </select>
		</td>
		<td class="dd">
		  <g:message code="sort.itemsPerPage" default="Items per page"/>
		  <select name="count_${instance}" size="1" class="dd_perpage" onchange="Marketplace.sortItemList('${container}', '${controller}', '${action}', {'max':this.value, 'offset':'0', 'sort':'${params.sort}', 'order':'${params.order}'});">
			<option value="5" ${sel5}>5</option>
			<option value="10" ${sel10}>10</option>
			<option value="25" ${sel25}>25</option>
		  </select>
		</td>
	  </tr>
	</table>
  </div>
</div>
