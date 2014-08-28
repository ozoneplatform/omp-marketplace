package marketplace

import org.hibernate.criterion.*

class GenericQueryService extends OzoneService {
    static def modelFields = [(ServiceItem.class): ServiceItem.class.declaredFields.collect { f -> f.name }]
    static def stringCondition = { field, value, criteria ->
        def m = value =~ /\*(.+)\*/
        if (m.matches())
            criteria.ilike(field, "%${m[0][1]}%")
        else
            criteria.eq(field, value)
    }
    static def opsMap = ['<': 'lt', '<=': 'le', '>': 'gt', '>=': 'ge', '<>': 'ne']
    static def intCondition = { field, value, criteria ->
        def pair = getConditionAndValue(value)
        criteria."${pair[0]}"(field, Integer.parseInt(pair[1]))
    }
    static def longCondition = { field, value, criteria ->
        def pair = getConditionAndValue(value.toString())
        Long val = pair[1] instanceof Long ? pair[1] : Long.parseLong(pair[1])
        criteria."${pair[0]}"(field, val)
    }

    static def booleanCondition = { field, value, criteria ->
        boolean val = value instanceof Boolean ? value : Boolean.parseBoolean(value)
        criteria.eq(field, val)
    }

    static def conditionMap = [(String.class): stringCondition, (Integer.class): intCondition,
        (Long.class): longCondition, (boolean): booleanCondition]

    // Restriction utilities
    static def longRestriction = { colName, field, value ->
        def pair = getConditionAndValue(value.toString())
        Long val = pair[1] instanceof Long ? pair[1] : Long.parseLong(pair[1])
        return Restrictions."${pair[0]}"(colName, val)
    }
    static def stringRestriction = { colName, field, value ->
        def m = value =~ /\*(\w+)\*/
        if (m.matches())
            Restrictions.ilike(colName, "%${m[0][1]}%")
        else
            Restrictions.eq(colName, value)
    }
    static def intRestriction = { colName, field, value ->
        def pair = getConditionAndValue(value)
        Restrictions."${pair[0]}"(colName, Integer.parseInt(pair[1]))
    }
    static def restrictionMap = [(String.class): stringRestriction, (Long.class): longRestriction]

    static def getConditionAndValue(value) {
        def m = value.trim() =~ /(?:(<|<=|>|>=|<>)\s+)?(\d+)/
        def op
        def val
        if (m.matches()) {
            val = m[0][2]
            op = m[0][1] ? opsMap[m[0][1]] : 'eq'
        }
        return [op, val]
    }

    boolean transactional = false

    def serviceItems(params) {
        log.debug "serviceItems: params = ${params}"
        checkReturnSettings(params)
        def model = [:]
        def c = ServiceItem.createCriteria()
        def useSort = false
        if (params.sort) {
            useSort = true
            params.sort_0 = params.sort
            params.remove("sort")
        }

        def serviceItemList = c.list(params) {
            and {
                addAccessConstraints(c, params)
                addVisibilityConstraints(c, params)
                addFieldConstraints(c, params)
                addSorting(c, params)
                addAuthor(c, params)
            }
        }
        if (useSort) {
            params.sort = params.sort_0
            params.remove("sort_0")
        }
        model.put("serviceItemList", serviceItemList)
        model.put("listSize", serviceItemList.totalCount)
        model.put("params", params)

        return model
    }

    //This is to get counts instead of the detail items
    def serviceItemsCount(params) {
        log.debug "serviceItemsCount: params = ${params}"

        def c = ServiceItem.createCriteria()

        def serviceItemList = c {
            projections {
                createAlias(params.countProperty, params.countProperty)
                groupProperty(params.countProperty)
                count(params.countProperty)
            }
            //TODO, loop through params, create new map with all params other than count/group, then use it to add restrictions with addFieldContstraints
        }

        return serviceItemList
    }


    void checkReturnSettings(params) {
        params.max = params.max ?: 5
        params.offset = params.offset ?: 0
    }

    def getAuthorParm(params) {
        return params.find { it -> it.key.startsWith('author') }
    }

    void addAccessConstraints(def c, def params) {
        def session = getSession()

        def accessType = params.accessType ?: session.accessType
        if (accessType == Constants.VIEW_USER) {
            def val = c.and {
                or {
                    ilike('approvalStatus', Constants.APPROVAL_STATUSES["APPROVED"])
                    owners {
                        eq('username', session.username)
                    }
                }
                and {
                    or {
                        eq('isHidden', 0)
                        owners {
                            eq('username', session.username)
                        }
                    }
                }
            }
        }
    }

