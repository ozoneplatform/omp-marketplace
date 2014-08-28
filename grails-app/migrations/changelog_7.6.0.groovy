/** * This changelog include the changesets for the 7.6 release of Marketplace.  * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.6.0-1*
 *  context: create|upgrade-only, 7.6.0
 */

databaseChangeLog = {
    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb',
            id: '7.6.0-1', context: 'create, 7.6.0') {
        createTable(tableName: 'screenshot') {
            column(name: 'id', autoIncrement: 'true', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'screenshotPK')
            }

            column(name: 'small_image_url', type: 'varchar(2083)') {
                constraints(nullable: false)
            }
            column(name: 'large_image_url', type: 'varchar(2083)')
            column(name: 'ordinal', type: 'java.sql.Types.INTEGER')

            column(name: 'service_item_id', type: 'java.sql.Types.BIGINT')

            column(name: "created_by_id", type: '${marketplace.profileId}')
            column(name: "created_date", type: "java.sql.Types.DATE")
            column(name: "edited_by_id", type: '${marketplace.profileId}')
            column(name: "edited_date", type: "java.sql.Types.DATE")
            column(name: "version", type: "java.sql.Types.BIGINT",
                    defaultValueNumeric: 0) {
                constraints(nullable: "false")
            }
        }

        addForeignKeyConstraint(baseTableName: 'screenshot', baseColumnNames: 'service_item_id',
            referencedTableName: 'service_item', referencedColumnNames: 'id',
            onDelete: 'CASCADE', constraintName: 'SCREENSHOT_SERVICE_ITEM_FK')
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb',
            id: '7.6.0-2', context: 'create, 7.6.0') {
        //migrate data from old screenshot columns and drop those columns
        sql("""
            INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot1url, 0, id
                FROM service_item
                WHERE screenshot1url IS NOT NULL AND screenshot1url <> ''
        """)

        sql("""
            INSERT INTO screenshot (small_image_url, ordinal, service_item_id)
                SELECT screenshot2url, 1, id
                FROM service_item
                WHERE screenshot2url IS NOT NULL AND screenshot2url <> ''
        """)

        dropColumn(tableName: 'service_item', columnName: 'screenshot1url')
        dropColumn(tableName: 'service_item', columnName: 'screenshot2url')
    }
}
