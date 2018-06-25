package marketplace

/**
 * Represents nuggets or bread crumbs of the latest search.
 */
class SearchNuggets {
    Map nuggetMap = [:]

    // AML-2608 owfWidgetType - do we need this for both here?
    private static final Map searchNuggetNames = ['queryString': 'query', 'typeIDs': 'type', 'categoryIDs': 'category', 'stateIDs': 'state', 'statuses': 'status', 'releasedFromDate': 'relFromDate', 'releasedToDate': 'relToDate']
    // AML-680  agency attribute for a listing - added agencyFilters to filterNuggetNames map
    private static final Map filterNuggetNames = ['typeFilters': 'type', 'categoryFilters': 'category', 'stateFilters': 'state', 'agencyFilters': 'agency', 'domainFilters': 'domain', 'rating': 'rating', 'scoreCardValue': 'scoreCardValue', 'scoreCardHighValue': 'scoreCardHighValue', 'owfWidgetType': 'owfWidgetType']
    List searchNuggets
    List filterNuggets
    List allNuggets

    void addNugget(String name, def value) {
        nuggetMap[(name)] = value
    }

    def getNugget(String name) {
        nuggetMap[(name)]
    }

    boolean hasFilter(String filterType, def value) {
        if (nuggetMap[(filterType)]) {
            return (nuggetMap[(filterType)].find { nugget ->
                if (nugget instanceof Map)
                    return nugget.id == value
                else
                    return nugget == value
            } != null)
        }
        return false
    }

    boolean hasFilter(String filterType) {
        return nuggetMap[(filterType)] as boolean
    }

    String getQuery() {
        return nuggetMap.queryString
    }

    boolean hasQuery() {
        nuggetMap.queryString as boolean
    }

    boolean hasNuggets() {
        nuggetMap.size() > 0
    }

    List getSearchNuggets() {
        if (!this.@searchNuggets) {
            def nugget = null
            this.@searchNuggets = []
            this.searchNuggetNames.eachWithIndex { nuggetNameEntry, index ->
                if (nuggetMap[(nuggetNameEntry.key)]) {
                    def nuggetContent = nuggetMap[(nuggetNameEntry.key)]
                    nugget = new Expando()
                    nugget.name = nuggetNameEntry.key
                    if (nuggetContent instanceof List) {
                        nugget.display = (nuggetContent as List).collect {
                            it instanceof Map ? it.display : it
                        }.join(', ')
                        nugget.values = (nuggetContent as List).collect {
                            it instanceof Map ? it.id : it
                        }.join(',')
                    } else {
                        nugget.display = nuggetContent.toString()
                    }
                    nugget.label = nuggetNameEntry.value
                    nugget.isLastSearch = false
                    nugget.isFilter = false
                    this.@searchNuggets << nugget
                }
            }
        }
        if (this.@searchNuggets) this.@searchNuggets.last()?.isLastSearch = true
        this.@searchNuggets
    }

    List getFilterNuggets() {
        if (!this.@filterNuggets) {
            def nugget = null
            this.@filterNuggets = []
            this.filterNuggetNames.eachWithIndex { nuggetNameEntry, index ->
                if (nuggetMap[(nuggetNameEntry.key)]) {
                    def nuggetContent = nuggetMap[(nuggetNameEntry.key)]
                    if (nuggetContent instanceof List) {
                        nuggetContent.each {
                            nugget = new Expando()
                            nugget.name = nuggetNameEntry.key
                            nugget.display = it instanceof Map ? it.display : it
                            nugget.values = it instanceof Map ? it.id : it
                            nugget.label = nuggetNameEntry.value
                            nugget.isFilter = true
                            nugget.isFirstFilter = false
                            nugget.isLastSearch = false
                            this.@filterNuggets << nugget
                        }
                    } else {
                        nugget = new Expando()
                        nugget.name = nuggetNameEntry.key
                        nugget.display = nuggetContent
                        nugget.values = nuggetContent
                        nugget.label = nuggetNameEntry.value
                        nugget.isFilter = true
                        nugget.isFirstFilter = false
                        nugget.isLastSearch = false
                        this.@filterNuggets << nugget
                    }
                }
            }
        }
        if (this.@filterNuggets) this.@filterNuggets.first()?.isFirstFilter = true
        this.@filterNuggets
    }

    List getAllNuggets() {
        if (!this.@allNuggets) {
            this.@allNuggets = []
            this.@allNuggets.addAll(getSearchNuggets())
            this.@allNuggets.addAll(getFilterNuggets())
            if (this.@allNuggets) {
                this.@allNuggets.last()?.isLastNugget = true
                this.@allNuggets.first()?.isFirstNugget = true
            }
        }
        this.allNuggets
    }

    boolean isJustQuery() {
        nuggetMap.queryString && nuggetMap.size() == 1
    }
}