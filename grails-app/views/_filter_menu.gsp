<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="marketplace.*" %>
<%@ page import="grails.util.Holders as configHolder" %>

<%
    def linkColor = "color: #4d4d4d"
    def domainTitle = g.message(code: "label.domain", encodeAs: 'HTML')
%>
<script>
require(['views/filter/FilterMenu', 'underscore'], function(FilterMenu, _) {
    var filterConfig = [{
            'id' : 'Type',
            'title' : 'Type',
            'aggregations' : JSON.parse("${typeAggregations.toString().encodeAsJavaScript()}"),
            'filterKind' : 'filterType',
            'filterRemoveField' : 'typeFilters'
        }, {
            'id' : 'Category',
            'title' : 'Category',
            'aggregations' : JSON.parse("${categoriesAggregations.toString().encodeAsJavaScript()}"),
            'filterKind' : 'filterCategory',
            'filterRemoveField' : 'categoryFilters'
        }],
        nuggetData = '${nuggets.toString().encodeAsJavaScript()}',
<%
%>
        nuggets;

    try{
        if(nuggetData) {
            nuggets = JSON.parse(nuggetData);
        }

        _.each(filterConfig, function(conf) {
            //it appears that the filterRemoveField is the key in the nuggets obj
            var nuggetRef = conf.filterRemoveField,
                selectedIds = _.object(_.map(nuggets[nuggetRef], function(nugget) {
                    return [nugget.id, true];
                }));

            _.each(conf.aggregations, function(aggregation) {
                aggregation.selected = selectedIds[aggregation.id];
            });
        });
	} catch (e){
        console.log("no nugget data?")
		//oh well, nothing here thats fine, we may be on the search page
	}


    (new FilterMenu({
        el: document.getElementById('filter_menu'),
        filterConfig: filterConfig
    })).render();
});

</script>
