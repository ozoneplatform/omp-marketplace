/**
 * This changelog include the changesets for the 7.1 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.1-1*
 *  context: create|ugrade-only
 */

databaseChangeLog = {

    changeSet(author: "marketplace", id: "7.1-1", dbms: "mssql, mysql, oracle, postgresql", context: "create, 7.1") {
        addColumn(tableName: "owf_properties") {
            column(name: "height", type: "java.sql.Types.BIGINT")
        }

        addColumn(tableName: "owf_properties") {
            column(name: "width", type: "java.sql.Types.BIGINT")
        }
    }

    changeSet(author: "marketplace", id: "7.1-2", dbms: "mssql, mysql, oracle, postgresql", context: "create, 7.1") {
        addColumn(tableName: "application_configuration") {
            column(name: "help", type: "java.sql.Types.VARCHAR(2000)")
        }
    }

}
