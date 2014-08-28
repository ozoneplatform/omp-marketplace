package marketplace

import org.springframework.context.MessageSource
import marketplace.search.SearchCriteria

class SearchNuggetService {

    boolean transactional = false
    MessageSource messageSource

    private Map ratingNuggets

    def nuggetize(SearchCriteria searchBean) {
        def nuggets = new SearchNuggets()

        if (searchBean.queryString) {
            nuggets.addNugget('queryString', searchBean.queryString)
        }

        // Don't use getAll because it bypasses the second level cache
        if (searchBean.categoryIDs) {
            nuggets.addNugget('categoryIDs', searchBean.categoryIDs.collect { id ->
                convertToNugget(Category.get(id))
            })
        }

        if (searchBean.typeIDs) {
            nuggets.addNugget('typeIDs', searchBean.typeIDs.collect { id ->
                convertToNugget(Types.get(id))
            })
        }

        if (searchBean.stateIDs) {
            nuggets.addNugget('stateIDs', searchBean.stateIDs.collect { id ->
                convertToNugget(State.get(id))
            })
        }

        if (searchBean.statuses) {
            nuggets.addNugget('statuses', searchBean.statuses)
        }

        if (searchBean.rating) {
            nuggets.addNugget('rating', getRatingNugget(searchBean.rating as int))
        }

        if (searchBean.releasedFromDate) {
            nuggets.addNugget('releasedFromDate', Helper.convertDate(searchBean.releasedFromDate, "yyyy-MM-dd", "MM/dd/yyyy"))
        }

        if (searchBean.releasedToDate) {
            nuggets.addNugget('releasedToDate', Helper.convertDate(searchBean.releasedToDate, "yyyy-MM-dd", "MM/dd/yyyy"))
        }

        if (searchBean.scoreCardValue && (searchBean.scoreCardValue as int) > 0) {
            nuggets.addNugget('scoreCardValue', searchBean.scoreCardValue)
        }

        if (searchBean.scoreCardHighValue && (searchBean.scoreCardHighValue as int) < ScoreCard.MAX_VALUE) {
            nuggets.addNugget('scoreCardHighValue', searchBean.scoreCardHighValue)
        }

        if (searchBean.categoryFilters) {
            nuggets.addNugget('categoryFilters', searchBean.categoryFilters.collect { id ->
                convertToNugget(Category.get(id))
            })
        }

        if (searchBean.typeFilters) {
            nuggets.addNugget('typeFilters', searchBean.typeFilters.collect { id ->
                convertToNugget(Types.get(id))
            })
        }

        if (searchBean.stateFilters) {
            nuggets.addNugget('stateFilters', searchBean.stateFilters.collect { id ->
                convertToNugget(State.get(id))
            })
        }

        // AML-680  agency attribute for a listing
        if (searchBean.agencyFilters) {
            nuggets.addNugget('agencyFilters', searchBean.agencyFilters)

        }

        // AML-726 domain attribute for a listing
        if (searchBean.domainFilters) {
            nuggets.addNugget('domainFilters', searchBean.domainFilters.collect { it })

        }

        // AML-2608 owfWidgetType
        if (searchBean.owfWidgetType) {
            nuggets.addNugget('owfWidgetType', searchBean.owfWidgetType)
        }

        return nuggets
    }


    def convertToNugget(def domain) {
        ['id': domain.id, 'title': domain.title, 'display': domain.titleDisplay()]
    }

    /**
     * Return the rating nugget. Cache the 5 rating nuggets after the first call.
     * @param rating
     * @return
     */
    def getRatingNugget(rating) {
        if (this.ratingNuggets == null) {
            initRatingNuggets()
        }
        this.ratingNuggets[(rating)]
    }

    def initRatingNuggets() {
        this.ratingNuggets = [:]
        (1..5).each { val ->
            String message = messageSource.getMessage('filter.rating.' + val, null, Locale.US)
            this.ratingNuggets[(val)] = [['id': val, 'title': message, 'display': message]]
        }
    }
}

