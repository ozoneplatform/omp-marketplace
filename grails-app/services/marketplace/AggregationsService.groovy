package marketplace

import org.elasticsearch.search.aggregations.bucket.terms.Terms


class AggregationsService {

    public Map<String, Object> extractAggregationInfo(result) {
        Map<String, Object> returnValue = [:]

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
                termCounts = aggregations.types.getAggregations().id.buckets
                returnValue['types'] = new TreeMap<Types, Integer>(comparator)
                termCounts.each { entry ->
                    Types type = Types.get(Long.valueOf(entry.getKey()))
                    returnValue['types'][(type)] = entry.getDocCount()
                }
            }

            if (aggregations.state) {
                termCounts = aggregations.state.getAggregations().id.buckets
                returnValue['states'] = new TreeMap<State, Integer>(comparator)
                termCounts.each { entry ->
                    State state = State.get(Integer.valueOf(entry.getKey()))
                    returnValue['states'][(state)] = entry.getDocCount()
                }
            }

            if (aggregations.categories) {
                termCounts = aggregations.categories.getAggregations().id.buckets
                returnValue['categories'] = new TreeMap<Category, Integer>(comparator)
                termCounts.each { entry ->
                    Category category = Category.get(Integer.valueOf(entry.getKey()))
                    returnValue['categories'][(category)] = entry.getDocCount()
                }
            }

            // AML-680  agency attribute for a listing - Extract the agency aggregation data
            if (aggregations.agency) {                
                termCounts = aggregations.types.getAggregations().id.buckets
                returnValue['agencies'] = new TreeMap<String, Integer>(comparator)
                termCounts.each { entry ->
                    Agency agency = Agency.get(Integer.valueOf(entry.getKey()))
                    returnValue['agencies'] = entry.getDocCount()
                }
            }

            // Get custom field aggregations. Currently these are the only queryaggregations  AML-726
            Map customFieldAggregations = aggregations.findAll {
                it.value.name == 'query'
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
                        termCounts = it.value.buckets
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
