package marketplace.search

import org.apache.log4j.Logger

class PredicateFactory implements Serializable {
    Map<String, AbstractPredicate> predicateTemplates

    private static PredicateFactory instance

    private static final log = Logger.getLogger(PredicateFactory.class)

    private PredicateFactory() {

        // AML-2608 Added owfWidgetType, a field in owfProperties
        predicateTemplates = [
                queryString: new QueryStringPredicate(predicateName: "query", indexFieldName: "_all", paramName: "queryString"),
                categoryIDs: new MultiValuePredicate(predicateName: "category", indexFieldName: 'category.id', paramName: "categoryIDs", operator: MultiValuePredicate.Operator.OR),
				typeIDs: new MultiValuePredicate(predicateName: 'types', indexFieldName: "types.id", paramName: "typeIDs", operator: MultiValuePredicate.Operator.OR),
				agencyIDs: new MultiValuePredicate(predicateName: "agency", indexFieldName: "agency.id", paramName: "agencyIDs", operator: MultiValuePredicate.Operator.OR),
                stateIDs: new MultiValuePredicate(predicateName: "state", indexFieldName: "state.id", paramName: "stateIDs", operator: MultiValuePredicate.Operator.OR),
                categoryFilters: new MultiValueFilterPredicate(predicateName: "categories", indexFieldName: "categories.id", paramName: "categoryFilters"),
				typeFilters: new MultiValueFilterPredicate(predicateName: "types", indexFieldName: "types.id", paramName: "typeFilters"),
				agencyFilters: new MultiValueFilterPredicate(predicateName: "agencies", indexFieldName: "agency.id", paramName: "agencyFilters"),
                stateFilters: new MultiValuePredicate(predicateName: "state", indexFieldName: "state.id", paramName: "stateFilters"),
                domainFilters: new MultiValuePredicate(predicateName: "domain", indexFieldName: "domain", paramName: "domainFilters", isCustomField: true),
                statuses: new MultiValuePredicate(predicateName: "status", indexFieldName: "approvalStatus", paramName: "statuses", operator: MultiValuePredicate.Operator.OR),
                rating: new RangePredicate(predicateName: "rating", indexFieldName: "avgRate", lowerBoundaryParamName: "rating"),
                scoreCardValue: new RangePredicate(predicateName: "scoreCard", indexFieldName: "scoreCard.score", lowerBoundaryParamName: "scoreCardValue"),
                releasedFromDate: new RangePredicate(predicateName: "fromDate", indexFieldName: "releaseDate", lowerBoundaryParamName: "releasedFromDate"),
                scoreCardHighValue: new RangePredicate(predicateName: "scoreCard", indexFieldName: "scoreCard.score", upperBoundaryParamName: "scoreCardHighValue"),
                releasedToDate: new RangePredicate(predicateName: "fromDate", indexFieldName: "releaseDate", upperBoundaryParamName: "releasedToDate"),
                state_isPublished: new SingleValuePredicate(predicateName: "isPublished", indexFieldName: "state.isPublished", paramName: "state_isPublished"),
                types_ozoneAware: new SingleValuePredicate(predicateName: "ozoneAware", indexFieldName: "types.ozoneAware", paramName: "types_ozoneAware"),
                accessType: new AccessTypePredicate(predicateName: "accessType", paramName: "accessType"),
                enabled_only: new IsEnabledPredicate(predicateName: "enabled", paramName: "enabled_only"),
                outside_only: new SingleValuePredicate(predicateName: "isOutside", indexFieldName: "isOutside", paramName: "outside_only"),
                serviceItemUuid: new SingleValuePredicate(predicateName: "serviceItemUuid", indexFieldName: "uuid", paramName: "serviceItemUuid") ,
                owfWidgetType: new SingleValuePredicate(predicateName: "owfWidgetType", indexFieldName: "owfProperties.owfWidgetType", paramName: "owfWidgetType"),
                type: new ExactValuePredicate(predicateName: "types", indexFieldName: "types.title", paramName: "type"),
                category: new MultiExactValuePredicate(predicateName: "type", indexFieldName: "categories.title", paramName: "category"),
                agency: new MultiValuePredicate(predicateName: "type", indexFieldName: "agency.title", paramName: "agency")
        ]
    }

    static PredicateFactory getInstance() {
        if (!instance) instance = new PredicateFactory()
        instance
    }

    Map<String, Predicate> buildFiltersForRequestParams(params) {

        Map<String, Predicate> result = [:]

        def predicate
        predicateTemplates.keySet().each { paramName ->
            if (params[(paramName)]) {
                predicate = predicateTemplates[(paramName)].clone().initializeFromParameters(params)
                if (predicate) result[(paramName)] = predicate
            }
        }

        result
    }

    Map<String, Predicate> addQueryStringPredicateIfMissing(Map<String, Predicate> predicateMap) {
        if (!predicateMap.containsKey("queryString")) {
            predicateMap.queryString = predicateTemplates.queryString.clone()
        }
        predicateMap
    }

    Predicate getPredicateForParam(def paramName, def value) {
        Predicate predicate = predicateTemplates[(paramName)]
        if (predicate) {
            return predicate.clone().initializeFromParameters([(paramName): value])
        }
    }
}
