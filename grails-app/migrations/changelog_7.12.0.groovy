/**
 * This changelog include the changesets for the 7.12.0 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.12.0-1*
 *  context: create|upgrade-only, 7.12.0
 */

databaseChangeLog = {
    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb', id: '7.12.0-1', context: 'create, 7.12.0') {
        dropTable(tableName: "change_log")
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb', id: '7.12.0-2', context: 'create, 7.12.0') {
        createTable(tableName: 'tag') {
            column(autoIncrement: 'true', name: 'id', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'tag_PK')
            }
            column(name: 'version', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }
            column(name: 'created_by_id', type: '${marketplace.legacyIdField}')
            column(name: 'created_date', type: 'java.sql.Types.DATE')
            column(name: 'edited_by_id', type: '${marketplace.legacyIdField}')
            column(name: 'edited_date', type: 'java.sql.Types.DATE')
            column(name: 'title', type: 'java.sql.Types.VARCHAR(16)') {
                constraints(nullable: 'false')
            }
        }


        createIndex(tableName: "tag", indexName: "tag_title_idx") {
            column(name: "title")
        }

        createTable(tableName: 'service_item_tag') {
            column(autoIncrement: 'true', name: 'id', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'service_item_tag_PK')
            }
            column(name: 'service_item_id', type: '${marketplace.legacyIdField}') {
                constraints(nullable: "false")
            }
            column(name: 'tag_id', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }
            column(name: 'created_by_id', type: '${marketplace.legacyIdField}'){
                constraints(nullable: "true")
            }
            column(name: 'version', type: 'java.sql.Types.BIGINT') {
                constraints(nullable: "false")
            }
        }

        createIndex(tableName: "service_item_tag", indexName: "service_item_tag_si_idx") {
            column(name: "service_item_id")
        }

        createIndex(tableName: "service_item_tag", indexName: "service_item_tag_tag_idx") {
            column(name: "tag_id")
        }
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, oracle, postgresql, hsqldb', id: '7.12.0-3', context: 'create, 7.12.0') {
        addForeignKeyConstraint(constraintName: "service_item_tag_FK_si", baseColumnNames: "service_item_id", baseTableName: "service_item_tag", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "service_item", referencesUniqueColumn: "false")
        addForeignKeyConstraint(constraintName: "service_item_tag_FK_tag",baseColumnNames: "tag_id",          baseTableName: "service_item_tag", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag",          referencesUniqueColumn: "false")
        addForeignKeyConstraint(constraintName: "service_item_tag_FK_cb", baseColumnNames: "created_by_id",   baseTableName: "service_item_tag", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile",      referencesUniqueColumn: "false")

        addUniqueConstraint(columnNames:"service_item_id, tag_id", constraintName:"service_item_tag_unique_idx",deferrable:"false", initiallyDeferred:"false", tableName:"service_item_tag")
    }
}
