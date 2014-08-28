/**
 * This changelog include the changesets for the 7.16.0 release of Marketplace.
 * Use the following conventions in this changelog:
 *
 *  author: marketplace
 *  id: 7.16.0-1*
 *  context: create|upgrade-only, 7.16.0
 */

databaseChangeLog = {

    changeSet(author: 'marketplace', dbms: 'mysql', id: '7.16.0-1', context: 'create, 7.16.0') {
        comment('switch to case-sensitive collation for the tags table')
        sql('ALTER TABLE tag CONVERT TO CHARSET latin1 COLLATE latin1_general_cs;')
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, hsqldb, oracle', id: '7.16.0-2', context: 'create, 7.16.0') {
        addColumn(tableName: "service_item") {
            column(name: "image_medium_url", type: "VARCHAR(2083)")
        }
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, oracle', id: '7.16.0-3', context: 'upgrade-only, 7.16.0') {
        update(tableName: 'service_item') {
            column(name: 'image_medium_url', valueComputed: 'image_large_url')
        }
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, oracle', id: '7.16.0-4', context: 'upgrade-only, 7.16.0') {
        update(tableName: "service_item") {
            column(name: "image_large_url", value: null)
        }
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, hsqldb, oracle', id: '7.16.0-5', context: 'create, 7.16.0') {
        createTable(tableName: 'service_item_score_card_item') {
          column(name: 'service_item_id', type: '${marketplace.legacyIdField}')
          column(name: 'score_card_item_id', type: 'java.sql.Types.BIGINT')
        }

        addForeignKeyConstraint(baseColumnNames: 'score_card_item_id', baseTableName: 'service_item_score_card_item', constraintName: 'FKBF91F93EF469C97', deferrable: 'false', initiallyDeferred: 'false', referencedColumnNames: 'id', referencedTableName: 'score_card_item', referencesUniqueColumn: 'false', onDelete: 'CASCADE')
        addForeignKeyConstraint(baseColumnNames: 'service_item_id', baseTableName: 'service_item_score_card_item', constraintName: 'FKBF91F939C51FA9F', deferrable: 'false', initiallyDeferred: 'false', referencedColumnNames: 'id', referencedTableName: 'service_item', referencesUniqueColumn: 'false')

        dropForeignKeyConstraint(baseTableName: 'score_card', constraintName: 'FK5E60409D7666C6D2')
        dropForeignKeyConstraint(baseTableName: 'score_card', constraintName: 'FK5E60409DE31CB353')
        dropForeignKeyConstraint(baseTableName: 'score_card_item_response', constraintName: 'FK80A6CBCB7666C6D2')
        dropForeignKeyConstraint(baseTableName: 'score_card_item_response', constraintName: 'FK80A6CBCBE31CB353')
        dropForeignKeyConstraint(baseTableName: 'score_card_item_response', constraintName: 'FK80A6CBCB190E00BC')
        dropForeignKeyConstraint(baseTableName: 'score_card_item_response', constraintName: 'FK80A6CBCBEF469C97')
        dropForeignKeyConstraint(baseTableName: 'service_item', constraintName: 'FK1571565D190E00BC')

        dropColumn(columnName: 'is_standard_question', tableName: 'score_card_item')
        dropColumn(columnName: 'weight', tableName: 'score_card_item')
        dropColumn(columnName: 'score_card_id', tableName: 'service_item')

        dropTable(tableName: 'score_card')
        dropTable(tableName: 'score_card_item_response')
    }

    //Adding a schema for h2 - previously did not have this in marketplace. This brings h2 up to date with all preceding changesets.
    include file: 'h2_7.16.0.groovy'

    //Include the app configs
    include file: 'app_config.groovy'

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, hsqldb, oracle, h2', id: '7.16.0-6', context: 'create, 7.16.0') {
        delete(tableName: 'application_configuration') {
            where("code = 'store.domains'")
        }

        update(tableName: 'application_configuration') {
            column(name: 'sub_group_name', value: 'Partner Store Image Configurations')
            where("sub_group_name = 'Image Configurations'")
        }

        update(tableName: 'application_configuration') {
            column(name: 'sub_group_name', value: 'Partner Store Search')
            where("sub_group_name = 'Affiliated Search'")
        }
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, hsqldb, oracle', id: '7.16.0-7', context: 'create, 7.16.0') {
        renameTable(oldTableName: 'U_DOMAIN_preferences', newTableName: 'u_domain_preferences')
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, hsqldb, oracle',
            id: '7.16.0-8', context: 'create, 7.16.0') {
        addColumn(tableName: "owf_properties") {
            column(name: "mobile_ready", type: "boolean", defaultValueBoolean: "false") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: 'marketplace', dbms: 'mssql, mysql, postgresql, h2, hsqldb, oracle',
            id: '7.16.0-9', context: 'create, 7.16.0') {

        //fix the faulty definition of the url.public application configuration
        sql("""
            UPDATE application_configuration SET type = 'String' WHERE type = 'string';
        """)
    }
}
