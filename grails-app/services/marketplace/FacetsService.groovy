package marketplace

class AggregationsService {

    public Map<String, Object> extractAggregationInfo(result) {
        Map<String, Object> returnValue = [:]

        System.err.println result

        if (result.aggregations) {
            Map<String, Object> aggregations = result.aggregations
            def termCounts
            def rangeCounts

            // Use the comparator with a sorted map to assure the filters are sorted alphabetically by respective titles.
            def comparator = [compare:
                    { a, b ->
                        int val = a.title.compareTo(b.title)
                        if (val == 0)
                            return a.id.compareTo(b.id)
                        else
                            return val
                    }
            ] as Comparator

            // Currently have separate procedural logic for each type of aggregation returned
            if (aggregations.types) {
                System.err.println(aggregations.types)
                termCounts = aggregations.types.termCounts
                returnValue['types'] = new TreeMap<Types, Integer>(comparator)
                termCounts.each { entry ->
                    Types type = Types.get(Integer.valueOf(entry.term))
                    System.err.println entry
                    System.err.println type
                    System.err.println entry.term
                    returnValue['types'][(type)] = entry.count
                }
            }

            if (aggregations.state) {
                termCounts = aggregations.state.termCounts
                returnValue['states'] = new TreeMap<State, Integer>(comparator)
                termCounts.each { entry ->
                    State state = State.get(Integer.valueOf(entry.term))
                    returnValue['states'][(state)] = entry.count
                }
            }

            if (aggregations.categories) {
                termCounts = aggregations.categories.termCounts
                returnValue['categories'] = new TreeMap<Category, Integer>(comparator)
                termCounts.each { entry ->
                    Category category = Category.get(Integer.valueOf(entry.term))
                    returnValue['categories'][(category)] = entry.count
                }
            }

            // AML-680  agency attribute for a listing - Extract the agency aggregation data
            if (aggregations.agency) {
                termCounts = aggregations.agency.termCounts
                returnValue['agencies'] = new TreeMap<String, Integer>(comparator)
                termCounts.each { entry ->
                    Agency agency = Agency.get(Integer.valueOf(entry.term))
                    returnValue['agencies'][(agency)] = entry.count
                }
            }

            // Get custom field aggregations. Currently these are the only queryaggregations  AML-726
            Map customFieldAggregations = aggregations.findAll {
                it.value.type == 'query'
            }

            // Get domain aggregation subset of custom field aggregations.  AML-726
            if (customFieldAggregations.size() > 0) {
                // domainAggregations are aggregations whose names begin with "domain."
                Map domainAggregations = customFieldAggregations.findAll {
                    it.key.startsWith("domain.")
                }
                // If domain aggregations exist, get them.
                if (domainAggregations.size() > 0) {
                    returnValue['domain'] = new TreeMap<String, Integer>(comparator)
                    for (it in domainAggregations) {
                        termCounts = it.value.termCounts
                        termCounts.each { entry ->
                            // QueryAggregations are retrieved for every defined value of a custom field.
                            // We ignore any counts that are not greater than 0, since we don't
                            // want to display them
                            if (entry.count > 0) {
                                def domain = new Expando()
                                // Name is the string that follows the "domain." in the term field
                                String name = entry.term.tokenize('.')[1]
                                domain.id = name
                                domain.title = name
                                domain.titleDisplay = { title }
                                returnValue['domain'][(domain)] = entry.count
                            }
                        }
                    }
                }

            }

            // Reverse the rating order
            comparator = [compare:
                    { a, b -> b.compareTo(a) }
            ] as Comparator

            if (aggregations.rating) {
                rangeCounts = aggregations.rating.rangeCounts
                returnValue['ratings'] = new TreeMap<Integer, Integer>(comparator)
                rangeCounts.each { entry ->
                    returnValue['ratings'][(entry.from as int)] = entry.count
                }
            }
        }
        returnValue
    }

}
