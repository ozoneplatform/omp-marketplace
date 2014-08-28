define([
  'jquery',
  'underscore'
],
function ($, _) {
    //********************************
    //HELPER METHODS
    //********************************

    var currentViewName = 'grid';

    //Helper object to track info on the currently displayed view
    function getCurrentView(){
        var $ = jQuery,
            view = (currentViewName === 'list' ? $("#listView") : $("#gridView"));

        return {
            type    : view,
            name    : currentViewName
        };
    }

    //This sets the view mode in the session
    function setResultUiViewSettings(viewGridOrList){
        var $ = jQuery;
        $.ajax({
            url: Marketplace.context + '/search/setResultUiViewSettings',
            type: 'POST',
            data: {
                viewGridOrList: viewGridOrList
            }
        });
    }


    //This is used by all the "simple sort" events that are triggered from above
    function replaceSortedData(view, data){
        view.html(data);
    }


    //This function will load more in the grid view
    function loadMoreGridView(toRecord){
        var $ = jQuery;
        var view = $("#gridView");
        getPaginatedResults(view, "grid", toRecord, function(data){

            //Append the data the fade in the last hidden grid table
            view.append("<div class='widget_grid_view_loadmore'>" + data + "</div>");
            view.find(".widget_grid_view_loadmore").last().show(1000);

            //Set the loaded records count and update the loaded 'X' of 'N' display
            view.trigger("captureLoaded");
            $("#loadedStatus").trigger("refresh", view);

            //If all the records are loaded hide the load more option
             $("#loadMore").trigger("refresh", view);
        });
    }

    //This function will load more in the list view
    function loadMoreListView(toRecord){
        var $ = jQuery;
        var view = $("#listView");
        getPaginatedResults(view, "list", toRecord, function(data){


            //Append the data in a hidden tag then fade it in
            view.append("<div class='widget_list_view_loadmore' style='display: none;'>" + data + "</div>");
            view.find(".widget_list_view_loadmore").last().slideDown(1000);

            //Set the loaded records count and update the loaded 'X' of 'N' display
            view.trigger("captureLoaded");
            $("#loadedStatus").trigger("refresh", view);

            //If all the records are loaded hide the load more option
             $("#loadMore").trigger("refresh", view);
        });
    };



    //********************************
    //AJAX STUFF
    //********************************

    //This function gets paginated results and is constrained to a max result size
    function getPaginatedResults(view, type, toRecord, callback){
        var $ = jQuery;

        Marketplace.showLoadMask();

        $.ajax({
            url: Marketplace.context + "/search/getSearchResults",
            dataType: "html",
            data: {
                viewGridOrList: type,
                offset:         view.data("loaded"),
                max:            toRecord,
                sort:           view.data("sortOn"),
                order:          view.data("sortOrder")
            }
            }).done(function ( data ) {
                callback(data);
            }).fail(function(){
                console.log("No data returned to gridView.  What allowed the user to load more?");
            }).always(function() {
                Marketplace.hideLoadMask();
            });
    }

    /**
     * @name getSortedResults
     * This function will get sorted data then display it
     * This is different then load more in that it may load more than one page size by overriding max
     * @param callback A callback function to render the data into the view
     * @param currentView (Optional) The view that is currently shown,where the fadeOut will be performed.
     *  Defaults to the value of the 'view' parameter
     */
    function getSortedResults(view, viewType, sortOn, sortOrder, callback, currentView){
        //Set the last sort on and order on the incoming view
        var $ = jQuery,
            fadeOutPromise,
            ajaxPromise;

        currentView = currentView || view;

        //using fadeTo so that display:none isn't set at the end
        fadeOutPromise = currentView.fadeTo('slow', 0).promise().done(function() {
            //show load mask AFTER fade out
            Marketplace.showLoadMask();
        });


        view.data("sortOn", sortOn);
        view.data("sortOrder", sortOrder);
        //Register the doRefreshSort data on the appropritate view so it can be inspected later
        if(viewType == 'list'){
            $("#listView").data("doRefreshSort", true);
            $("#gridView").data("doRefreshSort", false);
        } else{
            $("#gridView").data("doRefreshSort", true);
            $("#listView").data("doRefreshSort", false);
        }
        ajaxPromise = $.ajax({
            url: Marketplace.context + "/search/getSearchResults",
            dataType: "html",
            data: {
                viewGridOrList: viewType,
                sort:           sortOn,
                order:          sortOrder,
                max:            view.data("loaded")
            }
        });

        //when the fade and the ajax have both completed, fire the callback and
        //then hide the load mask and fade in the data
        $.when(ajaxPromise, fadeOutPromise)
            .done(function (ajaxResponse) {
                callback(ajaxResponse[0]);
            }).always(function() {
                Marketplace.hideLoadMask();

                //fade in (fadeIn function doesn't seem to work when fadeTo was used to fade out
                view.fadeTo('slow', 1);
            });
    }


    //********************************
    //ACTIONS
    //********************************

    //This sets up the page actions
    function setUpEvents(){
        var $ = jQuery;

        //determine the new sorting order based on whether or not the element
        //previously contained a css class for sorting
        function getNewSortOrder(el) {
            return $(el).hasClass('sorted_ascending') ? 'desc' : 'asc';
        }

        /**
         * Trigger a sorting event on the given element.
         * @param el the element to trigger on
         * @param dir the sort direction (either 'asc' or 'desc')
         * @param prefix The prefix of the event name.
         * example: if prefix = 'show' and dir = 'asc', 'showAscending' will be triggered
         * @param args Arguments to the event
         */
        function triggerSortEvt(el, dir, prefix, args) {
            return $(el).trigger(prefix + (dir === 'asc' ? 'Ascending' : 'Descending'), args);
        }

        //Register actions on the list view object.
        $("#listView").on("resetSorting", function(){
            if($(this).data("sortOn") === undefined){
                $(this).data("sortOn", "title");
                $(this).data("sortOrder", "asc");
            }
        }).on("refresh", function(){
            var gridView = $("#gridView");
            var listView = $("#listView");

            currentViewName = 'list';

            //Here we will either do nothing and fade the other div in or have to refresh the data and overlay the whole thing
            if(gridView.data("doRefreshSort") == true){
                //There was a sort event on grid so we need to refresh the list view with the sort from grid
                getSortedResults(listView, "list", gridView.data("sortOn"), gridView.data("sortOrder"), function(data){
                    gridView.hide();
                    replaceSortedData(listView, data);
                    $("#widget_listing_list_button").trigger("activate");
                    listView.data("doRefreshSort", false);
                }, gridView);
            } else {
                var gridButton = $("#widget_listing_grid_button");
                gridButton.off('click');
                gridView.fadeOut('slow', function(){
                    listView.fadeTo('slow', 1, function(){
                        gridButton.on('click', gridButtonClicked);
                        $("#widget_listing_list_button").trigger("activate");
                    });
                });
            }
        }).on("captureLoaded", function(){
            //The records loaded is the count of widget_list_item divs
            $(this).data("loaded", $(this).find(".widget_list_item").length);
        });

        //Register actions on the grid view object.
        $("#gridView").on("resetSorting", function(){
            if($(this).data("sortOn") === undefined){
                $(this).data("sortOn", "title");
                $(this).data("sortOrder", "asc");
            }
        }).on("refresh", function(){
            var listView = $("#listView");
            var gridView = $("#gridView");

            currentViewName = 'grid';

            //Here we will either do nothing and fade the other div in or have to refresh the data and overlay the whole thing
            if(listView.data("doRefreshSort") == true){
                //There was a sort event on list so we need to refresh the grid view with the sort from list
                getSortedResults(gridView, "grid", listView.data("sortOn"), listView.data("sortOrder"), function(data){
                    listView.hide();
                    replaceSortedData(gridView, data);
                    $("#widget_listing_grid_button").trigger("activate");
                    gridView.data("doRefreshSort", false);
                }, listView);
            } else {
                var listButton = $("#widget_listing_list_button");
                listButton.off('click');
                listView.fadeOut('slow', function(){
                    gridView.fadeTo('slow', 1, function(){
                        listButton.on('click', listButtonClicked);
                        $("#widget_listing_grid_button").trigger("activate");
                    });
                });
            }
        }).on("captureLoaded", function(){
            //The records loaded is the count of mini_badge_extended_div divs
            $(this).data("loaded", $(this).find(".mini_badge_extended_div").length);
        });


        var listButtonClicked = function() {
            var listView = $("#listView");
            listView.trigger("refresh");
            $("#loadedStatus").trigger("refresh", listView);
            $("#widget_listing_list_button").addClass('active').removeClass("inactive");
            $("#widget_listing_grid_button").addClass('inactive').removeClass("active");
        };

        //Register the showList function to the click event of the list link
        $("#widget_listing_list_button").on("click", listButtonClicked).on("activate", function(){
            setResultUiViewSettings('list');
        });

        var gridButtonClicked = function() {
            var gridView = $("#gridView");
            gridView.trigger("refresh");
            $("#loadedStatus").trigger("refresh", gridView);
            $("#widget_listing_grid_button").addClass('active').removeClass('inactive');
            $("#widget_listing_list_button").addClass('inactive').removeClass("active");
        };

        //Register the showGrid function to the click even of the grid link
        $("#widget_listing_grid_button").on("click", gridButtonClicked).on("activate", function(){
             setResultUiViewSettings('grid');
        });

        //Update the status that shows how many items have been loaded
        $("#loadedStatus").on("refresh", function(event, view){
            var loaded = $(view).data("loaded");
            var total = $(view).data("numTotalResults");
            if(loaded > total)
                loaded = total
            $(this).html(loaded + " of " + total);
            $("#loadMore").trigger("refresh", view);
        });

        //Register actions for load more functionality
        $("#loadMore").on("click", function(){
            var view = getCurrentView();
            if(view.name === 'grid'){
                loadMoreGridView();
            } else{
                loadMoreListView();
            }
        }).on("refresh", function(event, view){
            if($(view).data("loaded") >= $(view).data("numTotalResults")){
                $(this).fadeOut('slow');
            } else{
                $(this).show('slow');
            }
        });


        //////////////////////////////////////////////////////
        //  SORT EVENTS
        //
        /*The sort events all work in the same way just with different classes and names.
          1)  click:             the resetSorting event is called on the sort elements other than the one clicked
          2)  resetSorting:      the element clicked removes its sorting classes
          3)  sortAscending:     the current view is refreshed to show the ascending sort for the element
          4)  sortDescending:    the current view is refreshed to show the ascending sort for the element
          5)  showAscending:     the ascending class is added and the descending class is removed for the element
          6)  showDescending:    the descending class is added and the ascending class is removed for the element
        */

        var $sortByScore = $('#sortByScore');

        //Register events on the element that will sort by score
        $sortByScore.on("click", function(){
            //reverse previous sort order
            var sortOrder = getNewSortOrder(this),
                view = getCurrentView();

            $(".search_results_sort").trigger("resetSorting", ['scoreCard.score', sortOrder]);

            //trigger sortAsending or sortDescending according to the sortOrder
            triggerSortEvt(this, sortOrder, 'sort', view);
        }).on('resetSorting', function(event, sortOn, sortOrder){
            var $this = $(this);

            if(sortOn != "scoreCard.score"){
                $this.removeClass("search_results_sort_by_scorecard_asc search_results_sort_by_scorecard_desc");
                $this.addClass("sorted_ascending");  //Doing this makes the next sort descending
            } else{
                triggerSortEvt($this, sortOrder, 'show');
            }
        }).on('sortAscending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "scoreCard.score", "asc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('sortDescending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "scoreCard.score", "desc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('showAscending', function(){
            $(this).addClass("sorted_ascending search_results_sort_by_scorecard_asc").removeClass("search_results_sort_by_scorecard_desc").tooltip('hide');
        }).on('showDescending', function(){
            $(this).addClass("search_results_sort_by_scorecard_desc").removeClass("sorted_ascending search_results_sort_by_scorecard_asc").tooltip('hide');
        });

        var $sortByRating = $('#sortByRating');

        //Register events on the element that will sort by rating
        $sortByRating.on("click", function(){
            //reverse previous sort order
            var sortOrder = getNewSortOrder(this),
                view = getCurrentView();

            $(".search_results_sort").trigger("resetSorting", ['avgRate', sortOrder]);

            //trigger sortAsending or sortDescending according to the sortOrder
            triggerSortEvt(this, sortOrder, 'sort', view);
        }).on('resetSorting', function(event, sortOn, sortOrder){
            var $this = $(this);

            if(sortOn != "avgRate"){
                $this.removeClass("search_results_sort_by_rating_asc search_results_sort_by_rating_desc");
                $this.addClass("sorted_ascending");  //Doing this makes the next sort descending
            }else{
                triggerSortEvt($this, sortOrder, 'show');
            }
        }).on('sortAscending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "avgRate", "asc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('sortDescending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "avgRate", "desc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('showAscending', function(){
            $(this).addClass("sorted_ascending search_results_sort_by_rating_asc").removeClass("search_results_sort_by_rating_desc").tooltip('hide');
        }).on('showDescending', function(){
            $(this).addClass("search_results_sort_by_rating_desc").removeClass("sorted_ascending search_results_sort_by_rating_asc").tooltip('hide');
        });

        var $sortByCalendar = $('#sortByCalendar');

        //Register events on the element that will sort by approved date
        $sortByCalendar.on("click", function(){
            //reverse previous sort order
            var sortOrder = getNewSortOrder(this),
                view = getCurrentView();

            $(".search_results_sort").trigger("resetSorting", ['approvedDate', sortOrder]);

            //trigger sortAsending or sortDescending according to the sortOrder
            triggerSortEvt(this, sortOrder, 'sort', view);
        }).on('resetSorting', function(event, sortOn, sortOrder){
            var $this = $(this);

            if(sortOn != "approvedDate"){
                $this.removeClass("search_results_sort_by_calendar_asc search_results_sort_by_calendar_desc");
                $this.addClass("sorted_ascending");  //Doing this makes the next sort descending
            } else{
                triggerSortEvt($this, sortOrder, 'show');
            }
        }).on('sortAscending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "approvedDate", "asc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('sortDescending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "approvedDate", "desc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('showAscending', function(){
            $(this).addClass("sorted_ascending search_results_sort_by_calendar_asc").removeClass("search_results_sort_by_calendar_desc").tooltip('hide');
        }).on('showDescending', function(){
            $(this).addClass("search_results_sort_by_calendar_desc").removeClass("sorted_ascending search_results_sort_by_calendar_asc").tooltip('hide');
        });

        var $sortByAlpha = $('#sortByAlpha');

        //Register events on the element that will sort by title
        $sortByAlpha.on("click", function(){
            //reverse previous sort order
            var sortOrder = getNewSortOrder(this),
                view = getCurrentView();

            $(".search_results_sort").trigger("resetSorting", ['title', sortOrder]);

            //trigger sortAsending or sortDescending according to the sortOrder
            triggerSortEvt(this, sortOrder, 'sort', view);
        }).on('resetSorting', function(event, sortOn, sortOrder){
            var $this = $(this);

            if(sortOn != "title"){
                $this.removeClass("sorted_ascending search_results_sort_by_alpha_asc search_results_sort_by_alpha_desc");
            } else{
                triggerSortEvt($this, sortOrder, 'show');
            }
        }).on('sortAscending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name, "title", "asc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('sortDescending', function(event, view){
            var me = $(this);
            getSortedResults(view.type, view.name,"title", "desc",function(data){
                replaceSortedData(view.type, data);
            });
        }).on('showAscending', function(){
            $(this).addClass("sorted_ascending search_results_sort_by_alpha_asc").removeClass("search_results_sort_by_alpha_desc").tooltip('hide');
        }).on('showDescending', function(){
            $(this).addClass("search_results_sort_by_alpha_desc").removeClass("sorted_ascending search_results_sort_by_alpha_asc").tooltip('hide');
        });

        //
        //  END SORT EVENTS
        //////////////////////////////////////////////////////

        attachTooltips($sortByRating, $sortByCalendar, $sortByAlpha);
        updateFilterButtons();
    }

    function attachTooltips ($sortByRating, $sortByCalendar, $sortByAlpha) {
        BootstrapUtil.attachTooltip($sortByRating, {
            placement: 'top',
            title: function () {
                if($sortByRating.hasClass('search_results_sort_by_rating_asc')) {
                    return 'Rating - lowest to highest';
                }
                else {
                    return 'Rating - highest to lowest';
                }
            }
        });
        BootstrapUtil.attachTooltip($sortByCalendar, {
            placement: 'top',
            title: function () {
                if($sortByCalendar.hasClass('search_results_sort_by_calendar_desc')) {
                    return 'Newest first';
                }
                else {
                    return 'Oldest first';
                }
            }
        });
        BootstrapUtil.attachTooltip($sortByAlpha, {
            placement: 'top',
            title: function () {
                if($sortByAlpha.hasClass('search_results_sort_by_alpha_desc')) {
                    return 'Z-A';
                }
                else {
                    return 'A-Z';
                }
            }
        });

        BootstrapUtil.attachTooltip($('#widget_listing_list_button'), {
            placement: 'top',
            title: 'List'
        });

        BootstrapUtil.attachTooltip($('#widget_listing_grid_button'), {
            placement: 'top',
            title: 'Grid Layout'
        });
    }

    function updateFilterButtons () {
        var href = window.location.href.split('?'),
            queryParams = href[1],
            params,
            $button;

        params = queryParams ? $.deparam(queryParams) : {};

        if(params.sort === 'avgRate' || !params.sort) {
            $button = $("#sortByRating");
        }
        else if(params.sort === 'title') {
            $button = $("#sortByAlpha");
        }
        else if(params.sort === 'approvedDate') {
            $button = $("#sortByCalendar");
        }

        $button.trigger(params.order === 'asc' ? 'showAscending' : 'showDescending');
    }


    return {
        //Set defaults if this is the first time we are on the page//This is the entry point to the javascript stuff
        setDefaultView: function (gridOrListView, sortOn, sortOrder) {
            currentViewName = gridOrListView;

            setUpEvents();
            var $ = jQuery;
            var listView = $("#listView");
            var gridView = $("#gridView");

            if (gridOrListView === 'list') {
                listView.show();
                gridView.hide();
                $("#widget_listing_list_button").trigger('activate').addClass('active');
                listView.data("sortOn", sortOn);
                listView.data("sortOrder", sortOrder);
            } else  {
                gridView.show();
                listView.hide();
                gridView.data("sortOn", sortOn);
                gridView.data("sortOrder", sortOrder);
                $("#widget_listing_grid_button").trigger('activate').addClass('active');
            }

            //Capture initial values for loaded and shown result counts
            gridView.trigger("captureLoaded");
            gridView.data("numTotalResults", $("#numTotalResults").html());

            //Capture initial values for loaded and shown result counts
            listView.trigger("captureLoaded");
            listView.data("numTotalResults", $("#numTotalResults").html());

            //Show the load more button
            $("#loadMore").slideDown('slow');

            //Make sure that our default sort order is the same as the server side
            $.event.trigger("resetSorting", [sortOn, sortOrder]);
            //$("#sortByRating").addClass("sorted_ascending");
        }
    };


});
