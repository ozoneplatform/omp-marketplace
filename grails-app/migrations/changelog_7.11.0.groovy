/**
 * This changelog include the changesets for the 7.11.0 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.11.0-1*
 *  context: create|upgrade-only, 7.11.0
 */

databaseChangeLog = {
    changeSet(author: 'marketplace', dbms: 'hsqldb, mssql, mysql, postgresql', id: '7.11.0-1', context: 'upgrade-only, 7.11.0') {
        createTable(tableName: 'change_detail_temp') {
            comment('create temporary table to hold change detail information while the tables are modified')

            column(autoIncrement: 'true', name: 'id', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'change_detail_tempPK')
            }
            column(name: 'service_item_activity_id', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false')
            }
            column(name: 'new_value', type: 'java.sql.Types.VARCHAR(4000)')
            column(name: 'old_value', type: 'java.sql.Types.VARCHAR(4000)')
            column(name: 'field_name', type: 'java.sql.Types.VARCHAR(255)') {
                constraints(nullable: 'false')
            }
        }

        sql("""
            INSERT INTO change_detail_temp(new_value, old_value, field_name, service_item_activity_id)
                SELECT change_detail.new_value, change_detail.old_value, change_detail.field_name, service_item_activity.id
                FROM change_detail INNER JOIN service_item_activity
                    ON change_detail.object_id=service_item_activity.service_item_id
                        AND change_detail.object_version=service_item_activity.service_item_version
                WHERE service_item_activity.action='MODIFIED';
        """)

        sql("""
            INSERT INTO change_detail_temp(service_item_activity_id, new_value, field_name)
                SELECT id, details, 'Scorecard Response'
                FROM service_item_activity
                WHERE details!='null' AND action='LOCAL_SCORECARD_QUESTION_UPDATED';
        """)

        sql("""
            DELETE FROM change_detail
        """)
    }

    changeSet(author: 'marketplace', dbms: 'hsqldb, mssql, mysql, postgresql', id: '7.11.0-2', context: 'create, 7.11.0') {
        addColumn(tableName: 'change_detail') {
            column(name: 'service_item_activity_id', type: '${marketplace.legacyIdField}') {
                constraints(nullable: 'false')
            }
        }

        createIndex(indexName: 'FKB4467BC0855307BD', tableName: 'change_detail') {
            column(name: 'service_item_activity_id')
        }

        addForeignKeyConstraint(baseColumnNames: 'service_item_activity_id', baseTableName: 'change_detail', constraintName: 'FKB4467BC0855307BD', deferrable: 'false', initiallyDeferred: 'false', referencedColumnNames: 'id', referencedTableName: 'service_item_activity', referencesUniqueColumn: 'false')

        dropColumn(columnName: 'service_item_version', tableName: 'service_item_activity')
        dropColumn(columnName: 'details', tableName: 'service_item_activity')
        dropColumn(columnName: 'object_class_name', tableName: 'change_detail')
        dropColumn(columnName: 'object_id', tableName: 'change_detail')
        dropColumn(columnName: 'object_version', tableName: 'change_detail')
    }

    changeSet(author: 'marketplace', dbms: 'hsqldb, mssql, mysql, postgresql', id: '7.11.0-3', context: 'upgrade-only, 7.11.0') {
        sql("""
            INSERT INTO change_detail (old_value, new_value, field_name, service_item_activity_id, version)
                SELECT old_value, new_value, field_name, service_item_activity_id, 0
                FROM change_detail_temp;
        """)
    }
}
