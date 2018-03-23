package marketplace.search

/**
 * A super-class of all predicates. A predicate corresponds to an ElasticSearch criterion.
 */
abstract class AbstractPredicate implements Predicate, Cloneable, Serializable {

    /**
     * A logical predicate name
     */
    String predicateName

    /**
     * The name of a corresponding field in ElasticSearch index
     */
    String indexFieldName

    /**
     * The name of the parameter in the parameter map containing the value for this search criterion
     */
    String paramName

    /**
     * Optional extra data, in case the predicate relies more than on one parameter
     */
    Map<String, String> extraData

    /**
     * If true, this predicate forms an ElasticSearch filter - a search criterion that does not affect the score.
     * Most predicates will be filters.
     */
    boolean isFilter = true

    boolean isCustomField = false

    /**
     * Initializes the predicate state from the parameter map
     * @param params
     * @return
     */
    abstract Predicate initializeFromParameters(params)

    /**
     * Returns a string representation of the predicate value
     * @return
     */
    abstract String getStringValue()

    /**
     * Allows population of extra data using Map semantic
     * @param name
     * @param value
     * @return
     */
    def propertyMissing(String name, value) {
        if (!extraData) extraData = [:]
        extraData[(name)] = value
    }

    /**
     * Allows access to extra parameters using Map semantic
     * @param name
     * @return
     */
    def propertyMissing(String name) {
        if (extraData) {
            extraData[(name)]
        } else {
            null
        }
    }

    boolean isFilter() {
        return isFilter
    }
}