    void addVisibilityConstraints(def c, def params) {

        // can be constrained by outside_only param
        // or by isOutside = false AND userInListingAgency = false
        // userInListingAgency = listingAgency == userAgency
        // Right now we are only checking against outside_only param since other infrastructure is not yet available

        if (params.outside_only) {
            def val = c.and {
                eq('isOutside', params.outside_only)
            }
        }
    }

    /**
     * Returns a sorted list of paramter values
     * For example
     *
     * sort_2="A", sort_1="B", sort_0="C",
     *
     * returns "C,B,A"
     *
     */
    private def getParameterValuesInOrder(def prefix, def params) {
        def map = new TreeMap()
        params.each { param ->
            if (param.key.startsWith(prefix)) {
                List keys = Arrays.asList(param.key.split('_'))
                if (keys.size() > 1) {
                    if (keys[1].isInteger()) {
                        map[keys[1].toInteger()] = param.value
                    }
                } else {
                    map[0] = param.value
                }
            }
        }
        return map.values().asList()
    }

    /**
     *  If you are only sorting by one field then just use the standard gorm sort="avgRate" order="desc"
     *
     *  For a secondary sort:
     *
     *  sort_0: "avgRate", sort_1: "totalVotes", order_0: "desc", order_1: "desc"
     *
     */
    void addSorting(def c, def params) {
        def sortParams = getParameterValuesInOrder("sort", params)
        def orderParams = getParameterValuesInOrder("order", params)
        def i = 0
        if (sortParams.size() > 0) {
            sortParams.each { sort ->
                def order = orderParams[i]
                if (order) addSortCondition(c, sort, order)
                else addSortCondition(c, sort)
                i++
            }
        } else {
            addSortCondition(c, 'title', 'asc')
        }
    }

    void addSortCondition(def c, def field, def order = "asc") {
        def fields = field?.split('_')
        if (fields) {
            if (fields.length == 2) {
                c."${fields[0]}" {
                    c.order(fields[1], order)
                }
            } else
                c.order(fields[0], order)
        }
    }

    void addFieldConstraints(c, params) {
        def ord = [:]
        params.each { param ->
            List keys = Arrays.asList(param.key.split('_'))
            def value = param.value
            def field = keys[0] == 'or' ? keys[1] : keys[0]
            if (isField(field)) {
                if (keys[0] == 'or') {
                    ord[keys.tail()] = value
                } else {
                    if (value.class.isArray()) {
                        c.or {
                            value.each { v ->
                                addCondition(keys, v, c, ServiceItem.class)
                            }
                        }
                    } else {
                        c.and {
                            addCondition(keys, value, c, ServiceItem.class)
                        }
                    }
                }
            }
        }

        if (ord) {
            c.or {
                ord.each { key, value ->
                    addCondition(key, value, c, ServiceItem.class)
                }
            }
        }
    }

    private def isHasManyField(field, klass) {
        return klass?.hasMany[field]
    }

    private void addCondition(List keys, v, c, klass) {
        if (keys.size() == 1)
            addCondition(keys[0], v, c, getFieldClass(keys[0], klass))
        else {
            def relClass = isHasManyField(keys[0], klass)
            if (relClass) {
                def detachedCriteria = DetachedCriteria.forClass(ServiceItem.class).setProjection(Property.forName("id"))

                def alias = "${keys[0]}_alias"
                def fclass = getFieldClass(keys[1], relClass)
                detachedCriteria.createAlias(keys[0], alias).add(getRestriction("${alias}.${keys[1]}", keys[1], v, fclass))

                c.and {
                    criteria.add(Subqueries.propertyIn("id", detachedCriteria))
                }
            } else {
                c."${keys[0]}" {
                    addCondition(keys.tail(), v, c, getFieldClass(keys[0], klass))
                }
            }
        }
    }

    private def getRestriction(def colName, def field, def value, def klass) {
        return restrictionMap[klass](colName, field, value)
    }

    private void addCondition(String field, v, c, klass) {
        if (klass) conditionMap[klass](field, v, c)
    }

    private def isField(field) {
        try {
            return ServiceItem.class.getDeclaredField(field)
        } catch (Exception e) {
            return null
        }
    }

    private def getFieldClass(field, model) {
        /* def fieldList = modelFields[model]
        if (!fieldList)
        {
        fieldList = model.declaredFields.collect{ f -> f.name }
        modelFields[model] = fieldList
        }*/
        try {
            return model.getDeclaredField(field).type
        }
        catch (Exception e) {
            return null
        }
    }

    private addAuthor(c, params) {
        if (params.author_displayName) {
            c.and {
                owners {
                    eq('displayName', params.author_displayName)
                }
            }
        } else if (params.author_username) {
            c.and {
                owners {
                    eq('username', params.author_username)
                }
            }
        } else if (params.author_id) {
            c.and {
                owners {
                    eq('id', params.author_id as long)
                }
            }
        }
    }
}
