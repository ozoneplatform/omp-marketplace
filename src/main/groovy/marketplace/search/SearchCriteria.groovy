package marketplace.search

import org.apache.log4j.Logger
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortOrder

class SearchCriteria implements Cloneable, Serializable {
    private static final log = Logger.getLogger(SearchCriteria.class)

    static final String SECONDARY_SORT = 'avgRate'
    static final SortOrder SECONDARY_ORDER = SortOrder.DESC
    static final String TERTIARY_SORT = 'sortTitle'
    static final SortOrder TERTIARY_ORDER = SortOrder.ASC

    static final Integer DEFAULT_AGGREGATION_SIZE = 100

    static final Collection<String> TYPES_TO_SEARCH = ['marketplace.ServiceItem', 'marketplace.ExtServiceItem']

    static final String[] TERM_AGGREGATIONS = ['types', 'categories', 'agencies']

    String sort
    String order = "asc"
    boolean aggregations = false
    def max
    def offset

    PredicateFactory predicateFactory

    /**
     * Collection of predicates form which search clause is generated.
     */
    Map<String, Predicate> predicateMap

    public SearchCriteria(params) {
        predicateFactory = PredicateFactory.instance

        predicateMap = predicateFactory.buildFiltersForRequestParams(params)
        predicateMap = predicateFactory.addQueryStringPredicateIfMissing(predicateMap)

        sort = params.sort
        order = params.order
        max = params.max
        offset = params.offset
        aggregations = params.aggregations
    }

    public updateBean(params) {
        if (params.sort) {
            sort = params.sort
        }
        if (params.order) {
            order = params.order
        }
        if (params.max) {
            max = params.max
        }
        if (params.offset) {
            offset = params.offset
        }
        if(params.aggregations) {
            aggregations = params.aggregations
        }

        // Add predicates from parameter map
        predicateMap.putAll(predicateFactory.buildFiltersForRequestParams(params))
    }

    public updateParams(params) {
        if (sort) {
            params.sort = sort
        }
        if (order) {
            params.order = order
        }
        if (max) {
            params.max = max
        }
        if (offset) {
            params.offset = offset
        }
        if(aggregations) {
            params.aggregations = aggregations
        }
        params
    }

    /**
     * Add a new search criterion for the given attribute. If this attribute already has values,
     * append to the list
     * @param field
     * @param val
     * @return
     */
    def addSearch(String field, String val) {
        log.info "OP-3759: addSearch $field, $val"
        if (this.predicateMap[(field)] && this.predicateMap[(field)] instanceof MultiValuePredicate) {
            MultiValuePredicate multiValueFilter = this.predicateMap[(field)]
            multiValueFilter.addValue(val)
        } else {
            replaceSearch(field, val)
        }
    }

    /**
     * Replace a search criterion values for the given attribute, if one exists
     * @param field
     * @param val
     * @return
     */
    def replaceSearch(String field, String val) {
        log.info "OP-3759: replaceSearch $field, $val"
        log.info "OP-3759: predicateMap = ${this.predicateMap}"
        this.predicateMap[(field)] = this.predicateFactory.getPredicateForParam(field, val)
    }

    /**
     * Remove a search criterion value for the given attribute. Remove the entire criterion if the last value is removed.
     * @param field
     * @param val
     * @return
     */
    def clearSearch(String field, String val) {
        if (val && this.predicateMap[(field)] && this.predicateMap[(field)] instanceof MultiValuePredicate) {
            MultiValuePredicate multiValuePredicate = this.predicateMap[(field)]
            multiValuePredicate.removeValue(val)
            if (!multiValuePredicate.hasValues()) this.predicateMap.remove(field)
        } else {
            this.predicateMap.remove(field)
        }
        this.predicateMap = this.predicateFactory.addQueryStringPredicateIfMissing(this.predicateMap)
    }

    /**
     * Builds search clause for ElasticSearch query based on the collection of predicates.
     * @return
     */
    def getSearchClause() {
        List<Predicate> allPredicates = predicateMap.values().toList()
        def result 
        if (allPredicates) {
            result = {
                bool {
                    filter {

                            bool {
                                allPredicates.each { Predicate query ->
                                    Closure searchClause = (Closure) query.getSearchClause()
                                    searchClause.delegate = delegate.delegate
                                    searchClause()

                                }
                            }
                        
                    }
                }
            }
        } else {
            result = {
                bool {
                    must {
                        query_string(default_field: "_all", query: "*")
                    }
                }
            }
        }

        result
    }

    /**
     * Return the named filter value as a property of this class
     * @param name
     * @return
     */
    def propertyMissing(String name) {
        if (predicateMap[(name)])
            return predicateMap[(name)].value
        else
            null
    }

    /**
     * Replace search predicate
     * @param name
     * @param value
     * @return
     */
    def propertyMissing(String name, value) {
        replaceSearch(name, value)
    }

    SearchSourceBuilder getExtraSearchSource() {
        SearchSourceBuilder source = new SearchSourceBuilder()

        addSort(source)

        if (aggregations) {
            TERM_AGGREGATIONS.each { String term ->
                source.aggregation(AggregationBuilders.nested("${term}", "${term}").subAggregation(
                    AggregationBuilders.terms("id").field("${term}.id").size(DEFAULT_AGGREGATION_SIZE)
                ))
            }
        }

        return source
    }

    def addSort(SearchSourceBuilder source) {
        //title is tokenized per word, but we want to sort on the whole title,
        //which is stored in the sortTitle field of the index
        if (sort == 'title') {
            sort = 'sortTitle'
        }

        if(sort == 'score') {
            source.sort(new ScoreSortBuilder().order(SortOrder.DESC))
        } else if(sort) {
            source.sort(new FieldSortBuilder(sort).order(SortOrder.valueOf(order.toUpperCase()) ?: SortOrder.ASC))
        }

        if(sort != SECONDARY_SORT) {
            source.sort(new FieldSortBuilder(SECONDARY_SORT).order(SECONDARY_ORDER))
        }

        if(sort != TERTIARY_SORT) {
            source.sort(new FieldSortBuilder(TERTIARY_SORT).order(TERTIARY_ORDER))
        }
    }

}
