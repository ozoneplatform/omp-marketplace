define([
  'jquery',
  'underscore',
  'backbone',
  'marketplace',
  '../search/SearchResultsGridRowView'
], function ($, _, Backbone, Marketplace, SearchResultsGridRowView) {
    
    return Backbone.View.extend({

        tagName: "table",
        
        className: "widget_grid_view_table",
        
        columnCount:  6,
        
        initialize: function (options) {
            this.searchResults = options.searchResults;
        },      
        
        //This will iterated through the results chunking it out in rows and rendering each row at at time
        render: function () {
            var searchResults = this.searchResults;
            var columnCount = this.columnCount;
            var listSize =   searchResults.length;
            var numColumns = columnCount;
            var numRows =    Math.ceil(listSize / numColumns);

            for(var row = 0; row < numRows; row++) {
                var gridRow = [];
                for (var column = 0; column < numColumns ; column++) {
                    var elementIndex = parseInt(column) + numColumns * row;
                    if (elementIndex < listSize)
                        gridRow.push(searchResults.at(elementIndex));
                    else
                        break;
                }
                this.renderRow(gridRow, false);
            }   
            return this;    
        },
        
        //Renders a table row by delegating to SearchResultsGridRowView
        renderRow: function (items, showScoreCard) {
            var gridRow = new SearchResultsGridRowView({
                items : items,
                showScoreCard: showScoreCard
            });
            this.$el.append(gridRow.render().el);
        }
        
    });
});
