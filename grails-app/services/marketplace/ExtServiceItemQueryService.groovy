package marketplace

import org.hibernate.criterion.*

class ExtServiceItemQueryService extends OzoneService {

    AccountService accountService

    static final stringCondition = { field, value, criteria ->
        def m = value =~ /\*(.+)\*/
        if (m.matches())
            criteria.ilike(field, "%${m[0][1]}%")
        else
            criteria.eq(field, value)
    }
    static final opsMap = ['<': 'lt', '<=': 'le', '>': 'gt', '>=': 'ge']
    static final intCondition = { field, value, criteria ->
        def pair = getConditionAndValue(value)
        criteria."${pair[0]}"(field, Integer.parseInt(pair[1]))
    }
    static final longCondition = { field, value, criteria ->
        Long val = value instanceof Long ? value : Long.parseLong(value)
        criteria.eq(field, val)
    }

    static final booleanCondition = { field, value, criteria ->
        boolean val = value instanceof Boolean ? value : Boolean.parseBoolean(value)
        criteria.eq(field, val)
    }

    static final conditionMap = [(String.class): stringCondition, (Integer.class): intCondition,
        (Long.class): longCondition, (boolean): booleanCondition]

    // Restriction utilities
    static final longRestriction = { colName, field, value ->
        Long val = value instanceof Long ? value : Long.parseLong(value)
        return Restrictions.eq(colName, val)
    }
    static final stringRestriction = { colName, field, value ->
        def m = value =~ /\*(\w+)\*/
        if (m.matches())
            Restrictions.ilike(colName, "%${m[0][1]}%")
        else
            Restrictions.eq(colName, value)
    }
    static final intRestriction = { colName, field, value ->
        def pair = getConditionAndValue(value)
        Restrictions."${pair[0]}"(colName, Integer.parseInt(pair[1]))
    }
    static final restrictionMap = [(String.class): stringRestriction, (Long.class): longRestriction]

    static def getConditionAndValue(value) {
        def m = value.trim() =~ /(?:(<|<=|>|>=)\s+)?(\d+)/
        def op
        def val
        if (m.matches()) {
            val = m[0][2]
            op = m[0][1] ? opsMap[m[0][1]] : 'eq'
        } else
            return [op, val]
    }

    boolean transactional = false

    def getItems(params, exemplar) {
        log.info "serviceItems: params = ${params}"
        Class classIn = exemplar.class
        //  checkAccessType(params)
        checkReturnSettings(params)
        def model = [:]
        def c = exemplar.createCriteria()
        def useSort = false
        if (params.sort) {
            useSort = true
            params.sort_0 = params.sort
            params.remove("sort")
        }
        def itemList = c.list(params) {
            and {
                addAccessConstraints(c, params)
                addFieldConstraints(c, params, classIn)
                addSorting(c, params)
            }
        }
        if (useSort) {
            params.sort = params.sort_0
            params.remove("sort_0")
        }
        model.put("serviceItemList", itemList)
        model.put("listSize", itemList.totalCount)
        model.put("params", params)

        return model
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

        if (accountService.isExtAdmin()) {
            log.debug "addAccessConstraints: for external admin ${session.username}"
            // TODO: add in check for enabled
            //                         eq('isHidden',0)
            c.and {
                or {
                    ilike('approvalStatus', Constants.APPROVAL_STATUSES["APPROVED"])
                    owners {
                        eq('username', session.username)
                    }
                    createdBy {
                        eq('username', session.username)
                    }
                }
            }
            return
        }

        def accessType = params.accessType ?: session.accessType
        if (accessType == Constants.VIEW_USER) {
            c.and {
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

    private def isServiceItemField(field) {
        try {
            return ServiceItem.class.getDeclaredField(field)
        } catch (Exception e) {
            return null
        }
    }

    void addFieldConstraints(c, params, Class classIn) {
        log.debug 'addFieldConstraints:'
        def ord = [:]
        params.each { param ->
            List keys = Arrays.asList(param.key.split('_'))
            def value = param.value
            def field = keys[0] == 'or' ? keys[1] : keys[0]
            log.debug "2 field = ${field}  isField = ${isField(field, classIn)}"
            //log.debug "2 field = ${field}  isServiceItemField = ${isServiceItemField(field)}"
            if (isField(field, classIn)) {
                log.debug 'it was a field!'
                if (keys[0] == 'or') {
                    ord[keys.tail()] = value
                } else {
                    if (value.class.isArray()) {
                        c.or {
                            value.each { v ->
                                addCondition(keys, v, c, classIn)
                            }
                        }
                    } else {
                        c.and {
                            addCondition(keys, value, c, classIn)
                        }
                    }
                }
            }
        }

        if (ord) {
            c.or {
                ord.each { key, value ->
                    addCondition(key, value, c, classIn)
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

    private def isField(field, Class classIn) {
        //log.debug "isField: field = ${field}  classIn = ${classIn}"
        try {
            // Note that the fields in grails domain objects are private and are accessed by
            // dynamically created accessors.
            return classIn.getDeclaredField(field)
        }
        catch (Exception e) {
            Class superClass = classIn.getSuperclass()
            //log.debug "isField: superClass = ${superClass}"
            if (superClass && superClass != Object.class) {
                return isField(field, superClass)
            }
        }

        return null
    }

    private def getServiceItemFieldClass(field) {
        try {
            log.debug "getServiceItemFieldClass: field = ${field}  type = ${ServiceItem.class.getDeclaredField(field).type}"
            return ServiceItem.class.getDeclaredField(field).type
        }
        catch (Exception e) {
            return null
        }
    }

    private def getFieldClass(field, classIn) {
        try {
            log.debug "getFieldClass: field = ${field}  class = ${classIn}  type = ${classIn.getDeclaredField(field).type}"
            return classIn.getDeclaredField(field).type
        }
        catch (Exception e) {
            Class superClass = classIn.getSuperclass()
            //log.debug "isField: superClass = ${superClass}"
            if (superClass && superClass != Object.class) {
                return getFieldClass(field, superClass)
            }
        }

        return null
    }

}
