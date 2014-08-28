/**
 * This changelog include the changesets for the 7.10 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.10.0-1*
 *  context: create|upgrade-only, 7.10.0
 */

databaseChangeLog = {
    changeSet(author: "marketplace", dbms: "hsqldb, mssql, mysql, postgresql", id: "7.10.0-1", context: "create, 7.10.0") {
        addColumn(tableName: "service_item") {
            column(name: "opens_in_new_browser_tab", type: "java.sql.Types.BOOLEAN")
        }
    }


    changeSet(author: "marketplace", dbms: "hsqldb, mssql, mysql, postgresql", id: "7.10.0-2", context: "create, 7.10.0") {
        addColumn(tableName: "profile") {
            column(name: "user_roles", type: "java.sql.Types.VARCHAR(255)")
        }
    }

    changeSet(author: "marketplace", dbms: "mssql, mysql, postgresql", id: "7.10.0-4", context: "upgrade-only, 7.10.0") {
        update(tableName: "service_item") {
            column(name: "opens_in_new_browser_tab", valueBoolean: "false")
        }
    }
}
