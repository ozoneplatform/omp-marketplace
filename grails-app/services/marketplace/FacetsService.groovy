package marketplace

class FacetsService {

    public Map<String, Object> extractFacetInfo(result) {
        Map<String, Object> returnValue = [:]

        System.err.println result

        if (result.facets) {
            Map<String, Object> facets = result.facets
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

            // Currently have separate procedural logic for each type of facet returned
            if (facets.types) {
                System.err.println(facets.types)
                termCounts = facets.types.termCounts
                returnValue['types'] = new TreeMap<Types, Integer>(comparator)
                termCounts.each { entry ->
                    Types type = Types.get(Integer.valueOf(entry.term))
                    System.err.println entry
                    System.err.println type
                    System.err.println entry.term
                    returnValue['types'][(type)] = entry.count
                }
            }

            if (facets.state) {
                termCounts = facets.state.termCounts
                returnValue['states'] = new TreeMap<State, Integer>(comparator)
                termCounts.each { entry ->
                    State state = State.get(Integer.valueOf(entry.term))
                    returnValue['states'][(state)] = entry.count
                }
            }

            if (facets.categories) {
                termCounts = facets.categories.termCounts
                returnValue['categories'] = new TreeMap<Category, Integer>(comparator)
                termCounts.each { entry ->
                    Category category = Category.get(Integer.valueOf(entry.term))
                    returnValue['categories'][(category)] = entry.count
                }
            }

            // AML-680  agency attribute for a listing - Extract the agency facet data
            if (facets.agency) {
                termCounts = facets.agency.termCounts
                returnValue['agencies'] = new TreeMap<String, Integer>(comparator)
                termCounts.each { entry ->
                    Agency agency = Agency.get(Integer.valueOf(entry.term))
                    returnValue['agencies'][(agency)] = entry.count
                }
            }

            // Get custom field facets. Currently these are the only queryfacets  AML-726
            Map customFieldFacets = facets.findAll {
                it.value.type == 'query'
            }

            // Get domain facet subset of custom field facets.  AML-726
            if (customFieldFacets.size() > 0) {
                // domainFacets are facets whose names begin with "domain."
                Map domainFacets = customFieldFacets.findAll {
                    it.key.startsWith("domain.")
                }
                // If domain facets exist, get them.
                if (domainFacets.size() > 0) {
                    returnValue['domain'] = new TreeMap<String, Integer>(comparator)
                    for (it in domainFacets) {
                        termCounts = it.value.termCounts
                        termCounts.each { entry ->
                            // QueryFacets are retrieved for every defined value of a custom field.
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

            if (facets.rating) {
                rangeCounts = facets.rating.rangeCounts
                returnValue['ratings'] = new TreeMap<Integer, Integer>(comparator)
                rangeCounts.each { entry ->
                    returnValue['ratings'][(entry.from as int)] = entry.count
                }
            }
        }
        returnValue
    }

}
